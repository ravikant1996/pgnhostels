package com.example.testlogin;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.testlogin.Models.User;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class change_password extends AppCompatActivity {
     EditText edtOldPass, edtNewPass, edtConfirmPass;
     Button btnChangePass;
     String getPassword;
     int id;
     int aid;
     String type;
     String command;
     String password;
     ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            id = bundle.getInt("GET_ID_FOR_PASSWORD", -1);
            aid = bundle.getInt("aid", -1);
            command= bundle.getString("type", null);
        }

        progressBar =  findViewById(R.id.progressBar_passwa);

        edtOldPass= (EditText) findViewById(R.id.edtoldPassword);
        edtNewPass = (EditText) findViewById(R.id.edtNewPassword);
        edtConfirmPass = (EditText) findViewById(R.id.edtConfirmPassword);
        btnChangePass = (Button) findViewById(R.id.btnChangePassword);
        progressBar.setVisibility(View.INVISIBLE);
        getpassword();
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
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

    public void getpassword(){
        //Creating an object of our api interface
        String password = String.valueOf(aid);
        String type= command;
        ApiInterface api = ApiClient.getApiService();
        Call<User> call = api.updatepassword(id, password, type);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                    try {
                        if (response.isSuccessful()) {
                            getPassword = response.body().getsPassword();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "11111111", Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                if (t instanceof IOException) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void check(){
        if(getPassword != null) {
            if (edtOldPass.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "Old Password cannot be Blank", Toast.LENGTH_LONG)
                        .show();
                edtOldPass.setError("Old Password cannot be Blank");
                return;
            }
            else if (edtNewPass.getText().toString().length() == 0) {
                Toast.makeText(getApplicationContext(),
                        "New Password cannot be Blank", Toast.LENGTH_LONG)
                        .show();
                edtNewPass.setError("New Password cannot be Blank");
                return;
            }
            else if (edtNewPass.getText().length() < 8) {

                Toast.makeText(getApplicationContext(),
                        "Password must be 8 characters above",
                        Toast.LENGTH_LONG).show();
                edtNewPass.setError("Password must be 8 characters above");

                return;

            }
            else if (!(getPassword.equals(md5(edtOldPass.getText().toString())))) {
                Toast.makeText(getApplicationContext(), "Old Password Is Not Match.",
                        Toast.LENGTH_SHORT).show();
                edtOldPass.setError("Old Password Is Not Match");
            }
            else if (!edtNewPass.getText().toString().equals(edtConfirmPass.getText().toString())) {
                Toast.makeText(getApplicationContext(),
                        "Confirm Password Is Not Match.",
                        Toast.LENGTH_SHORT).show();
                edtConfirmPass.setError("Confirm Password Is Not Match.");
            }
            else {
                btnChangePass.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                select();
            }
        }
    }
    public void select(){
        try {
            switch (command) {
                case "getAD":
                    changeAdminpassword();
                    break;
                case "getMAN":
                    changeManagerpassword();
                    break;
                case "getUSR":
                    changeUserpassword();
                    break;
            }
        }catch (Exception e){

            btnChangePass.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "method selection error" , Toast.LENGTH_LONG).show();
        }
    }

    public void changeAdminpassword(){
        type = "admin_";
        password = md5(edtNewPass.getText().toString());

        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();
        Call<User> call = api.updatepassword(id, password, type);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                        edtOldPass.setText("");
                        edtNewPass.setText("");
                        edtConfirmPass.setText("");
                        btnChangePass.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        btnChangePass.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    btnChangePass.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                btnChangePass.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                if (t instanceof IOException) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void changeManagerpassword(){
        type = "_manager";
        password = edtNewPass.getText().toString();

        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();
        Call<User> call = api.updatepassword(id, password, type);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.isSuccessful()) {
                        //Dismiss Dialog
                        Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                        edtOldPass.setText("");
                        edtNewPass.setText("");
                        edtConfirmPass.setText("");

                        btnChangePass.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
                        finish();
                    } else {

                        btnChangePass.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    btnChangePass.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                btnChangePass.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                if (t instanceof IOException) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void changeUserpassword(){
        type = "user_";
        password = edtNewPass.getText().toString();

        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();
        Call<User> call = api.updatepassword(id, password, type);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.isSuccessful()) {
                        //Dismiss Dialog
                        Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                        edtOldPass.setText("");
                        edtNewPass.setText("");
                        edtConfirmPass.setText("");

                        btnChangePass.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        btnChangePass.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    btnChangePass.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                btnChangePass.setEnabled(true);
                progressBar.setVisibility(View.GONE);
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


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
