package com.example.assignment2;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.assignment2.Adapter.MemoAdapter;

public class EmotionFragment extends Fragment {

    private Button MenuButton;
    private boolean type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emotion, container, false);
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
}