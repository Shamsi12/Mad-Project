package com.example.accessories_rental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RentActivity extends AppCompatActivity implements View.OnClickListener{
    EditText name,emailaddress,phoneNumber,address;
    Button submit;
    FirebaseAuth registration;
    FirebaseFirestore onrent_data;
    String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

            name=findViewById(R.id.inputname);
            emailaddress=findViewById(R.id.inputemail);
            phoneNumber=findViewById(R.id.inputphoneno);
            address=findViewById(R.id.address);
            submit=findViewById(R.id.btnsubmit);
            registration=FirebaseAuth.getInstance();
            onrent_data=FirebaseFirestore.getInstance();
            submit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnsubmit:
                    String getname=name.getText().toString().trim();
                    String getemail=emailaddress.getText().toString().trim();
                    String getphoneno=phoneNumber.getText().toString().trim();
                    String getaddress=address.getText().toString().trim();
                    if(getname.isEmpty() || getemail.isEmpty() || getphoneno.isEmpty() || getaddress.isEmpty()) {

                        if (getname.isEmpty()) {
                            name.setError("This is required field");
                        }
                        if (getemail.isEmpty()) {
                            emailaddress.setError("This is required field");
                        }
                        if (getphoneno.isEmpty()) {
                            phoneNumber.setError("This is required field");
                        }
                        if (getaddress.isEmpty()) {
                            address.setError("This is required field");
                        }

                    }

                    else if(!getemail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.com")){
                        emailaddress.setError("Email is invalid");
                    }
                    else {
                        final RentalDb db=new RentalDb();
                        db.setName(getname);
                        db.setEmailaddress(getemail);
                        db.setPhonenumber(getphoneno);
                        db.setAddress(getaddress);
                        UserID=registration.getCurrentUser().getUid();
                        String documentid=onrent_data.collection("Users").document(UserID).collection("Rental Person").document().getId();
                        onrent_data.collection("Users").document(UserID).collection("Rental Person").document(documentid).set(db)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                           Toast.makeText(RentActivity.this,"Your record has submitted now you can deal with owner",Toast.LENGTH_LONG).show();
                                                        }

                                                    });

                                            Intent intent=new Intent(RentActivity.this,MainActivity.class);
                                            startActivity(intent);
                    }

            }
        }

}