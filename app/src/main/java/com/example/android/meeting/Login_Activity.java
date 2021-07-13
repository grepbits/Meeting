package com.example.android.meeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//LOGIN PAGE (Already registered user can login.)
// if new user, then can go to signup page
public class Login_Activity extends AppCompatActivity {

    //variables declaration
    EditText userETLogin, passETlogin;
    Button loginBtn, RegisterBtn;

    // Firebase:
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();

        //getting current user from database
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Checking for users existance: Saving the current user
        if (firebaseUser !=null){
            Intent i = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        //Initializing by assigning views
        userETLogin = findViewById(R.id.editText);
        passETlogin = findViewById(R.id.editText3);
        loginBtn    = findViewById(R.id.buttonLogin);
        RegisterBtn = findViewById(R.id.registerBtn);

        // Firebase Auth and getting current user from database
        auth = FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        // Checking for users existance: Saving the current user
        if (firebaseUser !=null){
            Intent i = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        }


        // Register Button:
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Activity.this, RegisterActivity.class);
                startActivity(i);
            }
        });




        // Login Button clicklistener-> get into app is user is already in database:
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email_text = userETLogin.getText().toString();
                String pass_text = passETlogin.getText().toString();


                // Checking if input values are empty:
                if (TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text))
                {
                    Toast.makeText(Login_Activity.this, "Please fill the Fields", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    //signing in with email and password
                    auth.signInWithEmailAndPassword(email_text, pass_text)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {//if user already present then go into app
                                        goToMain();
                                    } else
                                        {
                                        //if error occured then show error message
                                        Toast.makeText(Login_Activity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                }
                            });
                    }
            }
        });
    }

    //function to go to MainActivity intent
    private void goToMain() {
        Intent i = new Intent(Login_Activity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();

    }


}