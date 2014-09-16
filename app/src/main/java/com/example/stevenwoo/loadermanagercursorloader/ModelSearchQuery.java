package com.example.stevenwoo.loadermanagercursorloader;

/**
 * Created by stevenwoo on 9/15/14.
 */
public class ModelSearchQuery {
    private long mId;
    private String mSearchQuery;
    private int mCount;
    private long mUpdatedAt;

    public String toString(){
        return "query:" + mSearchQuery + " mUpdatedAt:" + mUpdatedAt + " mCount:" +mCount;
    }

    public long getId() {
        return mId;
    }

    public void setId(long Id) {
        this.mId = Id;
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.mSearchQuery = searchQuery;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int searchCount) {
        this.mCount = searchCount;
    }

    public long getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.mUpdatedAt = updatedAt;
    }
}
