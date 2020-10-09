package com.example.assignment2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.example.assignment2.Utils.StepUtil;
import com.example.assignment2.Utils.StepService;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;


    @Override
    protected  void onResume() {
        super.onResume();
//        drawerLayout.closeDrawers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.addActivity(this);
//        drawerLayout = findViewById(R.id.drawer);
        Fragment mainFragment = new MainFragment();
        MyApplication.setContext(getApplicationContext());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment,mainFragment)
                .addToBackStack(null)
                .commit();
        initStep();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(MainActivity.this).setTitle("Exit").setMessage("Do you want to exit？")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            MyApplication.exit();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }) .show();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    /*
     * init step count service
     */
    private void initStep() {
        Intent intent = new Intent(this, StepService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
//        check whether this phone support step count
//        if (!StepUtil.isSupportStep(this)) {
//            drawerLayout.setText("This phone does not support step count");
//            return;
//        }
//
//        on click listener
//        mBtRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String steps = StepUtil.getTodayStep(MainActivity.this) + "steps";
//                mTvSteps.setText(steps);
//            }
//        });
    }
}