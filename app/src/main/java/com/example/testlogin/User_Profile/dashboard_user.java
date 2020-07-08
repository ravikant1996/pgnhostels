package com.example.testlogin.User_Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.util.HashMap;

import androidx.fragment.app.Fragment;

public class dashboard_user extends Fragment {
    SessionManager session;
    TextView namme, idd;
    int aid;

    public dashboard_user() {
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
        View view = inflater.inflate(R.layout.fragment_dashboard_user, container, false);

        getActivity().setTitle("Dashboard");
        session = new SessionManager(getActivity());

        namme = view.findViewById(R.id.namme);
        idd = view.findViewById(R.id.idd);
        HashMap<String, String> user = session.getUserDetails();
        //      String  id= user.get(SessionManager.KEY_ID);
        String name = user.get(SessionManager.KEY_NAME);
        String email = user.get(SessionManager.KEY_EMAIL);

        namme.setText(name);
        idd.setText(email);

        //  String email = user.get(SessionManager.KEY_EMAIL);
        // displaying user data
        //     Id.setText(Html.fromHtml("PG Code: <b>" + id + "</b>"));
        //    cardName.setText(Html.fromHtml("<b>" + name + "</b>"));

        return view;
    }

}
