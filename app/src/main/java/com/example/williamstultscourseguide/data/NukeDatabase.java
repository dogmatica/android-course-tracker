package com.example.williamstultscourseguide.data;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class NukeDatabase extends AppCompatActivity {

    public static String LOG_TAG = "NukeData";
    MainDatabase db;

    public void nuke(Context context) {
        db = MainDatabase.getInstance(context);
        try {
            deleteAssessments();
            deleteCoursementors();
            deleteCourses();
            deleteTerms();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Nuke DB Failed");
        }
    }

    private void deleteAssessments() {
        db.assessmentDao().nukeAssessmentTable();
    }

    private void deleteCoursementors() {
        db.coursementorDao().nukeCoursementorTable();
    }

    private void deleteCourses() {
        db.courseDao().nukeCourseTable();
    }

    private void deleteTerms() {
        db.termDao().nukeTermTable();
    }
}
