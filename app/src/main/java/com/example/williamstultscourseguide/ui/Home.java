package com.example.williamstultscourseguide.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Home extends AppCompatActivity {

    MainDatabase db;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate todaysDate = LocalDate.now();
    String todaysDateString;
    TextView todaysDateTextView;
    Button goButton;
    Button nukeButton;
    Button populateButton;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = MainDatabase.getInstance(getApplicationContext());

        mTextView = (TextView) findViewById(R.id.text);
        todaysDateTextView = findViewById(R.id.todaysDateTextView);
        goButton = findViewById(R.id.goButton);
        nukeButton = findViewById(R.id.nukeButton);
        populateButton = findViewById(R.id.populateButton);
        todaysDateString = dtf.format(todaysDate);
        todaysDateTextView.setText(todaysDateString);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateViews() {

        int coursesPending = 0;
        int coursesCompleted = 0;
        int assessmentsPending = 0;
        int assessmentsPassed = 0;
        int assessmentsFailed = 0;
        try {
            List<Term> termList = db.termDao().getAllTerms();
            List<Course> courseList = db.courseDao().getAllCourses();
            List<Assessment> assessmentList = db.assessmentDao().getAllAssessments();

            try {
                for (int i = 0; i < courseList.size(); i++) {
                    if (courseList.get(i).getCourse_status().contains("Pending")) coursesPending++;
                    if (courseList.get(i).getCourse_status().contains("In-Progress")) coursesPending++;
                    if (courseList.get(i).getCourse_status().contains("Completed")) coursesCompleted++;
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < assessmentList.size(); i++) {
                if (assessmentList.get(i).getAssesment_status().contains("Pending")) assessmentsPending++;
                if (assessmentList.get(i).getAssesment_status().contains("Passed")) assessmentsPassed++;
                if (assessmentList.get(i).getAssesment_status().contains("Failed")) assessmentsFailed++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}