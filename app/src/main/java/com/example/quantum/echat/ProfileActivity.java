package com.example.quantum.echat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.Attributes;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName,profileEmail,profileAge;
    private Button editButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = (TextView) findViewById(R.id.tvprofilename);
        profileEmail = (TextView) findViewById(R.id.tvprofileemail);
        profileAge = (TextView) findViewById(R.id.tvprofileage);
        editButton = (Button) findViewById(R.id.editbtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.child(firebaseUser.getUid()).getValue(UserProfile.class);
                if(userProfile!=null) {
                    profileName.setText("Name:"+userProfile.getUserName());
                    profileEmail.setText("Email:"+userProfile.getUserEmail());
                    profileAge.setText("Age:"+userProfile.getUserAge());
                }else {
                    Log.d("Error","User is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,UpdateActivity.class));

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
