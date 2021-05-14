package com.app.teacher;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.teacher.ui.announcement.Announcement;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.studioidan.httpagent.HttpAgent;
import com.studioidan.httpagent.JsonCallback;
import com.studioidan.httpagent.StringCallback;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import api.DownLoadImageTask;
import api.URLs;
import db.SharedPref;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    TextView userName, userEmail;
    ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Announcement.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_marks, R.id.nav_quiz)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) &&
                checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)){

        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1000);
        }


        userName = navigationView.getHeaderView(0).findViewById(R.id.userFullName);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);
        imgProfile = navigationView.getHeaderView(0).findViewById(R.id.imageView2);

        String token = new SharedPref(this).loadToken();

        if(! token.equals("")){
            HttpAgent.get(URLs.AUTH_INFO)
                    .headers("Authorization", "Bearer " + token, "Content-Type", "application/json")
                    .goJson(new JsonCallback() {
                        @Override
                        protected void onDone(boolean success, JSONObject jsonObject) {
                            if(success && (jsonObject != null)){
                                try {
                                    userName.setText(jsonObject.getString("name"));
                                    userEmail.setText(jsonObject.getString("email"));

                                    String imgURL = jsonObject.getString("avatar");
                                    new DownLoadImageTask(imgProfile).execute(imgURL);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
    }

    public boolean checkPermission(Context context, String permission){
        int check = ContextCompat.checkSelfPermission(context, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}