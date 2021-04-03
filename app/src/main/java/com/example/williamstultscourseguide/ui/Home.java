package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.NukeDatabase;
import com.example.williamstultscourseguide.data.PopulateDatabase;
import com.example.williamstultscourseguide.data.Term;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.williamstultscourseguide.data.PopulateDatabase.LOG_TAG;

public class Home extends AppCompatActivity {

    MainDatabase db;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate todaysDate = LocalDate.now();
    String todaysDateString;
    TextView todaysDateTextView;
    TextView coursesTitleView;
    TextView coursePendingTextView;
    TextView courseCompletedTextView;
    TextView assessmentsTitleView;
    TextView assessmentsPendingTextView;
    TextView assessmentsPassedTextView;
    TextView assessmentsFailedTextView;
    TextView coursesPendingCountTextView;
    TextView completedCountTextView;
    TextView assessmentsPendingCountTextView;
    TextView passedCountTextView;
    TextView failedCountTextView;
    Button goButton;
    Button nukeButton;
    Button populateButton;
    Button button3;
    Button button4;

    private TextView mTextView;
    //private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
        db = MainDatabase.getInstance(getApplicationContext());

        mTextView = (TextView) findViewById(R.id.notesTitle);
        todaysDateTextView = findViewById(R.id.todaysDateTextView);
        coursesTitleView = findViewById(R.id.coursesTitleView);
        coursePendingTextView = findViewById(R.id.coursePendingTextView);
        assessmentsPendingTextView = findViewById(R.id.assessmentsPendingTextView);
        assessmentsPassedTextView = findViewById(R.id.assessmentsPassedTextView);
        assessmentsFailedTextView = findViewById(R.id.assessmentsFailedTextView);
        completedCountTextView = findViewById(R.id.completedCountTextView);
        assessmentsTitleView = findViewById(R.id.assessmentsTitleView);
        coursesPendingCountTextView = findViewById(R.id.coursesPendingCountTextView);
        assessmentsPendingCountTextView = findViewById(R.id.assessmentsPendingCountTextView);
        courseCompletedTextView = findViewById(R.id.courseCompletedTextView);
        passedCountTextView = findViewById(R.id.passedCountTextView);
        failedCountTextView = findViewById(R.id.failedCountTextView);
        goButton = findViewById(R.id.goButton);
        nukeButton = findViewById(R.id.nukeButton);
        populateButton = findViewById(R.id.populateButton);
        todaysDateString = dtf.format(todaysDate);
        todaysDateTextView.setText(todaysDateString);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        // Enables Always-on
        //setAmbientEnabled();

        updateViews();

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsList.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CoursesList.class);
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AssessmentsList.class);
                startActivity(intent);
            }
        });

        populateButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "populate DB button pressed");
            PopulateDatabase populateDatabase = new PopulateDatabase();
            populateDatabase.populate(getApplicationContext());
            updateViews();
        });

        nukeButton.setOnClickListener(v -> {
            Log.d(LOG_TAG, "nuke DB button pressed");
            NukeDatabase nukeDatabase = new NukeDatabase();
            nukeDatabase.nuke(getApplicationContext());
            updateViews();
        });
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
                if (assessmentList.get(i).getAssessment_status().contains("Pending")) assessmentsPending++;
                if (assessmentList.get(i).getAssessment_status().contains("Passed")) assessmentsPassed++;
                if (assessmentList.get(i).getAssessment_status().contains("Failed")) assessmentsFailed++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        coursesPendingCountTextView.setText(String.valueOf(coursesPending));
        completedCountTextView.setText(String.valueOf(coursesCompleted));
        assessmentsPendingCountTextView.setText(String.valueOf(assessmentsPending));
        passedCountTextView.setText(String.valueOf(assessmentsPassed));
        failedCountTextView.setText(String.valueOf(assessmentsFailed));
    }

    //@Override
    //protected void onResume() {
    //    super.onResume();
    //    updateViews();
    //}
}