package com.example.nirapotta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    Button callSignUp, login;
    ImageView image;
    TextView logo, slogan;
    TextInputLayout username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        image= findViewById(R.id.logo_image);
        logo=findViewById(R.id.logo_name);

        slogan=findViewById(R.id.slogan_name);
        username= findViewById(R.id.username);
        password=findViewById(R.id.password);
        login=findViewById(R.id.loginbtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();

                //Intent i= new Intent(LoginInputViewActivity.this,UserActivity.class);
                // startActivity(i);
            }
        });



        callSignUp=findViewById(R.id.signup_screen);
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (LoginActivity.this,RegistrationActivtiy.class);
                Pair[]pairs=new Pair[6];

                pairs[0]=new Pair<View, String>(logo,"logo_text");
                pairs[1]=new Pair<View, String>(slogan,"logo_desc");
                pairs[2]=new Pair<View, String>(username,"username_tran");
                pairs[3]=new Pair<View, String>(password,"password_tran");
                pairs[4]=new Pair<View, String>(login,"login_go_button_tran");
                pairs[5]=new Pair<View, String>(callSignUp,"login_signup_tran");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(intent, options.toBundle());

            }
        });



    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser() {
        //Validate Login Info
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");


        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    final String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    //if (called_from != null && called_from.equalsIgnoreCase("add"))

                    if (passwordFromDB.equals(userEnteredPassword)){
                        username.setError(null);
                        username.setErrorEnabled(false);
                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String passwordfrom = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);
                        DataTemp dt= new DataTemp(usernameFromDB);
                        final MyDBfunctions2 mf =new MyDBfunctions2(getApplicationContext());
                        mf.addingDatatoTable(dt);
                        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                       intent.putExtra("name", nameFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                        finish();

                    } else {
                        //  progressBar.setVisibility(View.GONE);
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {
                    //progressBar.setVisibility(View.GONE);
                    username.setError("No such User exist");
                    username.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}