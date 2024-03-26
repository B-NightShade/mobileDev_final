package com.example.cs435_final;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ListView listViewStates;
    ArrayList<String>states;
    ArrayAdapter<String>arrayAdapter;
    ImagesSQLiteHelper imagesSQLiteHelper;
    static boolean lightmode;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.light) {
            if (lightmode == true) {
                Log.v("here", "light");
                lightmode = false;
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
            }else{
                Log.v("here", "dark");
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);
                lightmode = true;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imagesSQLiteHelper = new ImagesSQLiteHelper(getApplicationContext());
        imagesSQLiteHelper.getWritableDatabase();

        listViewStates = findViewById(R.id.listViewStates);
        states = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(getAssets().open("states.txt")));
            String line;
            int count = imagesSQLiteHelper.getStateCount();
            while((line = bufferedReader.readLine())!= null){
                Log.v("hey", line);
                states.add(line);
                if (count == 0){
                    imagesSQLiteHelper.insertState(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter<String>(
              getApplicationContext(),
              R.layout.list_view_home,
              R.id.textViewMainList,
              states
        );


        listViewStates.setAdapter(arrayAdapter);

        listViewStates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), EntriesActivity.class);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });
    }
}