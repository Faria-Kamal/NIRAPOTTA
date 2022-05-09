package com.example.nirapotta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNoActivity extends AppCompatActivity {

    String verificationCodeBySystem, name, username, email, code, phoneNo, password;
    private FirebaseAuth mAuth;
    Button verify_btn;
    EditText phoneNoEnteredByTheUser;
    ProgressBar progressBar;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);
        mAuth = FirebaseAuth.getInstance();

        verify_btn = findViewById(R.id.verify_btn);
        phoneNoEnteredByTheUser = findViewById(R.id.verification_code_entered_by_user);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        name = getIntent().getStringExtra("name");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        code = getIntent().getStringExtra("code");
        phoneNo = getIntent().getStringExtra("phoneNo");
        password = getIntent().getStringExtra("password");
        sendVerificationCodeToUser(phoneNo);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = phoneNoEnteredByTheUser.getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    phoneNoEnteredByTheUser.setError("Wrong OTP...");
                    phoneNoEnteredByTheUser.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });


    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+"+code+phoneNo)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+880" + phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                setActivity(this),   // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks*/
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //Get the code in global variable
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneNoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };
    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);

    }
    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNoActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            rootNode = FirebaseDatabase.getInstance();
                            reference = rootNode.getReference("users");

                            UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);
                            reference.child(username).setValue(helperClass);
                            DataTemp dt= new DataTemp(username);
                            final MyDBfunctions2 mf =new MyDBfunctions2(getApplicationContext());
                            mf.addingDatatoTable(dt);


                            Toast.makeText(VerifyPhoneNoActivity.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();

                            //Perform Your required action here to either let the user sign In or do something required
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);


                        } else {
                            Toast.makeText(VerifyPhoneNoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}