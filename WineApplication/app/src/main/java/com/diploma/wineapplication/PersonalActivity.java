package com.diploma.wineapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    TextView email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(user == null) {
            findViewById(R.id.action_personal).setClickable(false);
        }
        else{
            findViewById(R.id.action_personal).setClickable(true);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_personal);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.action_lenta:
                                Intent intent1 = new Intent(PersonalActivity.this, CatalogActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent1);
                                break;
                            case R.id.action_cam:
                                Intent intent = new Intent(PersonalActivity.this,  OcrCaptureActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                break;
                            case R.id.action_recommend:
                                Intent intent2 = new Intent(PersonalActivity.this, RecommendActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent2);
                                break;
                            case R.id.action_personal:
                                Intent intent3 = new Intent(PersonalActivity.this, PersonalActivity.class);
                                intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent3);
                                break;
                        }
                        return false;
                    }
                }
        );
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        FirebaseUser user = mAuth.getCurrentUser();

        email = findViewById(R.id.email);
        email.setText(user.getEmail());

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_my_wines)
        {
            FirebaseUser user = mAuth.getCurrentUser();
            Intent intent = new Intent(PersonalActivity.this, CatalogActivity.class);
            intent.putExtra("user", user.getEmail());
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout)
        {
            mAuth.getInstance().signOut();
            Intent intent = new Intent(PersonalActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
