package com.example.testlogin.Admin_Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.Room;
import com.example.testlogin.Models.RoomResponse;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class bed_mgmt_fragment extends Fragment {
    EditText  bedNo;
    TextView b1;
    Spinner room_spinner;
    String Status;
    int getcount;
    int rid;
    int count;
    String[] s;
    int aid;
    ArrayList<String> brList;
    Button addBed;
    SessionManager session;
    private List<Room> getRoom;
    ArrayAdapter<String> a;
    ProgressBar progressBar;

    public bed_mgmt_fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_bed_mgmt_fragment, container, false);

        getActivity().setTitle("Add Bed");

        session = new SessionManager(getContext());
        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);
     /*   if(aid==0){
            HashMap<String, Integer> users = session.getAID();
            aid=users.get(SessionManager.KEY_AID);
        }
       */ brList = new ArrayList<>();
        getRoom = new ArrayList<>();
        bedNo=view.findViewById(R.id.bed);
        b1=view.findViewById(R.id.b1);
        room_spinner=view.findViewById(R.id.room_spinner);
        addBed=view.findViewById(R.id.addBBtn);
        progressBar=view.findViewById(R.id.b_pro);
        progressBar.setVisibility(View.INVISIBLE);
        getroomDetails();
        spinnerRoom();
        //   getbedcount();
        //   buttonclick();
        addBed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        return view;
    }

    public void getroomDetails() {
        a = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, brList);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room_spinner.setAdapter(a);
        ApiInterface api = ApiClient.getApiService();
        Call<RoomResponse> userCall = api.getroomdetails(aid);
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
                        a.notifyDataSetChanged();
                    }else if(getActivity()!=null) {
                        Toast.makeText(getActivity(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
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

    public void validate(){
        if(b1.getText().toString().length() == 0){
            b1.setError("Select Room No");
            Toast.makeText(getActivity(), "Select Room No", Toast.LENGTH_SHORT).show();
            return;
        }else if(bedNo.getText().toString().length() == 0){
            bedNo.setError("Enter Bed No");
            Toast.makeText(getActivity(), "Enter Bed No", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            addBed.setEnabled(false);
            createBed();
        }
    }
    public void createBed() {
        if (count == getcount) {
            Toast.makeText(getActivity(), "Room full", Toast.LENGTH_LONG).show();
            addBed.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        } else {
               if (count > getcount) {
                   createBedField();
               }
        }
    }

    public void createBedField() {

        String bed_no = bedNo.getText().toString();
        ApiInterface api = ApiClient.getApiService();
        Call<Room> userCall = api.Add_bed(bed_no, rid, aid);
        userCall.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if(response.body().getResponse().equals("inserted")){
                    Log.e("response", response.body().getResponse());
                    bedNo.setText("");
                    addBed.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Successfully Inserted", Toast.LENGTH_LONG).show();
                    closeFragment();
                } else if (response.body().getResponse().equals("exists")) {
                    addBed.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Room No is exixted", Toast.LENGTH_LONG).show();
                } else {
                    addBed.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                addBed.setEnabled(true);
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

    public void spinnerRoom() {
        room_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               /* int[] arr = new int[getRoom.size()];
                int[] noOfBed= new int[getRoom.size()];
                int pos = room_spinner.getSelectedItemPosition();

              //  Log.e("poss", "" + pos);
                for (int i = 0; i < getRoom.size(); i++) {
                    noOfBed[i]= getRoom.get(i).getNo_of_bed();
                    arr[i] = getRoom.get(i).getRoomId();
                    if (pos == i) {
                        rid = arr[i];
                        count= noOfBed[i];
                        getbedcount();
                        room_spinner.setSelection(pos);
                    }
                }*/
                int[] arr = new int[getRoom.size()];
                int[] noOfBed= new int[getRoom.size()];
                String [] test= new String[getRoom.size()];
                String pos = String.valueOf(room_spinner.getSelectedItem());
             //   closeFragment();

                for (int i = 0; i < getRoom.size(); i++) {
                    test[i]=getRoom.get(i).getRoomNo();
                    noOfBed[i]= getRoom.get(i).getNo_of_bed();
                    arr[i] = getRoom.get(i).getRoomId();
                    if (pos.equals(test[i])) {
                        rid = arr[i];
                        count= noOfBed[i];
                        b1.setText(test[i]);
                        getbedcount();
                      /*  if(count==getcount){
                            clear();
                        }*/
                        // room_spinner.setSelection(pos);
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //     Toast.makeText(getActivity(), "Please Select the Room!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getbedcount(){
        ApiInterface api = ApiClient.getApiService();
        Call<Room> userCall = api.room_count(rid, aid);
        userCall.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.isSuccessful()) {
                    getcount = response.body().getNo_of_bed();
                    Log.e("count beds", "" + getcount);
                }
            }
            @Override
            public void onFailure(Call<Room> call, Throwable t) {


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
   /* public void clear(){
        int id1 = rid;
        String type = "room";
        int id2 = 0;
        int id3 = 0;
        ApiInterface api = ApiClient.getApiService();
        Call<ResponseBody> call = api.clear(id1, id2, id3, aid, type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.e("delete", "free");
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
    }
*/
    public void closeFragment() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().detach(bed_mgmt_fragment.this).attach(bed_mgmt_fragment.this).commit();
        }
    }
}
