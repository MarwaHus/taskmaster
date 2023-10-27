package com.androidlab.taskmaster;

import androidx.annotation.NonNull;

public enum TaskEnum {
    New("New"),
    assigned("Assigned"),
    in_progress("In Progress"),
    complete("Complete");

    private final String taskText;

    TaskEnum(String taskText){
        this.taskText = taskText;
    }

    public String getTaskText(){
        return this.taskText;
    }
    public static TaskEnum fromString(String possibleTaskText){
        for(TaskEnum task : TaskEnum.values())
        {
            if (task.taskText.equals(possibleTaskText))
            {
                return task;
            }
        }
        return null;
    }
    @NonNull
    @Override
    public String toString(){
        if(taskText == null){
            return "";
        }
        return taskText;
    }
}

