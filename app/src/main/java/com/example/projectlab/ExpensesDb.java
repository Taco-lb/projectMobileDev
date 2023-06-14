package com.example.projectlab;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class ExpensesDb extends ContentProvider{

    static final String PROVIDER_NAME = "com.example.provider.ExpensesApp";
    static final String URL = "content://" + PROVIDER_NAME + "/expenses";
    static final String USERSURL = "content://" + PROVIDER_NAME + "/users";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final Uri CONTENT_URI_USER = Uri.parse(USERSURL);

    //Here I crated final variables since i do not want to change their values
    static final String _ID = "_id";
    static final String EXPNAME = "name";
    static final String EXPRICE = "price";
    static final String EXPDATE = "date";
    static final String EXPTYPE = "type";
    static final String EXPCAT = "category";

    static final String EXPUSERID = "_useridexp";

    static final String _USERID = "_userid";

    static final String USERNAME = "username";

    static final String USERMAIL = "email";

    static final String USERPASSWORD = "password";

    protected static String USERFINAL = "";

    protected static int USERFINALID = 0;


    //Creating a hash map to be able to store the expenses information as pairs
    private static HashMap <String, String> EXPENSES_PROJECTION_MAP;

    private static HashMap <String, String> USERS_PROJECTION_MAP;

    static final int EXP = 1;
    static final int EXP_ID = 2;

    static final int USR = 3;

    static final int USR_ID = 4;
    static final UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "expenses", EXP);
        uriMatcher.addURI(PROVIDER_NAME,"expenses/#", EXP_ID);
        uriMatcher.addURI(PROVIDER_NAME, "users", USR);
        uriMatcher.addURI(PROVIDER_NAME,"users/#", USR_ID);
    }

    //Database declarations
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Expenses";
    static final String EXPENSES_TABLE_NAME = "expenses";
    static final String USERS_TABLE_NAME = "users";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE = " CREATE TABLE " + EXPENSES_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + " name TEXT NOT NULL, " + " price FLOAT NOT NULL, " + " date TEXT NOT NULL," + " type TEXT NOT NULL, " + " category TEXT NOT NULL," + "_useridexp INTEGER, FOREIGN KEY (_useridexp) REFERENCES users(_userid) );";

    static final String CREATE_DB_USERSTABLE = " CREATE TABLE " + USERS_TABLE_NAME + " (_userid INTEGER PRIMARY KEY AUTOINCREMENT, " + " username TEXT NOT NULL, " + " email TEXT NOT NULL," + " password TEXT NOT NULL );";

    //We create a class here in order to create and manage the provider's underlying data.

    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
            db.execSQL(CREATE_DB_USERSTABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + EXPENSES_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            onCreate(db);
        }
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        // Create a writeable database and if already existent it wont.
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        Cursor c = null;

        if (uri.equals(CONTENT_URI)) {

            qb.setTables(EXPENSES_TABLE_NAME);
            switch (uriMatcher.match(uri)) {
                case EXP:
                    qb.setProjectionMap(EXPENSES_PROJECTION_MAP);
                    break;
                case EXP_ID:
                    qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }

            //This if statement makes sure that in case the sortOrder is empty it will direclty sort by
            //name
            if (sortOrder == null || sortOrder.equals("")) {
                sortOrder = EXPNAME;
            }

            c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }

        if (uri.equals(CONTENT_URI_USER)) {

            qb.setTables(USERS_TABLE_NAME);
            switch (uriMatcher.match(uri)) {
                case USR:
                    qb.setProjectionMap(USERS_PROJECTION_MAP);
                    break;
                case USR_ID:
                    qb.appendWhere(_USERID + "=" + uri.getPathSegments().get(1));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            if (sortOrder == null || sortOrder.equals("")) {
                sortOrder = _USERID;
            }
            c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            c.setNotificationUri(getContext().getContentResolver(), uri);

        }

        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            // Get all expenses records
            case EXP:
                return "vnd.android.cursor.dir/vnd.example.expenses";
            // Get a particular expenses
            case EXP_ID:
                return "vnd.android.cursor.item/vnd.example.expenses";
            case USR:
                return "vnd.android.cursor.dir/vnd.example.users";
            case USR_ID:
                return "vnd.android.cursor.item/vnd.example.users";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri _uri = null;
        switch(uriMatcher.match(uri)) {
            case EXP:
                long rowID = db.insert(EXPENSES_TABLE_NAME, "", values);
                //Check if the record was added successfully to the database
                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return uri;
                }
                throw new SQLException("Failed to add a record into " + uri);

            case USR:
                long rowID1 = db.insert(USERS_TABLE_NAME, "", values);
                //Check if the record was added successfully to the database
                if (rowID1 > 0 ){
                    _uri = ContentUris.withAppendedId(CONTENT_URI_USER, rowID1);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return uri;
                }
                throw new SQLException("Failed to add a record into " + uri);

        }
        return uri;
    }

    //fix methods down below

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case EXP:
                count = db.delete(EXPENSES_TABLE_NAME, selection, selectionArgs);
                break;
            case EXP_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(EXPENSES_TABLE_NAME,_ID + " = " + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    @Override
    public int update(Uri uri, ContentValues values,  String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case EXP:
                count = db.update(EXPENSES_TABLE_NAME, values, selection, selectionArgs);
                break;
            case EXP_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(EXPENSES_TABLE_NAME, values,_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
