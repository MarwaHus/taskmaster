package com.androidlab.taskmaster.database;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.androidlab.taskmaster.dao.TaskDao;
import com.androidlab.taskmaster.model.Task;

@Database(entities = {Task.class}, version = 1)
@TypeConverters({TaskDataBaseConverters.class})
public abstract class TaskDataBase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
