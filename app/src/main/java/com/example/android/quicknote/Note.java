package com.example.android.quicknote;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class Note {

    /** Title string */
    private String mTitle;

    /** Notes string */
    private String mNotes;

    public Note(String mTitle, String mNotes) {
        this.mTitle = mTitle;
        this.mNotes = mNotes;
    }

    /** Date stamp string */
    private String mDate;

    /** Time stamp string */
    private String mTime;

    public Note(String mTitle, String mNotes, String mDate, String mTime) {
        this.mTitle = mTitle;
        this.mNotes = mNotes;
        this.mDate = mDate;
        this.mTime = mTime;
    }


    public String getmTitle() { return mTitle; }

    public void setmTitle(String mTitle) { this.mTitle = mTitle; }

    public String getmNotes() { return mNotes; }

    public void setmNotes(String mNotes) { this.mNotes = mNotes; }

    public String getmDate() { return mDate; }

    public void setmDate(String mDate) { this.mDate = mDate; }

    public String getmTime(){ return mTime; }

    public void setmTime(String mTime) { this.mTime = mTime; }

}
