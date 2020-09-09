package com.example.accessories_rental;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import javax.annotation.Nullable;

public class ProductNameFragment extends Fragment {

    public EditText product_name;
    View v;
    private ProductNameFragmentListerner listener;

    public interface ProductNameFragmentListerner{
        void setText(EditText s);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.product_name, container, false);
        product_name=v.findViewById(R.id.productNameEditText);
        listener.setText(product_name);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ProductNameFragmentListerner){
            listener=(ProductNameFragmentListerner) context;
        }
        else{
            throw  new RuntimeException(context.toString()+"must implement ProductNameFragmentListerner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }


}