package com.example.nirapotta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView alertmsg;
    Button danger;
    int count = 0;
    String[] data;
    String s="";
    double longitude=0.0;
    double latitude=0.0;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean isAccelerometerSensorAvailable,itIsnotFirstTime= false;
    List<Sensor> devicesensor;
    private float currentx, currenty, currentz, lastx, lasty, lastz;
    private float xdiff, ydiff, zdiff;
    private float shakeThreshold = 10f;
    private Vibrator vibrator;
    int c=0;

    Animation blinkanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        alertmsg=(TextView)findViewById(R.id.alert_text);

        blinkanim = AnimationUtils.loadAnimation(this, R.anim.blink_anim);
        alertmsg.setAnimation(blinkanim);

        menu = navigationView.getMenu();


        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        danger = (Button) findViewById(R.id.dangerbtn);



        danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;

                if (count == 3) {
                    count = 0;
                    locationTrack = new LocationTrack(DashboardActivity.this);


                    if (locationTrack.canGetLocation()) {


                        longitude = locationTrack.getLongitude();
                        latitude = locationTrack.getLatitude();


                    } else {

                        locationTrack.showSettingsAlert();
                    }

                     s= getCompleteAddressString(latitude,longitude);
                       // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();


                    /*Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(DashboardActivity.class, Locale.getDefault());

                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL*/

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {

                       if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
                       {
                                sendSMS(longitude,latitude);
                        }
                        else
                        {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                        }
                    }

                }
            }
        });

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        {
            accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable=true;
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Accelerometer not found!\n",Toast.LENGTH_LONG).show();
            isAccelerometerSensorAvailable=false;
        }


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


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(DashboardActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //locationTrack.stopListener();
    }

    private void sendSMS(double longitude, double latitude) {
        MyDBFunctions mf = new MyDBFunctions(getApplication());

        data = mf.fetch_phone_number();
        String SMS = "Help me! I'm in trouble" + "\n"+"My current location: longitude: "+longitude+" , latitude: "+latitude+" , "+s;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (int i = 0; i < data.length; i++) {
                smsManager.sendTextMessage(data[i],null,SMS,null,null);
            }
            Toast.makeText(getApplicationContext(),  "Message sent \n", Toast.LENGTH_LONG).show();

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),  "Failed to sent \n", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {


            case R.id.nav_contacts:
                Intent i = new Intent(DashboardActivity.this, TrustedContactsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_About:
                Intent in = new Intent(DashboardActivity.this,AboutActivity.class);
                startActivity(in);
                break;

            case R.id.nav_logout: menu.findItem(R.id.nav_logout).setVisible(false);
                menu.findItem(R.id.nav_profile).setVisible(false);
                finish();
                break;

            case R.id.nav_profile:
                Intent intentprof = new Intent(DashboardActivity.this, UserActivity.class);
                startActivity(intentprof);
                break;

/*
            case R.id.nav_selling:
                Intent intent = new Intent(DashboardView.this, SellingWindowView.class);
                startActivity(intent);
                break;

            case R.id.nav_buying:
                Intent intentbuy = new Intent(DashboardView.this, BuyingWindowView.class);
                startActivity(intentbuy);
                break;



            /*case R.id.nav_login: menu.findItem(R.id.nav_logout).setVisible(true);
                menu.findItem(R.id.nav_profile).setVisible(true);
                menu.findItem(R.id.nav_login).setVisible(false);
                break;
            case R.id.nav_logout: menu.findItem(R.id.nav_logout).setVisible(false);
                menu.findItem(R.id.nav_profile).setVisible(false);
                menu.findItem(R.id.nav_login).setVisible(true);
                break;
            case R.id.nav_share: Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show(); break;*/
        }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(isAccelerometerSensorAvailable)
        {
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isAccelerometerSensorAvailable)
        {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        currentx=event.values[0];
        currenty=event.values[1];
        currentz=event.values[2];

        if(itIsnotFirstTime)
        {
            xdiff = Math.abs(lastx-currentx);
            ydiff = Math.abs(lasty-currenty);
            zdiff = Math.abs(lastz-currentz);
            // Toast.makeText(getApplicationContext(),xdiff+"\n"+ydiff+"\n"+zdiff+"\n",Toast.LENGTH_LONG).show();

            if(((xdiff>shakeThreshold && ydiff>shakeThreshold) || (xdiff>shakeThreshold && zdiff>shakeThreshold) || (ydiff>shakeThreshold && zdiff>shakeThreshold) ))
            {
                c++;
                if(c>5) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        c=0;
                        locationTrack = new LocationTrack(DashboardActivity.this);
                        if (locationTrack.canGetLocation()) {


                            longitude = locationTrack.getLongitude();
                            latitude = locationTrack.getLatitude();
                            s= getCompleteAddressString(latitude,longitude);
                            sendSMS(longitude,latitude);

                        } else {

                            locationTrack.showSettingsAlert();
                        }

                        /*longitude = locationTrack.getLongitude();
                        latitude = locationTrack.getLatitude();*/
                    } else
                        vibrator.vibrate(500);
                }
            }

        }
        lastx = currentx;
        lasty=currenty;
        lastz=currentz;
        itIsnotFirstTime=true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}