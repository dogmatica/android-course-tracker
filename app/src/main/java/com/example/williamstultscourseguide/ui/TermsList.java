package com.example.williamstultscourseguide.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.williamstultscourseguide.R;
import com.example.williamstultscourseguide.data.MainDatabase;
import com.example.williamstultscourseguide.data.Term;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TermsList extends AppCompatActivity {

    //private TextView mTextView;
    public static String LOG_TAG = "TermsListActivityLog";
    MainDatabase db;
    ListView listView;
    FloatingActionButton addTermButton;
    @Override
    protected void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);
        setTitle("Term List");
        listView = findViewById(R.id.termsListView);
        addTermButton = findViewById(R.id.addTermButton);
        db = MainDatabase.getInstance(getApplicationContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Position clicked: " + position);
                Intent intent = new Intent(getApplicationContext(), TermDetail.class);
                int term_id;
                List<Term> termsList = db.termDao().getTermList();
                term_id = termsList.get(position).getTerm_id();
                intent.putExtra("termId", term_id);
                System.out.println("termId selected = " + String.valueOf(term_id));
                startActivity(intent);
            }
        });

        updateList();



        //mTextView = (TextView) findViewById(R.id.text);

        // Enables Always-on
        //setAmbientEnabled();
    }

    private void updateList() {
        List<Term> allTerms = db.termDao().getTermList();
        System.out.println("Number of Rows in Terms Table: " + allTerms.size());

        String[] items = new String[allTerms.size()];
        if(!allTerms.isEmpty()) {
            for (int i = 0; i < allTerms.size(); i++) {
                items[i] = allTerms.get(i).getTerm_name();
                System.out.println("Term in position = " + i + " with name = " + items[i]);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}