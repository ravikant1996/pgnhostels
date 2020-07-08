package com.example.testlogin.PG_manager_profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.Admin_Profile.admin_details_filling;
import com.example.testlogin.Admin_Profile.staff_reg;
import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class admin_profile_frag extends Fragment {
    int aid;
    SessionManager  session;
    private String city, state;
    TextView pgid, pgname, pgemail, pgowner, pgownerphoneno, pgcontactperson;
    TextView contactpmobile, pglocality, pgcity, pgstate, description;
    ImageView editProfile;

    public admin_profile_frag() {
        // Required empty public constructor
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_frag, container, false);
        getActivity().setTitle("Profile");
        session = new SessionManager(getContext());
        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);

        pgid =    view.findViewById(R.id.pgid);
        pgid.setText("PG ID: PGNH0080A"+aid);

     //   editProfile = view.findViewById(R.id.editprofile);

        pgcontactperson = view.findViewById(R.id.pgcontactperson);
        contactpmobile = view.findViewById(R.id.contactpmobile);
        pgstate = view.findViewById(R.id.pgstate);
        description = view.findViewById(R.id.description);
        pgname =    view.findViewById(R.id.pgname);
        pgemail = view.findViewById(R.id.pgemail);
        pgcity =    view.findViewById(R.id.pgcity);
        pglocality =   view.findViewById(R.id.pglocality);
        pgowner =     view.findViewById(R.id.pgowner);
        pgownerphoneno = view.findViewById(R.id.pgownerphoneno);

        getdetail();
     /*   editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geteditProfile();
              }
        });
       */ return view;
    }

   /* public void geteditProfile(){
      //  Toast.makeText(getActivity(), "Edit Profile", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), admin_details_filling.class);
        startActivity(intent);
    }
*/
    public void getdetail() {

        ApiInterface api = ApiClient.getApiService();
        Call<User> userCall = api.getAdmindetails(aid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
               try {
                   if (response.isSuccessful()) {
                    String Response = response.body().getResponse();
                       Log.e("response", response.body().getResponse());
                       pgcontactperson.setText(response.body().getContactperson());
                       contactpmobile.setText(response.body().getAphoneno());
                       pgcity.setText(response.body().getCity());
                       pgemail.setText(response.body().getaEmail());
                       pgstate.setText(response.body().getState());
                       description.setText(response.body().getDescription());
                       pgname.setText(response.body().getaName());
                       pglocality.setText(response.body().getAlocation());
                       pgowner.setText(response.body().getAowner());
                       pgownerphoneno.setText(response.body().getSphoneno());
                  //     Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();

                   } else {
                       Log.e("response", response.body().getResponse());
                       Toast.makeText(getActivity(), "information is already existed", Toast.LENGTH_LONG).show();
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
