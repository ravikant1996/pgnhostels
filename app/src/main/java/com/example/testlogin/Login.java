package com.example.testlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testlogin.Admin_Profile.adminProfile;
import com.example.testlogin.Models.SignUpResponse;
import com.example.testlogin.Models.User;
import com.example.testlogin.PG_manager_profile.pgManagerProfile;
import com.example.testlogin.User_Profile.userProfile;
import com.example.testlogin.super_admin.superAdmin;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Login extends AppCompatActivity {

    EditText Email, Password;
    Button login;
    List<SignUpResponse> signupResposne;
    int Id;
    String getEmail, getName, getPassword;
    // Session Manager Class
    SessionManager session;
    private SharedPreferences sharedPreferences;

    static final String PREF_NAME = "Reg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());
        // login type checker
        logintype_check();
        // init the EditText and Button
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
    //    session.isLoggedIn();
        login = findViewById(R.id.Login);

        // implement setOnClickListener event on sign up Button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate the fields and call sign method to implement the api
                check();
            }
        });
    }
    public void logintype_check(){
        if (session.isLoggedIn()) {
            HashMap<String, String> user = session.getUserDetails();

            String logintype = user.get(SessionManager.KEY_LOGINTYPE);
            if (logintype != null) {
                if (logintype.equals("super")) {
                    startActivity(new Intent(Login.this, superAdmin.class));
                    finish();
                } else if (logintype.equals("manager")) {
                    startActivity(new Intent(Login.this, pgManagerProfile.class));
                    finish();
                } else if (logintype.equals("admin")) {
                    startActivity(new Intent(Login.this, adminProfile.class));
                    finish();
                } else {
                    startActivity(new Intent(Login.this, userProfile.class));
                    finish();
                }
            }
        }
    }

    private void check() {
        if (Email.getText().toString().length() == 0) {

            Toast.makeText(getApplicationContext(),
                    "Email id cannot be Blank", Toast.LENGTH_LONG).show();
            Email.setError("Email cannot be Blank");
            return;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                Email.getText().toString()).matches()) {

            Toast.makeText(getApplicationContext(), "Invalid Email",
                    Toast.LENGTH_LONG).show();
            Email.setError("Invalid Email");
            return;
        } else if (Password.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Password cannot be Blank", Toast.LENGTH_LONG).show();
            Password.setError("Password cannot be Blank");
            return;
        } else if (Password.length() < 6) {
            Password.setError("Password  may be at least 6 characters long.");
            return;
        } else {
            getlogin();
        }
    }
    public final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void getlogin() {
        final String email = Email.getText().toString();
        final String password;
        if(email.equals("kant.ravi747@gmail.com")){
            password= Password.getText().toString();
        }else {
           password = md5(Password.getText().toString());
        }
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        ApiInterface api = ApiClient.getApiService();

        Call<User> call = api.login(email, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.isSuccessful()) {
                        String var = response.body().getResponse();
                        if (var.equals("super")) {
                            Id = response.body().getsid();
                            getName = response.body().getsName();
                            getEmail = response.body().getsEmail();
                            progressDialog.dismiss();
                            session.createIdSession(Id);
                            session.createLoginSession(var, getName, getEmail);
                            Intent intent = new Intent(Login.this, superAdmin.class);
                            startActivity(intent);
                            finish();
                        } else if (var.equals("manager")) {
                            Id = response.body().getaid();
                            getName = response.body().getmanagerName();
                            getEmail = response.body().getmanagerEmail();
                            progressDialog.dismiss();
                            session.createIdSession(Id);
                            session.createLoginSession(var, getName, getEmail);
                            Intent intent = new Intent(Login.this, pgManagerProfile.class);
                            startActivity(intent);
                            finish();
                        } else if (var.equals("admin")) {
                            int pid = response.body().getpid();
                            Id = response.body().getaid();
                            getName = response.body().getaName();
                            getEmail = response.body().getaEmail();
                            progressDialog.dismiss();
                            session.createIdSession(Id);
                            session.createPidSession(pid);
                            session.createLoginSession(var, getName, getEmail);
                            Intent intent = new Intent(Login.this, adminProfile.class);
                            startActivity(intent);
                            finish();
                        } else if (var.equals("user")) {
                            Id = response.body().getUid();
                            int aid = response.body().getaid();
                            getName = response.body().getuName();
                            getEmail = response.body().getuEmail();
                            progressDialog.dismiss();
                            session.createIdSession(Id);
                            session.createPidSession(aid);
                            session.createLoginSession(var, getName, getEmail);
                            Intent intent = new Intent(Login.this, userProfile.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Credentials are not Valid.", Toast.LENGTH_LONG).show();
                            Email.setText("");
                            Password.setText("");
                            progressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(Login.this, "Credentials are not Valid2.", Toast.LENGTH_LONG).show();
                        switch (response.code()) {
                            case 404:
                                Toast.makeText(getApplicationContext(), "not found", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(getApplicationContext(), "server broken", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "unknown error", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                try {
                    if (t instanceof IOException) {
                        if (t instanceof SocketTimeoutException) {
                            Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }


                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // disable going back to the super_admin
        moveTaskToBack(true);
    }
}

