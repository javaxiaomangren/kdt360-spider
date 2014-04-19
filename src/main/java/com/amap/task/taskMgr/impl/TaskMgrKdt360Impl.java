package com.amap.task.taskMgr.impl;

import com.amap.task.beans.Task;
import com.amap.task.taskMgr.Persistence;
import com.amap.task.taskMgr.TaskManager;
import com.amap.task.utils.Utils;

import java.util.List;


public class TaskMgrKdt360Impl implements TaskManager {
    private Persistence persistence;

    public TaskMgrKdt360Impl(Persistence dbHandler) {
        this.persistence = dbHandler;
    }

    @Override
    public Task getTask() {
        return null;
    }

    @Override
    public List<Task> getTasks() {
        return persistence.getTasks(Utils.COMM_CONF.getInt("limit"));
    }

    @Override
    public void assignment(Task task) {
        persistence.assignTask(task);
    }
}
