package com.example.assignment2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.assignment2.Adapter.MemoAdapter;
import com.example.assignment2.Model.Memo;

import java.util.ArrayList;



public class MemoFragment extends Fragment {

    private RecyclerView listView;
    private MemoAdapter memoAdapter;
    private ArrayList<Memo> memo;
    private Button MenuButton;
    private boolean type;
    private TextView banner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_login for this fragment
        View view = inflater.inflate(R.layout.fragment_memo, container, false);
        dataInit();
        listView = view.findViewById(R.id.memoList);
        memoAdapter = new MemoAdapter(getContext(), R.layout.memo_item, memo);
        listView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        listView.setAdapter(memoAdapter);
        type = false;
        MenuButton = view.findViewById(R.id.menuButton);
        banner = view.findViewById(R.id.banner);
        Typeface rl = ResourcesCompat.getFont(getActivity(),R.font.rl);
        banner.setTypeface(rl);
        MainActivity mainActivity = (MainActivity) getActivity();
        //final DrawerLayout drawerLayout = mainActivity.drawerLayout;
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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void dataInit(){
        memo = new ArrayList<>();
        for(int i = 0;i < 10;i++) {
            memo.add(new Memo("title-" + i,"content-" + i));
        }
    }
}