package com.example.testlogin.Admin_Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.FlipperAdapter;
import com.example.testlogin.Models.Image;
import com.example.testlogin.Models.Room;
import com.example.testlogin.Models.proImages;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;
import com.example.testlogin.change_password;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class dashboard_admin extends Fragment {
    TextView cardName;
    CardView card1, card2, card3, card4, card5, card6, card7, card8, card9,
            card10, card11, card12, card13, card14, card15, card16, card17, card18;
    SessionManager session;
    RecyclerView recyclerView;
    userlistAdapter adapter;
    private List<Room> userList;
    private RecyclerView.LayoutManager layoutManager;
    private AdapterViewFlipper adapterViewFlipper;
    int aid;
    public dashboard_admin() {
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
        View view = inflater.inflate(R.layout.fragment_dashboard_admin, container, false);

        getActivity().setTitle("Dashboard");
        session = new SessionManager(getActivity());
        HashMap<String, Integer> userID = session.getUserIDs();
        aid = userID.get(SessionManager.KEY_ID);

        userList = new ArrayList<>();
        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);
        card17 = view.findViewById(R.id.card17);
        card5 = view.findViewById(R.id.card5);
        card6 = view.findViewById(R.id.card6);
        card9 = view.findViewById(R.id.card9);
        card10 = view.findViewById(R.id.card10);
        card11 = view.findViewById(R.id.card11);
     //   card12 = view.findViewById(R.id.card12);
        card13 = view.findViewById(R.id.card13);
        card17 = view.findViewById(R.id.card17);
        card18 = view.findViewById(R.id.card18);
        adapterViewFlipper = (AdapterViewFlipper) view.findViewById(R.id.adapterViewFlipper);


        HashMap<String, String> user = session.getUserDetails();
        //      String  id= user.get(SessionManager.KEY_ID);
        String name = user.get(SessionManager.KEY_NAME);
        //  String email = user.get(SessionManager.KEY_EMAIL);
        // displaying user data
        //     Id.setText(Html.fromHtml("PG Code: <b>" + id + "</b>"));
        //    cardName.setText(Html.fromHtml("<b>" + name + "</b>"));

        methods();
        viewFlipper();
        return view;
    }
    public void methods(){
        FragmentManager fragmentManager =  ((AppCompatActivity)getActivity()).getSupportFragmentManager();

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    room_mgmt_fragment fragment = new room_mgmt_fragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bed_mgmt_fragment fragment = new bed_mgmt_fragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bed_allotment fragment = new bed_allotment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_reg_frag fragment = new user_reg_frag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   room_mgmt_fragment fragment = new room_mgmt_fragment();
             //   fragmentTransaction.replace(R.id.admin_main_content, fragment);
            //    Toast.makeText(getActivity(), "Tenent List", Toast.LENGTH_LONG).show();
                userList fragment = new userList();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        card9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager_reg_frag fragment = new manager_reg_frag();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staff_reg fragment = new staff_reg();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
              //  Toast.makeText(getActivity(), "Assigning pg to the [manager", Toast.LENGTH_LONG).show();
            }
        });
        card11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staff_list fragment = new staff_list();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        card13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inbox_admin fragment = new inbox_admin();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.admin_main_content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        card17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   manager_reg_frag fragment = new manager_reg_frag();
                //   fragmentTransaction.replace(R.id.admin_main_content, fragment);
                startActivity(new Intent(getActivity(), changePg.class));
            }
        });
        card18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   if(getActivity()!=null) {
                       Intent intent = new Intent(getActivity(), change_password.class);
                       Bundle extras = new Bundle();
                       String type = "getAD";
                       extras.putString("type", type);
                       extras.putInt("GET_ID_FOR_PASSWORD", aid);
                       intent.putExtras(extras);
                       getActivity().startActivity(intent);
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        });
    }

    public void viewFlipper() {
        ApiInterface api = ApiClient.getApiService();
        Call<proImages> call = api.getImage();
        //making the call
        call.enqueue(new Callback<proImages>() {
            @Override
            public void onResponse(Call<proImages> call, Response<proImages> response) {
                try {
                    ArrayList<Image> pic = response.body().getResult();
                    //creating adapter object
                    try {
                        if(response.isSuccessful()) {
                            FlipperAdapter adapter = new FlipperAdapter(getActivity(), pic);
                            //adding it to adapterview flipper
                            adapterViewFlipper.setAdapter(adapter);
                            adapterViewFlipper.setFlipInterval(5000);
                            adapterViewFlipper.startFlipping();
                        }else {
                            switch (response.code()) {
                                case 404:
                                    Toast.makeText(getActivity(), "not found", Toast.LENGTH_SHORT).show();
                                    break;
                                case 500:
                                    Toast.makeText(getActivity(), "server broken", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }catch (Exception e){
                        e.getMessage();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<proImages> call, Throwable t) {
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                if (t instanceof IOException) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else if(getActivity()!=null){
                    Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
