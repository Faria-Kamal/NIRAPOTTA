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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SingleContact extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    EditText e;
    Button b, d;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_About).setVisible(false);


        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        e = (EditText) findViewById(R.id.edittext);
        b = (Button) findViewById(R.id.updatedatabase);
        d = (Button) findViewById(R.id.delete_data);


        final int rec_pos = getIntent().getIntExtra("MyKEY", 999);

        final MyDBFunctions obj = new MyDBFunctions(getApplicationContext());

        e.setText(obj.fetch_phone(rec_pos+1));
        e.setSelection(e.getText().length());

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obj.update_phone((rec_pos+1), e.getText().toString());
                Toast.makeText(SingleContact.this, "Updated Successfully!", Toast.LENGTH_SHORT).show();


            }
        });


        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj.delete_phone(obj.fetch_phone(rec_pos+1));
                Toast.makeText(getApplicationContext(), "Deleted Successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);



    }

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
                Intent in = new Intent(SingleContact.this,AboutActivity.class);
                startActivity(in);
                break;

            case R.id.nav_home:
                Intent intent = new Intent(SingleContact.this, DashboardActivity.class);
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