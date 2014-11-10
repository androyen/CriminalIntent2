package com.androyen.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by rnguyen on 11/10/14.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
