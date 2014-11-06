package com.androyen.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by rnguyen on 11/5/14.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment(); //Adding fragment to Activity
    }
}
