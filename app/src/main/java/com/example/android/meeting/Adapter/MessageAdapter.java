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
import com.example.android.meeting.Model.Chat;
import com.example.android.meeting.Model.Users;
import com.example.android.meeting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

//MESSAGE ADAPTER : Manages all operations happening on message
//depending on type of msg as receiver or sent msg
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    //Creating variables
    private Context context;
    private List<Chat> mChat;
    private String imgURL;

    // Firebase
    FirebaseUser fuser;

    //Making types of msg
    public static final  int MSG_TYPE_LEFT = 0; //sent msg
    public static final int MSG_TYPE_RIGHT  = 1; //received msg

    //constructor
    public MessageAdapter(Context context, List<Chat> mChat,String imgURL) {
        this.context = context;
        this.mChat = mChat;
        this.imgURL=imgURL;

    }


    //Attaching view according to type of msg
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //if msg is sent by us then use layout for right chat item.
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,
                    parent,
                    false);
            return new MessageAdapter.ViewHolder(view);

        } else {

            //if msg is received by us then use layout for left chat item.
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,
                    parent,
                    false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position)
        {

            Chat chat = mChat.get(position);
            //showing msg we got
            holder.show_message.setText(chat.getMessage());

            //setting image adjacent to received msg
            if (imgURL.equals("default")){
                holder.profile_image.setImageResource(R.mipmap.ic_launcher);
            }else{
                Glide.with(context).load(imgURL).into(holder.profile_image);
            }

        }

        //getting size of our chats
    @Override
    public int getItemCount() {
        return mChat.size();
    }

        //setting view holder to views: textview and profile image
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);

        }
    }

    //getting type of item. So as to get idea about which type of msg we want to operate on
    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        //if I (user managing account) sent the msg then its type is MSG_TYPE_RIGHT
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }

    }

}
