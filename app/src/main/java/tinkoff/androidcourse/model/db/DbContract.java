package tinkoff.androidcourse.model.db;

import android.provider.BaseColumns;

/**
 * @author Sergey Boishtyan
 */
public final class DbContract {

    static final String CREATE_SCRIPT = "CREATE TABLE " + DialogEntry.TABLE_NAME + " (" +
            DialogEntry._ID + " INTEGER PRIMARY KEY," +
            DialogEntry.COLUMN_TITLE + " TEXT," +
            DialogEntry.COLUMN_TIMESTAMP + " INTEGER," +
            DialogEntry.COLUMN_DESCRIPTION + " TEXT" +
            " )";

    private DbContract() {
        //no instance
    }

    public static final class DialogEntry implements BaseColumns {

        public static final String TABLE_NAME = "dialogs";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
