package com.example.testlogin.Admin_Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.Room;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class room_mgmt_fragment extends Fragment {

    EditText addRoom, addBed;
    Spinner spinner;
    Button btnAdd;
    String  room_no;
    int no_of_bed;
    int aid;
    SessionManager session;
    ProgressBar progressBar;

    public room_mgmt_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_room_mgmt_fragment, container, false);

        getActivity().setTitle("Add Room");

        session = new SessionManager(getContext());

        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);
       /* if(aid==0){
            HashMap<String, Integer> users = session.getAID();
            aid=users.get(SessionManager.KEY_AID);
        }*/
        addRoom=view.findViewById(R.id.room);
        addBed=view.findViewById(R.id.bed);
        btnAdd= view.findViewById(R.id.addBtn);
        progressBar= view.findViewById(R.id.rp);

        progressBar.setVisibility(View.INVISIBLE);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        return view;
    }
    public void check(){
        if(addRoom.getText().toString().length() == 0){
            addRoom.setError("Enter Room No");
            return;
        }
        else if(addBed.getText().toString().length()== 0){
            addBed.setError("No of Bed");
            return;
        }else{
            btnAdd.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            createRoomField();
        }
    }
    public void createRoomField(){

        room_no= addRoom.getText().toString();
        no_of_bed= Integer.parseInt(addBed.getText().toString());
        ApiInterface api = ApiClient.getApiService();
        Call<Room> userCall = api.Add_Room(room_no, no_of_bed, aid);
        userCall.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.body().getResponse().equals("inserted")) {
                    int rid=response.body().getRoomId();
                    Log.e("response", response.body().getResponse());
                    Toast.makeText(getActivity(), "Successfully Inserted", Toast.LENGTH_LONG).show();
                    addRoom.setText("");
                    addBed.setText("");
                    session.createRoomNo(room_no);
                    session.create_roomId_Session(rid,no_of_bed);
                    closeFragment();
                    progressBar.setVisibility(View.GONE);
                    btnAdd.setEnabled(true);
                } else if(response.body().getResponse().equals("exists"))  {
                    progressBar.setVisibility(View.GONE);
                    btnAdd.setEnabled(true);
                    Toast.makeText(getActivity(), "Room No is exixted", Toast.LENGTH_LONG).show();
                }else if(response.body().getResponse().equals("error"))  {
                    progressBar.setVisibility(View.GONE);
                    btnAdd.setEnabled(true);
                    Toast.makeText(getActivity(), "Error2", Toast.LENGTH_LONG).show();
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    btnAdd.setEnabled(true);
                    Toast.makeText(getActivity(), "Error1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnAdd.setEnabled(true);
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                if (t instanceof IOException) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void closeFragment() {
        if (getFragmentManager() != null) {
         //   getFragmentManager().beginTransaction().remove(room_mgmt_fragment.this).commit();
            getFragmentManager().beginTransaction().detach(room_mgmt_fragment.this).attach(room_mgmt_fragment.this).commit();

        }
    }


}
