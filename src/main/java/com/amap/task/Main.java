package com.amap.task;

import com.amap.task.executor.TaskExecution;
import com.amap.task.taskMgr.Persistence;
import com.amap.task.taskMgr.impl.PersistenceMysql;
import com.amap.task.taskMgr.impl.TaskHandlerKdt360Impl;
import com.amap.task.taskMgr.impl.TaskMgrKdt360Impl;
import com.amap.task.utils.SimpleMailSender;
import com.amap.task.utils.Utils;

/**
 * Main Class
 * Created by yang.hua on 14-4-1.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Persistence persistence = new PersistenceMysql();
            new TaskExecution(new TaskMgrKdt360Impl(persistence), new TaskHandlerKdt360Impl(persistence)).activate();
        } catch (Exception e) {
            e.printStackTrace();
            SimpleMailSender.notify(e.getMessage(), Utils.getStackTrace(e.getStackTrace()));
        }
    }
}
