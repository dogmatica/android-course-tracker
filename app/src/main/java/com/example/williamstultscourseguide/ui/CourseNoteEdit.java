package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CourseNoteEdit extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "NoteEditActivityLog";
    MainDatabase db;
    int termId;
    int courseId;
    Intent intent;
    Term selectedTerm;
    Course selectedCourse;
    EditText noteEditText;
    FloatingActionButton noteSaveButton;

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_edit);
        setTitle("Add or Edit Note");
        noteEditText = findViewById(R.id.noteEditText);
        noteSaveButton = findViewById(R.id.noteSaveButton);

        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        courseId = intent.getIntExtra("courseId", -1);
        selectedTerm = db.termDao().getTerm(termId);
        selectedCourse = db.courseDao().getCourse(termId, courseId);
        updateViews();

        noteSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("save note button pressed");
                selectedCourse.setCourse_notes(String.valueOf(noteEditText.getText()));
                db.courseDao().updateCourse(selectedCourse);
                Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
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
            Log.d(CourseEdit.LOG_TAG, "selected course is not null");
            noteEditText.setText(selectedCourse.getCourse_notes());
        } else {
            Log.d(CourseEdit.LOG_TAG, "selected course is null");
            selectedCourse = new Course();
        }
    }
}