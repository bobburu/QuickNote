package data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NotesContract {

    public NotesContract() {
    }

    /** Content authority */
    public static final String CONTENT_AUTHORITY = "com.example.android.quicknote";

    /** path for the table - notes */
    public static final String NOTES_PATH = "notes";

    /** Base content uri */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static class NotesEntry implements BaseColumns{

        /** Content Uri */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, NOTES_PATH);

        /** Table name - 'notes' string */
        public static final String TABLE_NAME = "notes";

        /** Table columns - 'id, title, notes' names strings */
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOTES = "notes";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";

    }
}
