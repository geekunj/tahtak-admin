package com.sjjs.newsadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.sjjs.newsadmin.MainActivity;
import com.sjjs.newsadmin.R;
import com.sjjs.newsadmin.adapters.NewsListAdapter;

import static java.security.AccessController.getContext;

public class NewsListActivity extends AppCompatActivity implements NewsListAdapter.OnNewsItemClickListener {

    private String RECEIVED_CAT = "";

    private RecyclerView newsListRecycler;
    private Toolbar toolbar;
    private ViewGroup mEmptyView;

    private Query mQuery;
    private FirebaseFirestore mFirestore;
    private NewsListAdapter adapter;


    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        RECEIVED_CAT = getIntent().getExtras().getString("category");

        newsListRecycler = findViewById(R.id.rv_news);
        toolbar = findViewById(R.id.toolbar);
        mEmptyView = findViewById(R.id.view_empty);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(RECEIVED_CAT + " News");


        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("news").whereEqualTo("newsCategory", RECEIVED_CAT).limit(50);

        adapter = new NewsListAdapter(mQuery,this, this){

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    newsListRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                } else {
                    newsListRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);

                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        newsListRecycler.setLayoutManager(new LinearLayoutManager(this));
        newsListRecycler.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                adapter.deleteNewsItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(newsListRecycler);

    }

    @Override
    public void onNewsItemClick(DocumentSnapshot documentSnapshot, int position, String newsId) {

        //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        Intent mIntent = new Intent(this, EditNewsActivity.class);
        mIntent.putExtra("docId", newsId);
        startActivity(mIntent);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onSupportNavigateUp();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}