package com.example.mytravellerapp.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.dto.TourListItem;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.ui.fragments.CommentFragment;
import com.example.mytravellerapp.ui.fragments.GalleryFragment;
import com.example.mytravellerapp.ui.fragments.HomeFragment;
import com.example.mytravellerapp.ui.fragments.TourDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lombok.core.Main;

public class TourListRecycleAdapter extends RecyclerView.Adapter<TourListRecycleAdapter.ViewHolder> {

    private static final String TAG = "TourListRecycleAdapter";

    public static String getTAG() {
        return TAG;
    }

    private ArrayList<TourListItem> listdata;
    private Context mContext;
    //delete this
    private static final MainActivity mainActivity = new MainActivity();

    public TourListRecycleAdapter(ArrayList<TourListItem> listdata, Context context) {
        this.listdata = listdata;
        this.mContext = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        View listItem = layoutInflater.inflate(R.layout.layout_tour_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TourListItem item = listdata.get(position);
        final String imageUrl = DomainConstants.TOUR_IMAGE_URL + item.getCoverImage();
        /*setting the default values*/
        holder.tour_name.setText("");
        holder.package_price.setText("");
        holder.no_ofDays.setText("");
        holder.no_ofVisits.setText("");
        holder.no_ofLikes.setText("");
        holder.no_ofComments.setText("");
        holder.no_ofPhotos.setText("");
        holder.btn_like.setImageResource(R.drawable.ic_thumb_off);
        holder.no_ofLikes.getResources().getColor(R.color.white);


        if (item != null) {

            holder.tour_name.setText(item.getTourName());
            holder.package_price.setText(item.getPackagePrice());
            holder.no_ofDays.setText(item.getNumberOfDays());
            holder.no_ofVisits.setText(Integer.toString(item.getVisitsCount()));
            holder.no_ofLikes.setText(Integer.toString(item.getLikesCount()));
            holder.no_ofComments.setText(Integer.toString(item.getCommentCount()));
            holder.no_ofPhotos.setText(Integer.toString(item.getNumberOfPhotos()));
            Picasso.with(mContext).load(imageUrl).resize(500, 400)
                    .centerCrop().into(holder.tour_image);
            if (item.isLike()) {
                holder.btn_like.setImageResource(R.drawable.ic_thumb_on);
                holder.no_ofLikes.setTextColor(mContext.getResources().getColor(R.color.blue));
            } else {
                holder.btn_like.setImageResource(R.drawable.ic_thumb_off);
                holder.no_ofLikes.setTextColor(mContext.getResources().getColor(R.color.white));
            }
            holder.likes_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do nothing
                    if(HomeFragment.homeFragment != null) {
                        HomeFragment.homeFragment.performLike(item.get_id(), position);
                    }
                }
            });


            /*onClick listener to display tour details*/
            holder.image_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (HomeFragment.homeFragment != null) {
                        HomeFragment.homeFragment.performGetTour(item.get_id());
                    }
                }
            });

//            holder.location_container.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (HomeFragment.homeFragment != null) {
//                        HomeFragment.homeFragment.performGetTour(item.get_id());
//                    }
//                }
//            });

            holder.comments_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //starting the comment fragment onClick
                    ((MainActivity) mContext).setFragment(new CommentFragment().newInstance(item.get_id(), imageUrl, item.getCommentCount()), TourListRecycleAdapter.getTAG());
                }
            });

            holder.gallery_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((MainActivity)mContext).selectSpecificTab(2);
//                    ((MainActivity)mContext).loadFragment(new GalleryFragment().newInstance(item.getTourName()));
                    ((MainActivity) mContext).setFragment(new GalleryFragment().newInstance(item.getTourName()), GalleryFragment.getTAG());
                    ((MainActivity) mContext).selectSpecificTab(2);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public void updateData(ArrayList<TourListItem> messageListNew, int flag) {
        if (flag == 0) { //append
            for (int i = 0; i < messageListNew.size(); i++) {
                listdata.add(messageListNew.get(i));
                notifyItemInserted(getItemCount());
            }
            //notifyDataSetChanged();
        } else { //clear all
            listdata.clear();
            notifyDataSetChanged();
        }
    }

    public void updateLikeCount(int item_position, boolean isLiked) {
        int changed_position = item_position;
        TourListItem updated_item = listdata.get(changed_position);
        if (isLiked == true) {
            updated_item.setLikesCount(updated_item.getLikesCount() + 1);
            updated_item.setLike(true);
        } else if (isLiked == false) {
            updated_item.setLikesCount(updated_item.getLikesCount() - 1);
            updated_item.setLike(false);
        }
        notifyItemChanged(item_position);
    }

    public void updateLikeCountById(String id, boolean isLiked){
        for(int i = 0; i < listdata.size(); i++){
            if(listdata.get(i).get_id().equals(id)){
                listdata.get(i).setLike(isLiked);
                int count = listdata.get(i).getLikesCount();
                if(isLiked) count++;
                else count --;
                listdata.get(i).setLikesCount(count);
                notifyItemChanged(i);
                return;
            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView tour_image;
        private TextView tour_name;
        private TextView package_price;
        private TextView no_ofDays;

        private TextView no_ofVisits;
        private TextView no_ofLikes;
        private TextView no_ofComments;
        private TextView no_ofPhotos;

        private ImageView btn_like;
        private RelativeLayout image_container;
        private LinearLayout comments_container;
        private LinearLayout gallery_container;
        private LinearLayout likes_container;
        private LinearLayout location_container;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tour_image = (ImageView) itemView.findViewById(R.id.tour_image);
            this.tour_name = (TextView) itemView.findViewById(R.id.tour_name);
            this.package_price = (TextView) itemView.findViewById(R.id.package_price);
            this.no_ofDays = (TextView) itemView.findViewById(R.id.no_ofDays);

            this.no_ofVisits = (TextView) itemView.findViewById(R.id.no_ofVisits);
            this.no_ofLikes = (TextView) itemView.findViewById(R.id.no_ofLikes);
            this.no_ofComments = (TextView) itemView.findViewById(R.id.no_ofComments);
            this.no_ofPhotos = (TextView) itemView.findViewById(R.id.no_ofPhotos);

            this.btn_like = (ImageView) itemView.findViewById(R.id.btn_like);
            this.image_container = (RelativeLayout) itemView.findViewById(R.id.image_container);
            this.comments_container = (LinearLayout) itemView.findViewById(R.id.comments_container);
            this.gallery_container = (LinearLayout) itemView.findViewById(R.id.gallery_container);
            this.likes_container = (LinearLayout) itemView.findViewById(R.id.likes_container);
            this.location_container = (LinearLayout) itemView.findViewById(R.id.location_container);
        }
    }
}
