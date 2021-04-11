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
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.Calendar;
import java.util.List;

public class TermsList extends AppCompatActivity {

    //TermsList view displays a selectable ListView of all terms currently in the term table

    public static String LOG_TAG = "TermsListActivityLog";
    MainDatabase db;
    ListView listView;
    FloatingActionButton addTermButton;
    int termId;

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
        setContentView(R.layout.activity_terms_list);
        setTitle("Term List");
        listView = findViewById(R.id.termsListView);
        addTermButton = findViewById(R.id.addTermButton);
        db = MainDatabase.getInstance(getApplicationContext());

        //Query the database and update current layout with appropriate data:

        updateList();

        //When a term that is a member of the displayed list is pressed:

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Loading the term detail view, passing variable termId:
                Intent intent = new Intent(getApplicationContext(), TermDetail.class);
                int term_id;
                List<Term> termsList = db.termDao().getTermList();
                term_id = termsList.get(position).getTerm_id();
                intent.putExtra("termId", term_id);
                startActivity(intent);
            }
        });

        //When the add term button under the terms list is pressed:

        addTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //A new blank term is created, populated with default values,
                //and loaded into the add / edit term view
                //variable termId is passed
                Intent intent = new Intent(getApplicationContext(), TermEdit.class);
                Calendar calendar = Calendar.getInstance();
                int dbCount = db.termDao().getTermList().size() + 1;
                Term tempTerm = new Term();
                String tempTermName = "New Term " + dbCount;
                tempTerm.setTerm_name(tempTermName);
                tempTerm.setTerm_start(calendar.getTime());
                tempTerm.setTerm_end(calendar.getTime());
                db.termDao().insertTerm(tempTerm);
                tempTerm = db.termDao().getTermByName(tempTermName);
                termId = tempTerm.getTerm_id();
                intent.putExtra("termId", termId);
                startActivity(intent);
            }
        });
    }

    //Query the database and update current layout with appropriate data:

    private void updateList() {
        List<Term> allTerms = db.termDao().getTermList();
        String[] items = new String[allTerms.size()];
        if(!allTerms.isEmpty()) {
            for (int i = 0; i < allTerms.size(); i++) {
                items[i] = allTerms.get(i).getTerm_name();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}