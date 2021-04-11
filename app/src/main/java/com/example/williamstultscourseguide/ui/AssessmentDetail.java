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
import androidx.core.app.NotificationCompat;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.utility.AlertReceiver;
import com.example.williamstultscourseguide.utility.Converters;
import com.example.williamstultscourseguide.utility.NotificationHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssessmentDetail extends AppCompatActivity {

    //private TextView mTextView;
    private NotificationHelper aNotificationHelper;
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
    Button assessmentAlertButton;

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_assessment_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                return true;
            case R.id.item1:
                //int tempRequestCode = AlertReceiver.getNumAlert();
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
                //AlertReceiver.setNumAlert(requestCode);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        setTitle("Assessment Details");
        aNotificationHelper = new NotificationHelper(this);
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

        assessmentDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AssessmentDetail.this).setTitle("Confirm").setMessage("Delete Assessment?").setPositiveButton("Ok", null).setNegativeButton("Cancel", null).show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
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
            }


            //mTextView = (TextView) findViewById(R.id.notesTitle);

            // Enables Always-on
            //setAmbientEnabled();
        });
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

        public void sendOnChannel1 (String title, String message){
            NotificationCompat.Builder nb = aNotificationHelper.getChannel1Notification(title, message);
            aNotificationHelper.getManager().notify(1, nb.build());
        }
    }
