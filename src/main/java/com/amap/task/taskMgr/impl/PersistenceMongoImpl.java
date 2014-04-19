package com.amap.task.taskMgr.impl;

import com.amap.task.beans.Task;
import com.amap.task.taskMgr.Persistence;
import com.amap.task.utils.MongoUtils;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by yang.hua on 14-4-2.
 */
public class PersistenceMongoImpl implements Persistence {
    private DB mongo = MongoUtils.getMongoDb();
    private DBCollection task = mongo.getCollection("task");
    private DBCollection data = mongo.getCollection("data");


    //TODO insure index

    @Override
    public Task getTask(String id) {
        return null;
    }

    @Override
    public List<Task> getTasks(int limit) {
        return null;
    }

    @Override
    public void save(List<Map<String, Object>> params, Task task) {

    }

    @Override
    public void assignTask(Task task) {

    }
}
