package com.example.williamstultscourseguide.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.utility.AlertReceiver;
import com.example.williamstultscourseguide.utility.Converters;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssessmentDetail extends AppCompatActivity {

    //AssessmentDetail view displays the title, type, due/goal dates and status of an assessment.

    public static String LOG_TAG = "AssessmentDetailActivityLog";
    MainDatabase db;
    Intent intent;
    int termId;
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

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    //Inflation of hidden menu on action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_assessment_alert, menu);
        return true;
    }

    //Actions related to hidden menu selection

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //The hidden menu in the AssessmentDetail view provides additional options:
        switch (item.getItemId()) {
            //When "Home" is selected:
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                return true;
            //When Alert > Goal Date is selected:
            case R.id.item1:
                //A unique value for the request code is generated based on the current time in milliseconds
                int requestCode = (int) System.currentTimeMillis();
                System.out.println("Request code is " + requestCode);
                Date alertDate = selectedAssessment.getAssessment_goal();
                String tempAlertDate = formatter.format(alertDate);
                Intent intent2 = new Intent(this, AlertReceiver.class);
                intent2.putExtra("title", selectedAssessment.getAssessment_title());
                intent2.putExtra("message", "Goal date for " + selectedAssessment.getAssessment_title() + " has arrived: " + tempAlertDate);
                PendingIntent sender = PendingIntent.getBroadcast(this, requestCode, intent2, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, Converters.dateToTimestamp(alertDate), sender);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        setTitle("Assessment Details");
        assessmentTitleTextView = findViewById(R.id.assessmentTitleTextView);
        assessmentTypeTextView = findViewById(R.id.assessmentTypeTextView);
        assessmentDateDueTextView = findViewById(R.id.assessmentDateDueTextView);
        assessmentGoalTextView = findViewById(R.id.assessmentGoalTextView);
        assessmentStatusTextView = findViewById(R.id.assessmentStatusTextView);
        assessmentEditButton = findViewById(R.id.assessmentEditButton);
        assessmentDeleteButton = findViewById(R.id.assessmentDeleteButton);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        assessmentId = intent.getIntExtra("assessmentId", -1);
        selectedAssessment = db.assessmentDao().getAssessment(courseId, assessmentId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        //Query the database and update current layout with appropriate data:

        updateViews();

        //When the edit button for the assessment is pressed:

        assessmentEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Loading the add / edit assessment view, passing variables courseId and assessmentId:
                Intent intent = new Intent(getApplicationContext(), AssessmentEdit.class);
                intent.putExtra("courseId", courseId);
                intent.putExtra("assessmentId", assessmentId);
                startActivity(intent);
            }
        });

        //When the delete button for the assessment is pressed

        assessmentDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A confirmation dialog is shown asking the user
                // if they are sure they want to delete:
                AlertDialog dialog = new AlertDialog.Builder(AssessmentDetail.this).setTitle("Confirm").setMessage("Delete Assessment?").setPositiveButton("Ok", null).setNegativeButton("Cancel", null).show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                //When the user clicks "Ok" to delete:
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String assessmentName = selectedAssessment.getAssessment_title();
                        db.assessmentDao().deleteAssessment(assessmentId);
                        Toast.makeText(getApplicationContext(), "Assessment " + assessmentName + " was deleted.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
                        intent.putExtra("termId", termId);
                        intent.putExtra("courseId", courseId);
                        startActivity(intent);
                    }
                });
                //If the user clicks cancel, the previous screen is restored
            }
        });
    }

        //Query the database and update current layout with appropriate data:

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
