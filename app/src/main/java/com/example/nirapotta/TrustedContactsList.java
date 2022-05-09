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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;

public class TrustedContactsList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    ListView lv1;
    String[] data;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_contacts_list);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);

        menu = navigationView.getMenu();
       // menu.findItem(R.id.nav_About).setVisible(false);


        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        lv1= (ListView)findViewById(R.id.myfriendphone);
        MyDBFunctions mf= new MyDBFunctions(getApplication());

        data = mf.my_data();

        lv1.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.lview, R.id.mytext, data));


        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SingleContact.class);
                intent.putExtra("MyKEY", position);
                startActivity(intent);
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
                Intent in = new Intent(TrustedContactsList.this,AboutActivity.class);
                startActivity(in);
                break;

            case R.id.nav_home:
                Intent intent = new Intent(TrustedContactsList.this, DashboardActivity.class);
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