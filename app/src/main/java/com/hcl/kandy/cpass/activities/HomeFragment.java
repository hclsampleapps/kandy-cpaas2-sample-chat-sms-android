package com.hcl.kandy.cpass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;
import com.hcl.kandy.cpass.App;
import com.hcl.kandy.cpass.R;
import com.hcl.kandy.cpass.fragments.BaseFragment;
import com.hcl.kandy.cpass.fragments.ChatFragment;
import com.hcl.kandy.cpass.fragments.MultiMediaChatFragment;
import com.hcl.kandy.cpass.fragments.SMSFragment;
import com.hcl.kandy.cpass.utils.jwt.JWT;

public class HomeFragment extends BaseFragment{

    private DrawerLayout drawerLayout;
  //  private NavController navController;
    private NavigationView navigationView;

//    Fragment chatFragment = ChatFragment.newInstance();
//    Fragment smsFragment = SMSFragment.newInstance();
//    Fragment multimediaChatFragment = MultiMediaChatFragment.newInstance();
    Toolbar toolbar;
    String idToken = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getActivity().getIntent().getExtras();
        String accessToken = null;
        String baseUrl = null;
        if (extras != null) {
            idToken = extras.getString(LoginActivity.id_token);
            accessToken = extras.getString(LoginActivity.access_token);
            baseUrl = extras.getString(LoginActivity.base_url);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_home, container, false);

        showProgressBar("");
        toolbar = inflate.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawer = inflate.findViewById(R.id.drawer_layout);
      //  navController = Navigation.findNavController(getActivity(), R.id.container);
        navigationView = inflate.findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

       /* App app = (App) getActivity().getApplicationContext();
        app.setCpass(baseUrl, accessToken, idToken, new CpassListner() {
            @Override
            public void onCpassSuccess() {
                hideProgressBAr();
            }

            @Override
            public void onCpassFail() {
                hideProgressBAr();
            }
        });*/

        setUserInfo(idToken);

      //  onNavigationItemSelected(navigationView.getMenu().getItem(0));

        return inflate;
    }

   /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/
/*
    @SuppressWarnings("StatementWithEmptyBody")
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
            startActivity(new Intent(HomeFragment.this, LoginActivity.class));
            Toast.makeText(HomeFragment.this, "Logout", Toast.LENGTH_SHORT).show();
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

        View hView = navigationView.getHeaderView(0);

        TextView tvName = hView.findViewById(R.id.tvName);
        TextView tvEmail = hView.findViewById(R.id.tvEmail);
        tvEmail.setText(email);
        tvName.setText(name);
    }

    public interface CpassListner {
        void onCpassSuccess();

        void onCpassFail();
    }
}
