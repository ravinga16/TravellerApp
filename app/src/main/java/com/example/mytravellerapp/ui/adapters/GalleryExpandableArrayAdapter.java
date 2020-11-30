package com.example.mytravellerapp.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.dto.GalleryGroupItem;
import com.example.mytravellerapp.ui.activities.MainActivity;
import com.example.mytravellerapp.ui.fragments.GalleryFragment;

import java.util.ArrayList;
import java.util.List;

public class GalleryExpandableArrayAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "GalleryExpandableArrayA";
    private ArrayList<GalleryGroupItem> completeMenu;
    private Context context;
    public LayoutInflater minflater;
    private ArrayList<Integer> loadedIndexes;
    private ExpandableListView mExpandableListView;

    public GalleryExpandableArrayAdapter(ArrayList<GalleryGroupItem> galleryGroupItemArrayList, Context context, ExpandableListView mExpandableListView) {
        this.completeMenu = galleryGroupItemArrayList;
        this.context = context;
        this.loadedIndexes = new ArrayList<>();
        this.mExpandableListView = mExpandableListView;
        this.minflater = ((Activity)context).getLayoutInflater();

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                GalleryFragment.add(groupPosition);
            }
        });
        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

                GalleryFragment.remove(groupPosition);
            }
        });
    }

    private boolean isIndexAvailable(int index) {
        return loadedIndexes.contains(index);
    }

    private void addLoadedIndex(int index) {
        loadedIndexes.add(index);
    }
    @Override
    public int getGroupCount() {
        return completeMenu.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (completeMenu.get(groupPosition) != null) ? 1 : 0;
    }

    @Override
    public String getGroup(int groupPosition) {
        return completeMenu.get(groupPosition).getTourName();
    }

    @Override
    public ArrayList<String> getChild(int groupPosition, int childPosition) {
        return completeMenu.get(groupPosition).getImageGallery();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupViewHolder holder;

        if (convertView == null) {
            convertView = minflater.inflate(R.layout.row_gallery_group_parent, null);
            holder = new GroupViewHolder();

            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvSeeAll = (TextView) convertView.findViewById(R.id.tv_see_all);
            holder.titleSeparator = (ImageView) convertView.findViewById(R.id.title_separator);

            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        GalleryGroupItem galleryGroupItem = completeMenu.get(groupPosition);
        if (galleryGroupItem != null && galleryGroupItem.getTourName() != null && !galleryGroupItem.getTourName().equals("")) {
            holder.tvTitle.setText(galleryGroupItem.getTourName());

            if (!isIndexAvailable(groupPosition)) {
                addLoadedIndex(groupPosition);

            }
            if (isExpanded) {
                holder.tvSeeAll.setText(context.getResources().getString(R.string.lbl_see_less));
                holder.titleSeparator.setVisibility(View.GONE);
            } else {
                holder.tvSeeAll.setText(context.getResources().getString(R.string.lbl_see_all));
                holder.titleSeparator.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;

        if (convertView == null) {
            convertView = minflater.inflate(R.layout.row_gallery_group_child, null);
            holder = new ChildViewHolder();

            holder.mRecyclerView = (RecyclerView) convertView.findViewById(R.id.recycler_view);

            int spanCount = 4;
            holder.mRecyclerView.setLayoutManager(new GridLayoutManager(context, spanCount));

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        try {
            new Handler(new Handler.Callback() {

                @Override
                public boolean handleMessage(Message msg) {

                    GalleryGridRecyclerAdapter galleryArrayAdapter =
                            new GalleryGridRecyclerAdapter(context, completeMenu.get(groupPosition).getImageGallery());
                    holder.mRecyclerView.setAdapter(galleryArrayAdapter);

                    return false;

                }

            }).sendEmptyMessage(0);

        } catch (Exception ex) {
            Log.e(TAG, "getChildView: " + ex.toString());
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
        mExpandableListView.setSelection(groupPosition);
    }
    static class GroupViewHolder {
        TextView tvTitle;
        TextView tvSeeAll;
        ImageView titleSeparator;
    }

    static class ChildViewHolder {
        RecyclerView mRecyclerView;
    }


    /*Method from the travellerandroid*/
    public void updateData(List<GalleryGroupItem> messageList, int flag) {

        if (flag == 0) { // append
            for (int i = 0; i < messageList.size(); i++) {
                completeMenu.add(messageList.get(i));
            }
            notifyDataSetChanged();
            updateImageList();


        } else { // clear all
            completeMenu.clear();
            notifyDataSetChanged();
        }
    }

    public void updateImageList() {
        List<String> galleryItems = new ArrayList<>();
        for (int i = 0; i < completeMenu.size(); i++) {
            galleryItems.addAll(completeMenu.get(i).getImageGallery());
        }

        if (GalleryFragment.galleryFragment != null)
            GalleryFragment.galleryFragment.updateFullScreenGalleryImages(galleryItems);
    }

}
