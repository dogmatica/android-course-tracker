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
import com.example.williamstultscourseguide.data.MainDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssessmentDetail extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "AssessmentDetailActivityLog";
    MainDatabase db;
    Intent intent;
    int courseId;
    int assessmentId;
    Assessment selectedAssessment;
    SimpleDateFormat formatter;
    TextView assessmentTitleTextView;
    TextView assessmentTypeTextView;
    TextView assessmentDateDueTextView;
    TextView assessmentGoalTextView;
    TextView assessmentStatusTextView;
    FloatingActionButton assessmentEditButton;
    FloatingActionButton assessmentDeleteButton;
    Button assessmentAlertButton;

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        setTitle("Add or Edit Assessment");
        assessmentTitleTextView = findViewById(R.id.assessmentTitleTextView);
        assessmentTypeTextView = findViewById(R.id.assessmentTypeTextView);
        assessmentDateDueTextView = findViewById(R.id.assessmentDateDueTextView);
        assessmentGoalTextView = findViewById(R.id.assessmentGoalTextView);
        assessmentStatusTextView = findViewById(R.id.assessmentStatusTextView);
        assessmentEditButton = findViewById(R.id.assessmentEditButton);
        assessmentDeleteButton = findViewById(R.id.assessmentDeleteButton);
        assessmentAlertButton = findViewById(R.id.assessmentAlertButton);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        assessmentId = intent.getIntExtra("assessmentId", -1);
        selectedAssessment = db.assessmentDao().getAssessment(courseId, assessmentId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        updateViews();

        assessmentEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("edit assessment button pressed");
                Assessment tempAssessment = db.assessmentDao().getAssessment(courseId, assessmentId);
                System.out.println("current assessment title is: " + tempAssessment.getAssessment_title());
                Intent intent = new Intent(getApplicationContext(), AssessmentEdit.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("assessmentId", assessmentId);
                startActivity(intent);
            }
        });

        //mTextView = (TextView) findViewById(R.id.notesTitle);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateViews() {
        if (selectedAssessment != null) {
            Log.d(AssessmentDetail.LOG_TAG, "selected assessment is not null");
            Date dueDate = selectedAssessment.getAssessment_due();
            Date goalDate = selectedAssessment.getAssessment_goal();
            String tempDue = formatter.format(dueDate);
            String tempGoal = formatter.format(goalDate);
            assessmentTitleTextView.setText(selectedAssessment.getAssessment_title());
            assessmentTypeTextView.setText(selectedAssessment.getAssessment_type());
            assessmentStatusTextView.setText(selectedAssessment.getAssessment_status());
            assessmentDateDueTextView.setText(tempDue);
            assessmentGoalTextView.setText(tempGoal);
        } else {
            Log.d(AssessmentEdit.LOG_TAG, "selected assessment is null");
            selectedAssessment = new Assessment();
        }
    }
}