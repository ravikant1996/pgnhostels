package com.example.testlogin.PG_manager_profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.Admin_Profile.adminProfile;
import com.example.testlogin.Admin_Profile.adminReply;
import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class inbox_admin_adapter extends RecyclerView.Adapter<inbox_admin_adapter.ViewHolder> {

    public List<User> userList;
    static Context context;

    public inbox_admin_adapter(Context context, List<User> userList) {
        this.userList = new ArrayList<User>();
        this.context = context;
        this.userList = userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Cid, usertime, complaint, category, uid, uname;
        RadioButton replyhint;

        public ViewHolder(View view) {
            super(view);
            complaint = (TextView) view.findViewById(R.id.iComplaint);
            Cid = (TextView) view.findViewById(R.id.icid);
            usertime = (TextView) view.findViewById(R.id.iTimestamp);
            category = (TextView) view.findViewById(R.id.icategory);
            uid = (TextView) view.findViewById(R.id.uid);
            uname = (TextView) view.findViewById(R.id.uname);
         //   replyhint = (RadioButton) view.findViewById(R.id.replyhint);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, adminReply.class);
                    String Complaint= complaint.getText().toString();
                    String cid= Cid.getText().toString();
                    String userComplaintTime= usertime.getText().toString();
                    String Category= category.getText().toString();
                    String Uid= uid.getText().toString();
                    String Uname= uname.getText().toString();
                    Bundle extras = new Bundle();
                    extras.putInt("cid", Integer.parseInt(cid));
                    extras.putString("complaint", Complaint);
                    extras.putString("userComplaintTime", userComplaintTime);
                    extras.putString("category", Category);
                    extras.putInt("uid", Integer.parseInt(Uid));
                    extras.putString("uname", Uname);
                    intent.putExtras(extras);
                    context.startActivity(intent);

                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    try {
                        new AlertDialog.Builder(context).setTitle("close complaint")
                                .setMessage("Do you want to close complaint?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {

                                            int aid= Integer.parseInt(Cid.getText().toString());
                                            String type= "complaint";
                                            int id1=0, id2=0, id3=0;
                                            ApiInterface api = ApiClient.getApiService();
                                            Call<ResponseBody> call = api.clear(id1, id2, id3, aid, type);
                                            call.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try{
                                                        if(response.isSuccessful()){
                                                            Intent intent1=new Intent(context, pgManagerProfile.class);
                                                            context.startActivity(intent1);
                                                            ((Activity)context).finish();
                                                        }
                                                    }catch (Exception e) {
                                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                                                    try {
                                                        if (t instanceof IOException) {
                                                            if (t instanceof SocketTimeoutException) {
                                                                Toast.makeText(context, "Oops something went wrong", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(context, "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(context, "Oops something went wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }catch (Exception e){
                                                        Toast.makeText(context, "Oops something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
    }
            @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_layout, null);
                inbox_admin_adapter.ViewHolder viewHolder = new inbox_admin_adapter.ViewHolder(view);
                return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User repo = userList.get(position);
        holder.complaint.setText(String.valueOf("Complaint: "+repo.getDescription()));
        holder.usertime.setText(String.valueOf(repo.getaName()));
        holder.Cid.setText(String.valueOf(repo.getsid()));
        holder.category.setText(String.valueOf("Category: "+repo.getuName()));
        holder.uname.setText(String.valueOf(repo.getsName()));
        holder.uid.setText(String.valueOf(repo.getUid()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
