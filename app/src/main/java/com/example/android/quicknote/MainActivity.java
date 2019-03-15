package com.example.android.quicknote;


import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import data.NotesContract.NotesEntry;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView notesListView;
    NotesCursorAdapter mCursorAdapter;
    private static final int NOTES_LOADER = 0;
    FloatingActionButton fab;
    String cursorFilter;
    MenuItem in, de, searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher_round);

        fab = findViewById(R.id.add_new_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNote = new Intent(MainActivity.this, AddNotes.class);
                startActivity(addNote);

            }
        });


        notesListView = (ListView) findViewById(R.id.notes_list);
        mCursorAdapter = new NotesCursorAdapter(this, null);
        notesListView.setAdapter(mCursorAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent editIntent = new Intent(MainActivity.this, AddNotes.class);
                Uri currentUri = ContentUris.withAppendedId(NotesEntry.CONTENT_URI, id);
                editIntent.setData(currentUri);
                startActivity(editIntent);
            }
        });

        getSupportLoaderManager().initLoader(NOTES_LOADER, null, this);
        invalidateOptionsMenu();

    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    public void insertNewNotes() {

        ContentValues values = new ContentValues();
        values.put(NotesEntry.COLUMN_TITLE, "One night at the CallCenter");
        values.put(NotesEntry.COLUMN_NOTES, getResources().getString(R.string.temp_note));

        Date dateAndTime = Calendar.getInstance().getTime();

        String date = formattedDate(dateAndTime);
        String time = formattedTime(dateAndTime);

        values.put(NotesEntry.COLUMN_DATE, date);
        values.put(NotesEntry.COLUMN_TIME, time);

        Uri newUri = getContentResolver().insert(NotesEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, "Error inserting dummy data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Notes saved", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        in = menu.findItem(R.id.insert_dummy_data_opt);
        de = menu.findItem(R.id.delete_all_notes_opt);
        searchView = menu.findItem(R.id.search_notes_opt);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search_notes_opt:
                final  SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        cursorFilter = !TextUtils.isEmpty(s) ? s : null;
                        getSupportLoaderManager().restartLoader(NOTES_LOADER, null, MainActivity.this);
                        return true;
                    }
                });
                item.getIcon().setVisible(false, false);
                break;

            case R.id.insert_dummy_data_opt:
                insertNewNotes();
                break;

            case R.id.delete_all_notes_opt:
                getContentResolver().delete(NotesEntry.CONTENT_URI, null, null);
                break;

            default:
                Toast.makeText(this, "Invalid data", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu.findItem(R.id.search_notes_opt).isActionViewExpanded()){
            searchView.collapseActionView();
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String selection = null;
        String[] selectionArgs = null;
        if (cursorFilter != null) {
            selection = NotesEntry.COLUMN_TITLE + "=?";
            selectionArgs = new String[]{cursorFilter};
        }

        String[] projection = {NotesEntry._ID,
                NotesEntry.COLUMN_TITLE,
                NotesEntry.COLUMN_NOTES,
                NotesEntry.COLUMN_DATE,
                NotesEntry.COLUMN_TIME};


        return new CursorLoader(this, NotesEntry.CONTENT_URI, projection,
                selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}
