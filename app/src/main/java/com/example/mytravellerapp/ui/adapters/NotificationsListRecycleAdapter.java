package com.example.mytravellerapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.dto.NotificationListItem;
import com.example.mytravellerapp.ui.fragments.NotificationsFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsListRecycleAdapter extends RecyclerView.Adapter<NotificationsListRecycleAdapter.MyViewHolder> {

    private static final String TAG = "NotificationsListRecycl";
    private List<NotificationListItem> notificationItemList;
    private Context mContext;
    private int lastPosition = -1;
    private ImageLoader imageLoader;
    public NotificationsListRecycleAdapter(List<NotificationListItem> notificationListItems, Context mContext) {
        this.notificationItemList = notificationListItems;
        this.mContext = mContext;
        this.imageLoader = ImageLoader.getInstance();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_item_view, null);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NotificationListItem item = notificationItemList.get(position);
        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        holder.description.loadDataWithBaseURL("", "", mimeType, encoding, "");
        holder.description.setOnTouchListener(null);
        createImageView("", holder.circleImageView, holder.imageProgress);

        if(item != null){
//            Picasso.with(mContext).load(DomainConstants.PROFILE_IMAGE_URL+item.getImage()).into(holder.circleImageView);
            createImageView(DomainConstants.PROFILE_IMAGE_URL + item.getImage(), holder.circleImageView, holder.imageProgress);
            holder.description.loadDataWithBaseURL("", item.getAndroidContent(), mimeType, encoding, "");
            holder.description.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        if (item.getTourId() != null) {
                            if (NotificationsFragment.notificationsFragment != null) {
                                NotificationsFragment.notificationsFragment.performGetTour(item.getTourId());
                            }
                        }
                    }
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return (null != notificationItemList ? notificationItemList.size() : 0);
    }

    public void updateData(List<NotificationListItem> messageListNew, int flag) {
        if (flag == 0) { //append
            if(messageListNew.size()>0 && messageListNew!=null){
                for (int i = 0; i < messageListNew.size(); i++) {
                    notificationItemList.add(messageListNew.get(i));

                }
            }
            notifyDataSetChanged();
        } else { //clear all
            lastPosition = -1;
            notificationItemList.clear();
            notifyDataSetChanged();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private WebView description;
        private ProgressBar imageProgress;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.circleImageView = itemView.findViewById(R.id.notification_image);
            this.description = (WebView) itemView.findViewById(R.id.notification_text);
            this.imageProgress = itemView.findViewById(R.id.image_progress);
        }
    }

    /*image handling*/
    private void setProductImage(String imageUrl, ImageView productImage, final ProgressBar imageProgress) {
        if(imageUrl != null && !imageUrl.isEmpty()){
            ImageLoader.getInstance().displayImage(imageUrl, productImage, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    if (imageProgress != null) imageProgress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    if (imageProgress != null) imageProgress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (imageProgress != null) imageProgress.setVisibility(View.GONE);
                }
            });
        } else {
            if (imageProgress != null) imageProgress.setVisibility(View.GONE);
        }

    }

    private void createImageView( final String imageURL, final ImageView productImageView, final ProgressBar imageProgress) {
        Handler.Callback callback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    if(imageURL != null && !imageURL.isEmpty())setProductImage(imageURL, productImageView, imageProgress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
        new Handler(callback).sendEmptyMessage(1);
    }
}
