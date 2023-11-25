package com.androidlab.taskmaster.model;


import com.androidlab.taskmaster.TaskEnum;

import java.util.Date;

public class Task {
    public Long id;
    String title;
    String body;
    TaskEnum state;

    java.util.Date dateCreated;

    public Task() {
        this.title = title;
        this.body = body;
        this.state = state;
        this.dateCreated = dateCreated;
    }

    public static com.amplifyframework.datastore.generated.model.Task.NameStep builder() {
        return null;
    }

    public String getId() {
        return String.valueOf(id);
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

  /*  public Date getDateCreated() {
        return dateCreated;
    }*/

   /* public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }*/


}
