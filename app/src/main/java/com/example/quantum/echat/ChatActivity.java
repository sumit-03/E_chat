package com.example.quantum.echat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText chat;
    private ImageView send;
   private String id;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chat=(EditText) findViewById(R.id.etmessage);
        send=(ImageView) findViewById(R.id.ivsend);

        id=getIntent().getStringExtra("userId");
        recyclerView=(RecyclerView) findViewById(R.id.chatrecyclerview);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        databaseReference=FirebaseDatabase.getInstance().getReference("Message");
        //databaseReference.child(firebaseUser.getUid()).child(id).child("chatMessage").setValue("");
        //databaseReference.child(id).child(firebaseUser.getUid()).child("chatMessage").setValue("");


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        final String currMessage=chat.getText().toString();
                       //final Map map=new HashMap();
                       // map.put("message",currMessage);
                        try{
                           databaseReference.child(firebaseUser.getUid()).child(id).child("chatMessage").push().setValue(currMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child(id).child(firebaseUser.getUid()).child("chatMessage").push().setValue(currMessage);

                                    Toast.makeText(ChatActivity.this, "message saved", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (DatabaseException e){
                            Log.d("error","messaage is not going");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        // UserProfile userProfile=new UserProfile();
        //  userProfile.setChatMessage(message);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {

        super.onStart();
        FirebaseRecyclerAdapter<String,UserViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<String,UserViewHolder>(
                String.class,
                R.layout.chat,
                UserViewHolder.class,
                databaseReference.child(firebaseUser.getUid()).child(id).child("chatMessage")
        ) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder,final String model, final int position) {
                viewHolder.setMessage(model);

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class  UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        /*private DatabaseReference mData;
        private FirebaseDatabase mFire;
        private FirebaseAuth firebaseAuth;*/
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setMessage(String message){
           /* firebaseAuth=FirebaseAuth.getInstance();
            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
            String uid=firebaseUser.getUid();
            mData=FirebaseDatabase.getInstance().getReference("Message");*/

            TextView displayMessage=(TextView) mView.findViewById(R.id.tvmessagechat);
            displayMessage.setText(message);
            displayMessage.setBackgroundColor(Color.BLUE);
        }
    }


}
