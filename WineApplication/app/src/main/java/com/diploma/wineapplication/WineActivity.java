package com.diploma.wineapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WineActivity extends AppCompatActivity {

    TextView Color, Country, Description, Maker, Name, Region, Sort, Sweetness, Year;
    RatingBar  bar;
    Button rateButton;

    String us, id;

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wine);


        final Bundle bundle = getIntent().getExtras();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        bar = findViewById(R.id.ratingBar);
        rateButton = findViewById(R.id.rate);

        Color = findViewById(R.id.textColor);
        Country = findViewById(R.id.textCountry);
        Description = findViewById(R.id.textDescription);
        Maker = findViewById(R.id.textMaker);
        Name = findViewById(R.id.textName);
        Region = findViewById(R.id.textRegion);
        Sort = findViewById(R.id.textSort);
        Sweetness = findViewById(R.id.textSweetness);
        Year = findViewById(R.id.textYear);

        Color.setText(bundle.getString("color"));
        Country.setText(bundle.getString("country"));
        Description.setText(bundle.getString("description"));
        Maker.setText(bundle.getString("maker"));
        Name.setText(bundle.getString("name"));
        Region.setText(bundle.getString("region"));
        Sort.setText(bundle.getString("sort"));
        Sweetness.setText(bundle.getString("sweetness"));
        Year.setText(bundle.getString("year"));

        lookForRating();

        final FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
        {
            bar.setVisibility(View.INVISIBLE);
            rateButton.setVisibility(View.INVISIBLE);
        }

        View.OnClickListener OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.rate:
                        adding(bar.getRating());
                        break;
                    default:
                        break;
                }

            }
        };

        rateButton.setOnClickListener(OnClickListener);
    }

    public void lookForRating()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        Query QueryRef = reference.child("Ratings/").orderByChild("user").equalTo(user.getUid());

        QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RatingModel model = dataSnapshot.getValue(RatingModel.class);
                if(model.Name != null && model.Name.equals(Name.getText().toString()))
                {
                    bar.setRating(model.mark);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void adding(float mark)
    {
        us =  mAuth.getCurrentUser().getUid();
        Query QueryRef = reference.child("Ratings/").orderByChild("user").equalTo(us);
        QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RatingModel model = dataSnapshot.getValue(RatingModel.class);
                if(model.Name != null && model.Name.equals(Name.getText().toString()))
                {
                    dataSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        id = reference.child(us).push().getKey();

        reference.child("Ratings/" + id + "/user").setValue(us);
        reference.child("Ratings/" + id + "/mark").setValue(mark);
        reference.child("Ratings/" + id + "/Color").setValue(Color.getText());
        reference.child("Ratings/" + id + "/Country").setValue(Country.getText());
        reference.child("Ratings/" + id + "/Description").setValue(Description.getText());
        reference.child("Ratings/" + id + "/Maker").setValue(Maker.getText());
        reference.child("Ratings/" + id + "/Name").setValue(Name.getText());
        reference.child("Ratings/" + id + "/Region").setValue(Region.getText());
        reference.child("Ratings/" + id + "/Sort").setValue(Sort.getText());
        reference.child("Ratings/" + id + "/Sweetness").setValue(Sweetness.getText());
        reference.child("Ratings/" + id + "/Year").setValue(Year.getText());

        Toast.makeText(WineActivity.this, getString(R.string.action_add), Toast.LENGTH_LONG).show();

    }
}
