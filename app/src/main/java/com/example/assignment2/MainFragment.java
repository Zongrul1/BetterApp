package com.example.assignment2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.assignment2.Subsciber.HelperSubscriber;
import com.example.assignment2.Subsciber.MainSubscriber;
import com.example.assignment2.View.CircleImageView;
import com.example.assignment2.rxRetrofit.RxRetrofit;
import com.example.assignment2.rxRetrofit.Utility;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class MainFragment extends Fragment {

    private RelativeLayout plan;
    private RelativeLayout memo;
    private RelativeLayout pedo;
    private RelativeLayout focus;

    private TextView username;
    private TextView introduction;
    private CircleImageView avater;
    private Button emotion;
    private HelperSubscriber getState;
    private HelperSubscriber putState;
    private String[] states = {"Happy","Unhappy"};

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
                                requestUpdateState(states[0],MyApplication.getToken());
                                break;
                            case 1:
                                avater.setImageResource(R.drawable.unicorn_unhappy);
                                requestUpdateState(states[1],MyApplication.getToken());
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getState = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                final String responseText = response.body().string();
                String code = Utility.handleUser(responseText);
                if(code.equals("200")){
                    String state = Utility.handleState(responseText);
                    if(state.equals("NULL")){
                        requestUpdateState(states[1],MyApplication.getToken());
                    }
                    else if(state.equals(states[0])){
                        avater.setImageResource(R.drawable.unicorn_happy);
                    }
                    else if(state.equals(states[1])){
                        avater.setImageResource(R.drawable.unicorn_unhappy);
                    }
                }
                else{
                    Toast.makeText(getActivity(),"Invalid Token",Toast.LENGTH_SHORT).show();
                }
            }
        };
        putState = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                final String responseText = response.body().string();
                String code = Utility.handleUser(responseText);
                if(code.equals("200")){
                    Toast.makeText(getActivity(),"Update Successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(),"Invalid Token",Toast.LENGTH_SHORT).show();
                }
            }
        };
        requestState(MyApplication.getToken());
    }

    public void requestState(String token){
        RxRetrofit.getInstance().requestState(new MainSubscriber<Response<ResponseBody>>(getState,getActivity()),token);
    }

    public void requestUpdateState(String newState, String token){
        RxRetrofit.getInstance().requestUpdateState(new MainSubscriber<Response<ResponseBody>>(putState,getActivity()),newState,token);
    }
}
