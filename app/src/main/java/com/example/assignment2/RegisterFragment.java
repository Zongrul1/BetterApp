package com.example.assignment2;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment2.Subsciber.HelperSubscriber;
import com.example.assignment2.Subsciber.MainSubscriber;
import com.example.assignment2.rxRetrofit.RxRetrofit;
import com.example.assignment2.rxRetrofit.Utility;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class RegisterFragment extends Fragment {

    private EditText username_input;
    private EditText password_input;
    private EditText confirm_input;
    private TextView banner;
    private Button back;
    private Button register;
    private HelperSubscriber getUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment_login for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        username_input = view.findViewById(R.id.username_input);
        password_input = view.findViewById(R.id.password_input);
        confirm_input = view.findViewById(R.id.confirm_input);
        back = view.findViewById(R.id.back);
        register = view.findViewById(R.id.register);
        banner = view.findViewById(R.id.banner);
        Typeface rt = ResourcesCompat.getFont(getActivity(),R.font.rt);
        Typeface rl = ResourcesCompat.getFont(getActivity(),R.font.rl);
        banner.setTypeface(rt);
        username_input.setTypeface(rl);
        password_input.setTypeface(rl);
        confirm_input.setTypeface(rl);
        back.setTypeface(rl);
        register.setTypeface(rl);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment loginFragment = new LoginFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_fragment,loginFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(username_input.getText().length() == 0){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"no username",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(password_input.getText().length() == 0){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"no password",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(confirm_input.getText().length() == 0 ||!confirm_input.getText().toString().equals(password_input.getText().toString())){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"no confirm",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    requestUser("register", username_input.getText().toString(), password_input.getText().toString());
                }
            }
        });
        return  view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getUser = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                try {
                    final String responseText = response.body().string();
                    String code = Utility.handleUser(responseText);
                    if(code.equals("200")){
                        Toast.makeText(getActivity(),"Register Successfully",Toast.LENGTH_SHORT).show();
                        Fragment loginFragment = new LoginFragment();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.layout_fragment,loginFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                    else{
                        Toast.makeText(getActivity(),"Register Failed",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void requestUser(String type,String name,String password) {
        RxRetrofit.getInstance().requestUser(new MainSubscriber<Response<ResponseBody>>(getUser, getActivity()), type, name, password);
    }
}