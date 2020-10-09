package com.example.assignment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment2.View.CircleImageView;



public class MainFragment extends Fragment {

    private RelativeLayout plan;
    private RelativeLayout memo;
    private RelativeLayout pedo;
    private RelativeLayout focus;

    private TextView username;
    private TextView introduction;
    private CircleImageView avater;
    private Button emotion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_login for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        plan = view.findViewById(R.id.plan);
        memo = view.findViewById(R.id.memo);
        pedo = view.findViewById(R.id.pedo);
        focus = view.findViewById(R.id.focus);
        introduction = view.findViewById(R.id.introduction);
        avater = view.findViewById(R.id.avater);
        introduction = view.findViewById(R.id.introduction);
        emotion = view.findViewById(R.id.emotion);
        introduction = view.findViewById(R.id.introduction);
        username = view.findViewById(R.id.username);
        username.setText(username.getText() + " "  + MyApplication.getUsername());
        Typeface rt = ResourcesCompat.getFont(getActivity(),R.font.rt);
        Typeface rl = ResourcesCompat.getFont(getActivity(),R.font.rl);
        introduction.setTypeface(rl);
        username.setTypeface(rl);
        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PlanListActivity.class);
                getActivity().startActivity(intent);
            }
        });

        emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.CardDialogStyle);
                builder.setTitle("Choose Your Emotion Today");
                //    指定下拉列表的显示数据
                final String[] contacts = {"Happy", "Unhappy"};
                //    设置一个下拉的列表选择项
                builder.setItems(contacts, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                avater.setImageResource(R.drawable.unicorn_happy);
                                break;
                            case 1:
                                avater.setImageResource(R.drawable.unicorn_unhappy);
                                break;

                        }
                    }
                });
                builder.show();
            }
        });


        memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new MemoFragment();
                ((AppCompatActivity)getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_fragment, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        pedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new PedometerFragment();
                ((AppCompatActivity)getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_fragment, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new FocusFragment();
                ((AppCompatActivity)getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_fragment, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });
        return view;
    }
}
