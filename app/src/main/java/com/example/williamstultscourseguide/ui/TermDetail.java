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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    /*
    TermDetail view displays name, start and end dates,
    as well as all courses for the term
    */

    public static String LOG_TAG = "TermDetailActivityLog";
    MainDatabase db;
    FloatingActionButton addTermCourseButton;
    FloatingActionButton editTermButton;
    FloatingActionButton deleteTermButton;
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
        setContentView(R.layout.activity_term_detail);
        setTitle("Term Details");
        addTermCourseButton = findViewById(R.id.addTermCourseButton);
        editTermButton = findViewById(R.id.editTermButton);
        deleteTermButton = findViewById(R.id.deleteTermButton);
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

        //Query the database and update current layout with appropriate data:

        updateViews();
        updateList();

        //When a course that is a member of the displayed list is pressed:

        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Loading the course detail view, passing variable termId and courseId
                Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
                int courseId = db.courseDao().getCourseList(termId).get(position).getCourse_id();
                intent.putExtra("termId", termId);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });

        //When the edit button for the term is pressed:

        editTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Loading the add / edit term view, passing variable termId
                Intent intent = new Intent(getApplicationContext(), TermEdit.class);
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });

        //When the add course button under the courses list is pressed:

        addTermCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A new blank course is created, populated with default values,
                //and loaded into the add / edit course view
                //variables courseId and termId are passed
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
                startActivity(intent);
            }
        });

        //When the delete button for the term is pressed

        deleteTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A confirmation dialog is shown asking the user
                // if they are sure they want to delete:
                AlertDialog dialog = new AlertDialog.Builder(TermDetail.this).setTitle("Confirm").setMessage("Delete Term?").setPositiveButton("Ok", null).setNegativeButton("Cancel", null).show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                //When the user clicks "Ok" to delete:
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int numCourses = db.courseDao().getCourseList(termId).size();
                        if (numCourses == 0) {
                            String termTitle = selectedTerm.getTerm_name();
                            db.termDao().deleteTerm(termId);
                            Toast.makeText(getApplicationContext(), "Term " + termTitle + " was deleted.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), TermsList.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Cannot delete a term that has courses.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //If the user clicks cancel, the previous screen is restored
            }
        });
    }

    //Query the database and update current layout with appropriate data:

    private void updateList() {
        List<Course> allTermCourses = db.courseDao().getCourseList(termId);
        String[] items = new String[allTermCourses.size()];
        if(!allTermCourses.isEmpty()) {
            for (int i = 0; i < allTermCourses.size(); i++) {
                items[i] = allTermCourses.get(i).getCourse_name();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        courseList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Query the database and update current layout with appropriate data:

    private void updateViews() {
        if (selectedTerm != null) {
            Log.d(TermDetail.LOG_TAG, "selected term is not null");
            Date startDate = selectedTerm.getTerm_start();
            Date endDate = selectedTerm.getTerm_end();
            String tempStart = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            termStartTextView.setText(tempStart);
            termEndTextView.setText(tempEnd);
            termTitleTextView.setText(selectedTerm.getTerm_name());
        } else {
            Log.d(TermDetail.LOG_TAG, "selected term is null");
            selectedTerm = new Term();
        }
    }
}