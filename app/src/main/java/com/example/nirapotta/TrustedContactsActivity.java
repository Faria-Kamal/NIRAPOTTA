package com.example.nirapotta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class TrustedContactsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    AutoCompleteTextView autoCompleteTextView;
    Toolbar toolbar;
    Menu menu;
    EditText et1,et2;
    Button savebtn, showbtn;
    ImageView dropdown;
    protected  String cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_contacts);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        menu = navigationView.getMenu();



        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        et1= (EditText)findViewById(R.id.name);
        et2=(EditText)findViewById(R.id.phoneNo);
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoComplete);
        autoCompleteTextView.setThreshold(1);

        dropdown=(ImageView)findViewById(R.id.dropdown);
        savebtn=(Button)findViewById(R.id.savebtn);
        showbtn=(Button)findViewById(R.id.showbtn);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, codes);
        autoCompleteTextView.setAdapter(adapter);
        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.showDropDown();

            }
        });


        final MyDBFunctions mf= new MyDBFunctions(getApplicationContext());

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cd = autoCompleteTextView.getText().toString();
                String _name= et1.getText().toString();
                String _phoneNo= "+"+cd+et2.getText().toString();

                if(validationcheck()==1)
                {
                    DataTemp dt= new DataTemp(_name,_phoneNo);
                    // class object = new object();

                    mf.addingDatatoTable(dt); //object.method(object)

                    Toast.makeText(getApplicationContext(),"Data inserted successfully!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //object.method()

        showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TrustedContactsList.class));
            }
        });



    }

    private Boolean validateName() {
        String val = et1.getText().toString();

        if (val.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    private Boolean validatePhoneNo() {
        String val = et2.getText().toString();

        if (val.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    private Boolean validateCode() {
        //String val = autoCompleteTextView.getText().toString();

        if (cd.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public int validationcheck(){

        if (!validateName() | !validatePhoneNo() | !validateCode()) {
            Toast.makeText(getApplicationContext(),"Fields cannot be empty",Toast.LENGTH_LONG).show();
            return 0;
        }
        else
            return 1;
    }
    private static final String[] codes = new String[] {"880","93","213", "54"};
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_About:
                Intent in = new Intent(TrustedContactsActivity.this,AboutActivity.class);
                startActivity(in);
                break;

            case R.id.nav_home:
                Intent intent = new Intent(TrustedContactsActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.nav_logout: menu.findItem(R.id.nav_logout).setVisible(false);
                menu.findItem(R.id.nav_profile).setVisible(false);
                finish();
                break;

           /* case R.id.nav_Feedback:
                Intent intent = new Intent(TrustedContactsActivity.this, Feedback.class);
                startActivity(intent);
                break;*/


/*
            case R.id.nav_selling:
                Intent intent = new Intent(DashboardView.this, SellingWindowView.class);
                startActivity(intent);
                break;

            case R.id.nav_buying:
                Intent intentbuy = new Intent(DashboardView.this, BuyingWindowView.class);
                startActivity(intentbuy);
                break;

            case R.id.nav_profile:
                Intent intentprof = new Intent(DashboardView.this, UserActivity.class);
                startActivity(intentprof);
                break;*/

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
}