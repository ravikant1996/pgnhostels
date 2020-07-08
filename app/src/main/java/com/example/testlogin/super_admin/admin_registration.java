package com.example.testlogin.super_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.testlogin.Admin_Profile.bed_mgmt_fragment;
import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.Room;
import com.example.testlogin.Models.RoomResponse;
import com.example.testlogin.Models.SignUpResponse;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class admin_registration extends AppCompatActivity {

    private EditText nameInput, phoneInput, emailInput, ownerInput, locationInput, passwordInput;
    Button regBtn;
    TextView sp;
    ToggleButton status;
    Boolean Status;
    RadioButton newpg, alreadypg;
    Button addButton;
    Spinner pg_spineer;
    private List<User> getPid;
    ArrayAdapter<String> a;
    ArrayList<String> brList;
    int pid;
    String owner;
    String phone;
    RadioGroup radioGroup;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);

        //back button  @override included
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        ownerInput = findViewById(R.id.ownerInput);
        locationInput = findViewById(R.id.locationInput);
        phoneInput = findViewById(R.id.phoneInput);
        progressBar = findViewById(R.id.pb_bar);
        sp = findViewById(R.id.sp);

        radioGroup = findViewById(R.id.radiogroup);
        newpg = findViewById(R.id.newpg);
        alreadypg = findViewById(R.id.alreadypg);
        pg_spineer = findViewById(R.id.pg_spinner);

        brList = new ArrayList<>();
        getPid = new ArrayList<>();

        nameInput.setVisibility(View.GONE);
        emailInput.setVisibility(View.GONE);
        passwordInput.setVisibility(View.GONE);
        locationInput.setVisibility(View.GONE);
        ownerInput.setVisibility(View.GONE);
        phoneInput.setVisibility(View.GONE);

      /*  addButton.setVisibility(View.GONE);
        regBtn.setVisibility(View.GONE);
*/
        pg_spineer.setVisibility(View.GONE);

        regBtn = findViewById(R.id.adminRegBtn);
        progressBar.setVisibility(View.GONE);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
             }
        });
        addButton= findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAlready();
            }
        });
    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        //now check which radio button is selected
        //android switch statement
        switch (v.getId()) {

            case R.id.newpg:
                if (checked) {
                    nameInput.setVisibility(View.GONE);
                    emailInput.setVisibility(View.GONE);
                    passwordInput.setVisibility(View.GONE);
                    locationInput.setVisibility(View.GONE);
                    addButton.setVisibility(View.GONE);
                    pg_spineer.setVisibility(View.GONE);

                    ownerInput.setVisibility(View.VISIBLE);
                    phoneInput.setVisibility(View.VISIBLE);
                    regBtn.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.alreadypg:
                if (checked) {

                    nameInput.setVisibility(View.VISIBLE);
                    emailInput.setVisibility(View.VISIBLE);
                    passwordInput.setVisibility(View.VISIBLE);
                    locationInput.setVisibility(View.VISIBLE);
                    ownerInput.setVisibility(View.GONE);
                    phoneInput.setVisibility(View.GONE);
                    pg_spineer.setVisibility(View.VISIBLE);
                    get_pid();
                    spinner_owner();
                    addButton.setVisibility(View.VISIBLE);
                    regBtn.setVisibility(View.GONE);

                  //  registerAlreadyAdmin();
                }
                 break;
        }
    }

    public void get_pid() {
        a = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, brList);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pg_spineer.setAdapter(a);
        ApiInterface api = ApiClient.getApiService();
        Call<SignUpResponse> userCall = api.getAll_pid();
        userCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        getPid = response.body().getResult();
                        brList.clear();
                        brList.add("Please Select Owner Name");
                        for (int i = 0; i < getPid.size(); i++) {
                            String pid_owner = getPid.get(i).getAowner();
                            int pid = getPid.get(i).getpid();
                            brList.add(pid_owner + " (" + pid + ")");
                        }
                        a.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Toast.makeText(admin_registration.this,"Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                if (t instanceof IOException) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }    }
        });
    }

    public void spinner_owner() {
        pg_spineer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    /*int[] Pid = new int[getPid.size()];

                    int pos = pg_spineer.getSelectedItemPosition();
                    String str = String.valueOf(pg_spineer.getSelectedItem());
                    for (int i = 0; i < getPid.size(); i++) {
                        Pid[i] = getPid.get(i).getpid();
                        if (pos == i) {
                            pid = Pid[i];
                            owner=getPid.get(pos).getAowner();
                            phone=getPid.get(pos).getAphoneno();

                         //   ownerInput.setText(owner);
                        }
                    }*/
                    String []arr= new String[getPid.size()];
                    int[] Pid = new int[getPid.size()];
                    String [] str1 = new String[getPid.size()];
                   // int pos = pg_spineer.getSelectedItemPosition();
                    String s = String.valueOf(pg_spineer.getSelectedItem());

                    int startIndex = s.indexOf('(');
                    Log.e("test", "indexOf = " + startIndex);
                    int endIndex = s.indexOf(')');
                    Log.e("test", "indexOf = " + endIndex);
                    String str2=s.substring(startIndex + 1 , endIndex);
                    Log.e("test", str2);

                    for (int i = 0; i < getPid.size(); i++) {
                        arr[i]= String.valueOf(getPid.get(i).getpid());
                        str1[i]= getPid.get(i).getAowner();
                        Pid[i] = getPid.get(i).getpid();
                        if (str2.equals(arr[i])) {
                            pid = Pid[i];
                            sp.setText(str1[i]);
                            owner=getPid.get(i).getAowner();
                            phone=getPid.get(i).getAphoneno();

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void check() {
        if (ownerInput.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "owner name", Toast.LENGTH_LONG).show();
            ownerInput.setError("owner name?");
            return;
        }
        else if (phoneInput.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Phone No ", Toast.LENGTH_LONG).show();
            phoneInput.setError("Phone No?");
            return;
        }
        else {
            regBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            registerAdmin();
        }
    }

    public void registerAdmin() {

        String phone = phoneInput.getText().toString();
        String owner = ownerInput.getText().toString();
        Status = true;

        ApiInterface api = ApiClient.getApiService();
        Call<User> userCall = api.registernewPG( owner, phone, Status);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("inserted")) {
                    Log.e("response", response.body().getResponse());
                    ownerInput.setText("");
                    phoneInput.setText("");
                   // restartActivity();
                    progressBar.setVisibility(View.GONE);
                    regBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);


            }
        });
    }

    private void checkAlready() {
        if (sp.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "owner name", Toast.LENGTH_LONG).show();
            sp.setError("owner name?");
            return;
        }else if (nameInput.getText().toString().length() == 0) {

            Toast.makeText(getApplicationContext(),
                    "PG Name cannot be Blank", Toast.LENGTH_LONG).show();
            nameInput.setError("PG Name cannot be Blank");
            return;
        } else if (emailInput.getText().toString().length() == 0) {

            Toast.makeText(getApplicationContext(),
                    "Email cannot be Blank", Toast.LENGTH_LONG).show();
            emailInput.setError("Email cannot be Blank");

            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                emailInput.getText().toString()).matches()) {

            Toast.makeText(getApplicationContext(), "Invalid Email",
                    Toast.LENGTH_LONG).show();
            emailInput.setError("Invalid Email");
            return;
        } else if (passwordInput.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Password?", Toast.LENGTH_LONG).show();
            passwordInput.setError("Password?");
            return;
        }
        else if (locationInput.getText().toString().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Phone No ", Toast.LENGTH_LONG).show();
            locationInput.setError("location?");
            return;
        } else {
            addButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            registerAlreadyAdmin();
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

    public void registerAlreadyAdmin() {

        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = md5(passwordInput.getText().toString());
        String location = locationInput.getText().toString();
        Status = true;

        ApiInterface api = ApiClient.getApiService();
        Call<User> userCall = api.registerAdmin(name, email, password, owner, location, phone, Status, pid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.body().getResponse().equals("inserted")) {
                        Log.e("response", response.body().getResponse());
                        nameInput.setText("");
                        emailInput.setText("");
                        passwordInput.setText("");
                        locationInput.setText("");
                        sp.setText("");
                        pg_spineer.setSelection(0);
                        progressBar.setVisibility(View.GONE);
                        addButton.setVisibility(View.VISIBLE);
                        //    restartActivity();
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        addButton.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
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


                progressBar.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
            }
        });
    }

  /*  public void restartActivity(){
        Intent mIntent = getIntent();
        finish();
        startActivity(mIntent);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
