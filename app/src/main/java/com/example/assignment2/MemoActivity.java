package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MemoActivity extends AppCompatActivity {
    private Button back;
    private Button confirm;
    private TextView banner;
    private TextView title;
    private TextView content;
    private EditText editTitle;
    private EditText editContent;
    private String type;
    private String MemoId;
    private HelperSubscriber<Response<ResponseBody>> updateMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo2);
        Typeface rl = ResourcesCompat.getFont(this,R.font.rl);
        banner = findViewById(R.id.banner);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        if(intent.getStringExtra("Id") != null) MemoId = intent.getStringExtra("Id");
        if(intent.getStringExtra("Title") != null) editTitle.setText(intent.getStringExtra("Title"));
        if(intent.getStringExtra("Content") != null) editContent.setText(intent.getStringExtra("Content"));
        banner.setTypeface(rl);
        title.setTypeface(rl);
        content.setTypeface(rl);
        editTitle.setTypeface(rl);
        editContent.setTypeface(rl);
        back = findViewById(R.id.back);
        confirm = findViewById(R.id.confirm);
        updateMemo = new HelperSubscriber<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> response) throws IOException {
                final String responseText = response.body().string();
                String code = Utility.handleUser(responseText);
                if(code.equals("200")){
                    Toast.makeText(MemoActivity.this,"Success",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MemoActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                }
            }
        };
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String arg1 = editTitle.getText().toString();
                String arg2 = editContent.getText().toString();
                if (arg1.length() == 0 || arg2.length() == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MemoActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if (type.equals("insert")) {
                        requestPostMemo(arg1, arg2, MyApplication.getToken());
                    } else if (type.equals("update")) {
                        requestPutMemo(MemoId,arg1, arg2, MyApplication.getToken());
                    }
                    finish();
                }
            }
        });
    }
    public void requestPutMemo(String memoId,String title,String content,String token){
        RxRetrofit.getInstance().requestPutMemo(new MainSubscriber<Response<ResponseBody>>(updateMemo,MemoActivity.this),memoId,title,content,token);
    }

    public void requestPostMemo(String title,String content,String token){
        RxRetrofit.getInstance().requestPostMemo(new MainSubscriber<Response<ResponseBody>>(updateMemo,MemoActivity.this),title,content,token);
    }
}