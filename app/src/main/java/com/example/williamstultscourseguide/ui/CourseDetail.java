package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;

public class CourseDetail extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "CourseDetailActivityLog";
    MainDatabase db;
    TextView courseTitleTextView;
    TextView courseStartDateTextView;
    TextView courseEndDateTextView;
    TextView statusTextView;
    TextView mentorTextView;
    TextView phoneTextView;
    TextView emailTextView;
    TextView courseStartTextView;
    TextView courseEndTextView;
    TextView courseStatusTextView;
    TextView courseMentorTextView;
    TextView mentorPhoneTextView;
    TextView mentorEmailTextView;
    FloatingActionButton editCourseButton;
    ListView courseAssessmentListView;
    TextView courseAssessmentTextView;
    FloatingActionButton addCourseAssessmentButton;
    TextView noteTextView;
    TextView courseNoteTextView;
    FloatingActionButton editCourseNoteButton;
    FloatingActionButton deleteCourseButton;
    Intent intent;
    int courseId;
    int termId;
    Course selectedCourse;
    SimpleDateFormat formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setTitle("Course Details");
        courseTitleTextView = findViewById(R.id.courseTitleTextView);
        courseStartTextView = findViewById(R.id.courseStartTextView);
        courseEndTextView = findViewById(R.id.courseEndTextView);
        courseStatusTextView = findViewById(R.id.courseStatusTextView);
        courseMentorTextView = findViewById(R.id.courseMentorTextView);
        mentorPhoneTextView = findViewById(R.id.mentorPhoneTextView);
        mentorEmailTextView = findViewById(R.id.mentorEmailTextView);
        courseAssessmentListView = findViewById(R.id.courseAssessmentListView);
        courseNoteTextView = findViewById(R.id.courseNoteTextView);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        selectedCourse = db.courseDao().getCourse(termId, courseId);


        //mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        //setAmbientEnabled();
    }
}