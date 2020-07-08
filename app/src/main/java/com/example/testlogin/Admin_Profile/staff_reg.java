package com.example.testlogin.Admin_Profile;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.BuildConfig;
import com.example.testlogin.Models.Room;
import com.example.testlogin.Models.RoomResponse;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class staff_reg extends Fragment implements View.OnClickListener {
    EditText Name, Email, Phone, Address, Date;
    String stafftype;
    Spinner stafftypespinner;
    TextView s1;
    Button register;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    SessionManager session;
    int aid;
    ArrayAdapter<String> typeadapter;
    private List<Room> getType;
    ArrayList<String> stringtypeList;
    ProgressBar progressBar;

    public staff_reg() {
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
        getActivity().setTitle("Staff registration");
        View view= inflater.inflate(R.layout.fragment_staff_reg, container, false);

        session = new SessionManager(getActivity());
        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);
    /*    if(aid==0){
            HashMap<String, Integer> users = session.getAID();
            aid=users.get(SessionManager.KEY_AID);
        }
*/
        getType = new ArrayList<>();
        stringtypeList = new ArrayList<>();

        Name= view.findViewById(R.id.tname);
        Email= view.findViewById(R.id.email);
        Phone= view.findViewById(R.id.phone);
        Address= view.findViewById(R.id.address);
        Date = view.findViewById(R.id.date);
        s1 = view.findViewById(R.id.typestaff);
        stafftypespinner= view.findViewById(R.id.stafftype);
        register= view.findViewById(R.id.staffregbtn);
        progressBar = view.findViewById(R.id.su_pro);

        progressBar.setVisibility(View.INVISIBLE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
       /* stafftypespinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){

                }
                return false;
            }
        });*/
        getstafftypeDetails();
        typespinner();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewsById();
        setDateTimeField();
        return view;
    }
    private void check() {
        if (Email.getText().toString().length() == 0) {

            Toast.makeText(getContext(),
                    "Email id cannot be Blank", Toast.LENGTH_LONG).show();
            Email.setError("Email cannot be Blank");

            return;
        }
        else if(Name.getText().toString().length() == 0) {

            Toast.makeText(getContext(),
                    "Name cannot be Blank", Toast.LENGTH_LONG).show();
            Name.setError("Name cannot be Blank");
            return;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                Email.getText().toString()).matches()) {

            Toast.makeText(getContext(), "Invalid Email",
                    Toast.LENGTH_LONG).show();
            Email.setError("Invalid Email");
            return;
        }else if (s1.getText().toString().length() == 0){
            Toast.makeText(getContext(),
                    "Please Select Stafftype", Toast.LENGTH_LONG).show();
            s1.setError("Please Select Stafftype");
            return;
        }
        else if (Phone.getText().toString().length() == 0){
            Toast.makeText(getContext(),
                    "Phone No?", Toast.LENGTH_LONG).show();
            Phone.setError("Phone No?");
            return;
        }
        else if (Address.getText().toString().length() == 0){
            Toast.makeText(getContext(),
                    "Address?", Toast.LENGTH_LONG).show();
            Address.setError( "Address?");
            return;
        } else if (Date.getText().toString().length() == 0){
            Toast.makeText(getContext(),
                    "Date?", Toast.LENGTH_LONG).show();
            Date.setError( "Date?");
            return;
        }

        else {
            progressBar.setVisibility(View.VISIBLE);
            register.setEnabled(false);
            registerUser();
        }
    }

    public void registerUser() {
        String name = Name.getText().toString();
        String email = Email.getText().toString();
        String phone = Phone.getText().toString();
        String address = Address.getText().toString();
        String joiningdate=Date.getText().toString();
        Boolean Status= true;

        ApiInterface api = ApiClient.getApiService();
        Call<User> userCall = api.registerStaff(name, email, address, phone, aid, stafftype, Status, joiningdate);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                register.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.body().getResponse().equals("inserted")) {
                        Log.e("response", response.body().getResponse());
                        Name.setText("");
                        Email.setText("");
                        Address.setText("");
                        Phone.setText("");
                        Date.setText("");
                        Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_LONG).show();
                        closeFragment();
                    } else {
                        Toast.makeText(getActivity(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                register.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),
                        "Error", Toast.LENGTH_LONG).show();

            }
        });
    }
  /*  private void closeFragment() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().remove(staff_reg.this).commit();
        }
    }*/
   public void closeFragment() {
       if (getFragmentManager() != null) {
           getFragmentManager().beginTransaction().detach(staff_reg.this).attach(staff_reg.this).commit();
       }
   }
    private void findViewsById() {
        Date.setInputType(InputType.TYPE_NULL);
        Date.requestFocus();
    }
    private void setDateTimeField() {
        Date.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Date.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }
    @Override
    public void onClick(View view) {
        if(view == Date) {
            fromDatePickerDialog.show();
        }
    }
    public void getstafftypeDetails() {
        typeadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stringtypeList);
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stafftypespinner.setAdapter(typeadapter);
        ApiInterface api = ApiClient.getApiService();
        Call<RoomResponse> userCall = api.getStafftype();
        userCall.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        getType = response.body().getResult();
                        stringtypeList.clear();
                        stringtypeList.add("Select Staff type");
                        for (int i = 0; i < getType.size(); i++) {
                            String type = getType.get(i).getStafftype();
                            stringtypeList.add(type);
                        }
                       typeadapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
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
    public void typespinner() {
        stafftypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] stafftypearr= new String[getType.size()];
                String pos = String.valueOf(stafftypespinner.getSelectedItem());
                for (int i = 0; i < getType.size(); i++) {
                    stafftypearr[i] = getType.get(i).getStafftype();
                    if (pos.equals(stafftypearr[i])) {
                        stafftype = stafftypearr[i];
                        s1.setText(stafftypearr[i]);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //     Toast.makeText(getActivity(), "Please Select the Room !!", Toast.LENGTH_LONG).show();
            }
        });
    }


}
