package com.amap.task.taskMgr.impl;

import com.amap.task.beans.Task;
import com.amap.task.taskMgr.Persistence;
import com.amap.task.taskMgr.TaskHandler;
import com.amap.task.utils.GetKdtData;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.dom4j.*;
import org.perf4j.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by yang.hua on 14-4-1.
 */
public class TaskHandlerKdt360Impl implements TaskHandler {
    private Persistence persistence;
    private final Logger logger = LoggerFactory.getLogger(TaskHandlerKdt360Impl.class);

    public TaskHandlerKdt360Impl(Persistence p) {
        this.persistence = p;
    }

    @Override
    public void handle(Task task) {
        try {
            String lng = (String) task.getTaskPart().get("lng");
            String lat = (String) task.getTaskPart().get("lat");
            StopWatch watch = new StopWatch();
            watch.start("kdt_http_interface");
            String result = GetKdtData.getByCoordinate(lng, lat);
            List<Map<String, Object>> ls = Lists.newArrayList();
            watch.stop();
            if (result != null && !"".equals(result) && result.length() > 20) {
                ls = GetKdtData.extraXml(result);
                if (ls != null && !ls.isEmpty()) {
                    persistence.save(ls, task);
                }
            }
            watch.stop();
            task.setStatus(Task.Status.COMPLETE);
            persistence.assignTask(task);
            logger.info("handle task={}, Get=[{}]", task.toString(), ls.size());
        } catch (IOException e) {
            logger.info("Get kdt360 time out");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TaskHandler handler = new TaskHandlerKdt360Impl(new PersistenceMysql());
        Task t = new Task();
        t.setTaskPart(ImmutableMap.of("lng","116.523205", "lat", "39.922766"));
        handler.handle(t);
    }
}
