package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;

public class CourseNote extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "CourseNoteActivityLog";
    MainDatabase db;
    int courseId;
    int termId;
    Course selectedCourse;
    Intent intent;
    TextView courseNote;
    Button editNotesButton;

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        inflater.inflate(R.menu.menu_note_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, courseNote.getText());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note);
        setTitle("Course Note");
        courseNote = findViewById(R.id.courseNote);
        editNotesButton = findViewById(R.id.editNotesButton);

        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        courseId = intent.getIntExtra("courseId", -1);
        termId = intent.getIntExtra("termId", -1);
        selectedCourse = db.courseDao().getCourse(termId, courseId);

        updateViews();

        editNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("edit note button pressed");
                Course tempCourse = db.courseDao().getCourse(termId, courseId);
                System.out.println("current course name: " + tempCourse.getCourse_name());
                Intent intent = new Intent(getApplicationContext(), CourseNoteEdit.class);
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
            Log.d(CourseNote.LOG_TAG, "selected course is not null");
            courseNote.setText(selectedCourse.getCourse_notes());
        } else {
            Log.d(CourseNote.LOG_TAG, "selected course is null");
            selectedCourse = new Course();
        }
    }
}