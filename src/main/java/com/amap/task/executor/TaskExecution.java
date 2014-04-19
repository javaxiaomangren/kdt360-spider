package com.amap.task.executor;

import com.amap.task.beans.Task;
import com.amap.task.taskMgr.TaskHandler;
import com.amap.task.taskMgr.TaskManager;
import com.amap.task.utils.DBUtils;
import com.amap.task.utils.Utils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * 多线程 task 处理
 * Created by yang.hua on 14-1-21.
 */
public class TaskExecution extends Thread {
    private final Logger logger = LoggerFactory.getLogger(TaskExecution.class);
    private PropertiesConfiguration conf = Utils.COMM_CONF;
    /*System processor counts*/
    private int proc = conf.getInt("threadCount");
    private BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>(conf.getInt("queueSize"));
    /*线程池， 任务处理器*/
    private final ExecutorService executor = new ThreadPoolExecutor(proc, proc, 0, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>() {
                @Override
                public boolean offer(Runnable runnable) {
                    try {
                        put(runnable);
                        return true;
                    } catch (InterruptedException e) {
                        logger.info(e.getMessage());
                    }
                    return true;
                }
            }
    );

    private TaskManager producer;
    private TaskHandler handler;

    public TaskExecution(TaskManager producer, TaskHandler handler) {
        this.producer = producer;
        this.handler = handler;
    }

    volatile boolean shutdown = false;
    private volatile int fetchCondition = proc * 2;
    /*添加任务线程*/
    private FetchTask fetchTask = new FetchTask();

    public void activate() {
        shutdown = false;
        fetchTask.start();
        this.start();
    }
    /*住线程run方法*/
    @Override
    public void run() {
        try {
            while (!shutdown) {
                if (queue.size() > 0) {
                    final Task task = queue.take();
                    executor.execute(new Execution(handler, task));
                    if (queue.size() < fetchCondition && !fetchTask.isInterrupted()) {
                        fetchTask.interrupt();
                        logger.info("fetchTask wake up");
                    }
                }
            }
            executor.shutdown();
            fetchTask.isInterrupted();
            logger.info("All update finished");
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.getInstance().cleanup();
        }
    }
    /*单个cp处理线程*/
    class Execution extends Thread {
        private TaskHandler handler;
        private Task task;

        public Execution(TaskHandler handler, Task task) {
            this.handler = handler;
            this.task = task;
        }

        @Override
        public void run() {
            try {
                handler.handle(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分页读取poiids
     */
    class FetchTask extends Thread {
        int retry = 0;
        @Override
        public void run() {
            while (!shutdown) {
                try {
                    if (queue.size() > fetchCondition) {
                        Thread.sleep(Integer.MAX_VALUE);
                    } else {
                        List<Task> tasks = producer.getTasks();
                        if (tasks != null && !tasks.isEmpty()) {
                            queue.addAll(tasks);
                            logger.info("Get task [{}]", tasks.size());
                        } else {
                            retry += 1;
                        }
                        if (retry > 5) {
                            shutdown = true;
                        }
                    }
                } catch (InterruptedException e) {
                    logger.debug("{}", Utils.getStackTrace(e.getStackTrace()));
                }
            }
            logger.info("fetch task finished");
        }
    }
}
