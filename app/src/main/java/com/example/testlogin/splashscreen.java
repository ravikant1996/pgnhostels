package com.example.testlogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import com.example.testlogin.Admin_Profile.adminProfile;
import com.example.testlogin.PG_manager_profile.pgManagerProfile;
import com.example.testlogin.User_Profile.userProfile;
import com.example.testlogin.super_admin.superAdmin;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {
    SessionManager session;

    public final int SPLASH_DISPLAY_LENGTH = 2000;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(splashscreen.this, Login.class);
                splashscreen.this.startActivity(mainIntent);
                splashscreen.this.finish();
               /* session = new SessionManager(getApplicationContext());
                if (session.isLoggedIn()) {
                    HashMap<String, String> user = session.getUserDetails();

                    String logintype = user.get(SessionManager.KEY_LOGINTYPE);
                    if (logintype != null) {
                        if (logintype.equals("super")) {
                            splashscreen.this.startActivity(new Intent(splashscreen.this, superAdmin.class));
                            splashscreen.this.finish();
                        } else if (logintype.equals("manager")) {
                            splashscreen.this.startActivity(new Intent(splashscreen.this, pgManagerProfile.class));
                            splashscreen.this.finish();
                        } else if (logintype.equals("admin")) {
                            splashscreen.this.startActivity(new Intent(splashscreen.this, adminProfile.class));
                            splashscreen.this.finish();
                        } else if (logintype.equals("user")) {
                            splashscreen.this.startActivity(new Intent(splashscreen.this, userProfile.class));
                            splashscreen.this.finish();
                        }
                    } else {
                        splashscreen.this.startActivity(new Intent(splashscreen.this, Login.class));
                        splashscreen.this.finish();
                    }
                }
          */  }
        }, SPLASH_DISPLAY_LENGTH);
    }
}