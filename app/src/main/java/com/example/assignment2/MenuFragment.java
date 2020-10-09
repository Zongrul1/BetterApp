package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.assignment2.Adapter.MenuAdapter;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

    private RecyclerView listView;
    private MenuAdapter menuAdapter;
    private TextView username;
    private ArrayList<String> menu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_login for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        dataInit();
        listView = view.findViewById(R.id.menuList);
        username = view.findViewById(R.id.username);
        username.setText(username.getText() + MyApplication.getUsername());
        menuAdapter = new MenuAdapter(getContext(), R.layout.menu_item, menu);
        listView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        listView.setAdapter(menuAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void dataInit(){
        menu = new ArrayList<>();
        menu.add("Plan");
        menu.add("Check in");
        menu.add("Memo");
        menu.add("Pedometer");
        menu.add("Focus");
        menu.add("Emotion");
    }


}