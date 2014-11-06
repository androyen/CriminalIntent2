package com.androyen.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rnguyen on 11/5/14.
 */
public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    public Crime() {

        //Generate unique identifier for crime
        mId = UUID.randomUUID();
        //Create date of create
        mDate = new Date();
    }

    //Getter and setter
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    //Need to Override toString() to customize view of ListView
    @Override
    public String toString() {
        return mTitle; //Displaying crime title in ListView
    }
}
