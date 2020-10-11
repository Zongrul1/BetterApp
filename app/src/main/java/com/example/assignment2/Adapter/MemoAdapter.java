package com.example.assignment2.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.LoginActivity;
import com.example.assignment2.MemoActivity;
import com.example.assignment2.Model.Memo;
import com.example.assignment2.MyApplication;
import com.example.assignment2.R;
import com.example.assignment2.Subsciber.HelperSubscriber;
import com.example.assignment2.Subsciber.MainSubscriber;
import com.example.assignment2.rxRetrofit.RxRetrofit;
import com.example.assignment2.rxRetrofit.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

import okhttp3.ResponseBody;
import retrofit2.Response;

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
        viewHolder.memoDes.setText(memo.get(position).getContent());
        viewHolder.id = memo.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return memo.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView memoTitle;
        TextView memoDes;
        LinearLayout memoItemLayout;
        Button delete;
        String type = "update";
        String id;
        View view;
        HelperSubscriber<Response<ResponseBody>> delMemo;
        ViewHolder(final View view) {
            super(view);
            this.view = view;
            Typeface rl = ResourcesCompat.getFont(view.getContext(),R.font.rl);
            delete = view.findViewById(R.id.delete);
            memoTitle = view.findViewById(R.id.memoTitle);
            memoDes = view.findViewById(R.id.memoDes);
            memoTitle.setTypeface(rl);
            memoDes.setTypeface(rl);
            memoItemLayout = view.findViewById(R.id.memoItemLayout);
            delMemo = new HelperSubscriber<Response<ResponseBody>>() {
                @Override
                public void onNext(Response<ResponseBody> response) throws IOException {
                    final String responseText = response.body().string();
                    String code = Utility.handleUser(responseText);
                    if(code.equals("200")){
                        Toast.makeText(view.getContext(),"Success",Toast.LENGTH_SHORT).show();
                        Message m = Message.obtain();
                        m.what = 0;
                        handler.sendMessage(m);
                    }
                    else{
                        Toast.makeText(view.getContext(),"Fail",Toast.LENGTH_SHORT).show();
                    }
                }
            };
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(view.getContext()).setTitle("Delete").setMessage("Do you want to delete？")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    requestDelMemo(id,MyApplication.getToken());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            }) .show();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MemoActivity.class);
                    intent.putExtra("Type",type);
                    intent.putExtra("Id",id);
                    intent.putExtra("Title",memoTitle.getText());
                    intent.putExtra("Content",memoDes.getText());
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void requestDelMemo(String id,String token) {
            RxRetrofit.getInstance().requestDeleteMemo(new MainSubscriber<Response<ResponseBody>>(delMemo, view.getContext()),id,token);
        }
    }

    private int resourceId;
    private List<Memo> memo;
    private Context context;
    private static Handler handler;

    public MemoAdapter(Context context, int resource, ArrayList<Memo> memo,Handler handler) {
        super();
        resourceId = resource;
        this.memo = memo;
        this.context = context;
        this.handler = handler;
    }

}