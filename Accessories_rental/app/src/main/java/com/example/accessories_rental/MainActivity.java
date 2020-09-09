package com.example.accessories_rental;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MyAdapter.onRentListener {
    private FirebaseAuth fauth;
    private String UserId;
    private FirebaseFirestore db;
    private DocumentReference userdata;
    private CollectionReference productcollection;
    BottomNavigationView bottomNavigationView;
    private LinearLayout product;
    private LinearLayout dashboard;
    private LinearLayout about;
    private static final int SUCCESS_CODE = 13;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private FancyButton addproduct;
    private static AppCompatActivity instance;
    TextView Name,Email,Phone;
    FancyButton onrent;
    ListView productslist;
    Button signout;
    ArrayList<String> arrayList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        product = (LinearLayout) findViewById(R.id.Products);
        dashboard = (LinearLayout) findViewById(R.id.Dashboard);
        about = (LinearLayout) findViewById(R.id.About);
        addproduct = (FancyButton) findViewById(R.id.add_product);
        productslist=findViewById(R.id.productListView);
        Name=findViewById(R.id.name);
        Email=findViewById(R.id.email);
        Phone=findViewById(R.id.phoneno);
        instance=this;
        signout=findViewById(R.id.signout);
        signout.setOnClickListener(this);
        fauth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        UserId=fauth.getCurrentUser().getUid();
        productcollection=db.collection("Users").document(UserId).collection("Tenant");
        setUpRecyclerView();

        CollectionReference collectiondata=db.collection("Users").document(UserId).collection("Tenant");
        collectiondata.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    ProductDb documents=documentSnapshot.toObject(ProductDb.class);
                    String name=documents.getProductName();
                    arrayList.add(name);
                    ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);
                    productslist.setAdapter(arrayAdapter);
                }
            }
        });
        userdata=db.collection("Users").document(UserId);
        userdata.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Name.setText(documentSnapshot.getString("username"));
                    Email.setText(documentSnapshot.getString("emailaddress"));
                    Phone.setText(documentSnapshot.getString("phonenumber"));
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Document Does not Exist",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ScreenSlidePagerActivity.class);
                startActivity(intent);

            }
        });

        product.setVisibility(View.GONE);
        dashboard.setVisibility(View.VISIBLE);
        about.setVisibility(View.GONE);



        //Create bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_products:
                                product.setVisibility(View.VISIBLE);
                                dashboard.setVisibility(View.GONE);
                                about.setVisibility(View.GONE);

                                break;
                            case R.id.ic_dashboard:
                                product.setVisibility(View.GONE);
                                dashboard.setVisibility(View.VISIBLE);
                                about.setVisibility(View.GONE);
                                break;
                            case R.id.ic_about:
                                product.setVisibility(View.GONE);
                                dashboard.setVisibility(View.GONE);
                                about.setVisibility(View.VISIBLE);
                                break;
                        }
                        return true;
                    }
                });

        //dialog for adding medicine

    }
    public static Context getContext() {
        return instance.getApplicationContext();
    }

    private void setUpRecyclerView() {
        Query query=productcollection.orderBy("productName",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Product> options=new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();
        adapter=new MyAdapter(options,this);
        RecyclerView recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
     }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signout:

               FirebaseAuth.getInstance().signOut();
                Intent login=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(login);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

    @Override
    public void onRentClick(int position,FancyButton onrent) {
        this.onrent=onrent;
        Intent rent=new Intent(this,RentActivity.class);
        startActivityForResult(rent,SUCCESS_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SUCCESS_CODE){
            if(resultCode == Activity.RESULT_OK){
              Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show();

            }
        }
    }
}
