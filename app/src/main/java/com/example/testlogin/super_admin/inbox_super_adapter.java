package com.example.testlogin.super_admin;

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

public class inbox_super_adapter extends RecyclerView.Adapter<inbox_super_adapter.ViewHolder> {

    public List<User> userList;
    static Context context;

    public inbox_super_adapter(Context context, List<User> userList) {
        this.userList = new ArrayList<User>();
        this.context = context;
        this.userList = userList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Cid, usertime, complaint, category, uid, uname, pgname;

        public ViewHolder(View view) {
            super(view);
            complaint = (TextView) view.findViewById(R.id.iComplaint);
            Cid = (TextView) view.findViewById(R.id.icid);
            usertime = (TextView) view.findViewById(R.id.iTimestamp);
            category = (TextView) view.findViewById(R.id.icategory);
            uid = (TextView) view.findViewById(R.id.uid);
            uname = (TextView) view.findViewById(R.id.uname);
            pgname = (TextView) view.findViewById(R.id.pg_name);


         /*   view.setOnClickListener(new View.OnClickListener() {
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
                                                            Intent intent1=new Intent(context, adminProfile.class);
                                                            context.startActivity(intent1);
                                                            ((Activity)context).finish();
                                                        }
                                                    }catch (Exception e) {
                                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
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
       */ }
    }
            @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_super_layout, null);
                inbox_super_adapter.ViewHolder viewHolder = new inbox_super_adapter.ViewHolder(view);
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
        holder.pgname.setText(String.valueOf("PG: "+repo.getSphoneno()+" ("+repo.getTotal()+")"));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
