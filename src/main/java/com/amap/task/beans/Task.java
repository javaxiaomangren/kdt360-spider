package com.amap.task.beans;

import java.util.Map;

/**
 * 任务定义
 * Created by yang.hua on 14-3-31.
 */
public class Task {
    private String id;
    private Map<String, ?> taskPart; //Task content define
    private Status status = Status.READY;
    private long lastTime = 0;
    private int retry = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, ?> getTaskPart() {
        return taskPart;
    }

    public void setTaskPart(Map<String, ?> taskPart) {
        this.taskPart = taskPart;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public static enum Status {
        READY(1),
        ASSIGNED(2),
        COMPLETE(3),
        ERROR(4),
        FAILED(5),
        CANCELLED(6),
        EXPIRED(7);

        public int value;
        Status(int value) {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                ", taskPart=" + taskPart +
                ", status=" + status +
                ", lastTime=" + lastTime +
                ", retry=" + retry +
                '}';
    }

    public static void main(String[] args) {
        System.out.println(new Task().getStatus().value);
    }
}
