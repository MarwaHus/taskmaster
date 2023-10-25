package com.androidlab.taskmaster.model;

import com.androidlab.taskmaster.TaskEnum;

public class Task {
    private String title;
    private String body;
    private TaskEnum state;

    public Task(String title, String body, TaskEnum state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public TaskEnum getState() {
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