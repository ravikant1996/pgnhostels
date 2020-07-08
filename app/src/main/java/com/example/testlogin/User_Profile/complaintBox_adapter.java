package com.example.testlogin.User_Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.testlogin.Models.User;
import com.example.testlogin.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class complaintBox_adapter extends RecyclerView.Adapter<complaintBox_adapter.ViewHolder> {

    public List<User> userList;
    static Context context;

    public complaintBox_adapter(Context context, List<User> userList) {
        this.userList = new ArrayList<User>();
        this.context = context;
        this.userList = userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Cid, usertime, complaint, category, Adminreply, adminreplytime, cidd;
        RadioButton replyhint;

        public ViewHolder(View view) {
            super(view);
            complaint = (TextView) view.findViewById(R.id.Complaint);
            Cid = (TextView) view.findViewById(R.id.cid);
            cidd = (TextView) view.findViewById(R.id.cidd);
            usertime = (TextView) view.findViewById(R.id.Timestamp);
            category = (TextView) view.findViewById(R.id.category);
            Adminreply = (TextView) view.findViewById(R.id.adminreply);
            adminreplytime = (TextView) view.findViewById(R.id.replytime);
         //   replyhint = (RadioButton) view.findViewById(R.id.replyhint);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, userReply.class);
                    String Complaint= complaint.getText().toString();
                    String cid= Cid.getText().toString();
                    String userComplaintTime= usertime.getText().toString();
                    String Category= category.getText().toString();
                    Bundle extras = new Bundle();
                    extras.putInt("cid", Integer.parseInt(cid));
                    extras.putString("complaint", Complaint);
                    extras.putString("userComplaintTime", userComplaintTime);
                    extras.putString("category", Category);
                    intent.putExtras(extras);
                    context.startActivity(intent);

                }
            });
        }
    }
            @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaintbox_layout, null);
                complaintBox_adapter.ViewHolder viewHolder = new complaintBox_adapter.ViewHolder(view);
                return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User repo = userList.get(position);
        holder.complaint.setText(String.valueOf("Complaint: "+repo.getDescription()));
        holder.usertime.setText(String.valueOf(repo.getaName()));
        holder.Cid.setText(String.valueOf(repo.getsid()));
        holder.cidd.setText(String.valueOf("Complaint ID: PGNHOSTELCNO"+repo.getsid()));
        holder.category.setText(String.valueOf("Category: "+repo.getuName()));
       // holder.replyhint.isChecked();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
