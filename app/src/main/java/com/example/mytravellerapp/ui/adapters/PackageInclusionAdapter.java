package com.example.mytravellerapp.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytravellerapp.R;

import java.util.List;

public class PackageInclusionAdapter extends RecyclerView.Adapter<PackageInclusionAdapter.ViewHolder>{
    private static final String TAG = "PackageInclusionAdapter";
    private List<String> packageInclusionList;

    public PackageInclusionAdapter(List<String> packageInclusionList) {
        this.packageInclusionList = packageInclusionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        View listItem= layoutInflater.inflate(R.layout.layout_item_package_inclusion, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String item = packageInclusionList.get(position);
        holder.package_inclusion_detail.setText("");
        if(item != null){
            holder.package_inclusion_detail.setText(""+item+"");
        }
    }

    @Override
    public int getItemCount() {
        return packageInclusionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView package_inclusion_detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.package_inclusion_detail = (TextView) itemView.findViewById(R.id.text_package_inclusion_detail);

        }
    }
}
