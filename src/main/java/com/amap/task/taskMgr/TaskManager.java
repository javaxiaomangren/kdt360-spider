package com.amap.task.taskMgr;

import com.amap.task.beans.Task;

import java.util.List;

/**
 * 任务管理
 * Created by yang.hua on 14-3-31.
 */
public interface TaskManager {

    public Task getTask();

    public List<Task> getTasks();

    public void assignment(Task task);

}
