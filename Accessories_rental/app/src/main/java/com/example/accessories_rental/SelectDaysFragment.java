package com.example.accessories_rental;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class SelectDaysFragment extends Fragment implements View.OnClickListener{
    Button  date;
    private SelectDaysFragment.SelectDaysFragmentListener listener;
    public interface SelectDaysFragmentListener{
        void setDate(String s);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.select_date, container, false);
        date=rootView.findViewById(R.id.btn_date);
        listener.setDate("default");
        date.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SelectDaysFragment.SelectDaysFragmentListener){
            listener=(SelectDaysFragment.SelectDaysFragmentListener) context;
        }
        else{
            throw  new RuntimeException(context.toString()+"must implement SelectDaysFragmentListerner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Override
    public void onClick(View v) {
        DatePickerFragment dayspick=new DatePickerFragment(listener);
        dayspick.show(getFragmentManager(),"date picker");

    }
}