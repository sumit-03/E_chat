package com.example.quantum.echat;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button logout_button;
    //private TextView displayName;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;
    private String userId;
    public String currentState="not_friend";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        toolbar=(Toolbar) findViewById(R.id.usertoolbar);
        recyclerView=(RecyclerView) findViewById(R.id.userlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       toolbar.setTitle("All users");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
       databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
       databaseReference1=FirebaseDatabase.getInstance().getReference("friend_request");

       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }


    @Override
    protected void onStart() {

        super.onStart();
        FirebaseRecyclerAdapter<UserProfile,UserViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserProfile, UserViewHolder>(
                UserProfile.class,
                R.layout.user_layout,
                UserViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, UserProfile model, final int position) {
                viewHolder.setName(model.getUserName());

               /* int count=recyclerView.getChildCount();
                for(int i=0;i<=count;i++)
                {
                    if(getRef(i).getKey().equals(firebaseUser.getUid())){
                        viewHolder.mView.setVisibility(View.GONE);

                        //recyclerView.removeViewInLayout(findViewById(i));
                    }else{
                        viewHolder.mView.setVisibility(View.VISIBLE);
                    }
                }*/
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userId=getRef(position).getKey();
                        Intent intent=new Intent(SecondActivity.this,MessageActivity.class);
                        intent.putExtra("userId",userId);
                        startActivity(intent);

                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static   class  UserViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setName(String name){
            TextView displayName=(TextView) mView.findViewById(R.id.tvdisplayname);
            displayName.setText(name);
        }

    }


    @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case R.id.logout: {
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SecondActivity.this, MainActivity.class));
                    break;
                }
                case R.id.editaccount: {
                    //finish();
                    startActivity(new Intent(SecondActivity.this, RegistrationActivity.class));
                    break;
                }
                case R.id.profile: {
                    startActivity(new Intent(SecondActivity.this, ProfileActivity.class));
                    break;
                }
                case android.R.id.home:{
                    onBackPressed();
                    break;
                }
            }
            return super.onOptionsItemSelected(item);
        }


}
