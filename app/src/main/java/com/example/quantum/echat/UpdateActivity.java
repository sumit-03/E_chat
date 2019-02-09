package com.example.quantum.echat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.String;

public class UpdateActivity extends AppCompatActivity {

    private EditText updateName, updateEmail, updateAge;
    private Button updateButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateName = (EditText) findViewById(R.id.etupdatename);
        updateEmail = (EditText) findViewById(R.id.etupdateemail);
        updateAge = (EditText) findViewById(R.id.etupdateage);
        updateButton = (Button) findViewById(R.id.updatebtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.child(firebaseAuth.getUid()).getValue(UserProfile.class);
                if (userProfile != null) {
                    updateName.setText((CharSequence) userProfile.getUserName());
                    updateEmail.setText((CharSequence) userProfile.getUserEmail());
                    updateAge.setText((CharSequence) userProfile.getUserAge());
                } else {
                    Log.d("Error", "User is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.lang.String newName = updateName.getText().toString();
                java.lang.String newEmail = updateEmail.getText().toString();
                String newAge = updateAge.getText().toString();

                UserProfile userProfile = new UserProfile(newName, newEmail, newAge,null);
                databaseReference.child(firebaseAuth.getUid()).setValue(userProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                onBackPressed();
            }
        }

        return super.onOptionsItemSelected(item);

    }
}
