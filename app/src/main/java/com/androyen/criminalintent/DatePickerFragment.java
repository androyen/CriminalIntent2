package com.androyen.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import java.util.Date;

/**
 * Created by rnguyen on 11/6/14.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.androyen.criminalintent.date";
    private Date mDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Inflate DatePicker widget
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null) //null value means no listener on the Ok button
                .create();
    }

    //Create static method to stash date in Fragment arguments
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        //Create new DatePickerFragment
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

}
