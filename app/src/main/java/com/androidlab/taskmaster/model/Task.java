package com.androidlab.taskmaster.model;

import com.androidlab.taskmaster.TaskEnum;

public class Task {
    private static String title;
    private static String body;
    private static TaskEnum state;

    public Task(String title, String body, TaskEnum state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public static String getTitle() {
        return title;
    }

    public static String getBody() {
        return body;
    }

    public static TaskEnum getState() {
        return state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setState(TaskEnum state) {
        this.state = state;
    }
}