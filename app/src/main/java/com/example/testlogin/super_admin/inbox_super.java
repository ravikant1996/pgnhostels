package com.example.testlogin.super_admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.testlogin.Admin_Profile.inbox_adapter;
import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.SignUpResponse;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class inbox_super extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    LinearLayoutManager layoutManager;
    inbox_super_adapter adapter;
    private List<User> userList;
    SessionManager session;
    int id,cid;

    public inbox_super() {
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
        View view= inflater.inflate(R.layout.fragment_inbox_super, container, false);
        getActivity().setTitle("Inbox");
        session = new SessionManager(getActivity());
        HashMap<String, Integer> userID = session.getUserIDs();
        id = userID.get(SessionManager.KEY_ID);
        userList = new ArrayList<>();
        recyclerView= view.findViewById(R.id.recycler_BoxAdmin);

      //  get_cid();
        sendtext();

        return  view;
    }

    public void sendtext(){
        String complaint_table= "supperer";
        ApiInterface api = ApiClient.getApiService();
        Call<SignUpResponse> call = api.getAllmessage(id, complaint_table);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                try {
                    userList = (ArrayList<User>) response.body().getResult();
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getActivity());
                    //write bottom to top
                   // layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter = new inbox_super_adapter(getActivity(), userList);
                    recyclerView.setAdapter(adapter);
                    //for scrolling bottom to top
                    //   recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    //       recycler();
                }catch (Exception e){
                    Toast.makeText(getActivity(), "ComplaintBox Error", Toast.LENGTH_SHORT).show();
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
