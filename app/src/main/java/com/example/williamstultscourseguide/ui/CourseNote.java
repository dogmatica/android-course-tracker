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

    //The CourseNote view displays the notes for the current course

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

    //Inflation of hidden menu on action bar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        inflater.inflate(R.menu.menu_note_share, menu);
        return true;
    }

    //Actions related to hidden menu selection

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //The hidden menu in the CourseNote view provides additional options:
        switch (item.getItemId()) {
            //When "Home" is selected:
            case R.id.home:
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                return true;
            //When Share is selected:
            case R.id.share:
                //A chooser is displayed enabling the user to select their method of sharing
                //The note text is passed to the user's selection in plain text
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

        //Query the database and update current layout with appropriate data:

        updateViews();

        //When the edit button for the note is pressed:

        editNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Loading the add / edit note view, passing variables courseId and termId:
                Intent intent = new Intent(getApplicationContext(), CourseNoteEdit.class);
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });
     }

    //Query the database and update current layout with appropriate data:

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