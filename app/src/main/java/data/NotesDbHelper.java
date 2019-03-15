package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.net.PortUnreachableException;


public class NotesDbHelper extends SQLiteOpenHelper {

    /**
     * Database name
     */
    public static final String DATABASE_NAME = "notes.db";

    /**
     * Database version
     */
    public static final int DATABASE_VERSION = 1;

    /** SQL statement for creating a table */
    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + NotesContract.NotesEntry.TABLE_NAME
            + " (" + NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NotesContract.NotesEntry.COLUMN_TITLE + " TEXT NOT NULL,"
            + NotesContract.NotesEntry.COLUMN_NOTES + " TEXT NOT NULL,"
            + NotesContract.NotesEntry.COLUMN_DATE + " TEXT NOT NULL,"
            + NotesContract.NotesEntry.COLUMN_TIME + " TEXT NOT NULL)";

    /** SQL statement for deleting a table */
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + NotesContract.NotesEntry.TABLE_NAME;

    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** @link onCreate() will create a table with the name defined
     *  in the create statement
     * @param sqLiteDatabase - database object
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
