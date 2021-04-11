package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TermEdit extends AppCompatActivity {

    //TermEdit view enables the user to add a new term or edit an existing term.

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
        setContentView(R.layout.activity_term_edit);
        setTitle("Add or Edit Term");
        termNamePlainText = findViewById(R.id.termNamePlainText);
        termStartDate = findViewById(R.id.termStartDate);
        termEndDate = findViewById(R.id.termEndDate);
        termSaveButton = findViewById(R.id.termSaveButton);
        db = MainDatabase.getInstance(getApplicationContext());
        intent = getIntent();
        termId = intent.getIntExtra("termId", -1);
        selectedTerm = db.termDao().getTerm(termId);
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        //Query the database and update current layout with appropriate data:

        updateViews();

        //When the save button for the assessment is pressed:

        termSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gathering field entries and inserting into term table
                // via a Term object
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
                    intent.putExtra("termId", termId);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Query the database and update current layout with appropriate data:

    private void updateViews() {
        if (selectedTerm != null) {
            Log.d(TermEdit.LOG_TAG, "selected term is not null");
            Date startDate = selectedTerm.getTerm_start();
            Date endDate = selectedTerm.getTerm_end();
            String tempStart = formatter.format(startDate);
            String tempEnd = formatter.format(endDate);
            termStartDate.setText(tempStart);
            termEndDate.setText(tempEnd);
            termNamePlainText.setText(selectedTerm.getTerm_name());
        } else {
            Log.d(TermEdit.LOG_TAG, "selected term is null");
            selectedTerm = new Term();
        }
    }
}