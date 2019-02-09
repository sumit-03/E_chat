package com.example.quantum.echat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.String;

public class MessageActivity extends AppCompatActivity {
        private TextView message;
        public Button send;
   private String currentState="not_friend";
       private String user_id;
        private FirebaseDatabase firebaseDatabase;
        private FirebaseAuth firebaseAuth;
        private FirebaseUser firebaseUser;
        private DatabaseReference databaseReference;
        private DatabaseReference databaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        user_id=getIntent().getStringExtra("userId");
        message=(TextView) findViewById(R.id.tvmessage);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("friend_request");
           final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("Users");
        send=(Button)findViewById(R.id.friendrequestbtn);

        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id)) {
                            String req = dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            String state=dataSnapshot.child(user_id).child("status").getValue().toString();
                            if(state.equals("Friends")){
                                send.setEnabled(false);
                                Intent intent=new Intent(MessageActivity.this,ChatActivity.class);
                                intent.putExtra("userId",user_id);
                                startActivity(intent);
                            }
                            if (req.equals("received")) {
                               currentState="req_received";
                               send.setText("Accept friend request");
                            }else if(req.equals("sent")){
                               currentState="req_sent";
                                send.setText("Cancel friend request");
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send.setEnabled(false);
                if (currentState.equals("not_friend")) {
                    databaseReference.child(firebaseUser.getUid()).child(user_id).child("status").setValue("process...");
                    databaseReference.child(firebaseUser.getUid()).child(user_id).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                databaseReference.child(user_id).child(firebaseUser.getUid()).child("status").setValue("process...");
                                databaseReference.child(user_id).child(firebaseUser.getUid()).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MessageActivity.this, "Friend request sent Successfully", Toast.LENGTH_SHORT).show();
                                        send.setEnabled(true);
                                        currentState = "req_sent";
                                        send.setText("Cancel friend request");


                                    }
                                });
                            } else {
                                Toast.makeText(MessageActivity.this, "Friend request sent failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else if (currentState.equals("req_sent")) {
                    databaseReference.child(firebaseUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MessageActivity.this, "Friend request sent decline", Toast.LENGTH_SHORT).show();

                            databaseReference.child(user_id).child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid){
                                    send.setEnabled(true);
                                    send.setText("send friend request");
                                    currentState = "not_friend";

                                }
                            });
                        }
                    });


                }else
                if(currentState.equals("req_received")){
                    databaseReference.child(firebaseUser.getUid()).child(user_id).child("status").setValue("Friends").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            send.setEnabled(false);
                            databaseReference.child(user_id).child(firebaseUser.getUid()).child("status").setValue("Friends").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MessageActivity.this,"Become friends-share message",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            }

        });


    }
}
