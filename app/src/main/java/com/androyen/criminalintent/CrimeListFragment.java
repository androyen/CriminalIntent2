package com.androyen.criminalintent;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by rnguyen on 11/5/14.
 */
public class CrimeListFragment extends ListFragment {

    private static final String TAG = CrimeListFragment.class.getSimpleName();
    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set title of the activity
        getActivity().setTitle(R.string.crimes_title);

        //Get list of crimes from singleton
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        //Using ArrayAdapter for this ListView
        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
        setListAdapter(adapter);
    }


    //When user taps on list item
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //Get item from the List Adapter and cast it to Crime
        Crime c = (Crime)(getListAdapter()).getItem(position);
        Log.d(TAG, c.getTitle() + " was clicked.");
    }

}
