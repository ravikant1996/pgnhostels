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

public class replyAdapter extends RecyclerView.Adapter<replyAdapter.ViewHolder> {

    public List<User> userList;
    static Context context;

    public replyAdapter(Context context, List<User> userList) {
        this.userList = new ArrayList<User>();
        this.context = context;
        this.userList = userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id, r_time, reply, type, Adminreply, adminreplytime;

        public ViewHolder(View view) {
            super(view);
            reply = (TextView) view.findViewById(R.id.reply);
            r_time = (TextView) view.findViewById(R.id.R_Timestamp);
            type = (TextView) view.findViewById(R.id.type);

          /*  view.setOnClickListener(new View.OnClickListener() {
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
            });*/
        }
    }
            @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_layout, null);
                replyAdapter.ViewHolder viewHolder = new replyAdapter.ViewHolder(view);
                return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User repo = userList.get(position);
        holder.reply.setText(String.valueOf("Complaint: "+repo.getDescription()+"       "+repo.getaName()));
        holder.r_time.setText(String.valueOf(repo.getaName()));
        holder.type.setText(String.valueOf(repo.getuName()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
