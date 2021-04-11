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
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.MainDatabase;
import java.util.List;

public class AssessmentsList extends AppCompatActivity {

    //AssessmentsList view displays a selectable ListView of all assessments currently in the assessment table

    public static String LOG_TAG = "AssessmentsListActivityLog";
    MainDatabase db;
    ListView allAssessmentsList;

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
        setContentView(R.layout.activity_assessments_list);
        setTitle("All Assessments");
        allAssessmentsList = findViewById(R.id.allAssessmentsList);
        db = MainDatabase.getInstance(getApplicationContext());

        //Query the database and update current layout with appropriate data:

        updateList();

        //When an assessment that is a member of the displayed list is pressed:

        allAssessmentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Loading the assessment detail view, passing variables courseId and assessmentId:
                Intent intent = new Intent(getApplicationContext(), AssessmentDetail.class);
                int assessmentId = db.assessmentDao().getAllAssessments().get(position).getAssessment_id();
                int courseId = db.assessmentDao().getAllAssessments().get(position).getCourse_id_fk();
                intent.putExtra("courseId", courseId);
                intent.putExtra("assessmentId", assessmentId);
                startActivity(intent);
            }
        });

     }

    //Query the database and update current layout with appropriate data:

    private void updateList() {
        List<Assessment> allAssessments = db.assessmentDao().getAllAssessments();
        //String array is created from the database query results
        String[] items = new String[allAssessments.size()];
        if(!allAssessments.isEmpty()) {
            for (int i = 0; i < allAssessments.size(); i++) {
                items[i] = allAssessments.get(i).getAssessment_title();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        allAssessmentsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}