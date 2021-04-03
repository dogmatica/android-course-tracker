package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;

import java.util.List;

public class CoursesList extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "CoursesListActivityLog";
    MainDatabase db;
    ListView allCoursesList;

    @Override
    protected void onResume() {
        super.onResume();
        updateList();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_list);
        setTitle("All Courses");
        allCoursesList = findViewById(R.id.allCoursesList);
        db = MainDatabase.getInstance(getApplicationContext());
        updateList();

        allCoursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position clicked: " + position);
                Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
                int courseId = db.courseDao().getAllCourses().get(position).getCourse_id();
                int termId = db.courseDao().getAllCourses().get(position).getTerm_id_fk();
                //intent.putExtra("courseId", courseId);
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });

        //mTextView = (TextView) findViewById(R.id.notesTitle);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateList() {
        List<Course> allCourses = db.courseDao().getAllCourses();
        System.out.println("Number of rows in course table: " + allCourses.size());

        String[] items = new String[allCourses.size()];
        if(!allCourses.isEmpty()) {
            for (int i = 0; i < allCourses.size(); i++) {
                items[i] = allCourses.get(i).getCourse_name();
                System.out.println("Course in position = " + i + " with name = " + items[i]);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        allCoursesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}