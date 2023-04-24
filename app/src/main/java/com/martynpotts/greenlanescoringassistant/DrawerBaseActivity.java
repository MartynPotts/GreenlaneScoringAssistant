package com.martynpotts.greenlanescoringassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout container = drawerLayout.findViewById(R.id.ActivityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.Home:
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0,0);
                break;
            case R.id.ArcherDetails:
                startActivity(new Intent(this, ArcherDetails.class));
                overridePendingTransition(0,0);
                break;
            case R.id.BowDetails:
                startActivity(new Intent(this, BowDetails.class));
                overridePendingTransition(0,0);
                break;

            default:

        }

        return false;
    }

    protected void allocateActivityTitle(String titleString){
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }

}