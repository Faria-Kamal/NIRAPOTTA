package com.example.nirapotta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.nirapotta.LocationTrack.latitude;
import static com.example.nirapotta.LocationTrack.longitude;

public class UserActivity extends AppCompatActivity {
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    TextInputLayout fullName,email, phoneNo, password;
    TextView fullNameLabel, usernameLabel,address;
    Button dashboardbtn;
    int c=0;
    protected String uname,fname,uemail,phone,pass,s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phone_no_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.fullname_field);
        usernameLabel = findViewById(R.id.username_field);
        dashboardbtn=findViewById(R.id.dashboardbtn);
        address=(TextView)findViewById(R.id.address_desc);

            showAllUserData();

        dashboardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(i);
            }
        });
    }
    private void showAllUserData() {
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        locationTrack = new LocationTrack(UserActivity.this);


        if (locationTrack.canGetLocation()) {


            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();


        } else {

            locationTrack.showSettingsAlert();
        }

        s= getCompleteAddressString(latitude,longitude);
        address.setText(s);

        /*if(c==0)
        {
            Intent intent = getIntent();
            final String user_username = intent.getStringExtra("username");
            final String user_name = intent.getStringExtra("name");
            final String user_email = intent.getStringExtra("email");
            final String user_phoneNo = intent.getStringExtra("phoneNo");
            final String user_password = intent.getStringExtra("password");
            c++;

            uname = user_username;
            fname = user_name;
            uemail = user_email;
            phone = user_phoneNo;
            pass=user_password;
        }*/
        final MyDBfunctions2 obj = new MyDBfunctions2(getApplicationContext());
        final String s= (obj.fetch_info());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("username").equalTo(s);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    final String passwordFromDB = dataSnapshot.child(s).child("password").getValue(String.class);

                    //if (called_from != null && called_from.equalsIgnoreCase("add"))



                        String nameFromDB = dataSnapshot.child(s).child("name").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(s).child("username").getValue(String.class);
                        String phoneNoFromDB = dataSnapshot.child(s).child("phoneNo").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(s).child("email").getValue(String.class);
                        String passwordfrom = dataSnapshot.child(s).child("password").getValue(String.class);

                    fullNameLabel.setText(nameFromDB);
                    usernameLabel.setText(usernameFromDB);
                    fullName.getEditText().setText(nameFromDB);
                    email.getEditText().setText(emailFromDB);
                    phoneNo.getEditText().setText(phoneNoFromDB);
                    password.getEditText().setText(passwordfrom);

                } else {
                    //progressBar.setVisibility(View.GONE)
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
}
