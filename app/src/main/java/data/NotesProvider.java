package data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quicknote.AddNotes;
import com.example.android.quicknote.R;

public class NotesProvider extends ContentProvider {

    /**
     * Uri matcher number for entire notes table
     */
    public static final int NOTES_CODE = 0;

    /**
     * Uri matcher number for particular row with id in the notes table
     */
    public static final int NOTES_CODE_WITH_ID = 1;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.NOTES_PATH, NOTES_CODE);
        sUriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.NOTES_PATH + "/#", NOTES_CODE_WITH_ID);
    }

    /**
     * NotesDbHelper object
     */
    NotesDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new NotesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {

            case NOTES_CODE:
                cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            case NOTES_CODE_WITH_ID:
                selection = NotesContract.NotesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Can not query unknown uri" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES_CODE:
                return insertNotes(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not possible with unknown uri : " + uri);
        }

    }

    /**
     * When we save data or insert data, the insert method will add now row and return a content uri
     * with now rowid of newly added items
     *
     * @param uri
     * @param contentValues
     * @return
     */
    public Uri insertNotes(Uri uri, ContentValues contentValues) {
        String title = contentValues.getAsString(NotesContract.NotesEntry.COLUMN_TITLE);
        String notes = contentValues.getAsString(NotesContract.NotesEntry.COLUMN_NOTES);

        if (title.isEmpty()) {
            throw new IllegalArgumentException("Notes require a title");
        }

        if (notes.isEmpty()) {
            throw new IllegalArgumentException("Notes require a valid data");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long newRowId = database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, newRowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES_CODE:
                rowsDeleted = database.delete(NotesContract.NotesEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case NOTES_CODE_WITH_ID:
                selection = NotesContract.NotesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(NotesContract.NotesEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Update is not possible with unknown uri" + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES_CODE:
                return updateNotes(uri, contentValues, selection, selectionArgs);

            case NOTES_CODE_WITH_ID:
                selection = NotesContract.NotesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNotes(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not possible with unknown uri: " + uri);
        }
    }


    private int updateNotes(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated;
        if (values.containsKey(NotesContract.NotesEntry.COLUMN_TITLE)) {
            String title = values.getAsString(NotesContract.NotesEntry.COLUMN_TITLE);
            if (title.isEmpty()) {
                throw new IllegalArgumentException("Require title");
            }
        }

        if (values.containsKey(NotesContract.NotesEntry.COLUMN_NOTES)) {
            String notes = values.getAsString(NotesContract.NotesEntry.COLUMN_NOTES);
            if (notes.isEmpty()) {
                throw new IllegalArgumentException("Require notes");
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        rowsUpdated = database.update(NotesContract.NotesEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}



