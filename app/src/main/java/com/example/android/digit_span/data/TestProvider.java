package com.example.android.digit_span.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.digit_span.data.TestContract.TestEntry;

/**
 * Created by 刘博林 on 7/25/2017.
 */

public class TestProvider extends ContentProvider {
    SQLiteOpenHelper mDbHelper;
    private static final String TAG = "TestProvider";

    @Override
    public boolean onCreate() {
        mDbHelper=new TestsDbHelper(getContext());
        return true;
    }




    @Nullable
    @Override
    public Cursor query( Uri uri,String[] projection, String selection,String[] selectionArgs,String sortOrder) {
        SQLiteDatabase database=mDbHelper.getReadableDatabase();
        Cursor cursor=null;
        int match=sUriMatcher.match(uri);
        switch (match){
            case TEST_ID:
                selection=TestEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
            case TESTS:
                cursor=database.query(TestEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    /*URI matcher codes for the contnet URI for the pets table*/
    private static final int TESTS=100;
    private static final int TEST_ID=101;
    private static final int PLAYER=102;
    private static final int PLAYER_ID=103;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(TestContract.CONTENT_AUTHORITY,TestContract.PATH_TESTS,TESTS);
        sUriMatcher.addURI(TestContract.CONTENT_AUTHORITY,TestContract.PATH_TESTS+"/#",TEST_ID);
        //TODO:add PLAYER
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final int match=sUriMatcher.match(uri);
        switch (match){
            case  TESTS:
                return insertTest(uri,values);
        }
        return null;
    }

    private Uri insertTest(Uri uri, ContentValues values) {
        String name=values.getAsString(TestEntry.TESTER_NAME);
        if(name==null){
            throw new IllegalArgumentException("No Tester Name");
        }
        Integer isForward=values.getAsInteger(TestEntry.ISFORWARD);
        Integer isAuditory=values.getAsInteger(TestEntry.ISAUDITORY);
        if((isForward==null)||isForward!=0&&isForward!=1){
            throw new IllegalArgumentException("ISFORWARD is invalid");
        }
        if((isAuditory==null)||isAuditory!=0&&isAuditory!=1){
            throw new IllegalArgumentException("ISAuditory is invalid");
        }
        Integer top=values.getAsInteger(TestEntry.ISFORWARD);
        if(top<0){
            throw  new IllegalArgumentException("top must be positive integer");
        }
        SQLiteDatabase database=mDbHelper.getWritableDatabase();
        long id=database.insert(TestEntry.TABLE_NAME,null,values);
        if(id==-1){
            Log.e(TAG, "insertTest: Failed to insert row for"+uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri,id);

    }
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match=sUriMatcher.match(uri);
        SQLiteDatabase database=mDbHelper.getWritableDatabase();
        int TestDeleted;
        switch (match){
            case TEST_ID:
                selection=TestEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf((ContentUris.parseId(uri)))};
            case TESTS:
                TestDeleted=database.delete(TestEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw  new IllegalArgumentException("Deletion is not support for "+uri);
        }
        if(TestDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return TestDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match=sUriMatcher.match(uri);
        switch (match){
            case TESTS:
                return updateTest(uri,values,selection,selectionArgs);
            case TEST_ID:
                selection=TestEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateTest(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update failed for "+uri);
        }
    }

    private int updateTest(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase mDataBasde=mDbHelper.getWritableDatabase();
        if(values.size()==0){
            return 0;
        }
        int rowsUpdated=mDataBasde.update(TestEntry.TABLE_NAME,values,selection,selectionArgs);
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return  rowsUpdated;
    }
}
