package com.androyen.criminalintent;

import java.util.UUID;

/**
 * Created by rnguyen on 11/5/14.
 */
public class Crime {

    private UUID mId;
    private String mTitle;

    public Crime() {

        //Generate unique identifier for crime
        mId = UUID.randomUUID();
    }

    //Getter and setter
    public UUID getID() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
