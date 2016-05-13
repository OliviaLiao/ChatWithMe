package com.android.olivia.chatwithus.app.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//import com.android.olivia.chatwithus.app.database.DatabaseHelper;

/**
 * Created by olivia on 5/4/16.
 */
public class DatabaseAdapter {
    public static final String KEY_ROWID = "id";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_USERTYPE = "user_type";
    public static final String KEY_TIME = "time";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "ChatWithUs";
    private static final String DATABASE_TABLE = "chathistory";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table if not exists chathistory (id integer primary key autoincrement, "
                    + "message VARCHAR not null, user_type VARCHAR, time user_type);";

    private final Context context;

    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DatabaseAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS number");
            onCreate(db);
        }
    }

    //---opens the database---
    public DatabaseAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() {
        DBHelper.close();
    }

    //---insert a record into the database---
    public long insertRecord(String message, String userType, String messageTime) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MESSAGE, message);
        initialValues.put(KEY_USERTYPE, userType);
        initialValues.put(KEY_TIME, messageTime);
        Log.i(TAG, "insert");
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular record---
    public boolean deleteContact(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the records---
    public Cursor getAllRecords() {
        return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_MESSAGE, KEY_USERTYPE, KEY_TIME},
                null, null, null, null, null);
    }

    //---retrieves a particular record---
    public Cursor getRecord(long rowId) throws SQLException {
        Log.i(TAG, "getdata");
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_MESSAGE, KEY_USERTYPE, KEY_TIME},
                        KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a record---
    public boolean updateRecord(long rowId, String message, String userType, String time) {
        ContentValues args = new ContentValues();
        args.put(KEY_MESSAGE, message);
        args.put(KEY_USERTYPE, userType);
        args.put(KEY_TIME, time);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
