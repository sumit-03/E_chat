package com.example.quantum.echat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.String;

public class PasswordReset extends AppCompatActivity {

    private EditText PasswordEmail;
    private Button PasswordButton;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        PasswordEmail=(EditText) findViewById(R.id.passwordreset);
        PasswordButton=(Button) findViewById(R.id.resetbtn);
        firebaseAuth=FirebaseAuth.getInstance();

        PasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail=PasswordEmail.getText().toString().trim();
                if(userEmail.equals("")){
                    Toast.makeText(PasswordReset.this,"Enter the email first",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PasswordReset.this,"Reset email sent successfully",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordReset.this,MainActivity.class));
                            }else{
                                Toast.makeText(PasswordReset.this,"Error in sending mail",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
