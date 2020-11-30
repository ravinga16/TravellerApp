package com.example.mytravellerapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.common.constants.DomainConstants;
import com.example.mytravellerapp.dto.TourSchedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TourDayDetailsRecycleAdapter extends RecyclerView.Adapter<TourDayDetailsRecycleAdapter.ViewHolder>{
    private List<TourSchedule> tourSchedule;
    private Context mContext;
    public TourDayDetailsRecycleAdapter(List<TourSchedule> tourSchedule, Context mContext) {
        this.tourSchedule = tourSchedule;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        View listItem= layoutInflater.inflate(R.layout.layout_card_tour_detail, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final TourSchedule item = tourSchedule.get(position);
        final String imageUrl = DomainConstants.TOUR_IMAGE_URL + item.getDay_image();
        holder.day_tour_name.setText("");
        holder.text_destination.setText("");
        holder.text_travel_time.setText("");
        holder.text_destination_name.setText("");
        holder.text_extra.setText("");
        holder.otherDetailContainer.setVisibility(View.GONE);
        holder.textOtherDetails.setText("");
        if(item!= null){
            Picasso.with(mContext).load(imageUrl).resize(400, 400)
                    .centerCrop().into(holder.dayImage);
            holder.day_tour_name.setText("Day "+(position+1)+" "+"\n"+item.getScheduleName()+"");
            holder.text_destination.setText(""+item.getStartLocation()+" to "+item.getEndLocation()+"");
            holder.text_travel_time.setText(""+item.getTravellingTime()+""+" drive ");
            holder.text_destination_name.setText(""+item.getScheduleName()+"");
            holder.textOtherDetails.setText(""+item.getDescription()+"");
            int i = 0;
            String otherVisitDetails = "";
            for(i=0; i<item.getOtherVisitingPlaces().size();i++){
                otherVisitDetails+=item.getOtherVisitingPlaces().get(i);
                if(i!=item.getOtherVisitingPlaces().size()-1){
                    otherVisitDetails+=","+"\n";
                }
            }otherVisitDetails+=".";
            holder.text_extra.setText(otherVisitDetails);
            holder.viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(holder.viewButton.getTag()!=null){
                        holder.otherDetailContainer.setVisibility(View.GONE);
                        holder.viewButton.setImageResource(R.drawable.ic_down);
                        holder.viewButton.setTag(null);
                    }else{
                        holder.otherDetailContainer.setVisibility(View.VISIBLE);
                        holder.viewButton.setImageResource(R.drawable.ic_up);
                        holder.viewButton.setTag("0");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tourSchedule.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView dayImage;
        private TextView day_tour_name;
        private TextView text_destination;
        private TextView text_travel_time;
        private TextView text_destination_name;
        private TextView text_extra;
        private FloatingActionButton viewButton;
        private LinearLayout otherDetailContainer;
        private TextView textOtherDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dayImage = (ImageView) itemView.findViewById(R.id.featured_image);
            this.day_tour_name = (TextView) itemView.findViewById(R.id.day_tour_name);

            this.text_destination = (TextView) itemView.findViewById(R.id.text_destination);
            this.text_travel_time = (TextView) itemView.findViewById(R.id.text_travel_time);
            this.text_destination_name = (TextView) itemView.findViewById(R.id.text_destination_name);
            this.text_extra = (TextView) itemView.findViewById(R.id.text_extra);

            this.viewButton = (FloatingActionButton) itemView.findViewById(R.id.view_button);
            this.otherDetailContainer = (LinearLayout) itemView.findViewById(R.id.other_detail_container);
            this.textOtherDetails = (TextView) itemView.findViewById(R.id.text_other_details);
        }
    }
}
