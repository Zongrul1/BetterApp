package com.example.assignment2;

import android.graphics.Typeface;
import android.os.Bundle;
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

import com.example.assignment2.Utils.StepUtil;

public class PedometerFragment extends Fragment {

    private Button MenuButton;
    private Button RefreshButton;
    private TextView countStep;
    private boolean type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedometer, container, false);
        type = false;
        MenuButton = view.findViewById(R.id.menuButton);
        RefreshButton = view.findViewById(R.id.refreshButton);
        countStep = view.findViewById(R.id.countStep);
        MainActivity mainActivity = (MainActivity) getActivity();
//        final DrawerLayout drawerLayout = mainActivity.drawerLayout;
        Typeface rl = ResourcesCompat.getFont(getActivity(),R.font.rl);
        countStep.setTypeface(rl);
        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MainFragment();
                ((AppCompatActivity)getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .addToBackStack(null)
                        .commit();
            }
        });
        //check whether this phone support step count
        if (!StepUtil.isSupportStep(getActivity())) {
//            drawerLayout.setText("This phone does not support step count");
            //return;
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(getActivity(),"This phone does not support step count",Toast.LENGTH_SHORT);
                }
            });
        }

        //on click listener
        RefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String steps = StepUtil.getTodayStep(getActivity()) + " steps";
                countStep.setText(steps);
            }
        });
        return view;
    }


}