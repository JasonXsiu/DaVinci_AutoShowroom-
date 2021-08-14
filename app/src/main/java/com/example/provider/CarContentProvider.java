package com.example.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class CarContentProvider extends ContentProvider {
    CarDatabase database;
    public static final String CONTENT_AUTHORITY = "fit2081.app.jason_CY";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final int SINGLE_ROW_ITEMS = 1;
    private static final int MULTIPLE_ROWS_ITEMS = 2;
    public CarContentProvider() {
    }
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        database = CarDatabase.getDatabase(getContext());
        return false;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        //Return the num of rows deleted
        int deletionCount = 0;
        deletionCount= database
                .getOpenHelper()
                .getWritableDatabase()
                .delete("cars",selection,selectionArgs);
        return deletionCount;

    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long rowId = database
                .getOpenHelper()
                .getWritableDatabase()
                .insert("cars", 0, values);

        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // TODO: Implement this to handle query requests from clients.
        //build a SQL query
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        //nevigate to the table
        builder.setTables("cars");
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);
        final Cursor cursor = database
                .getOpenHelper()
                .getReadableDatabase()
                .query(query, selectionArgs);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        int updateCount;
        updateCount = database
                .getOpenHelper()
                .getWritableDatabase()
                .update("cars", 0, values, selection, selectionArgs);

        return updateCount;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }



}