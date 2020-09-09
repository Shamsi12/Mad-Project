package com.example.accessories_rental;

import android.Manifest;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.annotation.Nullable;

public class SelectTimeFragment extends Fragment implements View.OnClickListener{
    Button selectime;
    private SelectTimeFragmentListener listener;
    public interface SelectTimeFragmentListener{
        void setTime(String s);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable  ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.select_time, container, false);
        selectime=rootView.findViewById(R.id.btn_time);
        selectime.setOnClickListener(this);
        listener.setTime("default");
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SelectTimeFragment.SelectTimeFragmentListener){
            listener=(SelectTimeFragment.SelectTimeFragmentListener) context;
        }
        else{
            throw  new RuntimeException(context.toString()+"must implement SelectTimeFragmentListerner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }
    @Override
    public void onClick(View v) {

        TimePickerFragment timepick=new TimePickerFragment(listener);
        timepick.show(getFragmentManager(),"time picker");

    }

}