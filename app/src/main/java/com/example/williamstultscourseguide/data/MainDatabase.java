package com.example.williamstultscourseguide.data;

import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Term.class, Course.class, Coursementor.class, Assessment.class}, exportSchema = false, version = 1)
@TypeConverters({Converters.class})
public abstract class MainDatabase extends RoomDatabase {

    private static final String DB_NAME = "main_db";
    private static MainDatabase instance;

    public static synchronized MainDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MainDatabase.class, DB_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract CoursementorDao coursementorDao();
    public abstract AssessmentDao assessmentDao();

}
