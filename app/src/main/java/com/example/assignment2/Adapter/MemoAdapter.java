package com.example.assignment2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.MemoActivity;
import com.example.assignment2.Model.Memo;
import com.example.assignment2.R;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        MemoAdapter.ViewHolder vh = new MemoAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.memoTitle.setText(memo.get(position).getTitle());
        viewHolder.memoDes.setText(memo.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return memo.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView memoTitle;
        TextView memoDes;
        LinearLayout memoItemLayout;
        ViewHolder(View view) {
            super(view);
            Typeface rl = ResourcesCompat.getFont(view.getContext(),R.font.rl);
            memoTitle = view.findViewById(R.id.memoTitle);
            memoDes = view.findViewById(R.id.memoDes);
            memoTitle.setTypeface(rl);
            memoDes.setTypeface(rl);
            memoItemLayout = view.findViewById(R.id.memoItemLayout);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MemoActivity.class);
                    view.getContext().startActivity(intent);
                }
            });

        }
    }

    private int resourceId;
    private List<Memo> memo;
    private Context context;

    public MemoAdapter(Context context, int resource, ArrayList<Memo> memo) {
        super();
        resourceId = resource;
        this.memo = memo;
        this.context = context;
    }

}