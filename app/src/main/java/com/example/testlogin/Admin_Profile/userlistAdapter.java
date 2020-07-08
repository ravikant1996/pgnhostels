package com.example.testlogin.Admin_Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class userlistAdapter extends RecyclerView.Adapter<userlistAdapter.ViewHolder> {

    public List<User> userList;
    static Context context;

    public userlistAdapter(Context context, List<User> userList){
        this.userList = new ArrayList<User>();
        this.context = context;
        this.userList = userList;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, id, tenentId, email, phone, bedno, status, bid, roomNo;
      public ViewHolder(View view) {
          super(view);
          name = (TextView) view.findViewById(R.id.username);
          id = (TextView) view.findViewById(R.id.id);
          tenentId = (TextView) view.findViewById(R.id.userId);
          email = (TextView) view.findViewById(R.id.useremail);
          phone = (TextView) view.findViewById(R.id.userphone);
          bedno = (TextView) view.findViewById(R.id.bedno);
          bid = (TextView) view.findViewById(R.id.bid);
          roomNo = (TextView) view.findViewById(R.id.roomno);
          status = (TextView) view.findViewById(R.id.user_status);

          status.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  try {
                      new AlertDialog.Builder(context).setTitle("delete")
                              .setMessage("Are you sure?")
                              .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int which) {
                                      try {
                                          int id1 = Integer.parseInt(tenentId.getText().toString());
                                          String type = "user";
                                          int id2 = Integer.parseInt(bid.getText().toString());
                                          int id3 = 0, aid=0;
                                          SessionManager session = new SessionManager(context);
                                          HashMap<String, Integer> userID = session.getUserIDs();
                                          try {
                                              aid = userID.get(SessionManager.KEY_ID);
                                          }catch (Exception e){
                                              e.printStackTrace();
                                          }
                                          ApiInterface api = ApiClient.getApiService();
                                          Call<ResponseBody> call = api.clear(id1, id2, id3, aid, type);
                                          call.enqueue(new Callback<ResponseBody>() {
                                              @Override
                                              public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                  try {
                                                      if (response.isSuccessful()) {
                                                          Intent intent1 = new Intent(context, adminProfile.class);
                                                          context.startActivity(intent1);
                                                          ((Activity) context).finish();
                                                      }
                                                  } catch (Exception e) {
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

    }
    @Override
    public userlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User repo = userList.get(position);

        try {
            holder.id.setText(String.valueOf("  "+(position+1)));
            holder.tenentId.setText(String.valueOf(repo.getUid()));
            holder.name.setText(String.valueOf("  "+repo.getuName()));
            holder.email.setText(String.valueOf("  "+repo.getuEmail()));
            holder.phone.setText(String.valueOf("  "+repo.getUphoneno()));
            holder.bedno.setText(String.valueOf("   "+repo.getUbedno()));
            holder.roomNo.setText(String.valueOf("   "+repo.getaName()));
            holder.bid.setText(String.valueOf(repo.getsid()));
            holder.status.setText(String.valueOf("   Click if you want freeup"));
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public int getItemCount() {
        try {
            return userList.size();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userList.size();
    }
}

