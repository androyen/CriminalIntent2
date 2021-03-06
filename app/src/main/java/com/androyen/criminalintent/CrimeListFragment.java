package com.androyen.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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

    //Save subtitle state during rotation
    private boolean mSubtitleVisible;

    private Callbacks mCallbacks;

    //Implement Callback interfrace for hosting activities
    public interface Callbacks {
        void onCrimeSelected(Crime crime);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); //Tells Fragment do receive Options Menu callback from Activity

        //Retain fragment during rotation
        setRetainInstance(true);
        mSubtitleVisible = false;

        //Set title of the activity
        getActivity().setTitle(R.string.crimes_title);

        //Get list of crimes from singleton
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

        //Using ArrayAdapter for this ListView
//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        setListAdapter(adapter);
    }

    //Override onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);


        //Set subtitle if flag is true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                getActivity().getActionBar().setSubtitle(R.string.subtitle);
            }
        }

        //Flag to register contextual menu. Registering it with default Android listView
        ListView listView = (ListView)v.findViewById(android.R.id.list);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //Use the floating context menus for Froyo and Gingerbread devices
            registerForContextMenu(listView);
        }
        else {
            //Use contextual action bar.   Enables multiples items to be deleted
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            //Callbacks for the contextual menu
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter)getListAdapter(); //Get adapter
                            CrimeLab crimeLab = CrimeLab.get(getActivity()); //Get list of crimes
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish(); //prepares action mode to be destroyed
                            adapter.notifyDataSetChanged();
                            return true;

                        default:
                            return false;

                    }

                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        return v;
    }


    //When user taps on list item
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        //Get item from the List Adapter and cast it to Crime
        Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);

//        //Start CrimePagerActivity and send the Crime UUID
//        Intent i = new Intent(getActivity(), CrimePagerActivity.class);
//        i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
//        startActivity(i);
        mCallbacks.onCrimeSelected(c);
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

        //If there is a subtitle set,  hide title
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_item_new_crime:
                //Create new crime and add it to mCrimes. Open it with CrimePagerActivity
                Crime crime = new Crime();
                mCrimes.add(crime);
//                //Send as extra to CrimeFragment
//                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
//                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId()); //To send as intent, need object to implement Serializable
//                startActivityForResult(i, 0);
                mCallbacks.onCrimeSelected(crime);
                return true;

            case R.id.menu_item_show_subtitle:

                //Check if there is a subtitle in the Action Bar
                if (getActivity().getActionBar().getSubtitle() == null) {
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    //Set title
                    item.setTitle(R.string.hide_subtitle);

                    //Set the subtitle state during rotation
                    mSubtitleVisible = true;
                }
                else {
                    //If there is a  title. Do not set
                    getActivity().getActionBar().setSubtitle(null);
                    //Set title
                    item.setTitle(R.string.show_subtitle);

                    mSubtitleVisible = false;
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Create preHoneyComb Contextual menu to delete
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Get menu info including position of the crime
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        //Get position of the listView
        int position = info.position;
        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
        //Get crime from the position
        Crime crime = adapter.getItem(position);

        switch (item.getItemId()) {

            case R.id.menu_item_delete_crime:
                //Get the singleton method of crimes. Chain it to delete the crime
                CrimeLab.get(getActivity()).deleteCrime(crime);
                //Tell listView data has been changed
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void updateUI() {
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
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
