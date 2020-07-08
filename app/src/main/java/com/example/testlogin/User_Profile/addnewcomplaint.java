package com.example.testlogin.User_Profile;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class addnewcomplaint extends Fragment {
    Spinner spinner;
    EditText box, spinText;
    TextView counter;
    Button submit;
    ArrayAdapter<String> a;
    ArrayList<String> brList;
    SessionManager session;
    int id, cid;
    String category;
    FloatingActionButton floatingActionButton;
    public addnewcomplaint() {
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
            View view= inflater.inflate(R.layout.fragment_addnewcomplaint, container, false);
            getActivity().setTitle("Add new complaints");
        session = new SessionManager(getActivity());

        HashMap<String, Integer> userID = session.getUserIDs();
        id = userID.get(SessionManager.KEY_ID);
   //     HashMap<String, Integer> user= session.getPid_admin();
    //    cid = user.get(SessionManager.KEY_PID_ID);

        brList = new ArrayList<>();
        spinner = view.findViewById(R.id.compoType);
        box = view.findViewById(R.id.chatbox);
        spinText = view.findViewById(R.id.spinvalue);
        submit = view.findViewById(R.id.send);
        counter = view.findViewById(R.id.counter);

        box.addTextChangedListener(mTextEditorWatcher);



        addnewcomplaint();
        return view;
    }
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            counter.setText(String.valueOf(s.length())+"/255");
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public void addnewcomplaint() {
        complaintType();
        getselectCategory();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }
    public void check(){
        if(category.equals("Select Category")){
            spinText.setError("Please Select category");
            Toast.makeText(getActivity(), "Please Select category", Toast.LENGTH_SHORT).show();
            return;
        }else if(box.getText().toString().length()==0){
            box.setError("Enter text");
            Toast.makeText(getActivity(), "Enter", Toast.LENGTH_SHORT).show();
        }
        else{
            submit.setEnabled(false);
            submitt();

        }
    }
    public void submitt(){
        String user_type = "send_complaint";
        String message = box.getText().toString();
        int cid=0;
        ApiInterface api = ApiClient.getApiService();
        Call<User> userCall = api.createChatRoomAndSendMessage(id, category, user_type, message, cid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try{
                    if(response.body().getResponse().equals("inserted")){
                        submit.setEnabled(true);
                        Toast.makeText(getActivity(), "Complaint send", Toast.LENGTH_LONG).show();
                        box.setText("");
                        spinner.setSelection(0);
                           closeFragment();
                        //  get_cid();
                    }/*else if(response.body().getResponse().equals("exists")){
                        submit.setEnabled(true);
                      //  spinText.setError("Chat Room Existed");
                        Toast.makeText(getActivity(), "Chat Room Existed", Toast.LENGTH_LONG).show();
                        //   closeFragment();
                        //  get_cid();
*/                     else {
                        submit.setEnabled(true);
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    submit.setEnabled(true);
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                submit.setEnabled(true);
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
    public void complaintType() {
        a = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, brList);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(a);
        brList.add("Select Category");
        brList.add("Room Mate");
        brList.add("Mesh");
        brList.add("Electricity");
        brList.add("Cleaning");
        brList.add("Manager");
        brList.add("Washing");
        brList.add("Water");
        a.notifyDataSetChanged();
    }


    public void getselectCategory() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String pos = String.valueOf(spinner.getSelectedItem());
                spinText.setText(pos);
                category=pos;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //     Toast.makeText(getActivity(), "Please Select the Room!!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void closeFragment() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().detach(addnewcomplaint.this).attach(addnewcomplaint.this).commit();
        }
    }
}
