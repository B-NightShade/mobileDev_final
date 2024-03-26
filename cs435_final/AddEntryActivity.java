package com.example.cs435_final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class AddEntryActivity extends AppCompatActivity
                                implements DatePickerDialog.OnDateSetListener{

    String date = "";
    String description = "";
    String replaced = "";
    Uri imageSelected;
    TextView textViewDate;
    ImageView imageViewPicked;
    EditText EditTextDescription;
    public final int PICKED = 200;
    ImagesSQLiteHelper imagesSQLiteHelper;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        imageViewPicked = findViewById(R.id.imageViewPickedImage);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        month = i1+1;
        day = i2;
        year = i;

        date = year + "/" + month + "/" + day ;
        textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(date);
    }

    public void addDate(View view) {
        DateFragment dateFragment = new DateFragment(this);
        dateFragment.show(getSupportFragmentManager(), "DATE");
    }

    public void pickImage(View view) {
        Log.v("hey", "ugh1");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICKED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICKED){
                imageSelected = data.getData();

                //scale image is used to make sure image is oriented right way
                Bitmap bitmap = scaleIamge(this,imageSelected);

                //removed the / so it can be a path in files
                replaced = String.valueOf(imageSelected).replaceAll("/", "");
                try {
                    //Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageSelected);

                    FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(replaced, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageViewPicked.setImageURI(imageSelected);
            }
        }
    }

    private Bitmap scaleIamge(AddEntryActivity addEntryActivity, Uri imageSelected) {
        //Note: image must be rescaled to fix issues with the image being too large to rotate
        try {
            Bitmap bitmap;
            InputStream inputStream = addEntryActivity.getContentResolver().openInputStream(imageSelected);
            BitmapFactory.Options options = new BitmapFactory.Options();
            //inJustDecodeBounds when set to true lets you alter the details of the bitmap
            options.inJustDecodeBounds = true;
            //open a stream on the image we want with this option set
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            int height, width;
            //this is a function to get the orientation
            int orientation = getOrientation(addEntryActivity, imageSelected);

            //test if they came out sideways if so flip image
            if (orientation == 90 || orientation == 270){
                //options has a get height and width method
                height = options.outWidth;
                width = options.outHeight;
            }else{
                //options has a get height and width method
                height = options.outHeight;
                width = options.outWidth;
            }
            inputStream = addEntryActivity.getContentResolver().openInputStream(imageSelected);
            if(width > 200 || height > 200){
                float widthRatio = ((float)width)/((float)200);
                float heightRatio = ((float)height)/((float)200);
                float ratio = Math.max(widthRatio, heightRatio);

                BitmapFactory.Options options1 = new BitmapFactory.Options();
                //inSampleSize basically rescales image into a ratio that it will fit into based on the math
                options.inSampleSize = (int)ratio;
                //set the options to this
                bitmap = BitmapFactory.decodeStream(inputStream, null, options1);
            }else{
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            inputStream.close();

            //make sure image is oreinted the right direction
            if (orientation > 0){
                Matrix matrix = new Matrix();
                matrix.postRotate(orientation);
            //create our bitmap from the options
                bitmap = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(), bitmap.getHeight(), matrix,true);
            }
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getOrientation(AddEntryActivity addEntryActivity, Uri imageSelected) {
        //The media store can be queried like a sqlite database
        Cursor cursor = addEntryActivity.getContentResolver().query(imageSelected,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION},
                null, null, null);
        if(cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }

    public void addEntry(View view) {
        EditTextDescription = findViewById(R.id.editTextDescription);
        description = String.valueOf(EditTextDescription.getText());
        boolean ok = true;
        if (date.equals("")){
            Toast.makeText(getApplicationContext(), "set date!", Toast.LENGTH_LONG).show();
            ok=false;
            Log.v("here", "date");
        }
        if (description.equals("")){
            Toast.makeText(getApplicationContext(), "set description!", Toast.LENGTH_LONG).show();
            ok = false;
            Log.v("here", "description");
        }
        if(imageViewPicked.getDrawable()==null){
            Toast.makeText(getApplicationContext(), "set image!", Toast.LENGTH_LONG).show();
            ok = false;
            Log.v("here", "image");
        }
        if (ok) {
            imagesSQLiteHelper = new ImagesSQLiteHelper(getApplicationContext());
            Intent intent = getIntent();
            int position = intent.getIntExtra("pos",0);
            imagesSQLiteHelper.insertEntry(year, month, day,date,description,replaced,position);
            EntriesActivity.entriesAdapter.notifyDataSetChanged();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            finish();
        }
    }
}