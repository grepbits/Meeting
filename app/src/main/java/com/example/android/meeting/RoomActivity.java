package com.example.android.meeting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

//ROOM ACTIVITY : Allow user to give name to their meeting and join meeting
public class RoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        //setting Jitsi server
        try {
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))

                    .setWelcomePageEnabled(false)
                    .build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //Joining meeting
    public void onButtonClick(View view) {
        EditText editText=findViewById(R.id.conferenceroom);
        String text=editText.getText().toString();

        //getting room name entered by user and adding random string to it
        //so as to make the meeting unique
        if(text.length()>0)
        {
            //randomization of room name
            String GeneratedString=getStringRandom();
            text=text+GeneratedString;

            //Starting Meeting
            JitsiMeetConferenceOptions options=new JitsiMeetConferenceOptions.Builder().setRoom(text).build();
            JitsiMeetActivity.launch(this,options);
        }else
        {
            Toast.makeText(this,"Enter room name",Toast.LENGTH_SHORT).show();
        }
    }

    //getting random string generated
    private String getStringRandom() {
        // lower limit for LowerCase Letters
        int lowerLimit = 48;

        // lower limit for LowerCase Letters
        int upperLimit = 122;


        Random random = new Random();

        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer(6);

        for (int i = 0; i <= 5; i++) {


            int nextRandomChar = lowerLimit
                    + (int)(random.nextFloat()
                    * (upperLimit - lowerLimit + 1));

            // append a character at the end of bs
            r.append((char)nextRandomChar);
        }

        // return the resultant string
        return r.toString();
    }

}