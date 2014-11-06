package com.androyen.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

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
        mViewPager.setOffscreenPageLimit(3);

        //Have ViewPager show the current selected crime in ListView when returned

        //Get crime Id
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);

        //Loop through mCrimes and find the crime with the matching crimeId
        for (int i = 0; i < mCrimes.size(); i++) {

            //If the crime matches the stashed crimeId
            if (mCrimes.get(i).getId().equals(crimeId)) {
                //Set ViewPager to leave off at the current index
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //Set the currently selected crime page to update the ActionBar title
                Crime crime = mCrimes.get(position);
                if (crime.getTitle() != null) {
                    //Set title of the Activity to the crime title
                    setTitle(crime.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
