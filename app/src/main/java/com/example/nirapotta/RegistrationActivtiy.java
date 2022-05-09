package com.example.nirapotta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivtiy extends AppCompatActivity {

    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;
    AutoCompleteTextView autoCompleteTextView;
    ImageView dropdown;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    protected String  name, username, email,phoneNo, password,cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration_activtiy);

        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        final AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autoComplete);
        dropdown=(ImageView)findViewById(R.id.dropdown);
        regPhoneNo = findViewById(R.id.reg_phoneNo);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_to_login_btn);
        autoCompleteTextView.setThreshold(1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, codes);
        autoCompleteTextView.setAdapter(adapter);
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootNode = FirebaseDatabase.getInstance();
               reference = rootNode.getReference("users");
               cd = autoCompleteTextView.getText().toString();

               name =regName.getEditText().getText().toString();
               username = regUsername.getEditText().getText().toString();
               email = regEmail.getEditText().getText().toString();
                phoneNo = regPhoneNo.getEditText().getText().toString();
                password = regPassword.getEditText().getText().toString();

                registerUser();

            }
        });

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                finish();
            }
        });


    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "(?=\\S+$)";

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        }else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"; //fariha@gmail.com

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid email address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }
    private static final String[] codes = new String[] {"880","93","213", "54"};
   private Boolean validateCode() {
        //String val = autoCompleteTextView.getText().toString();

        if (cd.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regPhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("Password is too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }



    //This function will execute when user click on Register Button
    public void registerUser() {

        if(!validateName() | !validateUsername()| !validatePhoneNo() | !validatePassword() | !validateEmail()|!validateCode() ){
            return;
        }
        else
        {
            /*Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_LONG).show();
            UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);
            reference.child(username).setValue(helperClass);*/
            Intent intent = new Intent(getApplicationContext(), VerifyPhoneNoActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            intent.putExtra("code", cd);
            intent.putExtra("phoneNo", phoneNo);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();

            //Toast.makeText(getApplicationContext(),cd,Toast.LENGTH_LONG).show();
            /*Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);*/
        }



    }
}