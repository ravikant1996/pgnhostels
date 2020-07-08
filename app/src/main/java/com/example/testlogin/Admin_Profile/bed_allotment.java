package com.example.testlogin.Admin_Profile;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.Room;
import com.example.testlogin.Models.RoomResponse;
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

public class bed_allotment extends Fragment {
    String Status;
    TextView r1, b1, ten;
    Button allotBed;
    String[] s;
    int aid, rid, bid, uid;
    ArrayList<String> brList;
    ArrayList<String> bedList;
    ArrayList<String> tenentList;
    ProgressBar progressBar;
    Button addBed;
    SessionManager session;
    private List<Room> getRoom;
    private List<Room> getBed;
    private List<User> getTenent;
    ArrayAdapter<String> roomAdapter;
    ArrayAdapter<String> bedAdapter;
    ArrayAdapter<String> tenentAdapter;
    ArrayAdapter<String> statusAdapter;

    Spinner room_spinner, bed_spinner, tenant_spineer ;
    public bed_allotment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_bed_allotment, container, false);

        getActivity().setTitle("Bed Allotment");
        session = new SessionManager(getContext());
        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);
    /*    if(aid==0){
            HashMap<String, Integer> users = session.getAID();
            aid=users.get(SessionManager.KEY_AID);
        }
*/
        brList = new ArrayList<>();
        getRoom = new ArrayList<>();
        bedList = new ArrayList<>();
        getBed = new ArrayList<>();
        getTenent = new ArrayList<>();
        tenentList= new ArrayList<>();

        room_spinner=view.findViewById(R.id.room_spinner);
        bed_spinner=view.findViewById(R.id.bed_spinner);
        tenant_spineer=view.findViewById(R.id.tenant_spinner);
        allotBed=view.findViewById(R.id.allot);
        r1=view.findViewById(R.id.room);
        b1=view.findViewById(R.id.bed);
        ten=view.findViewById(R.id.user);
        progressBar=view.findViewById(R.id.t_pro);

       progressBar.setVisibility(View.INVISIBLE);
       bed_spinner.setEnabled(false);
        getroomDetails();
        spinnerRoom();
        spinnerBed();
        gettenantDetails();
        spinnerTenant();
    //    statusSpinner();

        allotBed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        return view;
    }
    public void validate(){
        if (r1.getText().toString().length() == 0) {
            Toast.makeText(getContext(),
                    "Room no is invalid", Toast.LENGTH_LONG).show();
            r1.setError("Please Select Room No");
            return;
        }
        else if(b1.getText().toString().length() == 0) {

            Toast.makeText(getContext(),
                    "Bed no is invalid", Toast.LENGTH_LONG).show();
            b1.setError("Please Select Bed No");
            return;
        } else if(ten.getText().toString().length() == 0) {

            Toast.makeText(getContext(),
                    "Name is invalid", Toast.LENGTH_LONG).show();
            ten.setError("Please Select Tenent Name");
            return;
        }
        else {
            allotBed.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            allotment();
        }
    }
    public void getroomDetails() {
        roomAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, brList);
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room_spinner.setAdapter(roomAdapter);
        ApiInterface api = ApiClient.getApiService();
        Call<RoomResponse> userCall = api.roomdetailsforBedAllotment(aid);
        userCall.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        getRoom = response.body().getResult();
                        s = new String[getRoom.size()];
                        brList.clear();
                        brList.add("Select Room No");
                        for (int i = 0; i < getRoom.size(); i++) {
                            s[i] = getRoom.get(i).getRoomNo();
                            String roomNo = getRoom.get(i).getRoomNo();
                            brList.add(roomNo);
                        }
                        roomAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {


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
    public void spinnerRoom() {
        room_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               /* int[] arr = new int[getRoom.size()];
                int[] noOfBed= new int[getRoom.size()];
                int pos = room_spinner.getSelectedItemPosition();

                Log.e("poss", "" + pos);
                for (int i = 0; i < getRoom.size(); i++) {
                    noOfBed[i]= getRoom.get(i).getNo_of_bed();
                    arr[i] = getRoom.get(i).getRoomId();
                    if (pos == i) {
                        rid = arr[i];
                        room_spinner.setSelection(pos);
                        Log.e("rid ", "" + rid);
                        Log.e("arr[i] ", "" + arr[i]);

                        getbedDetails();
                    }
                }*/
                int[] arr = new int[getRoom.size()];
              //  int[] noOfBed= new int[getRoom.size()];
                String [] room= new String[getRoom.size()];
                String pos = String.valueOf(room_spinner.getSelectedItem());

              //  Log.e("poss", "" + pos);
                for (int i = 0; i < getRoom.size(); i++) {
                 //   noOfBed[i]= getRoom.get(i).getNo_of_bed();
                    room[i] = getRoom.get(i).getRoomNo();
                    arr[i] = getRoom.get(i).getRoomId();
                    if (pos.equals(room[i])) {
                        rid = arr[i];
                        r1.setText(room[i]);
                        bed_spinner.setEnabled(true);
                        getbedDetails();
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
           //     Toast.makeText(getActivity(), "Please Select the Room !!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getbedDetails() {
        bedAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bedList);
        bedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bed_spinner.setAdapter(bedAdapter);
        ApiInterface api = ApiClient.getApiService();
        Call<RoomResponse> userCall = api.getbeddetails(aid, rid);
        userCall.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        getBed = response.body().getResult();
                    //    s = new String[getRoom.size()];
                        bedList.clear();
                        bedList.add("Select Bed No");
                        for (int i = 0; i < getBed.size(); i++) {
                        //    s[i] = getBed.get(i).getRoomNo();
                            String bedNo = String.valueOf(getBed.get(i).getBed_no());
                            bedList.add(bedNo);
                        }
                        bedAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {


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
    public void spinnerBed() {
        bed_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             /*   int[] arr = new int[getBed.size()];
                int[] bedno= new int[getBed.size()];
                int pos = bed_spinner.getSelectedItemPosition();

                Log.e("poss", "" + pos);
                for (int i = 0; i < getBed.size(); i++) {
                    bedno[i]= getBed.get(i).getBed_no();
                    arr[i] = getBed.get(i).getBid();
                    if (pos == i) {
                        bid = arr[i];
                     //   count= bedno[i];
                        bed_spinner.setSelection(pos);
                        Log.e("rid ", "" + bid);
                        Log.e("arr[i] ", "" + arr[i]);
                    }
                }*/
                int[] arr = new int[getBed.size()];
                String  [] bedno= new String[getBed.size()];
                String  pos = String.valueOf(bed_spinner.getSelectedItem());

                for (int i = 0; i < getBed.size(); i++) {
                    bedno[i]= String.valueOf(getBed.get(i).getBed_no());
                    arr[i] = getBed.get(i).getBid();
                    if (pos.equals(bedno[i])) {
                        bid = arr[i];
                        b1.setText(bedno[i]);
                     //   count= bedno[i];
                    //    bed_spinner.setSelection(pos);
                     //   Log.e("rid ", "" + bid);
                     //   Log.e("arr[i] ", "" + arr[i]);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            //    Toast.makeText(getActivity(), "Please Select the Bed No !!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void gettenantDetails(){
        tenentAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, tenentList);
        tenentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tenant_spineer.setAdapter(tenentAdapter);
        ApiInterface api = ApiClient.getApiService();
        Call<SignUpResponse> userCall = api.getTenentdetails(aid);
        userCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                try {
                        if (response.isSuccessful()){                        //   String var=response.body().getResponse();
                        getTenent = response.body().getResult();
                        //    s = new String[getRoom.size()];
                        tenentList.clear();
                        tenentList.add("Select Tenent Name");
                        for (int i = 0; i < getTenent.size(); i++) {
                            //    s[i] = getBed.get(i).getRoomNo();
                            String Uname = String.valueOf(getTenent.get(i).getuName());
                            tenentList.add(Uname);
                        }
                        tenentAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
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
                    } else if(getActivity()!=null) {
                        Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void spinnerTenant() {
        tenant_spineer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             /*   int[] arr = new int[getTenent.size()];
                String [] tnName= new String[getTenent.size()];
                int pos = tenant_spineer.getSelectedItemPosition();

                Log.e("poss", "" + pos);
                for (int i = 0; i < getTenent.size(); i++) {
                    tnName[i]= getTenent.get(i).getuName();
                    arr[i] = getTenent.get(i).getUid();
                    if (pos == i) {
                        uid = arr[i];
                        //   count= bedno[i];
                        tenant_spineer.setSelection(pos);
                        Log.e("rid ", "" + uid);
                        Log.e("arr[i] ", "" + arr[i]);
                    }
                }*/
                int[] arr = new int[getTenent.size()];
                String [] tnName= new String[getTenent.size()];
                String  pos = String.valueOf(tenant_spineer.getSelectedItem());

                Log.e("poss", "" + pos);
                for (int i = 0; i < getTenent.size(); i++) {
                    tnName[i]= getTenent.get(i).getuName();
                    arr[i] = getTenent.get(i).getUid();
                    if (pos.equals(tnName[i])) {
                        uid = arr[i];
                        ten.setText(tnName[i]);
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
             //   Toast.makeText(getActivity(), "Please Select the Bed No !!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void allotment(){
        Boolean Status = false;
        ApiInterface api = ApiClient.getApiService();
        Call<Room> userCall = api.assignBed(bid, uid, aid, Status);
        userCall.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                try {
                    if (response.body().getResponse().equals("inserted")) {
                        Toast.makeText(getActivity(), "Successfully Inserted", Toast.LENGTH_LONG).show();
                        allotBed.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        closeFragment();
                    } else if (response.body().getResponse().equals("exists")) {
                        Toast.makeText(getActivity(), "Room No is existed", Toast.LENGTH_LONG).show();
                        allotBed.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        allotBed.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    allotBed.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                allotBed.setEnabled(true);
                progressBar.setVisibility(View.GONE);
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
            getFragmentManager().beginTransaction().detach(bed_allotment.this).attach(bed_allotment.this).commit();
        }
    }
}
