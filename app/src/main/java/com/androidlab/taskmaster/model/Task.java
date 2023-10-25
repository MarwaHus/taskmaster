package com.androidlab.taskmaster.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.androidlab.taskmaster.TaskEnum;

import java.util.Date;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public    Long id;
    String title;
     String body;
    TaskEnum state;

    java.util.Date dateCreated;

    public Task(String title, String body, TaskEnum state, Date dateCreated) {
        this.title = title;
        this.body = body;
        this.state = state;
        this.dateCreated = dateCreated;
    }
    public Long getId() {
        return id;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}