package com.androyen.criminalintent;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by rnguyen on 11/5/14.
 */
public class CrimeLab {

    //Serializing the Crimes to save
    private static final String TAG = CrimeLab.class.getSimpleName();
    private static final String FILENAME = "crimes.json";
    private CriminalIntentJSONSerializer mSerializer;

    //Create Singleton of stash of crimes
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private ArrayList<Crime> mCrimes;


    private CrimeLab(Context appContext) {
        mContext = appContext;
//        mCrimes = new ArrayList<Crime>();
//        //Load 100 crimes
//        for (int i = 0; i < 100; i++) {
//            Crime c = new Crime();
//            c.setTitle("Crime # " + i );
//            c.setSolved(i % 2 == 0);
//            mCrimes.add(c);
//        }

        //Grab the context of CrimeLab and save the list of crimes to disk crimes.json
        mSerializer = new CriminalIntentJSONSerializer(mContext, FILENAME);


        //Call to load the list of crimes from disk to the ArrayList when first accessed
        try {
            mCrimes = mSerializer.loadCrimes();
        }
        catch (Exception e) {
            //If there is no existing Crime ArrayList, create a new empty one
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ",  e);
        }
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

    //Return true/false if able to serialize and save crimes
    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }

    //Add crime from the Action Bar
    public void add(Crime c) {
        mCrimes.add(c);
    }

    //Delete crime from contextual menu
    public void deleteCrime(Crime c) {
        mCrimes.remove(c);
    }

}
