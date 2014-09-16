package com.example.stevenwoo.loadermanagercursorloader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Created by stevenwoo on 9/15/14.
 */
public class SearchQueryTableAdapter extends AbstractDatabaseAdapter {
    private static final String COLUMN_SEARCH_QUERY = "search";
    private static final String COLUMN_COUNT = "count";
    private static final String COLUMN_UPDATED_AT = "updated_at";

    public void debugDumpEntireTable(){
        Log.i(TABLE_SEARCH_QUERY_HISTORY, "dumping");
        String query = "select * from "+ TABLE_SEARCH_QUERY_HISTORY +"  ORDER BY "+COLUMN_COUNT+" DESC ";
        String parameters[] = {};
        Cursor wrapped = testDb().rawQuery(query, parameters);

        SearchQueryCursor cmc = new SearchQueryCursor(wrapped);

        if( cmc != null ){
            cmc.moveToFirst();
            int index =0;
            for(index = 0; index< cmc.getCount();++index){
                ModelSearchQuery mcm = cmc.getSearchQuery();
                if( mcm != null ){
                    Log.i(TABLE_SEARCH_QUERY_HISTORY, mcm.toString());
                }
                else {
                    Log.i(TABLE_SEARCH_QUERY_HISTORY, "somehow count wrong?");
                }
                cmc.moveToNext();
            }
            cmc.close();
        }
    }


    public SearchQueryTableAdapter(Context context) {
        super(context);
    }

    @Override
    public SQLiteDatabase openDb() {
        return super.openDb();
    }

    @Override
    public SQLiteDatabase testDb() {
        return super.testDb();
    }

    @Override
    public void closeDb() {
        super.closeDb();
    }

    public ContentValues setContent(ModelSearchQuery inputQuery) {
        ContentValues cv = new ContentValues();
        if (inputQuery.getId() != 0) {
            cv.put(COLUMN_ANDROID_ID, inputQuery.getId());
        }
        cv.put(COLUMN_COUNT, inputQuery.getCount());
        cv.put(COLUMN_SEARCH_QUERY, inputQuery.getSearchQuery());
        cv.put(COLUMN_UPDATED_AT, inputQuery.getUpdatedAt());
        return cv;
    }



    public static class SearchQueryCursor extends CursorWrapper {
        public SearchQueryCursor(Cursor c){
            super(c);
        }

        public ModelSearchQuery getSearchQuery(){
            if(isBeforeFirst() || isAfterLast()){
                return null;
            }
            ModelSearchQuery modelSearchQuery = new ModelSearchQuery();
            long localId = getLong(getColumnIndex(COLUMN_ANDROID_ID));
            modelSearchQuery.setId(localId);

            int count = getInt(getColumnIndex(COLUMN_COUNT));
            modelSearchQuery.setCount(count);
            String search = getString(getColumnIndex(COLUMN_SEARCH_QUERY));
            modelSearchQuery.setSearchQuery(search);
            int updatedAt = getInt(getColumnIndex(COLUMN_UPDATED_AT));
            modelSearchQuery.setUpdatedAt(updatedAt);
            return modelSearchQuery;
        }
    }
    public long insertSearchQuery(ModelSearchQuery inputSearchQuery){
        ContentValues cv = setContent(inputSearchQuery);
        return openDb().insert(TABLE_SEARCH_QUERY_HISTORY, null, cv);
    }

    public boolean updateSearchQuery(String query, String parameters[], ModelSearchQuery inputSearchQuery){
        ContentValues cv = setContent(inputSearchQuery);
        return openDb().update(TABLE_SEARCH_QUERY_HISTORY, cv, query , parameters) != 0;
    }

    public boolean updateSearchQuery(ModelSearchQuery inputSearchQuery){
        String query = COLUMN_SEARCH_QUERY + " = ? ";
        String parameters[] = {inputSearchQuery.getSearchQuery()};
        return updateSearchQuery(query, parameters, inputSearchQuery);
    }

    public void startSearchFor(String searchQuery){
        String sqlQuery = "select * from " + TABLE_SEARCH_QUERY_HISTORY + " where " + COLUMN_SEARCH_QUERY + " = ? ";
        String parameters[] = {searchQuery};
        Cursor wrapped = testDb().rawQuery(sqlQuery, parameters);
        SearchQueryCursor searchQueryCursor = new SearchQueryCursor(wrapped);
        long currentMs = System.currentTimeMillis()/1000;

        if( searchQueryCursor != null){
            if( searchQueryCursor.getCount() > 0 ){
                searchQueryCursor.moveToFirst();
                ModelSearchQuery modelSearchQuery = searchQueryCursor.getSearchQuery();
                if( modelSearchQuery != null ){
                    int count = modelSearchQuery.getCount();
                    modelSearchQuery.setCount(count+1);
                    modelSearchQuery.setUpdatedAt(currentMs);
                    updateSearchQuery(modelSearchQuery);
                    return;
                }
            }
            searchQueryCursor.close();
        }

        ModelSearchQuery    modelSearchQuery = new ModelSearchQuery();
        modelSearchQuery.setUpdatedAt(currentMs);
        modelSearchQuery.setCount(1);
        modelSearchQuery.setSearchQuery(searchQuery);
        long row = insertSearchQuery(modelSearchQuery);
    }

}
