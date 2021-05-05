package com.example.android.digit_span.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.digit_span.data.TestContract.TestEntry;

/**
 * Created by 刘博林 on 7/25/2017.
 */

public class TestsDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="tests.db";
    private  static final int DATABANE_VERSION=1;
    public TestsDbHelper(Context context){
      super(context,DATABASE_NAME,null,DATABANE_VERSION);
    };
    public TestsDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE="CREATE TABLE "+ TestEntry.TABLE_NAME+" ("
                +TestEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +TestEntry.TESTER_NAME+" TEXT, "
                +TestEntry.ISAUDITORY +" INTERGER NOT NULL, "
                +TestEntry.ISFORWARD+" INTEGER NOT NULL, "
                +TestEntry.TOP+" INTEGER NOT NULL DEFAULT 0, "
                +TestEntry.ALLRESULT+" TEXT)";
        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
