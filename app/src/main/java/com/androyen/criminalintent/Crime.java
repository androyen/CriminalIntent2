package com.androyen.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rnguyen on 11/5/14.
 */
public class Crime {

    //Convert crimes to JSON format
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";

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

    //Convert crime to JSON format
    public JSONObject toJSON() throws JSONException {

        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mSolved);
        json.put(JSON_DATE, mDate.toString());
        return json;
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
