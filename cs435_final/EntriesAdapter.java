package com.example.cs435_final;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EntriesAdapter extends RecyclerView.Adapter{

    ImagesSQLiteHelper imagesSQLiteHelper;
    EntriesAdapterListener entriesAdapterListener;

    public interface EntriesAdapterListener{
        public void click(int position);
    }

    public EntriesAdapter(ImagesSQLiteHelper imagesSQLiteHelper, EntriesAdapterListener entriesAdapterListener) {
        this.imagesSQLiteHelper = imagesSQLiteHelper;
        this.entriesAdapterListener = entriesAdapterListener;
    }

    class EntryHolder extends RecyclerView.ViewHolder{
        TextView textViewDesc;
        TextView textViewDate;
        ImageView imageViewEntry;
        public EntryHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDateEntry);
            textViewDesc = itemView.findViewById(R.id.textViewDescEntry);
            imageViewEntry = itemView.findViewById(R.id.imageViewEntry);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    entriesAdapterListener.click(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entries_layout_recyclerview
            ,parent, false);
        return new EntryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String date = imagesSQLiteHelper.getDate(EntriesActivity.statePosition +1, position);
        String description = imagesSQLiteHelper.getDescription(EntriesActivity.statePosition +1, position);
        Bitmap bitmap = imagesSQLiteHelper.getImage(EntriesActivity.statePosition +1, position);

        EntryHolder entryHolder = (EntryHolder) holder;
        entryHolder.textViewDate.setText(date);
        entryHolder.textViewDesc.setText(description);
        entryHolder.imageViewEntry.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return imagesSQLiteHelper.count(EntriesActivity.statePosition +1);
    }
}
