package com.example.testlogin.super_admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testlogin.Admin_Profile.bed_allotment;
import com.example.testlogin.ApiClient;
import com.example.testlogin.ApiInterface;
import com.example.testlogin.Models.Room;
import com.example.testlogin.R;
import com.example.testlogin.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class dashboard_super extends Fragment {
    TextView cardName;
    Button upload, load;
    public CardView addpg, viewpg, additional, promotion, complaint;
    SessionManager session;
    ImageView image;
    private Bitmap bitmap;
    Bitmap cameraBitmap;
    String encodedImage;
    private static final int PICK_IMAGE_REQUEST = 102;
    public dashboard_super() {
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
        View view = inflater.inflate(R.layout.fragment_dashboard_super, container, false);

        getActivity().setTitle("Dashboard");
        session = new SessionManager(getActivity());

        viewpg = view.findViewById(R.id.viewpg);
        addpg = view.findViewById(R.id.addpg);
        cardName = view.findViewById(R.id.cardname);
        complaint = view.findViewById(R.id.card_complaint);
        additional = view.findViewById(R.id.additional);
        promotion = view.findViewById(R.id.promotion);
        image= view.findViewById(R.id.image);
        upload= view.findViewById(R.id.upload);
        load= view.findViewById(R.id.load);


        HashMap<String, String> user = session.getUserDetails();

        String name = user.get(SessionManager.KEY_NAME);
        // email
        //  String email = user.get(SessionManager.KEY_EMAIL);

        // displaying user data
        //     Id.setText(Html.fromHtml("PG Code: <b>" + id + "</b>"));
        cardName.setText(Html.fromHtml("<b>" + name + "</b>"));


        viewpg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminList.class));
            }
        });

        additional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  session.logoutUser();
                //    startActivity(new Intent(getActivity(), admin_registration.class));
            }
        });
        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(getActivity()!=null) {
                        FragmentManager fragmentManager = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                        inbox_super fragment = new inbox_super();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.super_main_content, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        addpg.setOnClickListener(v -> {
          /*  admin_reg_frag nextFrag= new admin_reg_frag();
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.super_main_content, nextFrag)
                    .addToBackStack(null)
                    .commit();  */
            startActivity(new Intent(getActivity(), admin_registration.class));
        });

      /*  image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/

    //  image.setVisibility(View.INVISIBLE);
      load.setEnabled(false);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setEnabled(false);
                chooseFile();
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    postData();
                        load.setEnabled(false);
                        upload.setEnabled(true);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                image.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        load.setEnabled(true);
      //  image.setVisibility(View.VISIBLE);
    }

    private void postData() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Saving...");
        progressDialog.show();

     //   readMode();

        String images = null;
        if (bitmap == null) {
            images = "";
        } else {
            images = getStringImage(bitmap);
        }

        ApiInterface api = ApiClient.getApiService();
        Call<Room> call = api.uploadimage(images);

        call.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                try {
                    if(response.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "sent", Toast.LENGTH_LONG).show();
                        load.setEnabled(false);
                        upload.setEnabled(true);
                     //   image.setImageResource(R.drawable.addimage);
                        Glide.with(image).clear(image);
                    //    image.setImageBitmap(null);
                    //    image.destroyDrawingCache();
                    }else {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                        upload.setEnabled(true);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                progressDialog.dismiss();
                upload.setEnabled(true);
                Log.d(TAG, "Avs Exception -> " + (t.getMessage() != null ? t.getMessage() : "---"));
                if (t instanceof IOException) {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Please check your internet connection...", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Oops something went wrong", Toast.LENGTH_SHORT).show();
                }
             //   Toast.makeText(EditorActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
