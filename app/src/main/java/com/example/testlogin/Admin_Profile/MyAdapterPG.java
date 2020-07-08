package com.example.testlogin.Admin_Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapterPG extends RecyclerView.Adapter<MyAdapterPG.ViewHolder> {

    public List<User> userList;
    static Context context;

    public MyAdapterPG(Context context, List<User> userList) {
        this.userList = new ArrayList<User>();
        this.context = context;
        this.userList = userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView id, name, owner, email, loc;

        public ViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.code);
            name = (TextView) view.findViewById(R.id.tname);
            email = (TextView) view.findViewById(R.id.emailId);
            loc = (TextView) view.findViewById(R.id.loca);
            owner = (TextView) view.findViewById(R.id.owner);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionManager session = new SessionManager(context);
                    int pgcode= Integer.parseInt(id.getText().toString());
                    String pgname= name.getText().toString();
                    String Email= email.getText().toString();
                    session.createIdSession(pgcode);
                    session.createSwitchLoginSession(pgname, Email);
                    Intent intent=new Intent(context,adminProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });

        }
    }

    @Override
    public MyAdapterPG.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pid_admin_list, null);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyAdapterPG.ViewHolder holder, int position) {
        User repo = userList.get(position);
        holder.id.setText(String.valueOf(repo.getaid()));
        holder.name.setText(String.valueOf(repo.getaName()));
        holder.owner.setText(String.valueOf(repo.getAowner()));
        holder.email.setText(String.valueOf(repo.getaEmail()));
        holder.loc.setText(String.valueOf(repo.getAlocation()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
