package com.example.testlogin.Admin_Profile;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

public class admin_details_filling extends AppCompatActivity {
    private EditText phoneInput, Locatity_Input, des_Input ;
    Button regBtn;
    TextView s1, s2;
    Spinner city_Input, state_Input;
    private EditText contper_Input;
    SessionManager session;
    String city, state;
    int aid, uid;
    ProgressBar progressBar;
    ArrayAdapter<String> stateAdapter, cityAdapter;
    private List<User> getStates, getCitys;
    ArrayList<String> stateList, cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_details_filling);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(getApplicationContext());

        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);

       /* Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // 5. get status value from bundle
        aid = bundle.getInt("position");*/
       getCitys= new ArrayList<>();
       getStates= new ArrayList<>();
       stateList= new ArrayList<>();
       cityList= new ArrayList<>();

        contper_Input = findViewById(R.id.contper_Input);
        phoneInput =    findViewById(R.id.phoneInput);
        Locatity_Input = findViewById(R.id.Locatity_Input);
        city_Input =    findViewById(R.id.city_Input);
        state_Input =   findViewById(R.id.state_Input);
        des_Input =     findViewById(R.id.des_Input);
        progressBar =     findViewById(R.id.detail_prog);
        s1 =     findViewById(R.id.s1);
        s2 =     findViewById(R.id.s2);

        regBtn = findViewById(R.id.adminDetailsRegBtn);
        //  regBtn.setEnabled(false);
        progressBar.setVisibility(View.INVISIBLE);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
             //   Log.e("reg button", "clicked");
            }
        });
        spinnerState();
        stateSelect();
        citySelect();

    }

    private void check() {
        if(contper_Input.getText().toString().length() == 0) {
            contper_Input.setError("Contact person name cannot be Blank");
            return;
        }else if ( phoneInput.getText().toString().length() == 0) {
            phoneInput.setError("Contact person phone No?");
            return;
        }else if( Locatity_Input.getText().toString().length() == 0) {
            Locatity_Input.setError("Address cannot be Blank");
            return;
        }
        else if( s1.getText().toString().length() == 0) {
            s1.setError("Please Select State First");
            return;
        }else if( s2.getText().toString().length() == 0) {
            s2.setError("Please Select City First");
            return;
        }
        else if(des_Input.getText().toString().length() == 0 ){
            des_Input.setError("Description cannot be Blank");
            return;
        } else {
            //  regBtn.setEnabled(true);
            progressBar.setVisibility(View.VISIBLE);
            registerAdmin();
        }
    }

    public void registerAdmin() {

        String contact_person = contper_Input.getText().toString();
        String phone = phoneInput.getText().toString();
        String locatity = Locatity_Input.getText().toString();
        String description = des_Input.getText().toString();

        ApiInterface api = ApiClient.getApiService();
        Call<User> userCall = api.registerAdminDetails(contact_person, phone, locatity, city, state, description, aid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    if (response.isSuccessful()) {
                        String Response = response.body().getResponse();
                        Log.e("response", response.body().getResponse());
                        contper_Input.setText("");
                        phoneInput.setText("");
                        Locatity_Input.setText("");
                        des_Input.setText("");
                        state_Input.setSelection(0);
                        city_Input.setSelection(0);
                        Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(admin_details_filling.this, adminProfile.class));
                        finish();
                    }else {
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
                    e.printStackTrace();
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
            }
        });
    }

     public void spinnerState() {
         stateAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, stateList);
         stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         state_Input.setAdapter(stateAdapter);
         ApiInterface api = ApiClient.getApiService();
         Call<SignUpResponse> userCall = api.getStates();
         userCall.enqueue(new Callback<SignUpResponse>() {
             @Override
             public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                 try {
                     if (response.isSuccessful()) {
                         getStates = response.body().getResult();
                         stateList.clear();
                         stateList.add("Select State");
                         for (int i = 0; i < getStates.size(); i++) {
                             String type = getStates.get(i).getState();
                             stateList.add(type);
                         }
                       //  stateList.add("Select State");
                         stateAdapter.notifyDataSetChanged();
                     }else{
                      Toast.makeText(getApplicationContext(), response.body().getResponse(), Toast.LENGTH_LONG).show();
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
    public void stateSelect() {
        state_Input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] statearr= new String[getStates.size()];
                int [] state_id= new int[getStates.size()];
          //      int pos = state_Input.getSelectedItemPosition();
                String str = String.valueOf(state_Input.getSelectedItem());
                for (int i = 0; i < getStates.size(); i++) {
                    statearr[i] = getStates.get(i).getState();
                    state_id[i] = getStates.get(i).getUid();
                /*    if (pos == i) {
                        state = statearr[i];
                        uid = state_id[i];
                        spinnerCity();
                   }*/
                    if (str.equals(statearr[i])) {
                        state = statearr[i];
                        uid = state_id[i];
                        s1.setText(statearr[i]);
                        spinnerCity();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //     Toast.makeText(getActivity(), "Please Select the Room !!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void spinnerCity(){
        cityAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cityList);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city_Input.setAdapter(cityAdapter);
        ApiInterface api = ApiClient.getApiService();
        Call<SignUpResponse> userCall = api.getCity(uid);
        userCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        getCitys = response.body().getResult();
                        cityList.clear();
                        cityList.add("Select City");
                        for (int i = 0; i < getCitys.size(); i++) {
                            String type = getCitys.get(i).getCity();
                            cityList.add(type);
                        }
                    //    cityList.setPrompt("Select Staff Type");
                        cityAdapter.notifyDataSetChanged();
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
    public void citySelect() {
        city_Input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] cityarr= new String[getCitys.size()];
             //   int pos = city_Input.getSelectedItemPosition();
                String str= String.valueOf(city_Input.getSelectedItem());
                for (int i = 0; i < getCitys.size(); i++) {
                    cityarr[i] = getCitys.get(i).getCity();
               /*     if (pos == i) {
                        city = cityarr[i];
                    }*/
                    if(str.equals(cityarr[i])){
                        city= cityarr[i];
                        s2.setText(cityarr[i]);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //     Toast.makeText(getActivity(), "Please Select the Room !!", Toast.LENGTH_LONG).show();
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
