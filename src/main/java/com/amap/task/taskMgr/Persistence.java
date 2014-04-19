package com.amap.task.taskMgr;

import com.amap.task.beans.Task;

import java.util.List;
import java.util.Map;

/**
 * Created by yang.hua on 14-4-1.
 */
public interface Persistence {

    public Task getTask(String id);

    public List<Task> getTasks(int limit);

    public void save(List<Map<String, Object>> params, final Task task);

    public void assignTask(Task task);

}
