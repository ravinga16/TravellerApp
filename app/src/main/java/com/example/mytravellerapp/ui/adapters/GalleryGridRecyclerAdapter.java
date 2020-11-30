package com.example.mytravellerapp.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.ui.fragments.GalleryFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GalleryGridRecyclerAdapter extends RecyclerView.Adapter<GalleryGridRecyclerAdapter.MyViewHolder> {
    private ArrayList<String> imageList;    //Image list (not final)
    private ArrayList<String> finalImageUrlList;
    private Context context;

    public GalleryGridRecyclerAdapter(Context context, ArrayList<String> imageList) {
        this.imageList = imageList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        View listItem = layoutInflater.inflate(R.layout.gallery_item_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String child = imageList.get(position);
        final String imageUrl = DomainConstants.TOUR_IMAGE_URL + child;
        if (child != null) {
                    if (child != null && !child.equals("")) {
                        Picasso.with(context).load(imageUrl).resize(400, 300)
                                .centerCrop().into(holder.image);
                        holder.image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GalleryFragment.galleryFragment.gotoImageFullScreen(imageList, imageUrl);
                            }
                        });

                    }
            final String imagePath = DomainConstants.TOUR_IMAGE_URL + child;

        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.gallery_image);
        }
    }

    public ArrayList<String> getFinalImageUrlList(ArrayList<String> imageList){
        ArrayList<String> finalImageUrlList = new ArrayList<>();
        for(String i : imageList){
            String finalUrl = DomainConstants.TOUR_IMAGE_URL + i;
            finalImageUrlList.add(finalUrl);
        }
        return finalImageUrlList;
    }
}
