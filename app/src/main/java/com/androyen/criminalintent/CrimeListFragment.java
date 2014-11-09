package com.androyen.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

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

        setHasOptionsMenu(true); //Tells Fragment do receive Options Menu callback from Activity

        //Set title of the activity
        getActivity().setTitle(R.string.crimes_title);

        //Get list of crimes from singleton
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        //Using ArrayAdapter for this ListView
//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }


    //When user taps on list item
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //Get item from the List Adapter and cast it to Crime
        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);

        //Start CrimePagerActivity and send the Crime UUID
        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
        startActivity(i);
    }

    //Need to persist and update the ListView adapter when the ListView data set is changed
    //Calling in onResume as the changes are made on the top Activity stack
    @Override
    public void onResume() {

        super.onResume();
        //Notify ListAdapter of any changes in the data set
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();

    }

    //Created ActionBar and Options Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_item_new_crime:
                //Create new crime and add it to mCrimes. Open it with CrimePagerActivity
                Crime crime = new Crime();
                mCrimes.add(crime);
                //Send as extra to CrimeFragment
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId()); //To send as intent, need object to implement Serializable
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Create custom adapter as inner class
    private class CrimeAdapter extends ArrayAdapter<Crime> {

        public CrimeAdapter(ArrayList<Crime> crimes) {

            //Not using predefined layout. 0 as param
            super(getActivity(), 0, crimes);
        }



        //Override getView to display custom list items in ListView
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //If there is no view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }

            //Configure view and get the currently, selected crime
            Crime c = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
            //Set the Crime's title
            titleTextView.setText(c.getTitle());
            TextView dateTextView = (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());
            CheckBox solvedCheckBox = (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;


        }



    }

}
