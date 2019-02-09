package com.example.quantum.echat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button btn;
    private  Integer counter=5;
    private TextView registertxt,resetxt;
    private  FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name=(EditText) findViewById(R.id.e1);
       Password=(EditText) findViewById(R.id.e2);
        btn=(Button) findViewById(R.id.btn);
        registertxt=(TextView) findViewById(R.id.signup);
        resetxt=(TextView) findViewById(R.id.setbtn);
        progressDialog= new ProgressDialog(this);

        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();

        if(user!=null){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    validateLogIn(Name.getText().toString(), Password.getText().toString());
            }
        });


        registertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });

        resetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PasswordReset.class));
            }
        });

    }



    private void validateLogIn(String s, String s1) {
        progressDialog.setMessage("You can do other work until login complete");
        progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(s,s1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        //Toast.makeText(MainActivity.this,"LogIn Successful",Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(MainActivity.this,SecondActivity.class));
                       isEmailVerified();
                    }else{
                        progressDialog.dismiss();
                        counter--;
                        Toast.makeText(MainActivity.this,"LogIn failed",Toast.LENGTH_SHORT).show();
                        if(counter==0){
                            btn.setEnabled(false);
                        }
                    }
                }
            });
    }

    private void isEmailVerified(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();
        startActivity(new Intent(MainActivity.this,SecondActivity.class));

       /* if(emailflag){
            Toast.makeText(MainActivity.this,"LogIn Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));

        }else{
            firebaseAuth.signOut();
            Toast.makeText(MainActivity.this,"Verify your email first",Toast.LENGTH_SHORT).show();

        }*/
    }

    }
