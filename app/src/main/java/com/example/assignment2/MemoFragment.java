package com.example.assignment2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.assignment2.Adapter.MemoAdapter;
import com.example.assignment2.Model.Memo;
import com.example.assignment2.Subsciber.HelperSubscriber;
import com.example.assignment2.Subsciber.MainSubscriber;
import com.example.assignment2.rxRetrofit.RxRetrofit;
import com.example.assignment2.rxRetrofit.Utility;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class MemoFragment extends Fragment {

    private RecyclerView listView;
    private MemoAdapter memoAdapter;
    private ArrayList<Memo> memo = new ArrayList<>();;
    private Button MenuButton;
    private Button plus;
    private String type = "insert";
    private TextView banner;
    private TextView tips;
    private HelperSubscriber getMemo;
    private SwipeRefreshLayout swipe;
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message.what == 0){
                requestMemo(MyApplication.getToken());
            }
            return false;
        }
    });

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestMemo(MyApplication.getToken());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_login for this fragment
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        listView = view.findViewById(R.id.memoList);
        memoAdapter = new MemoAdapter(getContext(), R.layout.memo_item, memo,getHandler());
        listView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        listView.setAdapter(memoAdapter);
        MenuButton = view.findViewById(R.id.menuButton);
        plus = view.findViewById(R.id.plus);
        banner = view.findViewById(R.id.banner);
        tips = view.findViewById(R.id.tips);
        swipe = view.findViewById(R.id.swipe);
        Typeface rl = ResourcesCompat.getFont(getActivity(),R.font.rl);
        banner.setTypeface(rl);
        tips.setTypeface(rl);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestMemo(MyApplication.getToken());
                Toast.makeText(getActivity(),"Refresh", Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });
        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MainFragment();
                ((AppCompatActivity)getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_fragment, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .commit();
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MemoActivity.class);
                intent.putExtra("Type",type);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMemo = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                final String responseText = response.body().string();
                Log.d("memo",responseText);
                memo.clear();
                Utility.handleMemo(memo, responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        memoAdapter.notifyDataSetChanged();
                        if(memoAdapter.getItemCount() > 0) tips.setVisibility(View.GONE);
                        else tips.setVisibility(View.VISIBLE);
                    }
                });
            }
        };
        requestMemo(MyApplication.getToken());
    }

    public void requestMemo(String token) {
        RxRetrofit.getInstance().requestMemo(new MainSubscriber<Response<ResponseBody>>(getMemo, getActivity()),token);
    }
}