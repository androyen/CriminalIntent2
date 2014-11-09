package com.androyen.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by rnguyen on 11/5/14.
 */
public class CrimeLab {

    //Create Singleton of stash of crimes
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private ArrayList<Crime> mCrimes;


    private CrimeLab(Context appContext) {
        mContext = appContext;
        mCrimes = new ArrayList<Crime>();
//        //Load 100 crimes
//        for (int i = 0; i < 100; i++) {
//            Crime c = new Crime();
//            c.setTitle("Crime # " + i );
//            c.setSolved(i % 2 == 0);
//            mCrimes.add(c);
//        }

    }

    //Getter for this singleton
    public static CrimeLab get(Context c) {

        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {

        //Search through list of crimes and return the specific crime
        for (Crime c : mCrimes) {
            if (c.getId().equals(id)) {
                return c;
            }
        }

        return null;
    }

    //Add crime from the Action Bar
    public void add(Crime c) {
        mCrimes.add(c);
    }

}
