package com.example.testlogin.Admin_Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class manager_reg_frag extends Fragment {
    private EditText nameInput, phoneInput, emailInput, passwordInput;
    Button regBtn;
    int AdminId;
    Boolean Status;
    SessionManager session;
    ProgressBar progressBar;

    public manager_reg_frag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        getActivity().setTitle("Add Manager");
/*
        if (getArguments() != null) {
            AdminId = getArguments().getInt("id");
        }*/

        session = new SessionManager(getContext());

        HashMap<String, Integer> userID = session.getUserIDs();
        AdminId = userID.get(SessionManager.KEY_ID);

        View view =  inflater.inflate(R.layout.fragment_manager_reg_frag, container, false);
        nameInput = view.findViewById(R.id.nameInput);
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        phoneInput = view.findViewById(R.id.phoneInput);
        regBtn = view.findViewById(R.id.regBtn);
        progressBar = view.findViewById(R.id.pg_bar);
        progressBar.setVisibility(View.INVISIBLE);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
        return view;
    }
    private void check() {
        if (emailInput.getText().toString().length() == 0) {

            Toast.makeText(getContext(),
                    "Email id cannot be Blank", Toast.LENGTH_LONG).show();
            emailInput.setError("Email cannot be Blank");

            return;
        }
        else if(nameInput.getText().toString().length() == 0) {

                Toast.makeText(getContext(),
                        "Name cannot be Blank", Toast.LENGTH_LONG).show();
                nameInput.setError("Name cannot be Blank");
                return;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                emailInput.getText().toString()).matches()) {

            Toast.makeText(getContext(), "Invalid Email",
                    Toast.LENGTH_LONG).show();
            emailInput.setError("Invalid Email");
            return;
        }
        else if (passwordInput.getText().toString().length() == 0){
            Toast.makeText(getContext(),
                    "Password?", Toast.LENGTH_LONG).show();
            passwordInput.setError( "Password?");
            return;
        }
        else if (passwordInput.length() < 6){
            passwordInput.setError("Password  may be at least 6 characters long.");
        }
        else if (phoneInput.getText().toString().length() == 0){
            Toast.makeText(getContext(),
                    "Phone No?", Toast.LENGTH_LONG).show();
            phoneInput.setError("Phone No?");
            return;
        }
        else if (phoneInput.length() < 10){
            phoneInput.setError("Phone nubmer must be 10 digits");
        }
        else {
                progressBar.setVisibility(View.VISIBLE);
                regBtn.setEnabled(false);
                registerUser();
        }
   }

    public final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void registerUser() {
        int aid= AdminId;
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = md5(passwordInput.getText().toString());
        String phone = phoneInput.getText().toString();
        Status= true;

        ApiInterface api = ApiClient.getApiService();
        Call<User> userCall = api.registerManager(name, email, password, phone, aid, Status);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("inserted")) {
                    Log.e("response", response.body().getResponse());
                    nameInput.setText("");
                    emailInput.setText("");
                    passwordInput.setText("");
                    phoneInput.setText("");
                    regBtn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Registration Successful", Toast.LENGTH_LONG).show();
                   closeFragment();
                }else{
                    regBtn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), response.body().getResponse(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                regBtn.setEnabled(true);
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
    private void closeFragment() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction().remove(manager_reg_frag.this).commit();
        }
    }

}
