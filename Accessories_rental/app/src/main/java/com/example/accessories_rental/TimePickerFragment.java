package com.example.accessories_rental;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.jar.Manifest;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
public SelectTimeFragment.SelectTimeFragmentListener listener;

    public TimePickerFragment(SelectTimeFragment.SelectTimeFragmentListener listener) {
        this.listener=listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c= Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        return new TimePickerDialog(this.getActivity(),this,hour,minute, false);
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        listener.setTime(hourOfDay+":"+minute);
    }

}
