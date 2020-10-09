package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MemoActivity extends AppCompatActivity {
    private Button back;
    private Button confirm;
    private TextView banner;
    private TextView title;
    private TextView content;
    private EditText editTitle;
    private EditText editContent;

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
        banner.setTypeface(rl);
        title.setTypeface(rl);
        content.setTypeface(rl);
        editTitle.setTypeface(rl);
        editContent.setTypeface(rl);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}