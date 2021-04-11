package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;

import java.util.List;

public class CoursesList extends AppCompatActivity {

    //CoursesList view displays a selectable ListView of all courses currently in the course table

    public static String LOG_TAG = "CoursesListActivityLog";
    MainDatabase db;
    ListView allCoursesList;

    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    //Inflation of hidden menu on action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    //Actions related to hidden menu selection

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //When "Home" is selected:
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
        setContentView(R.layout.activity_courses_list);
        setTitle("All Courses");
        allCoursesList = findViewById(R.id.allCoursesList);
        db = MainDatabase.getInstance(getApplicationContext());

        //Query the database and update current layout with appropriate data:

        updateList();

        //When a course that is a member of the displayed list is pressed:

        allCoursesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Loading the course detail view, passing variables courseId and termId:
                Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
                int courseId = db.courseDao().getAllCourses().get(position).getCourse_id();
                int termId = db.courseDao().getAllCourses().get(position).getTerm_id_fk();
                intent.putExtra("courseId", courseId);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });
    }

    //Query the database and update current layout with appropriate data:

    private void updateList() {
        List<Course> allCourses = db.courseDao().getAllCourses();
        //String array is created from the database query results
        String[] items = new String[allCourses.size()];
        if(!allCourses.isEmpty()) {
            for (int i = 0; i < allCourses.size(); i++) {
                items[i] = allCourses.get(i).getCourse_name();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        allCoursesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}