package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.Coursementor;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CourseEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //private TextView mTextView;
    public static String LOG_TAG = "CourseEditActivityLog";
    MainDatabase db;
    int termId;
    int courseId;
    SimpleDateFormat formatter;
    Intent intent;
    Term selectedTerm;
    Course selectedCourse;
    Coursementor selectedCoursementor;
    Date newStartDate;
    Date newEndDate;
    FloatingActionButton courseSaveButton;
    EditText courseNamePlainText;
    EditText courseStartDate;
    EditText courseEndDate;
    Spinner spinner;
    EditText mentorNamePlainText;
    EditText mentorPhonePlainText;
    EditText mentorEmailPlainText;

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);
        setTitle("Add or Edit Course");
        courseSaveButton = findViewById(R.id.courseSaveButton);
        courseNamePlainText = findViewById(R.id.courseNamePlainText);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        mentorNamePlainText = findViewById(R.id.mentorNamePlainText);
        mentorPhonePlainText = findViewById(R.id.mentorPhonePlainText);
        mentorEmailPlainText = findViewById(R.id.mentorEmailPlainText);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.statuses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        System.out.println("current course is " + courseId);
        selectedTerm = db.termDao().getTerm(termId);
        selectedCourse = db.courseDao().getCourse(termId, courseId);
        selectedCoursementor = db.coursementorDao().getCoursementor(courseId);
        System.out.println("current course name is " + selectedCourse.getCourse_name());
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        updateViews();

        courseSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("save course button pressed");
                try {
                    Course newCourse = new Course();
                    newStartDate = formatter.parse(String.valueOf(courseStartDate.getText()));
                    newEndDate = formatter.parse(String.valueOf(courseEndDate.getText()));
                    newCourse.setCourse_id(courseId);
                    newCourse.setTerm_id_fk(termId);
                    newCourse.setCourse_name(String.valueOf(courseNamePlainText.getText()));
                    newCourse.setCourse_start(newStartDate);
                    newCourse.setCourse_end(newEndDate);
                    newCourse.setCourse_status(String.valueOf(spinner.getSelectedItem()));
                    db.courseDao().updateCourse(newCourse);
                    Coursementor newMentor = new Coursementor();
                    newMentor.setCoursementor_id(selectedCoursementor.getCoursementor_id());
                    newMentor.setCourse_id_fk(courseId);
                    newMentor.setCoursementor_name(String.valueOf(mentorNamePlainText.getText()));
                    newMentor.setCoursementor_phone(String.valueOf(mentorPhonePlainText.getText()));
                    newMentor.setCoursementor_email(String.valueOf(mentorEmailPlainText.getText()));
                    db.coursementorDao().updateCoursementor(newMentor);
                    Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
                    intent.putExtra("termId", termId);
                    intent.putExtra("courseId", courseId);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateViews() {
        if (selectedCourse != null) {
            Log.d(CourseEdit.LOG_TAG, "selected course is not null");
            Date startDate = selectedCourse.getCourse_start();
            Date endDate = selectedCourse.getCourse_end();
            System.out.println("Millisecond date: " + startDate.toString());
            String tempStart = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            System.out.println("Formatted date: " + tempStart);
            courseNamePlainText.setText(selectedCourse.getCourse_name());
            courseStartDate.setText(tempStart);
            courseEndDate.setText(tempEnd);
            mentorNamePlainText.setText(selectedCoursementor.getCoursementor_name());
            mentorPhonePlainText.setText(selectedCoursementor.getCoursementor_phone());
            mentorEmailPlainText.setText(selectedCoursementor.getCoursementor_email());
        } else {
            Log.d(CourseEdit.LOG_TAG, "selected course is null");
            selectedCourse = new Course();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}