package com.example.travelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder>
{
    private Context mContext;
    private ArrayList<PlanItem> mPlanItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public PlanAdapter(Context context, ArrayList<PlanItem> planList)
    {
        mContext = context;
        mPlanItems = planList;

    }


    @NonNull
    @Override
    public PlanAdapter.PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_item_setp, parent, false);
        return new PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.PlanViewHolder holder, int position) {
        PlanItem currentItem = mPlanItems.get(position);
        String platform = currentItem.getmPlatform();
        String destination = currentItem.getmDestination();
        String dTime = currentItem.getmDepTime();
        String aTime = currentItem.getmArrTime();
        String location = currentItem.getmLocation();
        String duration = currentItem.getmDuration();
        String stops = currentItem.getmStops();
        String arrStatus = currentItem.getmArrTime();
        String depStatus = currentItem.getmDepStatus();

        holder.mTextViewPlatform.setText(platform);
        holder.mTextViewDestination.setText(destination);
        holder.mTextViewD_Time.setText(dTime);
        holder.mTextViewA_Time.setText(aTime);
        holder.mTextViewLocation.setText(location);
        holder.mTextViewDuration.setText(duration);
        holder.mTextViewStops.setText(stops);
        holder.mTextViewDepTimeStatus.setText(depStatus);
        holder.mTextViewArrTimeStatus.setText(arrStatus);

    }

    @Override
    public int getItemCount() {
        return mPlanItems.size();
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextViewPlatform;
        public TextView mTextViewDestination;
        public TextView mTextViewD_Time;
        public TextView mTextViewA_Time;
        public TextView mTextViewLocation;
        public TextView mTextViewStops;
        public TextView mTextViewDuration;
        public TextView mTextViewDepTimeStatus;
        public TextView mTextViewArrTimeStatus;
        public PlanViewHolder(View itemView)
        {
            super(itemView);
            mTextViewPlatform = itemView.findViewById(R.id.lblPlatform);
            mTextViewDestination = itemView.findViewById(R.id.lblDestination);
            mTextViewA_Time = itemView.findViewById(R.id.lblAtime);
            mTextViewD_Time = itemView.findViewById(R.id.lblDtime);
            mTextViewLocation = itemView.findViewById(R.id.lblLocation);
            mTextViewDuration = itemView.findViewById(R.id.lblDuration);
            mTextViewStops = itemView.findViewById(R.id.lblStops);
            mTextViewArrTimeStatus = itemView.findViewById(R.id.lblAtimeStatus);
            mTextViewDepTimeStatus = itemView.findViewById(R.id.lblDtimeStatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);

                        }
                    }
                }
            });
        }
    }
}