package com.example.assignment2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;

import java.util.ArrayList;
import java.util.List;

public class CheckInAdapter extends RecyclerView.Adapter<CheckInAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        CheckInAdapter.ViewHolder vh = new CheckInAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkInItem.setText(checkIn.get(position));
    }

    @Override
    public int getItemCount() {
        return checkIn.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView checkInItem;
        RelativeLayout checkInItemLayout;

        public ViewHolder(@NonNull View view) {
            super(view);
            checkInItem = view.findViewById(R.id.checkInItem);
            checkInItemLayout = view.findViewById(R.id.checkInItemLayout);
        }
    }

    private int resourceId;
    private Context context;
    private List<String> checkIn;

    public CheckInAdapter(Context context, int resource, ArrayList<String> checkIn) {
        super();
        resourceId = resource;
        this.context = context;
        this.checkIn = checkIn;
    }
}
