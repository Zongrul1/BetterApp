package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    private TextView name;
    private TextView footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        MyApplication.addActivity(this);
        name = findViewById(R.id.name);
        footer = findViewById(R.id.footer);
        Typeface rl = ResourcesCompat.getFont(this,R.font.rl);
        name.setTypeface(rl);
        footer.setTypeface(rl);
        Thread t = new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        t.start();
    }
}