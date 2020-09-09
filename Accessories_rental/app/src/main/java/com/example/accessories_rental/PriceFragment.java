package com.example.accessories_rental;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import javax.annotation.Nullable;

public class PriceFragment extends Fragment {

    public EditText product_price;
    View v;
    private PriceFragment.PriceFragmentListerner listener;

    public interface PriceFragmentListerner{
        void setPriceText(EditText s);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.price, container, false);
        product_price=v.findViewById(R.id.priceEditText);
        listener.setPriceText(product_price);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof PriceFragment.PriceFragmentListerner){
            listener=(PriceFragment.PriceFragmentListerner) context;
        }
        else{
            throw  new RuntimeException(context.toString()+"must implement PriceFragmentListerner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }



}