package com.androyen.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rnguyen on 11/5/14.
 */
public class CrimeFragment extends Fragment {

    private static final String TAG = CrimeFragment.class.getSimpleName();

    public static final String EXTRA_CRIME_ID = "com.androyen.criminalintent.crime_id";

    //Show picture in ImageView
    private static final String DIALOG_IMAGE = "image";

    //TAG for DatePickerFragment
    private static final String DIALOG_DATE = "date";

    //Constant for setTargetFragment request code
    private static final int REQUEST_DATE = 0;
    //Constant to request photo from CrimeCameraFragment
    private static final int REQUEST_PHOTO = 1;
    //Constant to request contacts from Contacts app
    private static final int REQUEST_CONTACT = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Button mSuspectButton;

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
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        //If camera is not available, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras() > 0);
        if (!hasACamera) {
            mPhotoButton.setEnabled(false);
        }

        mPhotoView = (ImageView)v.findViewById(R.id.crime_ImageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Photo p = mCrime.getPhoto();
                if (p == null) {
                    return;
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);

            }
        });

        //Send report button
        Button reportButton = (Button)v.findViewById(R.id.crime_reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); //Set MIME format of intent
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report)); //Intent create chooser
                startActivity(i);
            }
        });

        mSuspectButton = (Button)v.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
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
        else if (requestCode == REQUEST_PHOTO) {
            //Create a new Photo object and attact it to the crime
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);

            if (filename != null) {

                //Create new Photo and set it to the current crime
                Photo p = new Photo(filename);
                mCrime.setPhoto(p);
                showPhoto();
            }
        }
        else if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData(); //Get Uri data from intent (Contact)

            //Specify which fields you want your query to return values for
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //Perform your query  the contactUri is like a "where clause
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            //Double check that results are returned
            if (c.getCount() == 0) {
                c.close();
                return;
            }

            //Pull out the first column of the first row of data
            //that is your suspects name
            c.moveToFirst();
            String suspect = c.getString(0);
            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);
            c.close();
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

    //Set scaled photo in ImageView
    private void showPhoto() {
        //(Re)Set the image buttons' image based on our photo
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }

        mPhotoView.setImageDrawable(b);
    }

    //Creates 4 strings for the crime report
    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        }
        else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        }
        else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    //Load photo in onStart() to become visible
    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    //Free up memory of the Bitmap images
    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

}
