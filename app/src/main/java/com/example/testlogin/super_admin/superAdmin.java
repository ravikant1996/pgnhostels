package com.example.testlogin.super_admin;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.Admin_Profile.adminProfile;
import com.example.testlogin.Admin_Profile.bed_allotment;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class superAdmin extends AppCompatActivity {
    private DrawerLayout dl_;
    private ActionBarDrawerToggle t;
    private NavigationView nv_Super;
    List<User> userList;
    int aid;

    SessionManager session;
    TextView Name, Id, Email;
    MyAdapter mAdapter;
    RecyclerView recycler_view;
    private Context context;
    LinearLayoutManager layoutManager;
    Button adminList, viewpg, addpg;
    CircleImageView drawerHeaderImage;
    TextView superName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);

        session = new SessionManager(getApplicationContext());

        dl_ = (DrawerLayout)findViewById(R.id.dl_super);
        t = new ActionBarDrawerToggle(this, dl_,R.string.Open, R.string.Close);

        dl_.addDrawerListener(t);
        t.syncState();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        session.isLoggedIn();
        session.checkLogin();

        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        //id
  //      String  id= user.get(SessionManager.KEY_ID);
        // name
        String name = user.get(SessionManager.KEY_NAME);
        // email
      //  String email = user.get(SessionManager.KEY_EMAIL);

        // displaying user data
   //     Id.setText(Html.fromHtml("PG Code: <b>" + id + "</b>"));
     //   Name.setText(Html.fromHtml("<b>" + name + "</b>"));
     //   Email.setText(Html.fromHtml("Email: <b>" + email + "</b>"));

        dashboardSuper();

        nv_Super = findViewById(R.id.nv_super);
        View headerView = nv_Super.inflateHeaderView(R.layout.super_admin_navigation_header);
        drawerHeaderImage = (CircleImageView) headerView.findViewById(R.id.profile_image);

        superName = (TextView) headerView.findViewById(R.id.loginTextId);
        superName.setText(Html.fromHtml("<b>" + name + "</b>"));
        nv_Super.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                switch(id)
                {
                    case R.id.dashboard_super:
                        dashboard_super dash = new dashboard_super();
                        fragmentTransaction.replace(R.id.super_main_content, dash).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.view_admin:
                        startActivity(new Intent(superAdmin.this, AdminList.class));
                        dl_.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.add_pg:
                      /*  admin_reg_frag addAdmin = new admin_reg_frag();
                        fragmentTransaction.replace(R.id.super_main_content, addAdmin).addToBackStack(null);
                        fragmentTransaction.commit();*/
                        startActivity(new Intent(superAdmin.this, admin_registration.class));
                        dl_.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.accou:
                        inbox_super fragment = new inbox_super();
                        fragmentTransaction.replace(R.id.super_main_content, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        session.logoutUser();
                    //    finish();
                        Toast.makeText(superAdmin.this, "Thank You!",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }
    public void dashboardSuper(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        dashboard_super addAdmin = new dashboard_super();
        fragmentTransaction.replace(R.id.super_main_content, addAdmin);
        fragmentTransaction.commit();

    }
/*    public void viewpg(View view){
        startActivity(new Intent(superAdmin.this, AdminList.class));
    }

    public void addpg(View view){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        admin_reg_frag addAdmin = new admin_reg_frag();
        fragmentTransaction.replace(R.id.super_main_content, addAdmin).addToBackStack(null);
        fragmentTransaction.commit();
    }*/
/*
    @Override
    public void onBackPressed() {
        // disable going back to the super_admin
        moveTaskToBack(true);
    }*/
@Override
public void onBackPressed() {
    //   moveTaskToBack(true);
    FragmentManager adminfm = getFragmentManager();
    if (adminfm.getBackStackEntryCount() > 0) {
        Log.i("MainActivity", "popping backstack");
        dl_.closeDrawer(GravityCompat.START);
        moveTaskToBack(true);
        adminfm.popBackStack();
    } else {
        Log.i("MainActivity", "nothing on backstack, calling super");
        super.onBackPressed();
        //  finish();
    }
}
/*
    @Override
    public void onBackPressed(){
      //  moveTaskToBack(true);
        FragmentManager supFM = getFragmentManager();
        if ((supFM.getBackStackEntryCount() > 0) || (dl_.isDrawerOpen(GravityCompat.START))) {
            dl_.closeDrawer(GravityCompat.START);
            Log.i("MainActivity", "popping backstack");
            supFM.popBackStack();
          //  finish();
        } else {
            new AlertDialog.Builder(superAdmin.this).setTitle("Logout")
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
                            dashboard_super dash = new dashboard_super();
                            fragmentTransaction.replace(R.id.super_main_content, dash).addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    })
                    .show();
        }
    }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);

    }
}

