package com.androyen.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * Created by rnguyen on 11/6/14.
 */

//Implementing ViewPager
public class CrimePagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instantiate ViewPager and set the View
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        //Get the array of crimes data set
        mCrimes = CrimeLab.get(this).getCrimes();

        //Manage Fragments for ViewPager
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

            @Override
            public Fragment getItem(int position) {

                //Gets the current position of mCrimes array and display it CrimeFragment
                Crime crime = mCrimes.get(position);

                //Call the static method besides the constructor to retrieve the crime Bundle Fragment arguments
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {

                return mCrimes.size();
            }
        });
    }
}
