package com.example.android.meeting.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.meeting.Adapter.UserAdapter;
import com.example.android.meeting.Model.Chatlist;
import com.example.android.meeting.Model.Users;
import com.example.android.meeting.R;
import com.example.android.meeting.RoomActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//CHAT FRAGMENT : Show us all our chats that we did.
//Can start a meeting through this fragment
public class ChatsFragment extends Fragment {

    //variables
    private UserAdapter userAdapter;
    private List<Users> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<Chatlist> usersList;

    RecyclerView recyclerView;

    // Required empty public constructor
    public ChatsFragment() {
    }


    //when chat fragment is created->
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflate the fragment_chats layout
        View view = inflater.inflate(R.layout.fragment_chats,
                container,
                false);

        //setting layout manage to recyclerView
        recyclerView = view.findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();

        //getting reference from database about all users with whom we did earlier
        reference = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fuser.getUid());

        //Adding all users to
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();

                // Loop for all users:
                for(DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    Chatlist chatlist = snapshot1.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                //get recent chats
                chatList();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //clicklistener of fab button allowing us to start a meeting
        FloatingActionButton fab;
        fab=view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getContext(), RoomActivity.class));
            }
        });

        return view;

    }

    private void chatList() {

        // Getting refernce to users in app
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Users user = snapshot1.getValue(Users.class);

                    //if user's id is same as that of id of person we chatted with
                    //then add this user to our list of recent chats
                    for (Chatlist chatlist : usersList){

                        if(user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }

                    }
                }

                //setting adapter to recyclerView of chat fragment
                userAdapter=new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}