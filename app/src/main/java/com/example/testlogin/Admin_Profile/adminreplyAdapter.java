package com.example.testlogin.Admin_Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testlogin.Models.User;
import com.example.testlogin.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class adminreplyAdapter extends RecyclerView.Adapter<adminreplyAdapter.ViewHolder> {

    public List<User> userList;
    static Context context;

    public adminreplyAdapter(Context context, List<User> userList) {
        this.userList = new ArrayList<User>();
        this.context = context;
        this.userList = userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, r_time, reply, type, Adminreply, adminreplytime;

        public ViewHolder(View view) {
            super(view);
            reply = (TextView) view.findViewById(R.id.Areply);
            r_time = (TextView) view.findViewById(R.id.AR_Timestamp);
            type = (TextView) view.findViewById(R.id.atype);
        }
    }
            @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminreply_layout, null);
                adminreplyAdapter.ViewHolder viewHolder = new adminreplyAdapter.ViewHolder(view);
                return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User repo = userList.get(position);
        holder.reply.setText(String.valueOf("Complaint: "+repo.getDescription()+"       "+ repo.getaName()));
        holder.r_time.setText(String.valueOf(repo.getaName()));
        holder.type.setText(String.valueOf(repo.getuName()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
