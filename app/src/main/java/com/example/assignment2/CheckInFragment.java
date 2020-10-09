package com.example.assignment2;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.assignment2.Adapter.CheckInAdapter;
import com.example.assignment2.Adapter.MenuAdapter;

import java.util.ArrayList;


public class CheckInFragment extends Fragment {

    private RecyclerView listView;
    private CheckInAdapter checkInAdapter;
    private ArrayList<String> checkIn;
    private Button MenuButton;
    private boolean type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_login for this fragment
        View view = inflater.inflate(R.layout.fragment_check_in, container, false);
        dataInit();
        listView = view.findViewById(R.id.checkInList);
        checkInAdapter = new CheckInAdapter(getContext(), R.layout.checkin_item, checkIn);
        listView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        listView.setAdapter(checkInAdapter);
        type = false;
        MenuButton = view.findViewById(R.id.menuButton);
        MainActivity mainActivity = (MainActivity) getActivity();
        final DrawerLayout drawerLayout = mainActivity.drawerLayout;
        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type) drawerLayout.openDrawer(GravityCompat.START);
                else drawerLayout.closeDrawers();
                type = !type;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void dataInit(){
        checkIn = new ArrayList<>();
        for(int i = 0;i < 20;i++) checkIn.add("item-"+i);
    }
}