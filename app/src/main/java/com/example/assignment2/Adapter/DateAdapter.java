package com.example.assignment2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;
import com.example.assignment2.bean.PlanListItem;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.NormalViewHolder>  {

    class NormalViewHolder extends PlanListAdapter.ViewHolder {
        ImageView status;
        TextView content;
        public NormalViewHolder(@NonNull View itemView, int itemViewType) {
            super(itemView);
            if(itemViewType== PlanListItem.TYPE_EMPTY)
                return;
            status = itemView.findViewById(R.id.date_item_status);
            content = itemView.findViewById(R.id.date_item_content);
        }
    }
    private List<PlanListItem> dataList;
    private Context context;

    public DateAdapter(Context context, List<PlanListItem> dataList){
        this.dataList=dataList;
        this.context=context;
    }
    @NonNull
    @Override
    public DateAdapter.NormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=null;
        if(viewType== PlanListItem.TYPE_EMPTY){
            itemView= LayoutInflater.from(context).inflate(R.layout.date_empty_item,parent,false);
        }else if(viewType== PlanListItem.TYPE_NORMAL){
            itemView= LayoutInflater.from(context).inflate(R.layout.date_normal_item,parent,false);
        }
        return new NormalViewHolder(itemView,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull DateAdapter.NormalViewHolder holder, int position) {
        int itemViewType=getItemViewType(position);
        if(itemViewType== PlanListItem.TYPE_NORMAL){
            int status=dataList.get(position).getStatus();
            String content=dataList.get(position).getContent();
            if(status== PlanListItem.FINISH){
                holder.status.setImageResource(R.drawable.finish);
            }else if(status== PlanListItem.UNFINISH){
                holder.status.setImageResource(R.drawable.un_finish);
            }else if(status== PlanListItem.NO_RECORD){
                holder.status.setImageResource(R.drawable.circle);
            }
            holder.content.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        if(dataList.size()==0){
            return 1;
        }
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.size()==0){
            return PlanListItem.TYPE_EMPTY;
        }else{
            return PlanListItem.TYPE_NORMAL;
        }
    }
}
