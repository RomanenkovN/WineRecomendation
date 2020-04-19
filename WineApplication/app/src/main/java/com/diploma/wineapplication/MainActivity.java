package com.diploma.wineapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    TextView textView;
    Button button1, button2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            findViewById(R.id.action_personal).setClickable(true);
            Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
            startActivity(intent);
        }
        else{
            findViewById(R.id.action_personal).setClickable(false);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_cam:
                                Intent intent = new Intent(MainActivity.this,  OcrCaptureActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                break;
                            case R.id.action_lenta:
                                Intent intent1 = new Intent(MainActivity.this, CatalogActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent1);
                                break;
                            case R.id.action_recommend:
                                Intent intent2 = new Intent(MainActivity.this, RecommendActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent2);
                                break;
                            case R.id.action_personal:
                                Intent intent3 = new Intent(MainActivity.this, PersonalActivity.class);
                                intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent3);
                                break;
                        }
                        return false;
                    }
                }
        );

        View.OnClickListener OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.button1:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.button2:
                        Intent intent1 = new Intent(MainActivity.this, CatalogActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }

            }
        };

        button1.setOnClickListener(OnClickListener);
        button2.setOnClickListener(OnClickListener);
    }

}

