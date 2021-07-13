package com.example.android.meeting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsService;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.meeting.MessageActivity;
import com.example.android.meeting.Model.Users;
import com.example.android.meeting.R;

import java.util.List;

//USER ADAPTER : Managing users in app an operations that can be carried out on them
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<Users> mUsers;


    //constructor
    public UserAdapter(Context context, List<Users> mUsers) {
        this.context = context;
        this.mUsers = mUsers;

    }


    //user_item layout get attached with userAdapter
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.user_item,
               parent,
               false);
        return new UserAdapter.ViewHolder(view);
    }

    //When user is binded with layout check for its image
    //if clicked on image present in user_items then go to message activity to start conversion
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users users=mUsers.get(position);
        holder.username.setText(users.getUsername());

        if(users.getImageURL().equals("default"))
        {
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
        }else
        {
            // Adding Glide Library
            Glide.with(context)
                    .load(users.getImageURL())
                    .into(holder.imageView);
        }


        //if clicked on image present in user_items then go to message activity to start conversion
        holder.imageView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, MessageActivity.class);
                i.putExtra("userid", users.getId());
                context.startActivity(i);

            }
        });


    }

    //get total number of users
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    //class for viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
           super(itemView);

            username = itemView.findViewById(R.id.textView30);
            imageView = itemView.findViewById(R.id.imageView);

        }
    }

}
