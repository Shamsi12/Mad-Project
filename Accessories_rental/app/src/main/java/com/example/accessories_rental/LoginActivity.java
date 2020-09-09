package com.example.accessories_rental;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button sign_in;
    EditText email,password;
    ProgressBar login_prog_bar;
    FirebaseAuth login_authentication;
    TextView createanaccount,resetpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_in=findViewById(R.id.buttonsignin);
        email=findViewById(R.id.inputemail);
        password=findViewById(R.id.inputpassword);
        sign_in.setOnClickListener(this);
        login_authentication=FirebaseAuth.getInstance();
        login_prog_bar=findViewById(R.id.loginprogressbar);
        if(login_authentication.getCurrentUser()!=null && login_authentication.getCurrentUser().isEmailVerified()){
            Intent Main=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(Main);
            finish();
        }
        createanaccount= (TextView) findViewById(R.id.CAAlink);
        resetpassword=findViewById(R.id.reset_pass_link);
        createanaccount.setMovementMethod(LinkMovementMethod.getInstance());
        createanaccount.setOnClickListener(this);
        resetpassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonsignin:
                final String getemail = email.getText().toString();
                String getpassword = password.getText().toString();

                if(getemail.isEmpty() && getpassword.isEmpty()) {
                    email.setError("This is required field");
                    password.setError("This is required field");
                }
                else if (getemail.isEmpty()){
                    email.setError("This is required field");
                }
                else if(getpassword.isEmpty()){
                    password.setError("This is required field");
                }
                else if(getpassword.length()<6){
                    password.setError("Password must be greater or equals to 6 character");

                }
                else if(!getemail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.com")){
                    email.setError("Email is invalid");
                }
                else
                {
                    login_prog_bar.setVisibility(View.VISIBLE);
                    login_authentication.signInWithEmailAndPassword(getemail, getpassword)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if(login_authentication.getCurrentUser().isEmailVerified()) {
                                            Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                                            Intent Main = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(Main);
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this,"Email is not Verified",Toast.LENGTH_LONG).show();
                                            login_prog_bar.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login  failed." + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                         login_prog_bar.setVisibility(View.GONE);
                                    }
                                }
                            });

                }
            break;
            case R.id.CAAlink:
                Intent intent=new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
             break;
            case R.id.reset_pass_link:
                final EditText reset_mail=new EditText(v.getContext());
                final AlertDialog.Builder passwordresetdialog=new AlertDialog.Builder(v.getContext());
                passwordresetdialog.setTitle("Reset Password:");
                passwordresetdialog.setMessage("Enter your email to recieve reset password link");
                passwordresetdialog.setView(reset_mail);

                passwordresetdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }

                });

                passwordresetdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close dialog
                        dialogInterface.dismiss();
                    }

                });
                final AlertDialog create=passwordresetdialog.create();
                create.show();
                create.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String resetmail = reset_mail.getText().toString().trim();
                        if (resetmail.isEmpty()) {
                            reset_mail.setError("This is required Field:");
                        }
                        else if (!resetmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.com")) {
                            reset_mail.setError("Email in  invalid");

                    }
                        else {
                            login_authentication.sendPasswordResetEmail(resetmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LoginActivity.this, "Reset Link has sent to your email",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Error: Reset Link is not sent" + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            });
                            create.dismiss();
                        }

                    }
                });

        }
    }
}
