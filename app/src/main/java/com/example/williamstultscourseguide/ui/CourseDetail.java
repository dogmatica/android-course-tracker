package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.Coursementor;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    //TextView noteTextView;
    //TextView courseNoteTextView;
    //FloatingActionButton editCourseNoteButton;
    FloatingActionButton deleteCourseButton;
    Button button;
    Button button2;
    Intent intent;
    int courseId;
    int termId;
    Course selectedCourse;
    Coursementor selectedMentor;
    SimpleDateFormat formatter;

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
        updateViews();
    }


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
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        //courseNoteTextView = findViewById(R.id.courseNoteTextView);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        selectedCourse = db.courseDao().getCourse(termId, courseId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        updateViews();
        updateList();

        courseAssessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position clicked: " + position);
                Intent intent = new Intent(getApplicationContext(), AssessmentDetail.class);
                int assessmentId = db.assessmentDao().getCourseAssessmentList(courseId).get(position).getAssessment_id();
                intent.putExtra("courseId", courseId);
                intent.putExtra("assessmentId", assessmentId);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseAlertEntry.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseNote.class);
                startActivity(intent);
            }
        });




        //mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateViews() {
        if (selectedCourse != null) {
            Log.d(TermDetail.LOG_TAG, "selected course is not null");
            Date startDate = selectedCourse.getCourse_start();
            Date endDate = selectedCourse.getCourse_end();

            System.out.println("Millisecond date: " + startDate.toString());
            String tempStart = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            System.out.println("Formatted date: " + tempStart);
            courseStartTextView.setText(tempStart);
            courseEndTextView.setText(tempEnd);
            courseTitleTextView.setText(selectedCourse.getCourse_name());
            courseStatusTextView.setText(selectedCourse.getCourse_status());
            //courseNoteTextView.setText(selectedCourse.getCourse_notes());

            selectedMentor = db.coursementorDao().getCoursementor(selectedCourse.getCourse_id());
            courseMentorTextView.setText(selectedMentor.getCoursementor_name());
            mentorPhoneTextView.setText(selectedMentor.getCoursementor_phone());
            mentorEmailTextView.setText(selectedMentor.getCoursementor_email());
        } else {
            Log.d(TermDetail.LOG_TAG, "selected term is null");
            selectedCourse = new Course();
        }
    }

    private void updateList() {
        List<Assessment> allCourseAssessments = db.assessmentDao().getCourseAssessmentList(selectedCourse.getCourse_id());
        System.out.println("Number of rows in course table matching " + courseId + ": " + allCourseAssessments.size());

        String[] items = new String[allCourseAssessments.size()];
        if(!allCourseAssessments.isEmpty()) {
            for (int i = 0; i < allCourseAssessments.size(); i++) {
                items[i] = allCourseAssessments.get(i).getAssessment_title();
                System.out.println("Assessment in position = " + i + " with name = " + items[i]);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        courseAssessmentListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}