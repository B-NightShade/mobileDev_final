package com.example.cs435_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class EntriesActivity extends AppCompatActivity implements EntriesAdapter.EntriesAdapterListener{

    TextView textViewStateName;
    ImagesSQLiteHelper imagesSQLiteHelper;
    RecyclerView recyclerViewEntries;
    static int statePosition;
    static EntriesAdapter entriesAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.entries_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add){
            Intent intent = new Intent(getApplicationContext(), AddEntryActivity.class);
            intent.putExtra("pos", statePosition +1);
            startActivity(intent);
            return true;
        }
        if (id == R.id.sort){
            SortFragment sortFragment = new SortFragment();
            sortFragment.show(getSupportFragmentManager(), "SORT");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries);
        textViewStateName = findViewById(R.id.textViewStateName);
        imagesSQLiteHelper = new ImagesSQLiteHelper(getApplicationContext());

        Intent intent = getIntent();
        statePosition = intent.getIntExtra("position", 0);
        String state = imagesSQLiteHelper.getState(statePosition);

        textViewStateName.setText(state);

        recyclerViewEntries = findViewById(R.id.recyclerViewEntries);
        entriesAdapter = new EntriesAdapter(imagesSQLiteHelper, this);
        recyclerViewEntries.setAdapter(entriesAdapter);
        recyclerViewEntries.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    public void click(int position) {
        Intent intent = new Intent(getApplicationContext(),EntryDetailsActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("state",statePosition+1);
        startActivity(intent);
    }
}