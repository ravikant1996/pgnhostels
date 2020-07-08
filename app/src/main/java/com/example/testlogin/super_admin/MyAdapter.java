package com.example.testlogin.super_admin;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {

    List<User> userList= new ArrayList<User>();
    Context context;
    List<User> userListFiltered = new ArrayList<User>();


    MyAdapter( Context context, List<User> userList) {
        this.userList = userList;
        this.context = context;
        this.userListFiltered = userList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView id, serialNo,  name, owner, mobile, email, loc, status;

        public ViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.pg_code);
            serialNo = (TextView) view.findViewById(R.id.serialno);
            name = (TextView) view.findViewById(R.id.pg_name);
            owner = (TextView) view.findViewById(R.id.owner);
            mobile = (TextView) view.findViewById(R.id.mobile);
            email = (TextView) view.findViewById(R.id.email);
            loc = (TextView) view.findViewById(R.id.location);
            status = (TextView) view.findViewById(R.id.admin_status);

            view.setOnClickListener(this);
            status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        new AlertDialog.Builder(context).setTitle("delete")
                                .setMessage("Are you sure?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            int aid= Integer.parseInt(id.getText().toString());
                                            String type= "admin";
                                            int id1=0, id2=0, id3=0;
                                            ApiInterface api = ApiClient.getApiService();
                                            Call<ResponseBody> call = api.clear(id1, id2, id3, aid, type);
                                            call.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try{
                                                        if(response.isSuccessful()){
                                                            Intent intent1=new Intent(context, superAdmin.class);
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
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, viewpg.class);
            String pgcode= id.getText().toString();
            String pgname= name.getText().toString();
            String pgowner= owner.getText().toString();
            String Mobile= mobile.getText().toString();
            String Email= email.getText().toString();
            String loca= loc.getText().toString();
            Bundle extras = new Bundle();
            extras.putInt("position", Integer.parseInt(pgcode));
            extras.putString("name", pgname);
            extras.putString("owner", pgowner);
            extras.putString("mobile", Mobile);
            extras.putString("email", Email);
            extras.putString("location", loca);
            intent.putExtras(extras);
            context.startActivity(intent);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();

                if (charString.isEmpty()){
                    userList = userListFiltered;
                }else{

                    List<User> filterList = new ArrayList<>();

                    for (User data : userListFiltered){

                        if (data.getaName().toLowerCase().contains(charString)){
                            filterList.add(data);
                        }
                    }

                    userList = filterList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = userList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                userList = (List<User>) results.values;
                notifyDataSetChanged();
            }
        };

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list_layout, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User repo = userList.get(position);
        holder.serialNo.setText(String.valueOf("  "+(position+1)));
        holder.id.setText(String.valueOf(repo.getaid()));
        holder.name.setText(String.valueOf("  "+repo.getaName()));
        holder.owner.setText(String.valueOf("  "+repo.getAowner()));
        holder.mobile.setText(String.valueOf("  "+repo.getAphoneno()));
        holder.email.setText(String.valueOf("  "+repo.getaEmail()));
        holder.loc.setText(String.valueOf("  "+repo.getAlocation()));
        holder.status.setText(String.valueOf("  Click if you want freeup"));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
