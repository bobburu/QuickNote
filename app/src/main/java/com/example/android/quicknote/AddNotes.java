package com.example.android.quicknote;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import data.NotesContract.NotesEntry;

public class AddNotes extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    EditText titleTextView;
    EditText notesTextView;
    Uri mCurrentUri;
    private static final int EXISTING_PET_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_notes);

        titleTextView = (EditText) findViewById(R.id.add_title);
        notesTextView = (EditText) findViewById(R.id.add_notes);

        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {
            setTitle("Add Notes");
        } else {
            setTitle("Edit Notes");
            getSupportLoaderManager().initLoader(EXISTING_PET_LOADER, null, this);
        }
    }

    @Override
    public void onBackPressed() {
        showDiscardDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                showDiscardDialog();
                break;

            case R.id.save_opt:
                saveNotes();
                finish();
                break;

            case R.id.delete_opt:
                int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
                if (rowsDeleted != 0) {
                    Toast.makeText(this, "Notes deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error while deleting the notes", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;

            default:
                Toast.makeText(this, "Invalid option", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCurrentUri == null) {
            menu.getItem(1).setVisible(false);
        }

        return true;
    }


    public void saveNotes() {

        String titleString = titleTextView.getText().toString();
        String notesString = notesTextView.getText().toString();

        Date dateAndTime = Calendar.getInstance().getTime();

        String date = formattedDate(dateAndTime);
        String time = formattedTime(dateAndTime);

        ContentValues values = new ContentValues();
        values.put(NotesEntry.COLUMN_TITLE, titleString);
        values.put(NotesEntry.COLUMN_NOTES, notesString);
        values.put(NotesEntry.COLUMN_DATE, date);
        values.put(NotesEntry.COLUMN_TIME, time);


        if (titleString.isEmpty() && notesString.isEmpty()) {
            if (mCurrentUri != null) {
                getContentResolver().delete(mCurrentUri, null, null);
                Toast.makeText(this, "Notes discarded due to empty fields", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Discarded due to empty fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mCurrentUri == null) {

            Uri newUri = getContentResolver().insert(NotesEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Error with saving data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notes saved", Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsUpdated = getContentResolver().update(mCurrentUri, values, null, null);

            if (rowsUpdated == 0) {
                Toast.makeText(this, "Error updating notes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Notes updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String formattedDate(Date dt) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL, yyyy");
        return dateFormat.format(dt);
    }

    private String formattedTime(Date dt) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dt);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {NotesEntry.COLUMN_TITLE, NotesEntry.COLUMN_NOTES};

        return new CursorLoader(this, mCurrentUri, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int titleIndex = cursor.getColumnIndex(NotesEntry.COLUMN_TITLE);
        int notesIndex = cursor.getColumnIndex(NotesEntry.COLUMN_NOTES);

        while (cursor.moveToNext()) {

            String titleString = cursor.getString(titleIndex);
            String notesString = cursor.getString(notesIndex);

            titleTextView.setText(titleString);
            notesTextView.setText(notesString);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        titleTextView.setText("");
        notesTextView.setText("");
    }

    public void showDiscardDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing");
        builder.setPositiveButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}


