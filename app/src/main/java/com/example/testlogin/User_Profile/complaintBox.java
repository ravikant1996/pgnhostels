package com.example.testlogin.User_Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Toast;

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

import static androidx.constraintlayout.widget.Constraints.TAG;

public class complaintBox extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    LinearLayoutManager layoutManager;
    complaintBox_adapter adapter;
    private List<User> userList;
    SessionManager session;
    int id,cid;
    String category;


    public complaintBox() {
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
        View view= inflater.inflate(R.layout.fragment_complaintbox, container, false);
        getActivity().setTitle("complaints");
        session = new SessionManager(getActivity());
        HashMap<String, Integer> userID = session.getUserIDs();
        id = userID.get(SessionManager.KEY_ID);
        userList = new ArrayList<>();
        recyclerView= view.findViewById(R.id.recycler_Box);
        floatingActionButton= view.findViewById(R.id.floatingActionButton);

      //  get_cid();
        sendtext();
        floatingActionButton.setOnClickListener(this);

        return  view;
    }


    @Override
    public void onClick(View v) {
        try {
            if(getActivity()!=null) {
                FragmentTransaction fragmentTransaction = (getActivity()).getSupportFragmentManager()
                        .beginTransaction();
                addnewcomplaint chat = new addnewcomplaint();
                fragmentTransaction.replace(R.id.user_main_content, chat);
                fragmentTransaction.commit();
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendtext(){
        String complaint_table= "compo_table";
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
                    adapter = new complaintBox_adapter(getActivity(), userList);
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
