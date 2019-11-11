package com.example.travelapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.NearbyViewHolder>
{
    private Context mContext;
    private ArrayList<NearbyItem> mNearbyList;
    private PlanAdapter.OnItemClickListener mListener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PlanAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public NearbyAdapter(Context context, ArrayList<NearbyItem> nearbyList)
    {
        mContext = context;
        mNearbyList = nearbyList;

    }

    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_item, parent, false);
        return  new NearbyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NearbyViewHolder holder, int position)
    {
        NearbyItem currentItem = mNearbyList.get(position);
        String name = currentItem.getmName();
        String type = currentItem.getmType();
        String desc = currentItem.getmDesc();
        String code = currentItem.getmCode();
        holder.mTextViewName.setText(name);
        holder.mTextViewDesc.setText(desc);
        holder.mTextViewType.setText(type);
        holder.mTextViewCode.setText(code);

    }

    @Override
    public int getItemCount()
    {
        //Get the number of items
        return mNearbyList.size();
    }

    public class NearbyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextViewName;
        public TextView mTextViewType;
        public TextView mTextViewDesc;
        public TextView mTextViewCode;

        public NearbyViewHolder(View itemView)
        {
            super(itemView);
            mTextViewType = itemView.findViewById(R.id.lblType);
            mTextViewDesc = itemView.findViewById(R.id.lblDesc);
            mTextViewName = itemView.findViewById(R.id.lblName);
            mTextViewCode = itemView.findViewById(R.id.lblCode);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}
