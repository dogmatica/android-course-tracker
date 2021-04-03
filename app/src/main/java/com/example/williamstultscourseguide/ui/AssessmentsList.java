package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.Assessment;
import com.example.williamstultscourseguide.data.MainDatabase;

import java.util.List;

public class AssessmentsList extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "AssessmentsListActivityLog";
    MainDatabase db;
    ListView allAssessmentsList;



    @Override
    protected void onResume() {
        super.onResume();
        updateList();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_list);
        setTitle("All Assessments");
        allAssessmentsList = findViewById(R.id.allAssessmentsList);
        db = MainDatabase.getInstance(getApplicationContext());
        updateList();

        allAssessmentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position clicked: " + position);
                Intent intent = new Intent(getApplicationContext(), AssessmentDetail.class);
                int assessmentId = db.assessmentDao().getAllAssessments().get(position).getAssessment_id();
                //intent.putExtra("courseId", courseId);
                intent.putExtra("assessmentId", assessmentId);
                startActivity(intent);
            }
        });

        //mTextView = (TextView) findViewById(R.id.notesTitle);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateList() {
        List<Assessment> allAssessments = db.assessmentDao().getAllAssessments();
        System.out.println("Number of rows in assessment table: " + allAssessments.size());

        String[] items = new String[allAssessments.size()];
        if(!allAssessments.isEmpty()) {
            for (int i = 0; i < allAssessments.size(); i++) {
                items[i] = allAssessments.get(i).getAssessment_title();
                System.out.println("Assessment in position = " + i + " with name = " + items[i]);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        allAssessmentsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}