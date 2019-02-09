package com.example.quantum.echat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static java.sql.DriverManager.println;

public class RegistrationActivity extends AppCompatActivity {
    EditText Name,Password,Email,Age;
    Button btn1;
    TextView loginText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String user_Email,user_Password,user_Age,user_Name;
    private ProgressDialog progressDialog;
    private String userId;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
        setValues();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //database storage
                  String userEmail=user_Email.trim();
                  String userPassword=user_Password.trim();
                   firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    try{
                                        sendUserData();
                                        firebaseAuth.signOut();
                                        Toast.makeText(RegistrationActivity.this,"Registration Successful,Data uploaded",Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));


                                    }catch (DatabaseException e){
                                        Log.d("tag","testing");
                                    }// emailVerification();

                                  }else{

                                Toast.makeText(RegistrationActivity.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }
        });
    }


  /*  private void emailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        sendUserData();

                        firebaseAuth.signOut();
                        Toast.makeText(RegistrationActivity.this,  "Verification link sent and Data uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(RegistrationActivity.this,"Verification failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(RegistrationActivity.this,  "Testing4", Toast.LENGTH_SHORT).show();

        }
    }*/

    private boolean validate() {
        Boolean result = false;
        setValues();

        if (user_Name.isEmpty() || user_Email.isEmpty() || user_Password.isEmpty() || user_Age.isEmpty()) {
            Toast.makeText(this, "please enter all details", Toast.LENGTH_SHORT).show();
        } else {
            result=true;
        }
        return result;

    }

    private void sendUserData() {
        setValues();
        UserProfile userInfo=new UserProfile(user_Name,user_Email,user_Age,null);
       // Map<String,Object> data=userInfo.toMap();

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(firebaseUser.getUid()).setValue(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegistrationActivity.this,"Data sent",Toast.LENGTH_SHORT).show();

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrationActivity.this,"Data not sent",Toast.LENGTH_SHORT).show();

            }
        });

      }


    private void init() {
        Name=(EditText) findViewById(R.id.et1);
        Email=(EditText) findViewById(R.id.et2);
        Password=(EditText) findViewById((R.id.pass));
        btn1=(Button) findViewById(R.id.register);
        loginText=(TextView) findViewById(R.id.login);
        Age=(EditText) findViewById(R.id.etage);

    }
   private void setValues(){
        user_Name=Name.getText().toString();
        user_Email=Email.getText().toString();
        user_Password=Password.getText().toString();
        user_Age=Age.getText().toString();

    }
}
