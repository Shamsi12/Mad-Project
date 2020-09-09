package com.example.accessories_rental;


import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "TAG";
    EditText username,emailaddress,phoneNumber,password,retypepassword;
    Button register;
    ProgressBar register_prog_bar;
    private FirebaseAuth registration;
    FirebaseFirestore register_data;
    String UserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username=findViewById(R.id.inputusername);
        emailaddress=findViewById(R.id.inputemail);
        phoneNumber=findViewById(R.id.inputphoneno);
        password=findViewById(R.id.inputpassword);
        retypepassword=findViewById(R.id.confirminputpassword);
        register=findViewById(R.id.btnregister);
        registration=FirebaseAuth.getInstance();
        register_data=FirebaseFirestore.getInstance();
        register_prog_bar=findViewById(R.id.registrationprogressbar);
        TextView signIn = (TextView) findViewById(R.id.signIn);
        signIn.setMovementMethod(LinkMovementMethod.getInstance());
        signIn.setOnClickListener(this);
        register.setOnClickListener(this);
        password.addTextChangedListener(this);

       //phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnregister:
                String getusername=username.getText().toString().trim();
                String getemail=emailaddress.getText().toString().trim();
                String getphoneno=phoneNumber.getText().toString().trim();
                String getpassword=password.getText().toString().trim();
                String getretypepassword=retypepassword.getText().toString().trim();
                if(getusername.isEmpty() || getemail.isEmpty() || getphoneno.isEmpty() || getpassword.isEmpty() || getretypepassword.isEmpty()) {

                    if (getusername.isEmpty()) {
                        username.setError("This is required field");
                    }
                    if (getemail.isEmpty()) {
                        emailaddress.setError("This is required field");
                    }
                    if (getphoneno.isEmpty()) {
                        phoneNumber.setError("This is required field");
                    }
                    if (getpassword.isEmpty()) {
                        password.setError("This is required field");
                    }
                    if(getretypepassword.isEmpty()){
                        retypepassword.setError("This is required field");
                    }

                }

                else if(getpassword.length()< 6){
                    password.setError("Password must be greater or equals to 6 character");
                }
                else if(!getpassword.equals(getretypepassword)){
                  retypepassword.setError("Password does not match");
                }
                else if(!getemail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.com")){
                    emailaddress.setError("Email is invalid");
                }
                else {
                    final RegisterDb db=new RegisterDb();
                    db.setUsername(getusername);
                    db.setEmailaddress(getemail);
                    db.setPhonenumber(getphoneno);
                    register_prog_bar.setVisibility(View.VISIBLE);
                    registration.createUserWithEmailAndPassword(getemail, getpassword)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user=registration.getCurrentUser();
                                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegistrationActivity.this, "Successfully Registered: Verification Email has been sent", Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "Failed:Email not sent"+e.getMessage());

                                            }
                                        });
                                        UserID=registration.getCurrentUser().getUid();
                                        register_data.collection("Users").document(UserID).set(db)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "OnSuccess:User is successfully created");
                                                    }

                                                });
                                                        registration.signOut();
                                        Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Registration failed." + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        register_prog_bar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
                    break;
            case R.id.signIn:
                Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length()!=0){
            retypepassword.setEnabled(true);
        }
        else{
            retypepassword.setEnabled(false);
        }
    }
}
