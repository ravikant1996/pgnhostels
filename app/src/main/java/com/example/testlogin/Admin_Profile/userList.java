package com.example.testlogin.Admin_Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

public class userList extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    userlistAdapter adapter;
    private List<User> userList;
    SessionManager session;
    int aid;

    public userList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Tenent List");
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        session = new SessionManager(getActivity());
        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);
    /*    if(aid==0) {
            HashMap<String, Integer> users = session.getAID();
            aid = users.get(SessionManager.KEY_AID);
        }
    */    userList = new ArrayList<User>();

        progressBar = view.findViewById(R.id.progress_bar_user);
        recyclerView = view.findViewById(R.id.recycler_view_user);
        progressBar.setVisibility(View.VISIBLE);
        getTenentsList();
        return view;

    }

    public void getTenentsList(){
        ApiInterface api = ApiClient.getApiService();
        //     layoutManager = new LinearLayoutManager(getActivity());
        //   recyclerView.setLayoutManager(layoutManager);
        Call<SignUpResponse> call = api.getUserDetails(aid);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                try {
                    if(response.isSuccessful()) {
                        userList = (ArrayList<User>) response.body().getResult();
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        adapter = new userlistAdapter(getActivity(), userList);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                        //   adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                try {
                    if (t instanceof IOException) {
                        if (t instanceof SocketTimeoutException) {
                            Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}

