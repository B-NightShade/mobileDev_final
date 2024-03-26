package com.example.cs435_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryDetailsActivity extends AppCompatActivity {

    TextView textViewDateDetails;
    TextView textViewDescriptionDetails;
    ImageView imageViewDetails;
    ImagesSQLiteHelper imagesSQLiteHelper;
    int position;
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        textViewDateDetails = findViewById(R.id.textViewDateDetail);
        textViewDescriptionDetails = findViewById(R.id.textViewDescDetail);
        imageViewDetails = findViewById(R.id.imageViewDetail);

        imagesSQLiteHelper = new ImagesSQLiteHelper(getApplicationContext());

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        state = intent.getIntExtra("state", 0);

        String date = imagesSQLiteHelper.getDate(state,position);
        String desc = imagesSQLiteHelper.getDescription(state, position);
        Bitmap image = imagesSQLiteHelper.getImage(state, position);

        textViewDescriptionDetails.setText(desc);
        textViewDateDetails.setText(date);
        imageViewDetails.setImageBitmap(image);
    }

    public void deleteEntry(View view) {
        int id = imagesSQLiteHelper.getEntryId(state,position);
        Log.v("hey", String.valueOf(id));
        imagesSQLiteHelper.delete(id);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
        EntriesActivity.entriesAdapter.notifyDataSetChanged();

        finish();
    }
}