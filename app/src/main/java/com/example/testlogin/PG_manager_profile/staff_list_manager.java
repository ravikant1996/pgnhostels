package com.example.testlogin.PG_manager_profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.Admin_Profile.staff_list_adapter;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class staff_list_manager extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    staff_list_manager_adapter adapter;
    private List<User> userList;
    SessionManager session;
    int aid;
    TextView name, mobile, email , mid;
    TextView mStatus;
    private View view;

    public staff_list_manager() {
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
        getActivity().setTitle("Staff List");
        View view = inflater.inflate(R.layout.fragment_staff_list_manager, container, false);

        session = new SessionManager(getActivity());
        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);
    /*    if(aid==0){
            HashMap<String, Integer> users = session.getAID();
            aid=users.get(SessionManager.KEY_AID);
        }
    */    userList = new ArrayList<User>();
        progressBar = view.findViewById(R.id.progress_bar_staff);
        recyclerView = view.findViewById(R.id.recycler_view_staff);
        name = view.findViewById(R.id.nam);
        email = view.findViewById(R.id.em);
        mobile = view.findViewById(R.id.mob);
     //   mStatus = view.findViewById(R.id.mStatus);
        mid = view.findViewById(R.id.mid);

     //   getManagerDetail();
        getStaffsList();
     /*   mStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearspace();
            }
        });*/
        return view;
    }

   /* public void clearspace() {
        try {
            new AlertDialog.Builder(getActivity()).setTitle("delete")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                               try{
                            int id1 = Integer.parseInt(mid.getText().toString());
                            String type = "manager";
                            int id2 = 0;
                            int id3 = 0;
                            ApiInterface api = ApiClient.getApiService();
                            Call<ResponseBody> call = api.clear(id1, id2, id3, aid, type);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        if (response.isSuccessful()) {
                                            *//*Intent intent1 = new Intent(getActivity(), adminProfile.class);
                                            getActivity().startActivity(intent1);
                                            ((Activity) getActivity()).finish();*//*
                                            closeFragment();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
   /* public void getManagerDetail(){
        ApiInterface api = ApiClient.getApiService();
        Call<User> call = api.getManager(aid);
        call.enqueue(new Callback<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if(response.isSuccessful()) {
                        String id= String.valueOf(response.body().getManager_id());
                        String Name =response.body().getmanagerName();
                        String Emailid= response.body().getmanagerEmail();
                        String mob= response.body().getManager_phone();
                        mid.setText(id);
                        name.setText("  "+Name);
                        email.setText("  "+Emailid);
                        mobile.setText("  "+mob);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                try{
                    if(t.equals("timeout")){
                        getStaffsList();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "rp :" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
*/

    public void getStaffsList(){
        ApiInterface api = ApiClient.getApiService();
        Call<SignUpResponse> call = api.getStaffList(aid);
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
                        adapter = new staff_list_manager_adapter(getActivity(), userList);
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

    public void closeFragment() {
        if (getFragmentManager() != null) {
            //   getFragmentManager().beginTransaction().remove(room_mgmt_fragment.this).commit();
            getFragmentManager().beginTransaction().detach(staff_list_manager.this).attach(staff_list_manager.this).commit();

        }
    }
}
