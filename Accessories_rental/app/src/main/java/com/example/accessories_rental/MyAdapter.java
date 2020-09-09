package com.example.accessories_rental;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class MyAdapter extends FirestoreRecyclerAdapter<Product,MyAdapter.MyHolder>{
    private static final int SUCCESS_CODE = 13;
    Context context = MainActivity.getContext();
    MyHolder holder;
    private onRentListener monRentListener;

    public MyAdapter(@NonNull FirestoreRecyclerOptions<Product> options,onRentListener onRentListener) {
        super(options);
        this.monRentListener=onRentListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull Product model) {
        holder.producttitle.setText(model.getProductName());
        holder.productdate.setText(model.getProductDate());
        holder.producttime.setText(model.getProductTime());
        holder.productprice.setText(model.getProductAmount());
        ArrayList<String> pictureList=new ArrayList<String>();
        pictureList.add(model.getProductImage1());
        pictureList.add(model.getProductImage2());
        ImageAdapter adapter=new ImageAdapter(context,pictureList);
        holder.pager.setAdapter(adapter);
        this.holder=holder;

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater .from(parent.getContext()).inflate(R.layout.product_card,parent,false);
        return new MyHolder(v,monRentListener) ;

    }


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView producttitle,productdate,producttime,productprice;
        ViewPager pager;
        FancyButton onRent;
        onRentListener onRentListener;
        public MyHolder(@NonNull View itemView,onRentListener onRentListener) {
            super(itemView);
            producttitle=itemView.findViewById(R.id.product_title);
            productdate=itemView.findViewById(R.id.date_of_product);
            producttime=itemView.findViewById(R.id.time_of_product);
            productprice=itemView.findViewById(R.id.price_of_product);
            this.onRentListener=onRentListener;
            pager=itemView.findViewById(R.id.media_image);
            onRent=itemView.findViewById(R.id.onrent_button);
            onRent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                onRentListener.onRentClick(getAdapterPosition(),onRent);

        }
    }
    public  interface onRentListener{
        void onRentClick(int position,FancyButton button);
    }

}
