package com.example.stevenwoo.loadermanagercursorloader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by stevenwoo on 5/15/14.
 */
public abstract class AbstractDatabaseAdapter {
    private static final String TAG = "AbstractDatabaseAdapter";
    public static final String COLUMN_ANDROID_ID = "_id"; //required column to use CursorAdapter
    protected static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "testdatabase.sqlite";

    protected Context mContext;

    protected static final String TABLE_SEARCH_QUERY_HISTORY = "search_query_history";



    protected static final String TABLE_CREATE_SEARCH_QUERIES ="create table search_query_history (_id integer primary key autoincrement, "+
            "  search text, count integer, updated_at integer  " +
            ")";


    public AbstractDatabaseAdapter(Context context){
        mContext = context;
    }

    public SQLiteDatabase openDb(){
        if( SolomoDatabaseHelper.get(mContext) != null){
            return SolomoDatabaseHelper.get(mContext).getWritableDatabase();
        }
        return null;
    }

    public SQLiteDatabase testDb(){
        if( SolomoDatabaseHelper.get(mContext) != null){
            return SolomoDatabaseHelper.get(mContext).getReadableDatabase();
        }
        return null;
    }


    public void closeDb(){
        if( SolomoDatabaseHelper.get(mContext) != null){
            SolomoDatabaseHelper.get(mContext).close();
        }
    }

    // convenience method to clear out data when user logs out
    public void clearUserContent(){
        if( SolomoDatabaseHelper.get(mContext) != null){
            SolomoDatabaseHelper.get(mContext).onClearUserContent();
        }

    }

    protected static class SolomoDatabaseHelper extends SQLiteOpenHelper {

        private static SolomoDatabaseHelper mSharedInstance;

        private SolomoDatabaseHelper( Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public static synchronized SolomoDatabaseHelper get(Context context){
            if( mSharedInstance == null ){
                mSharedInstance = new SolomoDatabaseHelper(context);
            }
            return mSharedInstance;
        }

        public void onClearUserContent(){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(" delete from " + TABLE_SEARCH_QUERY_HISTORY );
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEARCH_QUERY_HISTORY);
            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_SEARCH_QUERIES);
        }
    }
}
