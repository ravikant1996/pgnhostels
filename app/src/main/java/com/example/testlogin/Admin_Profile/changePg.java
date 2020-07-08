package com.example.testlogin.Admin_Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.SignUpResponse;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class changePg extends AppCompatActivity {

    private MyAdapterPG dataAdapter;
    private List<User> userList;
    private RecyclerView mRecyclerViewPg;
    private ProgressBar mProgressBar;
    LinearLayoutManager layoutManager;
    SessionManager sessionManager;
    int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pg);

        sessionManager = new SessionManager(getApplicationContext());
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        HashMap<String, Integer> userID = sessionManager.getPid_admin();
        try{
            pid = userID.get(SessionManager.KEY_PID_ID);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        userList = new ArrayList<>();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_pg);
        mProgressBar.setVisibility(View.VISIBLE);
        ApiInterface api = ApiClient.getApiService();
        Call<SignUpResponse> call = api.get_pid_All_Admin(pid);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        mProgressBar.setVisibility(View.GONE);
                        userList = (ArrayList<User>) response.body().getResult();
                        Log.d("TAG", "Response = " + userList);

                        mRecyclerViewPg = (RecyclerView) findViewById(R.id.recycler_view_pg);
                        mRecyclerViewPg.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(changePg.this);
                        mRecyclerViewPg.setLayoutManager(layoutManager);
                        mRecyclerViewPg.setItemAnimator(new DefaultItemAnimator());
                        dataAdapter = new MyAdapterPG(changePg.this, userList);
                        mRecyclerViewPg.setAdapter(dataAdapter);
                        mProgressBar.setVisibility(View.VISIBLE);
                    } else {
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
                }catch (Exception e){
                    Log.d("TAG","Response = "+response.toString());
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
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