package com.example.testlogin.User_Profile;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.Admin_Profile.inbox_admin;
import com.example.testlogin.change_password;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class userProfile extends AppCompatActivity {
    TextView Name, Id, Email;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv_User;
    RelativeLayout bar1, bar2, bar4, bar5;
    ImageView bar3;
    // admin id
    int uid, aid;
    String email;
    SessionManager session;
    TextView userName;
    CircleImageView cvimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        session = new SessionManager(getApplicationContext());

        dl = findViewById(R.id.activity_user_profile);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
      /*  Id= findViewById(R.id.id);
        Name= findViewById(R.id.name);
        Email= findViewById(R.id.email);
*/
        session.isLoggedIn();
        session.checkLogin();

        HashMap<String, Integer> users = session.getUserIDs();
        uid=users.get(SessionManager.KEY_ID);

        HashMap<String, Integer> userID = session.getPid_admin();
        aid = userID.get(SessionManager.KEY_PID_ID);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        String name = user.get(SessionManager.KEY_NAME);
        // email
        email = user.get(SessionManager.KEY_EMAIL);

        // displaying user data
     /*   Id.setText(Html.fromHtml("User Code: <b>" + id + "</b>"));
        Name.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
        Email.setText(Html.fromHtml("Email: <b>" + email + "</b>"));
*/
        //     valuefromIntent();
        dashbord();
        nv_User = findViewById(R.id.nv_user);
        View headerView = nv_User.inflateHeaderView(R.layout.user_navigation_header);
        cvimage = (CircleImageView) headerView.findViewById(R.id.user_profile_image);

        userName = (TextView) headerView.findViewById(R.id.userName);
        userName.setText(Html.fromHtml("<b>" + name + "</b>"));

        nv_User.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                        .beginTransaction();
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.uaccount:
                            dashboard_user addAdmin = new dashboard_user();
                            fragmentTransaction.replace(R.id.user_main_content, addAdmin);
                            fragmentTransaction.commit();
                            dl.closeDrawer(GravityCompat.START);
                            break;
                    case R.id.ucompa:
                            complaintBox dash = new complaintBox();
                            fragmentTransaction.replace(R.id.user_main_content, dash).addToBackStack(null);
                            fragmentTransaction.commit();
                            dl.closeDrawer(GravityCompat.START);
                            break;
                    case R.id.uchangepass:
                            Intent intent = new Intent(getApplicationContext(), change_password.class);
                            Bundle extras = new Bundle();
                            String type = "getUSR";
                            extras.putString("type", type);
                            extras.putInt("GET_ID_FOR_PASSWORD", uid);
                            extras.putInt("aid", aid);
                            intent.putExtras(extras);
                            startActivity(intent);
                            dl.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.ulogout:
                            session.logoutUser();
                            Toast.makeText(userProfile.this, "Thank You!",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }

                return true;
            }
        });
    }


    public void dashbord() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        dashboard_user addAdmin = new dashboard_user();
        fragmentTransaction.replace(R.id.user_main_content, addAdmin);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        //   moveTaskToBack(true);
        FragmentManager adminfm = getFragmentManager();
        if (adminfm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            dl.closeDrawer(GravityCompat.START);
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
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

  /*  public void passwordChange(View view) {
        Intent intent = new Intent(getApplicationContext(), change_password.class);
        Bundle extras = new Bundle();
        String type = "getUSR";
        extras.putString("type", type);
        extras.putInt("id", id);
        extras.putInt("aid", aid);
        intent.putExtras(extras);
        getApplicationContext().startActivity(intent);
    }

    public void homebtn(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        dashboard_user addAdmin = new dashboard_user();
        fragmentTransaction.replace(R.id.user_main_content, addAdmin);
        fragmentTransaction.commit();
    }

    public void complaint(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        complaintBox addAdmin = new complaintBox();
        fragmentTransaction.replace(R.id.user_main_content, addAdmin);
        fragmentTransaction.commit();
    }*/
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
