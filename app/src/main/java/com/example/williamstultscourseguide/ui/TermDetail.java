package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TermDetail extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "TermDetailActivityLog";
    MainDatabase db;
    FloatingActionButton addTermCourseButton;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        setTitle("Term Details");
        addTermCourseButton = findViewById(R.id.addTermCourseButton);
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