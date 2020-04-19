package com.diploma.wineapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class RecommendActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private List<RatingModel> rates;
    private List<LentaModel> wines;

    Button getRecommend, withMeet, withFruits, withCheese, withFish, withSweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        rates = new ArrayList<>();
        wines = new ArrayList<>();
        getRecommend = findViewById(R.id.getRecommend);
        withMeet = findViewById(R.id.withMeet);
        withFish = findViewById(R.id.withFish);
        withFruits = findViewById(R.id.withFruits);
        withCheese = findViewById(R.id.withCheese);
        withSweets = findViewById(R.id.withSweets);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        Query QueryRef = reference.child("Ratings").orderByChild("user").equalTo(mAuth.getUid());
        QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RatingModel model = dataSnapshot.getValue(RatingModel.class);
                rates.add(model);
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

        View.OnClickListener OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.getRecommend:
                        if (rates.size() < 4)
                        {
                            Toast.makeText(RecommendActivity.this, "Чтобы получить рекомендацию нужно оценить более 3х вин", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            recommend(rates, null);
                        }
                        break;
                    case R.id.withMeet:
                        recommend(rates, "мяс");
                        break;
                    case R.id.withFish:
                        recommend(rates, "рыб");
                        break;
                    case R.id.withCheese:
                        recommend(rates, "сыр");
                        break;
                    case R.id.withFruits:
                        recommend(rates, "фрукт");
                        break;
                    case R.id.withSweets:
                        recommend(rates, "десерт");
                        break;
                    default:
                        break;
                }

            }
        };

        getRecommend.setOnClickListener(OnClickListener);
        withSweets.setOnClickListener(OnClickListener);
        withCheese.setOnClickListener(OnClickListener);
        withFish.setOnClickListener(OnClickListener);
        withMeet.setOnClickListener(OnClickListener);
        withFruits.setOnClickListener(OnClickListener);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_recommend);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_cam:
                                Intent intent = new Intent(RecommendActivity.this,  OcrCaptureActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                break;
                            case R.id.action_lenta:
                                Intent intent1 = new Intent(RecommendActivity.this, CatalogActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent1);
                                break;
                            case R.id.action_recommend:
                                Intent intent2 = new Intent(RecommendActivity.this, RecommendActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent2);
                                break;
                            case R.id.action_personal:
                                Intent intent3 = new Intent(RecommendActivity.this, PersonalActivity.class);
                                intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent3);
                                break;
                        }
                        return false;
                    }
                }
        );
    }

    private void recommend(List<RatingModel> rates, String forWhat)
    {
        int red = 0, white = 0, end = 0, count = 0;
        String color;
        List<String[]> finalGroups = new ArrayList<>();
        List<RatingModel> ratingList = rates;
        Collections.sort(ratingList,Collections.reverseOrder());//сортировка по оценкам
        List<String[]> groups = new ArrayList<>();

        groups.add(new String[] { "Каберне Совиньон", "Мерло", "Каберне Фран", "Карменер", "Бордо", "Санджовезе", "Канайоло"});
        groups.add(new String[] { "Сира", "Шираз", "Мальбек", "Пти Сира", "Монастрель", "Мурведр", "Пинотаж"});
        groups.add(new String[] { "Зинфанделю", "Гренаш", "Гарнача", "Темпранилло", "Кариньян"});
        groups.add(new String[] { "Пино Нуар", "Гамэ", "Божоле"});
        groups.add(new String[] { "Шардоне", "Семильон", "Вионьер"});
        groups.add(new String[] { "Совиньон Блан", "Верментино ", "Вердехо", "Грюнер Вельтлинер", "Коломбар"});
        groups.add(new String[] { "Пино Гри", "Альбариньо", "Соаве", "Мюскаде", "Пино Гриджо"});
        groups.add(new String[] { "Рислинг", "Мускат", "Мюскаде", "Торронтес", "Гевюрцтраминер", "Шенин Блан"});
        groups.add(new String[] { "Торронтес ", "Гевюрцтраминер", "Шенин Блан"});

        float[][] matrix = new float[82][32];
        //1937+i

        for (RatingModel r: ratingList)
        {
            if(count == 3)
                break;
            for (String[] gr: groups)
            {
                if (end == 1)
                    break;
                for (String sort : gr)
                {
                    if(r.Sort.contains(sort))
                    {
                        if(!finalGroups.contains(gr))
                        {
                            finalGroups.add(gr);
                        }
                        end = 1;
                        break;
                    }
                }
            }
            end = 0;
            count++;
            if(r.Color.equals("Красное"))
            {
                red++;
            }
            else
            {
                white++;
            }
        }
        color = (red>white) ? "Красное" : "Белое";

        Intent intent = new Intent(RecommendActivity.this, CatalogActivity.class);
        intent.putExtra("recommend", "yes");
        intent.putExtra("color", color);
        intent.putExtra("sort", (Serializable) finalGroups);
        intent.putExtra("forWhat", forWhat);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
