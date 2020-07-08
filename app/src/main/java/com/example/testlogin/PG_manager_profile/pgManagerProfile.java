package com.example.testlogin.PG_manager_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.testlogin.Admin_Profile.bed_allotment;
import com.example.testlogin.Admin_Profile.bed_mgmt_fragment;
import com.example.testlogin.change_password;
import com.example.testlogin.Admin_Profile.inbox_admin;
import com.example.testlogin.Admin_Profile.room_mgmt_fragment;
import com.example.testlogin.Admin_Profile.user_reg_frag;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class pgManagerProfile extends AppCompatActivity {
    TextView Name, Id, Email;

    private DrawerLayout dl_manager;
    private ActionBarDrawerToggle t_manager;
    private NavigationView nv_PgManager;
    TextView managerName;
    CircleImageView circleImageView;
    int manager_id;
    // Session Manager Class
    SessionManager session;
    int aid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_manager_profile);

        session = new SessionManager(getApplicationContext());

        dl_manager = findViewById(R.id.activity_pg_manager_profile);
        t_manager = new ActionBarDrawerToggle(this, dl_manager,R.string.Open, R.string.Close);

        dl_manager.addDrawerListener(t_manager);
        t_manager.syncState();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
      /*  Id= findViewById(R.id.id);
        Name= findViewById(R.id.name);
        Email= findViewById(R.id.email);
*/
        session.isLoggedIn();
        session.checkLogin();
        // get user data from session

        HashMap<String, String> user = session.getUserDetails();
        //id
        String  id= user.get(SessionManager.KEY_ID);
        // name
        String name = user.get(SessionManager.KEY_NAME);
        // email
        String email = user.get(SessionManager.KEY_EMAIL);

        HashMap<String, Integer> users = session.getUserIDs();
        aid=users.get(SessionManager.KEY_ID);

        // displaying user data
       /* Id.setText(Html.fromHtml("PG Code: <b>" + id + "</b>"));
        Name.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
        Email.setText(Html.fromHtml("Email: <b>" + email + "</b>"));*/

        //     valuefromIntent();

        nv_PgManager = findViewById(R.id.nv_pgmanager);
        View headerView = nv_PgManager.inflateHeaderView(R.layout.pg_manager_navigation_header);
        circleImageView = (CircleImageView) headerView.findViewById(R.id.manager_profile_image);

        managerName = (TextView) headerView.findViewById(R.id.managerName);
        managerName.setText(Html.fromHtml("<b>" + name + "</b>"));
        
        dashboard();

        nv_PgManager.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                switch (id) {
                    case R.id.mprofile_admin:
                        admin_profile_frag dash = new admin_profile_frag();
                        fragmentTransaction.replace(R.id.manager_main_content, dash).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_manager.closeDrawer(GravityCompat.START);
                        break;
                   /* case R.id.account_detals:
                        admin_details_reg_frag adddetails = new admin_details_reg_frag();
                        fragmentTransaction.replace(R.id.manager_main_content, adddetails).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_manager.closeDrawer(GravityCompat.START);
                        break;
                    */
                    case R.id.madd_user:
                        user_reg_frag addUser = new user_reg_frag();
                        fragmentTransaction.replace(R.id.manager_main_content, addUser).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_manager.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.madd_room:
                        room_mgmt_fragment roomdetails = new room_mgmt_fragment();
                        //     adddetails.setArguments(bundle);
                        fragmentTransaction.replace(R.id.manager_main_content, roomdetails).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_manager.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.madd_bed:
                        Fragment beddetails = new bed_mgmt_fragment();
                        fragmentTransaction.replace(R.id.manager_main_content, beddetails).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_manager.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.muser_complaint:
                        Fragment compa = new inbox_admin();
                        fragmentTransaction.replace(R.id.manager_main_content, compa).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_manager.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.madd_bed_allotment:
                        bed_allotment allotmentdetails = new bed_allotment();
                        fragmentTransaction.replace(R.id.manager_main_content, allotmentdetails).addToBackStack(null);
                        fragmentTransaction.commit();
                        dl_manager.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mmanager_change_password:
                        Intent intent = new Intent(getApplicationContext(), change_password.class);
                        Bundle extras = new Bundle();
                        String type = "getMAN";
                        extras.putString("type", type);
                        extras.putInt("GET_ID_FOR_PASSWORD", aid);
                        intent.putExtras(extras);
                        startActivity(intent);
                        dl_manager.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.mlogout:
                        session.logoutUser();
                        break;
                }
                return true;
            }
        });
    }
    public void dashboard() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        dashboard_manager addAdmin = new dashboard_manager();
        fragmentTransaction.replace(R.id.manager_main_content, addAdmin);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        //   moveTaskToBack(true);
        FragmentManager adminfm = getFragmentManager();
        if (adminfm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            dl_manager.closeDrawer(GravityCompat.START);
            moveTaskToBack(true);
            adminfm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
            //  finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t_manager.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
 /*
    public void valuefromIntent(){

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");

        Id.setText(id);
        Name.setText(name);
        Email.setText(email);
    }

  */

}
