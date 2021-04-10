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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.Coursementor;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TermDetail extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "TermDetailActivityLog";
    MainDatabase db;
    FloatingActionButton addTermCourseButton;
    FloatingActionButton editTermButton;
    ListView courseList;
    TextView termTitleTextView;
    TextView startDateTextView;
    TextView endDateTextView;
    TextView termStartTextView;
    TextView termEndTextView;
    Intent intent;
    int termId;
    Term selectedTerm;
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
        setContentView(R.layout.activity_term_detail);
        setTitle("Term Details");
        addTermCourseButton = findViewById(R.id.addTermCourseButton);
        editTermButton = findViewById(R.id.editTermButton);
        termTitleTextView = findViewById(R.id.termTitleTextView);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        termStartTextView = findViewById(R.id.termStartTextView);
        termEndTextView = findViewById(R.id.termEndTextView);
        courseList = findViewById(R.id.courseList);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        selectedTerm = db.termDao().getTerm(termId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        updateViews();
        updateList();

        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position clicked: " + position);
                Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
                int courseId = db.courseDao().getCourseList(termId).get(position).getCourse_id();
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });

        editTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("edit term button pressed");
                Term tempTerm = db.termDao().getTerm(termId);
                System.out.println("current term name: " + tempTerm.getTerm_name());
                Intent intent = new Intent(getApplicationContext(), TermEdit.class);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });

        addTermCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CourseEdit.class);
                Calendar calendar = Calendar.getInstance();
                int dbCount = db.courseDao().getCourseList(termId).size() + 1;
                Course tempCourse = new Course();
                String tempCourseName = "New Course " + dbCount;
                tempCourse.setCourse_name(tempCourseName);
                tempCourse.setCourse_start(calendar.getTime());
                tempCourse.setCourse_end(calendar.getTime());
                tempCourse.setCourse_status("Pending");
                tempCourse.setCourse_notes("Enter notes here");
                tempCourse.setTerm_id_fk(termId);
                db.courseDao().insertCourse(tempCourse);
                tempCourse = db.courseDao().getCourseByName(termId, tempCourseName);
                int courseId = tempCourse.getCourse_id();
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                int dbMentorCount = db.coursementorDao().getCoursementorList().size() + 1;
                Coursementor tempCoursementor = new Coursementor();
                String tempCoursementorName = "New Mentor " + dbMentorCount;
                tempCoursementor.setCoursementor_name(tempCoursementorName);
                tempCoursementor.setCoursementor_phone("000-000-0000");
                tempCoursementor.setCoursementor_email("mentor@example.edu");
                tempCoursementor.setCourse_id_fk(courseId);
                db.coursementorDao().insertCoursementor(tempCoursementor);
                tempCoursementor = db.coursementorDao().getCoursementor(courseId);
                int coursementorId = tempCoursementor.getCoursementor_id();
                intent.putExtra("coursementorId", coursementorId);
                System.out.println("add course button pressed");
                startActivity(intent);

            }
        });



        //mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateList() {
        List<Course> allTermCourses = db.courseDao().getCourseList(termId);
        System.out.println("Number of rows in course table matching " + termId + ": " + allTermCourses.size());

        String[] items = new String[allTermCourses.size()];
        if(!allTermCourses.isEmpty()) {
            for (int i = 0; i < allTermCourses.size(); i++) {
                items[i] = allTermCourses.get(i).getCourse_name();
                System.out.println("Course in position = " + i + " with name = " + items[i]);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        courseList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void updateViews() {
        if (selectedTerm != null) {
            Log.d(TermDetail.LOG_TAG, "selected term is not null");
            Date startDate = selectedTerm.getTerm_start();
            Date endDate = selectedTerm.getTerm_end();

            System.out.println("Millisecond date: " + startDate.toString());
            String tempStart = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            System.out.println("Formatted date: " + tempStart);
            termStartTextView.setText(tempStart);
            termEndTextView.setText(tempEnd);
            termTitleTextView.setText(selectedTerm.getTerm_name());
        } else {
            Log.d(TermDetail.LOG_TAG, "selected term is null");
            selectedTerm = new Term();
        }
    }
}