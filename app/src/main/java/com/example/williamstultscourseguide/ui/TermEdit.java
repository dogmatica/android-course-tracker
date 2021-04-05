package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TermEdit extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "TermEditActivityLog";
    MainDatabase db;
    EditText termNamePlainText;
    EditText termStartDate;
    EditText termEndDate;
    FloatingActionButton termSaveButton;
    int termId;
    SimpleDateFormat formatter;
    Intent intent;
    Term selectedTerm;
    Date newStartDate;
    Date newEndDate;

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);
        setTitle("Add or Edit Term");
        termNamePlainText = findViewById(R.id.termNamePlainText);
        termStartDate = findViewById(R.id.termStartDate);
        termEndDate = findViewById(R.id.termEndDate);
        termSaveButton = findViewById(R.id.termSaveButton);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        System.out.println("Current term is " + String.valueOf(termId));
        selectedTerm = db.termDao().getTerm(termId);
        System.out.println("current term name is " + selectedTerm.getTerm_name());
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        updateViews();

        termSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("save term button pressed");
                try {
                    Term newTerm = new Term();
                    newStartDate = formatter.parse(String.valueOf(termStartDate.getText()));
                    newEndDate = formatter.parse(String.valueOf(termEndDate.getText()));
                    newTerm.setTerm_id(termId);
                    newTerm.setTerm_name(String.valueOf(termNamePlainText.getText()));
                    newTerm.setTerm_start(newStartDate);
                    newTerm.setTerm_end(newEndDate);
                    db.termDao().updateTerm(newTerm);
                    Intent intent = new Intent(getApplicationContext(), TermsList.class);
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
        if (selectedTerm != null) {
            Log.d(TermEdit.LOG_TAG, "selected term is not null");
            Date startDate = selectedTerm.getTerm_start();
            Date endDate = selectedTerm.getTerm_end();
            System.out.println("Millisecond date: " + startDate.toString());
            String tempStart = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            System.out.println("Formatted Date: " + tempStart);
            termStartDate.setText(tempStart);
            termEndDate.setText(tempEnd);
            termNamePlainText.setText(selectedTerm.getTerm_name());
        } else {
            Log.d(TermEdit.LOG_TAG, "selected term is null");
            selectedTerm = new Term();
        }
    }
}