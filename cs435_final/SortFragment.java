package com.example.cs435_final;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SortFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.fragment_sort,null);
        builder.setView(view);
        builder.setTitle("SORT BY: ");
        builder.setItems(R.array.sorts, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0){
                    Log.v("here", "by date");
                    ImagesSQLiteHelper.addGroupBy = true;
                    EntriesActivity.entriesAdapter.notifyDataSetChanged();
                }
                if (i == 1){
                    Log.v("here", "original");
                    ImagesSQLiteHelper.addGroupBy = false;
                    EntriesActivity.entriesAdapter.notifyDataSetChanged();
                }
            }
        });
        builder.show();
        return super.onCreateDialog(savedInstanceState);
    }
}