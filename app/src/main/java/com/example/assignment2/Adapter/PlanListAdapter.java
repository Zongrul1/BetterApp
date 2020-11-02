package com.example.assignment2.Adapter;

import android.animation.LayoutTransition;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.Model.PlanListItem;
import com.example.assignment2.R;
import com.example.assignment2.listener.OnBackPressListener;
import com.example.assignment2.listener.OnClickListener;
import com.example.assignment2.listener.OnNextListener;
import com.example.assignment2.listener.OnTextChangeListener;

import java.util.List;

public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView status;
        public EditText content_edit_text;
        public TextView content_text;
        public ImageView finish;
        public ImageView unFinish;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.content_edit_text=itemView.findViewById(R.id.item_content_edit_text); //the editing text when user is using
            this.content_text=itemView.findViewById(R.id.item_content_text); //the text when user has already edited
            this.status=itemView.findViewById(R.id.item_status);   //the status for the edited text
            this.finish=itemView.findViewById(R.id.item_finish);
            this.unFinish=itemView.findViewById(R.id.item_unfinish);
        }
    }
    private List<PlanListItem> dataList;
    private Context context;
    private LayoutTransition layoutT;

    //Listener
    private OnClickListener onFinishListener;
    private OnClickListener onUnFinishListener;
    private OnNextListener onNextListener;
    private OnTextChangeListener onTextChangeListener;
    private OnBackPressListener onBackPressListener;

    private boolean isNewItem;

    public PlanListAdapter(List<PlanListItem> dataList){
        this.dataList=dataList;
        layoutT=new LayoutTransition();
        isNewItem=true;
    }
    public void setOnFinishListener(OnClickListener onFinishListener){
        this.onFinishListener=onFinishListener;
    }
    public void setOnUnFinishListener(OnClickListener onUnFinishListener){
        this.onUnFinishListener=onUnFinishListener;
    }
    public void setOnNextListener(OnNextListener onNextListener){
        this.onNextListener=onNextListener;
    }
    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener){
        this.onTextChangeListener = onTextChangeListener;
    }
    public void setOnBackPressListener(OnBackPressListener onBackPressListener){
        this.onBackPressListener=onBackPressListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View itemView = LayoutInflater.from(context).inflate(R.layout.plan_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(itemView);
        ((ViewGroup)viewHolder.itemView).setLayoutTransition(layoutT);
        initEditListener(viewHolder);
        initButtonListener(viewHolder);
        return viewHolder;
    }
    private void initEditListener(final ViewHolder viewHolder){
        viewHolder.content_edit_text.addTextChangedListener(new TextWatcher() {
            //need three text changed function here 'before','on','after' for the TextWatcher()
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int afterChange) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int beforeChange, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int pos=viewHolder.getAdapterPosition();
                onTextChangeListener.onTextChange(s,pos);
            }
        });
        viewHolder.content_edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int pos=viewHolder.getAdapterPosition();
                onNextListener.onNext(pos);
                return true;
            }
        });
       // LogUtil.e("setOnKeyListener");
        viewHolder.content_edit_text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN&& event.getKeyCode() == KeyEvent.KEYCODE_DEL){
                    EditText editText = (EditText)v;
                //    LogUtil.e("execute OnBackPress now");
                    onBackPressListener.onBackPress(editText,viewHolder.getAdapterPosition());
                }
                return false;
            }
        });
    }
    private void initButtonListener(final ViewHolder viewHolder){
        viewHolder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewHolder.getAdapterPosition();
                onFinishListener.onClick(v,pos);
            }
        });
        viewHolder.unFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=viewHolder.getAdapterPosition();
                onUnFinishListener.onClick(v,pos);
            }
        });
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int pos) {
       // LogUtil.e("onBindViewHolder");
        int status=dataList.get(pos).getStatus();
        String content=dataList.get(pos).getContent();
        switch (status){
            case PlanListItem.NO_CONTENT:
                holder.content_edit_text.setText("");
                holder.content_edit_text.setVisibility(View.VISIBLE);
                holder.content_text.setVisibility(View.GONE);
                holder.status.setImageResource(R.drawable.circle);
                holder.finish.setVisibility(View.INVISIBLE);
                holder.unFinish.setVisibility(View.INVISIBLE);
                break;
            case PlanListItem.NO_RECORD:
                holder.content_edit_text.setText(content);
                holder.content_edit_text.setVisibility(View.VISIBLE);
                holder.content_text.setVisibility(View.GONE);
                holder.status.setImageResource(R.drawable.circle);
                holder.finish.setVisibility(View.VISIBLE);
                holder.unFinish.setVisibility(View.VISIBLE);
                break;
            case PlanListItem.FINISH:
                holder.content_edit_text.setVisibility(View.GONE);
                holder.content_text.setVisibility(View.VISIBLE);
                holder.content_text.setText(content);
                holder.status.setImageResource(R.drawable.finish);
                holder.finish.setVisibility(View.INVISIBLE);
                holder.unFinish.setVisibility(View.INVISIBLE);
                break;
            case PlanListItem.UNFINISH:
                holder.content_edit_text.setVisibility(View.GONE);
                holder.content_text.setVisibility(View.VISIBLE);
                holder.content_text.setText(content);
                holder.status.setImageResource(R.drawable.un_finish);
                holder.finish.setVisibility(View.INVISIBLE);
                holder.unFinish.setVisibility(View.INVISIBLE);
                break;
        }
        if(isNewItem&&pos == dataList.size()-1){
            holder.content_edit_text.requestFocus();
            holder.content_edit_text.setSelection(content.length());
        }
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void setNewItem(boolean newItem) {
        isNewItem = newItem;
    }
}
