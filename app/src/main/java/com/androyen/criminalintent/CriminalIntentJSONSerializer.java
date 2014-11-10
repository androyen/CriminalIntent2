package com.androyen.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by rnguyen on 11/9/14.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context c, String f) {

        mContext = c;
        mFilename = f;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {

        //Build an array in JSON
        JSONArray array = new JSONArray();
        //Loop through each crime to convert to JSON
        for (Crime c: crimes) {
            array.put(c.toJSON());
        }

        //Write the file to disk
        Writer writer = null;
        try {
            //Create output stream to the file system to write
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            //Chain OutputStream writer to the low level bytes of OutputStream to convert Java Strings
            writer = new OutputStreamWriter(out);
            //write Crimes array to file
            writer.write(array.toString());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }

    }


}
