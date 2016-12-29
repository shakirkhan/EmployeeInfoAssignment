package com.example.shaki.employeeinfocp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Binder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.HashMap;


/**
 * Created by shaki on 12/15/2016.
 */





public class EmployeeProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.shaki.employeeinfocp.EmployeeProvider";
    public static final String DESTINATION = "info";
    static final String URL = "content://" + PROVIDER_NAME + "/" + DESTINATION;
    static final Uri CONTENT_URL = Uri.parse(URL);


    private SQLiteDatabase db;  // why not final can be used? Is it because that the java class doesn't "extends" to SQLiteOpenHelper?
    public static final String dbName = "employeeCP.db";
    public static final String tableName = "infoTableCP";
    public static final String col_1 = "ID";
    public static final String col_2 = "NAME";
    public static final String col_3 = "DESIGNATION";
    public static final String col_4 = "COMPANY";
    public static final int dbVersion =6;
    static final int uriCode = 1; // how does uriCode helps?


//    private static HashMap<String, String> map; // What does hashMap represents? ans: pair of key values; here I named the HashMap as "map".
    // Without HashMap it is possible to build global query. ans: yes. See the implemetation.

    static final UriMatcher uriM;  // How does UriMatcher work? //See https://developer.android.com/guide/topics/providers/content-provider-creating.html
    static {
        uriM = new UriMatcher(UriMatcher.NO_MATCH); // what is NO_MATCH here?
        uriM.addURI(PROVIDER_NAME, DESTINATION, uriCode);
    }



    public boolean onCreate() {

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        db = dbHelper.getWritableDatabase();
        if(db != null){
            return true;
        } else
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uriQ, String[] projection, String selection, String[] selectionArgs, String sort) {  // Building global query
        String s =getContext().getPackageManager().getNameForUid(Binder.getCallingUid()); // returning the package name of the child app that is trying to use the query method

        Cursor cursorQ = null; //query method returns the data as a Cursor object. So, Cursor type.
        switch (uriM.match(uriQ)){
            case uriCode:
                cursorQ = db.query(tableName, projection, selection, selectionArgs, null, null, sort); // Using HashMap is avoided
//                queryBuilder.setProjectionMap(map);   // why only here (queryBuilder) HashMap is called? Why not the in inset/update?
                break;                                  // Is it possible not to use/utilize HashMap in query as well?
            default:
                throw new IllegalArgumentException("Unknown URI " + uriQ); //You may choose to throw this if your provider receives an invalid content URI
        }
//        Cursor c = queryBuilder.query(db, projection, selection, selectionArgs, null, null, null);
//        c.setNotificationUri(getContext().getContentResolver(),uri);
        return cursorQ;
    }

    @Nullable
    @Override
    public String getType(Uri uriG) {           //return the standard MIME type for data such as as text, HTML, or JPEG.
        switch(uriM.match(uriG)){
            case uriCode:
                return "vnd.android.cursor.dir/" + DESTINATION;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uriG);
        }
    }

//    @Nullable
//    @Override
//    public Uri insert(Uri uri, ContentValues colValues) {     // change colValues and try
//
//        long rowID = db.insert(tableName, null, colValues);
//        if(rowID>0){
//            Uri _uri = ContentUris.withAppendedId(CONTENT_URL, rowID); // Why I have to define a new _uri here?
//                                                                       // This _uri is not the same uri defined public Uri...
//                                                                       // In the public, it is already defined as uri, so I can't use the same name here again.
//            getContext().getContentResolver().notifyChange(_uri, null);
//            return _uri;
//        }
//        else{
//            Toast.makeText(getContext(), "Insert Failed", Toast.LENGTH_LONG).show();
//            return null;
//        }
//
//    }

//////////////////////////////////////////////////
////Using Toast in the main activity
//    @Nullable
//    @Override
//    public Uri insert(Uri uri, ContentValues colValues) {
//
//        long isInserted = db.insert(tableName, null, colValues);
//        if(isInserted>0){
//            Uri uriI = ContentUris.withAppendedId(CONTENT_URL, isInserted);
//            getContext().getContentResolver().notifyChange(uriI, null);
//            return uriI;
//        }
//        else{
//            return null;
//        }
//
//    }

///////////////////////////////////////////////////
///Using switch case...

    @Nullable
    @Override
    public Uri insert(Uri uriI, ContentValues colValues) {
        long isInserted; // the insert method returns a content URI for the newly-inserted row, so it is long type instead of int or String.
        switch (uriM.match(uriI)) {
            case uriCode:
                isInserted = db.insert(tableName, null, colValues);
                if(isInserted>0){
                ContentUris.withAppendedId(CONTENT_URL, isInserted);
                getContext().getContentResolver().notifyChange(uriI, null);
                return uriI;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uriI);
                                }
        return null;

    }


//////////////////////////////////////////////////

    @Override
    public int delete(Uri uriD, String selection, String[] selectionArgs) {
        int rowsDeleted; //Return the number of rows deleted, so int type.
        switch(uriM.match(uriD)){
            case uriCode:
                rowsDeleted = db.delete(tableName, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uriD);
        }
        getContext().getContentResolver().notifyChange(uriD, null);
        return rowsDeleted;


    }

    @Override
    public int update(Uri uriU, ContentValues colValues, String selection, String[] selectionArgs) {
        int rowsUpdated;
        switch (uriM.match(uriU)){
            case uriCode:
                rowsUpdated = db.update(tableName, colValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uriU);
        }
        getContext().getContentResolver().notifyChange(uriU, null);
        return rowsUpdated;
    }






    public static class DatabaseHelper extends SQLiteOpenHelper{


//        private SQLiteDatabase db;
//        public static final String dbName = "employeeCP.db";
//        public static final String tableName = "infoTableCP";
//        public static final String col_1 = "ID";
//        public static final String col_2 = "NAME";
//        public static final String col_3 = "DESIGNATION";
//        public static final String col_4 = "COMPANY";
//        public static final int dbVersion =3;



        DatabaseHelper(Context context) {
            super(context, dbName, null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try{
                db.execSQL("create table "+tableName+ "(" + col_1 + " Integer primary key, NAME TEXT, DESIGNATION TEXT,COMPANY TEXT);");
            }
            catch (Exception e){
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
            onCreate(db);
        }
    }

}

