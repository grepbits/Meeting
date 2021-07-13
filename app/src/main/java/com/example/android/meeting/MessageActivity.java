package com.example.android.meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.meeting.Adapter.MessageAdapter;
import com.example.android.meeting.Model.Chat;
import com.example.android.meeting.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//MESSAGE ACTIVITY: Manages sending and receiving msgs and reading them from database
public class MessageActivity extends AppCompatActivity {

    //creating variables
    TextView username;
    ImageView imageView;

    FirebaseUser fuser;
    DatabaseReference reference;

    Intent intent;
    String userid;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;
    EditText msg_editText;
    ImageButton sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        // Widgets initialisation
        imageView = findViewById(R.id.imageview_profile);
        username  = findViewById(R.id.usernamey);
        sendBtn = findViewById(R.id.btn_send);
        msg_editText = findViewById(R.id.text_send);


        //RecyclerView initialisation
        recyclerView=findViewById(R.id.recycler_view); //fragment users
        recyclerView.setHasFixedSize(true);


        //setting layout manager to recyclerView
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        //receiving value sent from userAdapter
        intent=getIntent();//from useradapter  "userid"
        userid=intent.getStringExtra("userid");


        //firebase: getting current user and
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        //adding value event listener to reference to change profile picture
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users user=snapshot.getValue(Users.class);
                username.setText(user.getUsername());

                //if imageurl for user's profile is default then put default pic as its profile pic
                if(user.getImageURL().equals("default"))
                {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                }else
                {
                    //else put the image that user selected as profile pic
                    Glide.with(MessageActivity.this)
                            .load(user.getImageURL())
                            .into(imageView);
                }

                //reading msgs
                readMessages(fuser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //when clicked send button and if msg if not empty then send msg
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_editText.getText().toString();
                if (!msg.equals("")){
                    //if msg is not empty then send msg
                    sendMessage(fuser.getUid(), userid, msg);
                }else
                {
                    Toast.makeText(MessageActivity.this, "Please send a non empty msg", Toast.LENGTH_SHORT).show();
                }

                //after sending msg set input text box to null
                msg_editText.setText("");
            }
        });

    }

    //function to send message
    private void sendMessage(String sender, String receiver, String message) {

        //getting database reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        //creating hashmap to store values which we want to insert in database.
        HashMap<String, Object> hashMap = new HashMap<>();
        //keep track of sender, holder and msg sent.
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        //inserting se
        reference.child("Chats").push().setValue(hashMap);

        //Adding the person we we sent msg to, to our recent chats i.e. in chat fragment
        //getting id of sender
        final DatabaseReference chatRef=FirebaseDatabase.getInstance()
                .getReference("ChatList")
                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if there are no earlier chats between sender and receiver then add the
                //receiver to chatlist of sender
                if(!snapshot.exists())
                {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Adding the person who sent msg to me, to my recent chats i.e. in chat fragment
        //getting id of receiver
        final DatabaseReference chatRef2=FirebaseDatabase.getInstance()
                .getReference("ChatList")
                .child(userid)
                .child(fuser.getUid());

        chatRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if there are no earlier chats between sender and receiver then add the
                //receiver to chatlist of sender
                if(!snapshot.exists())
                {
                    chatRef2.child("id").setValue(fuser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    //method to read message
    private void readMessages(final String myid,final String userid,final String imageurl)
    {
        mchat =new ArrayList<>(); //arraylist to hold chats

        //getting reference of chats section from database where all chats are stored
        reference=FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {

                    Chat chat=snapshot1.getValue(Chat.class);
                    //if other user sent me a message or if I sent msg to other user then add
                    //chat to the chats section in database
                    if(chat.getReceiver().equals(myid)&&chat.getSender().equals(userid)||
                    chat.getSender().equals(myid)&&chat.getReceiver().equals(userid))
                    {
                        mchat.add(chat);
                    }

                    //setting message adapter tp recyclerview
                    messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageurl);
                    recyclerView.setAdapter(messageAdapter);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}