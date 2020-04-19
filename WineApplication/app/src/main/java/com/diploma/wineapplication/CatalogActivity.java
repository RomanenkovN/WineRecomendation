package com.diploma.wineapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<LentaModel> result;
    private lenta_adapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_main);

        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null) {
            findViewById(R.id.action_personal).setClickable(false);
            findViewById(R.id.action_recommend).setClickable(false);
        }
        else
        {
            findViewById(R.id.action_personal).setClickable(true);
            findViewById(R.id.action_recommend).setClickable(true);
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_lenta);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_cam:
                                Intent intent = new Intent(CatalogActivity.this,  OcrCaptureActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                break;
                            case R.id.action_lenta:
                                Intent intent1 = new Intent(CatalogActivity.this, CatalogActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent1);
                                break;
                            case R.id.action_recommend:
                                Intent intent2 = new Intent(CatalogActivity.this, RecommendActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent2);
                                break;
                            case R.id.action_personal:
                                Intent intent3 = new Intent(CatalogActivity.this, PersonalActivity.class);
                                intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent3);
                                break;
                        }
                        return false;
                    }
                }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // делаем красивым toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Поиск");

        recyclerView =(RecyclerView) findViewById(R.id.lentalist);
        result = new ArrayList<>();

        //определяет откуда была вызвана форма и добавляет данные в соответствии с этим
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            if (bundle.getString("camera") != null)
            {
                fromCamera(bundle.getString("camera"));
            }
            if (bundle.getString("user")!= null){
                forUser();
            }
            if (bundle.getString("recommend") != null){
                List<String[]> groups = (ArrayList<String[]>) getIntent().getSerializableExtra("sort");
                String forWhat = bundle.getString("forWhat");
                recommend(bundle.getString("color"), groups, forWhat);
            }
        }
        else
        {
            updateList();
        }

        adapter = new lenta_adapter(result);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
    }


    private void fromCamera(final String cam)
    {
            Query QueryRef = reference.child("WineCatalog/");
            QueryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    LentaModel model = dataSnapshot.getValue(LentaModel.class);
                    if(cam.toUpperCase().indexOf(model.Original_name.toUpperCase()) >= 0){
                        result.add(model);
                        adapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Ассоциируем настройку поиска с SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // отслеживаем изменения текста в поисковом поле
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // фильтруем recycler view при окончании ввода
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // фильтруем recycler view при изменении текста
                adapter.getFilter().filter(query);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обрабатываем меню здесь. Нажатия на Home/Up
        // будут автоматически обработаны так, как указано в
        // родительской activity в AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateList (){
        Query QueryRef = reference.child("WineCatalog/");
        QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LentaModel model = dataSnapshot.getValue(LentaModel.class);
                result.add(model);
                adapter.notifyDataSetChanged();
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

    private void forUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        Query QueryRatings = reference.child("Ratings/").orderByChild("user").equalTo(user.getUid());
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LentaModel model = dataSnapshot.getValue(LentaModel.class);
                result.add(model);
                adapter.notifyDataSetChanged();
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
        };
        QueryRatings.addChildEventListener(childEventListener);
    }

    private void recommend(String color, final List<String[]> groups, final String forWhat){
        Query QueryRef = reference.child("WineCatalog/").orderByChild("Color").equalTo(color);
        QueryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LentaModel model = dataSnapshot.getValue(LentaModel.class);
                if(forWhat == null){
                    for (String[] group: groups)
                    {
                        for (String sort : group)
                        {
                            if(model.Sort.contains(sort)){
                                result.add(model);
                                break;
                            }
                        }
                    }
                }
                else{
                    for (String[] group: groups)
                    {
                        for (String sort : group)
                        {
                            if(model.Sort.contains(sort) && model.Description.contains(forWhat)){
                                result.add(model);
                                break;
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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

    public void clearItems() {
        result.clear();
        adapter.notifyDataSetChanged();
    }
}
