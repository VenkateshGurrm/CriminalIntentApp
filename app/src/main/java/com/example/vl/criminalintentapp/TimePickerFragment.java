package com.example.vl.criminalintentapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by vl on 11/13/2015.
 */
public class TimePickerFragment extends DialogFragment {

    public static final String TIME = "time";
    private Date mDate;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDate = (Date) getArguments().getSerializable(TIME);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);


        final int year = calendar.get(Calendar.YEAR);
        final int monthOfYear = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final int hours = calendar.get(Calendar.HOUR);
        final int min = calendar.get(Calendar.MINUTE);
        final int am = calendar.get(Calendar.AM_PM);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dailog_time, null);

        TimePicker timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(hours);                      // Set time.
        timePicker.setCurrentMinute(min);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, hourOfDay, min).getTime();
                getArguments().putSerializable(TIME, mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(
                        android.R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }


                        })
                .create();

    }

    private void sendResult(int resultCode){

        Log.d("Venky", "SEn");
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(TIME, mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }

    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TIME, date);

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);

        return timePickerFragment;
    }
}
