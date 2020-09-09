package com.example.accessories_rental;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ScreenSlidePagerActivity extends FragmentActivity implements View.OnClickListener,
        ProductNameFragment.ProductNameFragmentListerner,
        ProductPictureFragment.ProductPictureFragmentListerner,
        SelectTimeFragment.SelectTimeFragmentListener,
        SelectDaysFragment.SelectDaysFragmentListener,PriceFragment.PriceFragmentListerner{
    // This holds all the currently displayable views, in order from left to right.
    private ArrayList<View> views = new ArrayList<>();
    public static ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Button next;
    private Button cancel;
    private Button save;
    FirebaseFirestore UserData;
    FirebaseAuth registerUsers;
    String UserID;
    EditText product_name,product_price;
    String ProductTime,ProductDate;
    ImageView frontimage,backimage;
    ProductDb db=new ProductDb();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new ScreenSliderPagerAdapter(getSupportFragmentManager());
        mPager.setOffscreenPageLimit(5);
        mPager.setAdapter(mPagerAdapter);

        FragmentManager newFragment = getSupportFragmentManager();
        FragmentTransaction transaction = newFragment.beginTransaction();
        transaction.add(R.id.viewpager, new ProductNameFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        registerUsers= FirebaseAuth.getInstance();
        UserData=FirebaseFirestore.getInstance();
        next = findViewById(R.id.btnnext);
        next.setEnabled(false);
        cancel=findViewById(R.id.btncancel);
        save=findViewById(R.id.btnsave);
        save.setEnabled(false);
        next.setOnClickListener(this);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }
    @Override
    public void setText(EditText s) {
        product_name=s;
        product_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()!=0){
                    next.setEnabled(true);
                    next.setTextColor(Color.parseColor("#3F51B5"));
                }
                else{
                    next.setTextColor(Color.parseColor("#95000000"));
                    next.setEnabled(false);
                }

            }
        });
    }
    @Override
    public void setImageViewFront(ImageView frontimage) {
        this.frontimage=frontimage;
    }

    @Override
    public void setImageViewBack(ImageView backimage) {
        this.backimage=backimage;
    }


    @Override
    public void setTime(String Time) {
        ProductTime=Time;
    }
    @Override
    public void setDate(String s) {
    ProductDate=s;
    }
    @Override
    public void setPriceText(EditText s) {
        product_price=s;
        product_price.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()!=0){
                    next.setTextColor(Color.parseColor("#95000000"));
                    next.setEnabled(false);
                }
                else{

                    next.setEnabled(true);
                    next.setTextColor(Color.parseColor("#3F51B5"));
                }

            }
        });

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnnext:
                if(mPager.getCurrentItem()==0) {
                    String productname = product_name.getText().toString().trim();
                    db.setProductName(productname);
                    mPager.setCurrentItem(mPager.getCurrentItem()+1);

                }
                else if(mPager.getCurrentItem()==1){
                    if(!product_name.getText().toString().isEmpty() && !frontimage.getTag().toString().equals("default") && !backimage.getTag().toString().equals("default")){
                     String fronturi=frontimage.getTag().toString();
                        String backuri=backimage.getTag().toString();
                        db.setProductImage1(fronturi);
                        db.setProductImage2(backuri);
                        mPager.setCurrentItem(mPager.getCurrentItem()+1);
                    }
                    else{
                        Toast.makeText(ScreenSlidePagerActivity.this,"Kindly Set the images",Toast.LENGTH_LONG).show();
                    }
                        }
                else if(mPager.getCurrentItem()==2){

                    if(!ProductTime.equals("default")){
                        db.setProductTime(ProductTime);
                        mPager.setCurrentItem(mPager.getCurrentItem()+1);
                    }
                    else{
                        Toast.makeText(ScreenSlidePagerActivity.this,"Kindly Select the Time",Toast.LENGTH_LONG).show();
                    }
                }
                else if(mPager.getCurrentItem()==3){

                    if(!ProductDate.equals("default")){
                        db.setProductDate(ProductDate);
                        mPager.setCurrentItem(mPager.getCurrentItem()+1);
                        save.setEnabled(true);
                        save.setTextColor(Color.parseColor("#4CAF50"));
                    }
                    else{
                        Toast.makeText(ScreenSlidePagerActivity.this,"Kindly Select the Date",Toast.LENGTH_LONG).show();
                    }
                }

                break;
            case R.id.btncancel:
                this.finish();
                break;
            case R.id.btnsave:
                String productprice = product_price.getText().toString().trim();
                db.setProductAmount(productprice);
                UserID=registerUsers.getCurrentUser().getUid();
                String documentid=UserData.collection("Users").document(UserID).collection("Tenant").document().getId();
                UserData.collection("Users").document(UserID).collection("Tenant").document(documentid).set(db)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "OnSuccess:User is successfully created");
                            }

                        });
                this.finish();
                break;
    }
}
}







