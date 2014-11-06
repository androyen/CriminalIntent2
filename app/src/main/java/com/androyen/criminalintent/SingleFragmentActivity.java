package com.androyen.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by rnguyen on 11/5/14.
 */
public abstract class SingleFragmentActivity extends FragmentActivity {

    //Abstract method to get the current fragment
    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //Add Fragment to Activity
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = createFragment();

        if (fragment == null) {
            fm.beginTransaction().add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
