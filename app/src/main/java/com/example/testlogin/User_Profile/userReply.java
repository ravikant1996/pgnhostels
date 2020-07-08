package com.example.testlogin.User_Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.SignUpResponse;
import com.example.testlogin.Models.User;
import com.example.testlogin.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class userReply extends AppCompatActivity {
    int cid;
    public TextView Cid, usertime, complaint, category,  adminreplytime;
    Button replyBtn;
    EditText replytext;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    replyAdapter adapter;
    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reply);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try {
            cid = bundle.getInt("cid");
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String  Complaint= bundle.getString("complaint");
        String  userComplaintTime= bundle.getString("userComplaintTime");
        String  Category= bundle.getString("category");
        complaint = (TextView) findViewById(R.id.rComplaint);
        Cid = (TextView) findViewById(R.id.rcid);
        usertime = (TextView) findViewById(R.id.rTimestamp);
        category = (TextView) findViewById(R.id.rcategory);

        replyBtn =  (Button) findViewById(R.id.replybtn);
        replytext =  (EditText) findViewById(R.id.replyText);
        userList = new ArrayList<>();
        recyclerView= findViewById(R.id.recyclerReply);

        complaint.setText(Complaint);
        Cid.setText(" "+cid);
        usertime.setText(userComplaintTime);
        category.setText(Category);
        getMessage();
      //  getmes();
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(replytext.getText().toString().length() == 0){
                    replytext.setError("please write something");
                }else {
                    replyBtn.setEnabled(false);
                    submitt();
                }
            }
        });
    }
  /*  private final int FIVE_SECONDS = 5000;
    public void getmes() {
        handler.postDelayed(new Runnable() {
            public void run() {
                getMessage();          // this method will contain your almost-finished HTTP calls
                handler.postDelayed(this, FIVE_SECONDS);
            }
        }, FIVE_SECONDS);
    }
*/
    public void submitt(){
        int id= 0;
        String user_type = "Tenent";
        String message = replytext.getText().toString();
        String category="";
        ApiInterface api = ApiClient.getApiService();
        Call<User> userCall = api.createChatRoomAndSendMessage(id, category, user_type, message, cid);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try{
                    if(response.body().getResponse().equals("inserted")) {
                          replyBtn.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "send", Toast.LENGTH_LONG).show();
                        replytext.setText("");
                        getMessage();
                    }else {
                        replyBtn.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    replyBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                replyBtn.setEnabled(true);
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                try {
                    if (t instanceof IOException) {
                        if (t instanceof SocketTimeoutException) {
                            Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void getMessage(){
        int id=cid;
        String complaint_table= "reply_tabl";
        ApiInterface api = ApiClient.getApiService();
        Call<SignUpResponse> call = api.getAllmessage(id, complaint_table);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                try {
                    userList = (ArrayList<User>) response.body().getResult();
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    //write bottom to top
                  //  layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adapter = new replyAdapter(getApplicationContext(), userList);
                    recyclerView.setAdapter(adapter);
                    //for scrolling bottom to top
                    recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    //       recycler();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "No reply", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                if (t instanceof IOException) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
