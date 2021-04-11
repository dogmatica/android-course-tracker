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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.Coursementor;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.example.williamstultscourseguide.utility.AlertReceiver;
import com.example.williamstultscourseguide.utility.Converters;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    Term selectedTerm;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Calendar c = Calendar.getInstance();
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                return true;
            case R.id.subitem1:
                //int tempRequestCode = AlertReceiver.getNumAlert();
                int requestCode = (int) System.currentTimeMillis();
                System.out.println("Request code is " + requestCode);
                Date alertDate = selectedCourse.getCourse_start();
                String tempAlertDate = formatter.format(alertDate);
                Intent intent2 = new Intent(this, AlertReceiver.class);
                intent2.putExtra("title", selectedCourse.getCourse_name());
                intent2.putExtra("message", "Start date for " + selectedCourse.getCourse_name() + " has arrived: " + tempAlertDate);
                PendingIntent sender = PendingIntent.getBroadcast(this, requestCode, intent2, 0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, Converters.dateToTimestamp(alertDate), sender);
                //AlertReceiver.setNumAlert(requestCode);
            case R.id.subitem2:
                //int tempRequestCode2 = AlertReceiver.getNumAlert();
                int requestCode2 = (int) System.currentTimeMillis();
                System.out.println("Request code is " + requestCode2);
                Date alertDate2 = selectedCourse.getCourse_end();
                String tempAlertDate2 = formatter.format(alertDate2);
                Intent intent3 = new Intent(this, AlertReceiver.class);
                intent3.putExtra("title", selectedCourse.getCourse_name());
                intent3.putExtra("message", "End date for " + selectedCourse.getCourse_name() + " has arrived: " + tempAlertDate2);
                PendingIntent sender2 = PendingIntent.getBroadcast(this, requestCode2, intent3, 0);
                AlarmManager alarmManager2 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager2.setExact(AlarmManager.RTC_WAKEUP, Converters.dateToTimestamp(alertDate2), sender2);
                //AlertReceiver.setNumAlert(requestCode2);
            default:
                return super.onOptionsItemSelected(item);
        }
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
        editCourseButton = findViewById(R.id.editCourseButton);
        addCourseAssessmentButton = findViewById(R.id.addCourseAssessmentButton);
        deleteCourseButton = findViewById(R.id.deleteCourseButton);
        //courseNoteTextView = findViewById(R.id.courseNoteTextView);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        selectedCourse = db.courseDao().getCourse(termId, courseId);
        selectedTerm = db.termDao().getTerm(termId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        updateViews();
        updateList();

        courseAssessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position clicked: " + position);
                Intent intent = new Intent(getApplicationContext(), AssessmentDetail.class);
                int assessmentId = db.assessmentDao().getCourseAssessmentList(courseId).get(position).getAssessment_id();
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                intent.putExtra("assessmentId", assessmentId);
                startActivity(intent);
            }
        });

        editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("edit course button pressed");
                Course tempCourse = db.courseDao().getCourse(termId, courseId);
                System.out.println("current course name: " + tempCourse.getCourse_name());
                Intent intent = new Intent(getApplicationContext(), CourseEdit.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });

        addCourseAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("add assessment button pressed");
                Intent intent = new Intent(getApplicationContext(), AssessmentEdit.class);
                Calendar calendar = Calendar.getInstance();
                int dbCount = db.assessmentDao().getAssessmentList().size() + 1;
                Assessment tempAssessment = new Assessment();
                String tempAssessmentName = "New Assessment " + dbCount;
                tempAssessment.setCourse_id_fk(courseId);
                tempAssessment.setAssessment_title(tempAssessmentName);
                tempAssessment.setAssessment_type("Objective");
                tempAssessment.setAssessment_status("Pending");
                tempAssessment.setAssessment_due(calendar.getTime());
                tempAssessment.setAssessment_goal(calendar.getTime());
                db.assessmentDao().insertAssessment(tempAssessment);
                tempAssessment = db.assessmentDao().getAssessmentByTitle(tempAssessmentName);
                int assessmentId = tempAssessment.getAssessment_id();
                intent.putExtra("courseId", courseId);
                intent.putExtra("assessmentId", assessmentId);
                startActivity(intent);

            }
        });

        deleteCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(CourseDetail.this).setTitle("Confirm").setMessage("Delete Course?").setPositiveButton("Ok", null).setNegativeButton("Cancel", null).show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String courseName = selectedCourse.getCourse_name();
                        db.courseDao().deleteCourse(courseId);
                        Toast.makeText(getApplicationContext(), "Course " + courseName + " was deleted.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), TermDetail.class);
                        intent.putExtra("termId", termId);
                        startActivity(intent);
                    }
                });
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
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });




        //mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateViews() {
        if (selectedCourse != null) {
            Log.d(CourseDetail.LOG_TAG, "selected course is not null");
            Date startDate = selectedCourse.getCourse_start();
            System.out.println("Current course start date is " + startDate.toString());
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