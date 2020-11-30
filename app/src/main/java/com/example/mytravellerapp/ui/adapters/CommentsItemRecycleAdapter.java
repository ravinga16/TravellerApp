package com.example.mytravellerapp.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.dto.Comment;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CommentsItemRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CommentsItemRecycleAdap";
    private ArrayList<Comment> mCommentList;
    private String imageUrl; //header image
    private Context context;
    /*Select between the header and the comment view*/
    public static final int header = 0;
    public static final int normal = 2;

    public CommentsItemRecycleAdapter(ArrayList<Comment> commentsList, String imageUrl, Context context) {
        this.mCommentList = commentsList;
        this.imageUrl = imageUrl;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
        View rowView;
        switch (viewType) {
            case normal:
                rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_comment_list_card_view, null);
                return new CommentsRecycleItemRowHolder(rowView);
            case header:
                rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_comment_layout_header, null);
                return new HeaderViewHolder(rowView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            if (imageUrl != null && !imageUrl.isEmpty())
                Picasso.with(context).load(imageUrl).into(headerHolder.headerImage);

        }else if (holder instanceof CommentsRecycleItemRowHolder){
            CommentsRecycleItemRowHolder commentsRecycleItemRowHolder = (CommentsRecycleItemRowHolder) holder;
            final Comment item = mCommentList.get(position);
            commentsRecycleItemRowHolder.name.setText("");
            commentsRecycleItemRowHolder.comment.setText("");
            commentsRecycleItemRowHolder.timeAgo.setText("");

            commentsRecycleItemRowHolder.replyName.setText("Admin");
            commentsRecycleItemRowHolder.replyComment.setText("");
            commentsRecycleItemRowHolder.replyLayout.setVisibility(View.GONE);

            if (item != null) {
                if (!item.getReply().isEmpty()) {

                    commentsRecycleItemRowHolder.name.setText(item.getUser().getFName() + " " + item.getUser().getLName());
                    commentsRecycleItemRowHolder.comment.setText(item.getDescription() + "");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    Date date = null;
                    try {
                        date = dateFormat.parse(getLocalDate(item.getDate())); // "2017-04-26T20:55:00.000Z"
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String timeAgo = getTimeAgo(date);
                    commentsRecycleItemRowHolder.timeAgo.setText(timeAgo);

                    commentsRecycleItemRowHolder.replyName.setText("Admin");
                    commentsRecycleItemRowHolder.replyComment.setText(item.getReply() + "");
                    commentsRecycleItemRowHolder.replyLayout.setVisibility(View.VISIBLE);
                } else {
                    commentsRecycleItemRowHolder.name.setText(item.getUser().getFName() + " " + item.getUser().getLName());
                    commentsRecycleItemRowHolder.comment.setText(item.getDescription() + "");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    Date date = null;
                    try {
                        date = dateFormat.parse(getLocalDate(item.getDate())); // "2017-04-26T20:55:00.000Z"
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String timeAgo = getTimeAgo(date);
                    commentsRecycleItemRowHolder.timeAgo.setText(timeAgo);
                }
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return header;
        else
            return normal;
    }


    @Override
    public int getItemCount() {
        return (null != mCommentList ? mCommentList.size() : 0);
    }

    public void updateData(List<Comment> comment, int flag) {
        if (flag == 0) { //append
            for (int i = 0; i < comment.size(); i++) {
                mCommentList.add(comment.get(i));
            }
            notifyDataSetChanged();
        }else{
            mCommentList.clear();
            notifyDataSetChanged();
        }
    }

    private Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    private String getLocalDate(String ourDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(ourDate);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:00";
        }
        return ourDate;
    }

    private int getTimeDifferenceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    private String getTimeAgo(Date date) {

        if (date == null) return null;
        long time = date.getTime();

        Date curDate = currentDate();
        long now = curDate.getTime();
        if (time > now || time <= 0) {
            return null;
        }

        int timeDiff = getTimeDifferenceInMinutes(time);
        String timeAgo;

        if (timeDiff == 0) {
            timeAgo = "just now";
        } else if (timeDiff == 1) {
            return "1 minute ago";
        } else if (timeDiff >= 2 && timeDiff <= 44) {
            timeAgo = timeDiff + " minutes ago";
        } else if (timeDiff >= 45 && timeDiff <= 89) {
            timeAgo = "about an hour ago";
        } else if (timeDiff >= 90 && timeDiff <= 1439) {
            timeAgo = "about " + (Math.round(timeDiff / 60)) + " hours ago";
        } else if (timeDiff >= 1440 && timeDiff <= 2519) {
            timeAgo = "1 day ago";
        } else if (timeDiff >= 2520 && timeDiff <= 43199) {
            timeAgo = (Math.round(timeDiff / 1440)) + " days ago";
        } else if (timeDiff >= 43200 && timeDiff <= 86399) {
            timeAgo = "about a month ago";
        } else if (timeDiff >= 86400 && timeDiff <= 525599) {
            timeAgo = (Math.round(timeDiff / 43200)) + " months ago";
        } else if (timeDiff >= 525600 && timeDiff <= 655199) {
            timeAgo = "about a year ago";
        } else if (timeDiff >= 655200 && timeDiff <= 914399) {
            timeAgo = "over a year ago";
        } else if (timeDiff >= 914400 && timeDiff <= 1051199) {
            timeAgo = "almost 2 years ago";
        } else {
            timeAgo = "about " + (Math.round(timeDiff / 525600)) + " years ago";
        }

        return timeAgo;
    }

    public  class CommentsRecycleItemRowHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView comment;
        TextView timeAgo;

        LinearLayout replyLayout;
        TextView replyName;
        TextView replyComment;

        public CommentsRecycleItemRowHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            timeAgo = itemView.findViewById(R.id.time_ago);
            replyLayout = itemView.findViewById(R.id.reply_layout);
            replyName = itemView.findViewById(R.id.reply_name);
            replyComment = itemView.findViewById(R.id.reply_comment);
        }
    }

    public  class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView headerImage;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.headerImage = (ImageView) itemView.findViewById(R.id.header_image);
        }
    }
}