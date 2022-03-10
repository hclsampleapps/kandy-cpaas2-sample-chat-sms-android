package com.hcl.kandy.cpass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.hcl.kandy.cpass.R;
import com.hcl.kandy.cpass.utils.jwt.JWT;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavController navController;
    public NavigationView navigationView;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupNavigation();


        Bundle extras = getIntent().getExtras();
        String idToken = null;
        String accessToken = null;
        String baseUrl = null;
        if (extras != null) {
            idToken = extras.getString(LoginActivity.id_token);
            accessToken = extras.getString(LoginActivity.access_token);
            baseUrl = extras.getString(LoginActivity.base_url);
        }

        setUserInfo(idToken);

    }


    private void setupNavigation() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        drawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.nav_chat:
                navController.navigate(R.id.nav_chat);
                break;

            case R.id.nav_sms:
                navController.navigate(R.id.nav_sms);
                invalidateOptionsMenu();
                break;

            case R.id.nav_multimedia:
                navController.navigate(R.id.nav_multimedia);
                break;
            case R.id.nav_logout:
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;

    }


//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    /* @SuppressWarnings("StatementWithEmptyBody")
     @Override
     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
         // Handle navigation view item clicks here.
         FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

         int id = item.getItemId();

         if (id == R.id.nav_multimedia) {
             fragmentTransaction
                     .replace(R.id.container, multimediaChatFragment).commit();

             ActionBar supportActionBar = getSupportActionBar();
             if (supportActionBar != null)
                 supportActionBar.setTitle("Multimedia App");

             item.setChecked(true);
             invalidateOptionsMenu();
         }
         if (id == R.id.nav_chat) {
             fragmentTransaction
                     .replace(R.id.container, chatFragment).commit();

             ActionBar supportActionBar = getSupportActionBar();
             if (supportActionBar != null)
                 supportActionBar.setTitle("Chat App");

             item.setChecked(true);
             invalidateOptionsMenu();
         } else if (id == R.id.nav_sms) {
             fragmentTransaction
                     .replace(R.id.container, smsFragment).commit();

             ActionBar supportActionBar = getSupportActionBar();
             if (supportActionBar != null)
                 supportActionBar.setTitle("SMS App");

             item.setChecked(true);
             invalidateOptionsMenu();
         } else if (id == R.id.nav_logout) {
             startActivity(new Intent(HomeActivity.this, LoginActivity.class));
             Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
             finish();
         }
         DrawerLayout drawer = findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);
         return true;
     }
 */
    private void setUserInfo(String idToken) {
        JWT jwt = new JWT(idToken);
        String email = jwt.getClaim("email").asString();
        String name = jwt.getClaim("name").asString();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        TextView tvName = hView.findViewById(R.id.tvName);
        TextView tvEmail = hView.findViewById(R.id.tvEmail);
        tvEmail.setText(email);
        tvName.setText(name);
    }
}
