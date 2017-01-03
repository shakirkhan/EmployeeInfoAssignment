package com.example.shaki.employeeinfocp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Created by shaki on 12/15/2016.
 */





public class EmployeeProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.shaki.employeeinfocp.EmployeeProvider";
    public static final String EMPLOYEE_URI = "info";
    public static final String DETAILS_URI = "details";
    static final String URL1 = "content://" + PROVIDER_NAME + "/" + EMPLOYEE_URI;
    static final String URL2 = "content://" + PROVIDER_NAME + "/" + DETAILS_URI;
    static final Uri CONTENT_URI_EMPLOYEE = Uri.parse(URL1);
    static final Uri CONTENT_URI_DETAILS = Uri.parse(URL2);


    private SQLiteDatabase db;  // why not final can be used? Is it because that the java class doesn't "extends" to SQLiteOpenHelper?
    public static final String EMPLOYEE_CP_DB = "employeeCP.db";
    public static final String EMPLOYEE_CP_TABLE = "employeeTableCP";
    public static final String DETAILS_CP_TABLE = "detailsTableCP";
    public static final String COL_ID = "ID";
//    public static final String COL_IDb = "IDb";
    public static final String COL_NAME = "NAME";
    public static final String COL_DESIGNATION = "DESIGNATION";
    public static final String COL_COMPANY = "COMPANY";
    public static final String COL_SALARY = "SALARY";
    public static final String COL_DoB = "DOB";
    public static final int DB_VERSION =21;
//    static final int uriCode = 1; // how does uriCode helps?


//    private static HashMap<String, String> map; // What does hashMap represents? ans: pair of key values; here I named the HashMap as "map".
    // Without HashMap it is possible to build global query. ans: yes. See the implemetation.

    static final UriMatcher uriM;  // How does UriMatcher work? //See https://developer.android.com/guide/topics/providers/content-provider-creating.html

    public static final int EMPLOYEE_URI_CODE = 1;
    public static final int DETAILS_URI_CODE = 2;

    static {
        uriM = new UriMatcher(UriMatcher.NO_MATCH); // what is NO_MATCH here?
        uriM.addURI(PROVIDER_NAME, EMPLOYEE_URI, EMPLOYEE_URI_CODE);
        uriM.addURI(PROVIDER_NAME, DETAILS_URI, DETAILS_URI_CODE);

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
//        String s =getContext().getPackageManager().getNameForUid(Binder.getCallingUid()); // returning the package name of the child app that is trying to use the query method

        Cursor cursorQ = null; //query method returns the data as a Cursor object. So, Cursor type.
        switch (uriM.match(uriQ)){
            case EMPLOYEE_URI_CODE:
                cursorQ = db.query(EMPLOYEE_CP_TABLE, projection, selection, selectionArgs, null, null, sort); // Using HashMap is avoided
//                queryBuilder.setProjectionMap(map);   // why only here (queryBuilder) HashMap is called? Why not the in inset/update?
                break;                                  // Is it possible not to use/utilize HashMap in query as well?
            case DETAILS_URI_CODE:
                cursorQ = db.query(DETAILS_CP_TABLE, projection, selection, selectionArgs, null, null, sort);
            break;
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
            case EMPLOYEE_URI_CODE:
                return "vnd.android.cursor.dir/" + EMPLOYEE_URI;
            case DETAILS_URI_CODE:
                return "vnd.android.cursor.dir/" + DETAILS_URI;
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
            case EMPLOYEE_URI_CODE:
                isInserted = db.insert(EMPLOYEE_CP_TABLE, null, colValues); // it was null before ""
                if(isInserted>0){
                    ContentUris.withAppendedId(CONTENT_URI_EMPLOYEE, isInserted);
                    getContext().getContentResolver().notifyChange(uriI, null);
                    return uriI;
                }
                break;
            case DETAILS_URI_CODE:
                isInserted = db.insert(DETAILS_CP_TABLE, null, colValues); // it was null before ""
                if(isInserted>0){
                    ContentUris.withAppendedId(CONTENT_URI_DETAILS, isInserted);
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
            case EMPLOYEE_URI_CODE:
                rowsDeleted = db.delete(EMPLOYEE_CP_TABLE, selection, selectionArgs);
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
            case EMPLOYEE_URI_CODE:
                rowsUpdated = db.update(EMPLOYEE_CP_TABLE, colValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uriU);
        }
        getContext().getContentResolver().notifyChange(uriU, null);
        return rowsUpdated;
    }






    public static class DatabaseHelper extends SQLiteOpenHelper{


//        private SQLiteDatabase db;
//        public static final String EMPLOYEE_CP_DB = "employeeCP.db";
//        public static final String tableName = "infoTableCP";
//        public static final String col_1 = "ID";
//        public static final String col_2 = "NAME";
//        public static final String col_3 = "DESIGNATION";
//        public static final String col_4 = "COMPANY";
//        public static final int DB_VERSION =3;



        DatabaseHelper(Context context) {
            super(context, EMPLOYEE_CP_DB, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try{
                db.execSQL("create table "+ EMPLOYEE_CP_TABLE + "(" + COL_ID + " Integer primary key, NAME TEXT, DESIGNATION TEXT, COMPANY TEXT);");
            }
            catch (Exception e){
            }

            try{
                db.execSQL("create table "+ DETAILS_CP_TABLE + "(" + COL_ID + " Integer primary key, SALARY DOUBLE, DOB DATE);");
            }
            catch (Exception e){
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            db.execSQL("DROP TABLE IF EXISTS " + EMPLOYEE_CP_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DETAILS_CP_TABLE);
            onCreate(db);
        }
    }

}

