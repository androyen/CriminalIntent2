package com.androyen.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by rnguyen on 11/6/14.
 */
public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.androyen.criminalintent.date";
    private Date mDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Get the date from the fragment arguments
        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);

        //Create Calendar object to get year, month, and day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate); //Set calendar to the date from the fragment arguments bundle

        //Get int values for the date from the calendar object
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        //Inflate DatePicker widget
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);

        //Use int values of Calendar to set the DatePicker widget values
        DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //Translate year, month, day into a Date object using a calendar
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();

                //Update argument to preserve new date from screen rotation
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }
        });


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
