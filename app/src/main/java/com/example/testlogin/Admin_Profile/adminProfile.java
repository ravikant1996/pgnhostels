package com.example.testlogin.Admin_Profile;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.testlogin.R;
import com.example.testlogin.SessionManager;
import com.example.testlogin.change_password;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

public class adminProfile extends AppCompatActivity {
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";
    private DrawerLayout dl_pg;
    private ActionBarDrawerToggle t;
    private NavigationView nv_Admin;
    // Session Manager Class
    SessionManager session;
    int aid, pid;
    String pass;
    Button del;
    TextView Name, Id, Email;
    CircleImageView drawerHeaderImageAdmin;
    TextView adminName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   savedInstanceState.remove("android:support:fragments");
        setContentView(R.layout.activity_admin_profile);

        session = new SessionManager(getApplicationContext());

        Id = findViewById(R.id.id);
        Name = findViewById(R.id.tname);
        Email = findViewById(R.id.email);
        //  del = findViewById(R.id.btn);

        dl_pg = findViewById(R.id.activity_admin_profile);
        t = new ActionBarDrawerToggle(this, dl_pg, R.string.Open, R.string.Close);

        dl_pg.addDrawerListener(t);
        t.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        session.isLoggedIn();
        session.checkLogin();

        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);
//        pid = userID.get(SessionManager.KEY_PID_ID);

      /*  HashMap<String, String> pas = session.getUserPass();
        String pa= pas.get(SessionManager.KEY_PASSWORD);
        */

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        //id
        // name
        String name = user.get(SessionManager.KEY_NAME);
        // email
        //    String email = user.get(SessionManager.KEY_EMAIL);
        //  final int aid = user.get(SessionManager.KEY_ID);
        //   Name.setText(Html.fromHtml("PG Name: <b>" + name + "</b>"));
        //    Email.setText(Html.fromHtml("PG Email: <b>" + email + "</b>"));

        dashbord();

        nv_Admin = findViewById(R.id.nv_admin);
        View headerView = nv_Admin.inflateHeaderView(R.layout.admin_navigation_header);
        drawerHeaderImageAdmin = (CircleImageView) headerView.findViewById(R.id.admin_profile_image);

        adminName = (TextView) headerView.findViewById(R.id.adminName);
        adminName.setText(Html.fromHtml("<b>" + name + "</b>"));

        nv_Admin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                switch (id) {

                    case R.id.profile_admin:
                        admin_details_frag dash = new admin_details_frag();
                        fragmentTransaction.replace(R.id.admin_main_content, dash).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.change_pg:
                        startActivity(new Intent(adminProfile.this, changePg.class));
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.user_complaint:
                        inbox_admin adddetails = new inbox_admin();
                        fragmentTransaction.replace(R.id.admin_main_content, adddetails).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.add_user:
                        user_reg_frag addUser = new user_reg_frag();
                        fragmentTransaction.replace(R.id.admin_main_content, addUser).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.add_room:
                        room_mgmt_fragment roomdetails = new room_mgmt_fragment();
                        //     adddetails.setArguments(bundle);
                        fragmentTransaction.replace(R.id.admin_main_content, roomdetails).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.add_bed:
                        Fragment beddetails = new bed_mgmt_fragment();
                        fragmentTransaction.replace(R.id.admin_main_content, beddetails).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.add_bed_allotment:
                        bed_allotment allotmentdetails = new bed_allotment();
                        fragmentTransaction.replace(R.id.admin_main_content, allotmentdetails).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.add_manager:
                        manager_reg_frag addmanager = new manager_reg_frag();
                        fragmentTransaction.replace(R.id.admin_main_content, addmanager).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.admin_change_password:
                        Intent intent = new Intent(getApplicationContext(), change_password.class);
                        Bundle extras = new Bundle();
                        String type = "getAD";
                        extras.putString("type", type);
                        extras.putInt("GET_ID_FOR_PASSWORD", aid);
                        intent.putExtras(extras);
                        startActivity(intent);
                        dl_pg.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        session.logoutUser();
                        break;
                }
                return true;
            }
        });
    }

    public void dashbord() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        dashboard_admin addAdmin = new dashboard_admin();
        fragmentTransaction.replace(R.id.admin_main_content, addAdmin);
        fragmentTransaction.commit();
    }



   /* @Override
    public void onBackPressed() {
        // disable going back to the super_admin
        moveTaskToBack(true);
        *//*if (dl_pg.isDrawerOpen(GravityCompat.START)) {
            // if you want to handle DrawerLayout
            dl_pg.closeDrawer(GravityCompat.START);
        } else {
                super.onBackPressed();
        }*//*
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    /*
     *//*      // to remove all fragments
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        *//*
    }*/
  /*  @Override
    public void onBackPressed() {
        FragmentManager adminfm = getFragmentManager();
        if ((adminfm.getBackStackEntryCount() > 0) || (dl_pg.isDrawerOpen(GravityCompat.START))) {
            Log.i("MainActivity", "popping backstack");
            adminfm.popBackStack();
            dl_pg.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(adminProfile.this).setTitle("Logout")
                    .setMessage("Would you like to logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            session.logoutUser();
                            finish();  // Call finish here.
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                                    .beginTransaction();
                            dashboard_admin dashb = new dashboard_admin();
                            fragmentTransaction.replace(R.id.admin_main_content, dashb).addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    })
                    .show();
        }
    }*/

    @Override
    public void onBackPressed() {
       //   moveTaskToBack(true);
        FragmentManager adminfm = getFragmentManager();
        if (adminfm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            dl_pg.closeDrawer(GravityCompat.START);
            moveTaskToBack(true);
            adminfm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
          //  finish();
        }
    }/*
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }*/

}
