package com.androyen.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rnguyen on 11/5/14.
 */
public class CrimeFragment extends Fragment {

    private static final String TAG = CrimeFragment.class.getSimpleName();

    public static final String EXTRA_CRIME_ID = "com.androyen.criminalintent.crime_id";

    //TAG for DatePickerFragment
    private static final String DIALOG_DATE = "date";

    //Constant for setTargetFragment request code
    private static final int REQUEST_DATE = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); //Allows fragment to call Options Menu methods

        //DIRECT ACCESS FOR FRAGMENT TO ACCESS INTENT EXTRA
//        //Get the Crime ID from CrimeListFragment.  Using getSerializableExtra because UUID is a serializable object
//        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        //RETRIEVING THE EXTRA DATA FROM THE LOCAL FRAGMENT ARGUMENT STASH
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        //Get static method from CrimeLab the Crime from the UUID
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    //Create newInstance() static method. Used by Fragment to store data on a Bundle stash of the crime
    //Hosting activity will call this static method besides default constructor.
    //Bundle stash must be attached after fragment creation but before attached to hosting activity
    public static CrimeFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        //Putting serializable since we are stashing the crime UUID
        args.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        //Create the view    False to not attact it to parent view
        View v = inflater.inflate(R.layout.fragment_crime, parent, false);


        //Enable the Action Bar app icon as an UP icon. Set conditions to display this on API 11
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //Check if there is a parent activity in the manifest
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Set the crime title from EditText
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.crime_date);
//        mDateButton.setText(mCrime.getDate().toString());
        //REFACTOR CODE
        updateDate();

//        mDateButton.setEnabled(false);  //Disables the click on the button

        //Implement the DatePickerFragment dialog
        mDateButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Get and create DatePickerFragment
                FragmentManager fm = getActivity().getSupportFragmentManager();

                //Getting the date stashed in DatePickerFragment
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());

                //Set target Fragment CrimeFragment to send date
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);

                //Add dialog to FragmentManager to put on the screen
                dialog.show(fm, DIALOG_DATE);

            }
        });

        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set the crime's solved property
                mCrime.setSolved(isChecked);

            }
        });


        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_ImageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Start CrimeCameraFragment to open camera
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivity(i);
            }
        });

        //If camera is not available, diable camera functionalith
        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras() > 0);
        if (!hasACamera) {
            mPhotoButton.setEnabled(false);
        }

        return v;
    }

    //Implement UP icon in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                //Check if there is a Parent Activity Meta Tag
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity()); //Navigate to parent activity
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Overriding onActivityResult to get date from Intent extra
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Check the result code passed in
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            //Get the date from the Intent extra sent
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            //Set date on the crime
            mCrime.setDate(date);

            //Refresh Date button text to new date
            updateDate();
        }
    }

    //Refactor code to update Date button
    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());

    }

    //Saving the crimes data during onPause()
    @Override
    public void onPause() {
        super.onPause();

        //From singleton, get all the crimes and save it to disk
        CrimeLab.get(getActivity()).saveCrimes();
    }

}
