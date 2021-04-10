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
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.Course;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssessmentEdit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //private TextView mTextView;
    public static String LOG_TAG = "AssessmentEditActivityLog";
    MainDatabase db;
    int termId;
    int courseId;
    int assessmentId;
    SimpleDateFormat formatter;
    Intent intent;
    Term selectedTerm;
    Course selectedCourse;
    Assessment selectedAssessment;
    Date newDueDate;
    Date newGoalDate;
    EditText nameEditText;
    Spinner typeSpinner;
    EditText dueDateEditText;
    EditText goalDateEditText;
    Spinner statusSpinner;
    FloatingActionButton assessmentSaveButton;

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
        setContentView(R.layout.activity_assessment_edit);
        setTitle("Add or Edit Assessment");
        nameEditText = findViewById(R.id.nameEditText);
        dueDateEditText = findViewById(R.id.dueDateEditText);
        goalDateEditText = findViewById(R.id.goalDateEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        assessmentSaveButton = findViewById(R.id.assessmentSaveButton);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.results, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter1);
        statusSpinner.setOnItemSelectedListener(this);

        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", termId);
        courseId = intent.getIntExtra("courseId", courseId);
        assessmentId = intent.getIntExtra("assessmentId", assessmentId);
        System.out.println("current assessment is " + assessmentId);
        selectedCourse = db.courseDao().getCourse(termId, courseId);
        selectedAssessment = db.assessmentDao().getAssessment(courseId, assessmentId);
        System.out.println("current assessment title is" + selectedAssessment.getAssessment_title());
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        updateViews();

        assessmentSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("assessment save button pressed");
                try {
                    Assessment newAssessment = new Assessment();
                    newDueDate = formatter.parse(String.valueOf(dueDateEditText.getText()));
                    newGoalDate = formatter.parse(String.valueOf(goalDateEditText.getText()));
                    newAssessment.setAssessment_id(assessmentId);
                    newAssessment.setCourse_id_fk(courseId);
                    newAssessment.setAssessment_title(String.valueOf(nameEditText.getText()));
                    newAssessment.setAssessment_type(String.valueOf(typeSpinner.getSelectedItem()));
                    newAssessment.setAssessment_due(newDueDate);
                    newAssessment.setAssessment_goal(newGoalDate);
                    newAssessment.setAssessment_status(String.valueOf(statusSpinner.getSelectedItem()));
                    db.assessmentDao().updateAssessment(newAssessment);
                    Intent intent = new Intent(getApplicationContext(), AssessmentDetail.class);
                    intent.putExtra("courseId", courseId);
                    intent.putExtra("assessmentId", assessmentId);
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
        if (selectedAssessment != null) {
            Log.d(AssessmentEdit.LOG_TAG, "selected assessment is not null");
            Date dueDate = selectedAssessment.getAssessment_due();
            Date goalDate = selectedAssessment.getAssessment_goal();
            System.out.println("Millisecond date: " + dueDate.toString());
            String tempDue = formatter.format(dueDate);
            String tempGoal = formatter.format(goalDate);
            System.out.println("Formatted date: " + tempDue);
            nameEditText.setText(selectedAssessment.getAssessment_title());
            dueDateEditText.setText(tempDue);
            goalDateEditText.setText(tempGoal);
        } else {
            Log.d(AssessmentEdit.LOG_TAG, "selected assessment is null");
            selectedAssessment = new Assessment();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}