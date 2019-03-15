 package com.example.android.quicknote;

import android.content.Context;
import android.database.Cursor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import data.NotesContract;

public class NotesCursorAdapter extends CursorAdapter {

    public NotesCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titleView = (TextView) view.findViewById(R.id.item_title_view);
        TextView notesView = (TextView) view.findViewById(R.id.item_notes_view);
        TextView dateView = (TextView) view.findViewById(R.id.date_stamp_view);
        TextView timeView = (TextView) view.findViewById(R.id.time_stamp_view);

        int titleIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TITLE);
        int notesIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_NOTES);
        int dateIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DATE);
        int timeIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TIME);

        String title = cursor.getString(titleIndex);
        String notes = cursor.getString(notesIndex);
        String date = cursor.getString(dateIndex);
        String time = cursor.getString(timeIndex);

        titleView.setText(title);
        notesView.setText(notes);
        dateView.setText(date);
        timeView.setText(time);
    }
}
