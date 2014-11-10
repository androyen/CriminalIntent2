package com.androyen.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    //load crimes from the filesystem
    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
            //Open and read the file into a StringBuffer
            InputStream in = mContext.openFileInput(mFilename);
            //Chain the low level input stream to the memory BufferedReader
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            //Parse and read the incoming bytes in BufferedReader
            while ((line = reader.readLine()) != null) {
                //Line breaks are omitted and irrelevent
                jsonString.append(line);
            }

            //Parse teh JSON using JSONTokener
            JSONArray array =(JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //Build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        }
        catch (FileNotFoundException e) {
            //Ignore
        }
        finally {
            if (reader != null) {
                reader.close(); //Close the inputstream when done
            }
        }

        return crimes;
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
