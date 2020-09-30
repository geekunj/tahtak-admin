package com.sjjs.newsadmin;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sjjs.newsadmin.activities.LoginActivity;
import com.sjjs.newsadmin.fragments.AddNewsFragment;
import com.sjjs.newsadmin.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    BottomNavigationView mBottomNavigationView;

    private String userID;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private FirebaseUser user;

    FragmentTransaction ft;
    Fragment addNewsFragment = new AddNewsFragment();
    Fragment homeFragment = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        if(user != null){userID = fAuth.getCurrentUser().getUid();}

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mBottomNavigationView = findViewById(R.id.nv_bottom_menu);
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        initActionBar();

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationUI.setupWithNavController(mBottomNavigationView, Navigation.findNavController(this, R.id.nav_host_fragment));
        /*ft = getSupportFragmentManager().beginTransaction();
        getSupportActionBar().setTitle("All News");
        getSupportFragmentManager().popBackStack();
        ft.replace(R.id.frag_cn, homeFragment).commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.item_all_news:

                                Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.frag_cn);
                                if (mFragment instanceof HomeFragment){
                                    Log.d("MainActivity", "already exists");
                                }else {
                                    Log.d("MainActivity", "created again");
                                    ft = getSupportFragmentManager().beginTransaction();
                                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                    //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                    getSupportActionBar().setTitle("All News");
                                    getSupportFragmentManager().popBackStack();
                                    ft.replace(R.id.frag_cn, homeFragment).commit();

                                }

                                break;


                            case R.id.item_add_news:
                                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                                ft = getSupportFragmentManager().beginTransaction();
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                                //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                getSupportActionBar().setTitle("Add News");
                                getSupportFragmentManager().popBackStack();
                                ft.replace(R.id.frag_cn,addNewsFragment).commit();

                                break;


                            *//*case R.id.item_motivation:


                                break;


                            case R.id.item_more:

                                break;*//*


                        }
                        return true;
                    }

                });*/





    }
    public void initActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Admin Panel");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            /*case R.id.item_notify:
                Toast.makeText(getApplicationContext(), "home clicked", Toast.LENGTH_SHORT).show();
                return true;*/
            case R.id.item_logout:
                fAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}