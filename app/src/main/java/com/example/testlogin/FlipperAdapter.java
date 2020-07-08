package com.example.testlogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.testlogin.Models.Image;
import com.example.testlogin.Models.Room;
import com.example.testlogin.R;

import java.util.ArrayList;
import java.util.List;

public class FlipperAdapter extends BaseAdapter {

    static Context mCtx;
    public ArrayList<Image> pic;

    public FlipperAdapter(Context mCtx, ArrayList<Image> pic){
        this.mCtx = mCtx;
        this.pic = pic;
    }
    @Override
    public int getCount() {
        return pic.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("CheckResult")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            Image image = pic.get(position);

            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.promotion_list_image, null);
            ImageView textView = view.findViewById(R.id.pro_pic);
            try {
                if (!pic.isEmpty()) {
                    //   String imageUrl = "http://192.168.1.15/myProject/promotion_images/" + pic.get(position).getImages();
                    //   String imageUrl = "http://192.168.43.113/myProject/promotion_images/" + pic.get(position).getImages();
                    String imageUrl = "https://pgnhostels.com/myproject/promotion_images/" + pic.get(position).getImages();
                    Glide.with(mCtx)
                            .load(imageUrl)
                            .into(textView);
                }
            } catch (Exception e) {
                Glide.with(mCtx)
                        .load(R.drawable.hostel);
                //   Toast.makeText(mCtx, "", Toast.LENGTH_SHORT).show();
                Log.e("Error", "Loading Error");
            }
            return view;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return convertView;
    }

}
