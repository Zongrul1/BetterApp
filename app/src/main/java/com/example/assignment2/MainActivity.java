package com.example.assignment2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.content.Intent;
import android.widget.TextView;

import com.example.assignment2.Service.BindService;
import com.example.assignment2.Utils.ToastUtil;
import com.example.assignment2.listener.StepUpdateUI;

public class MainActivity extends AppCompatActivity {

    private BindService bindService;
    private boolean isBind;
    private Fragment selectedFragemnt;
    private long preTime;

    public Fragment getSelectedFragemnt() {
        return selectedFragemnt;
    }

    public void setSelectedFragemnt(Fragment selectedFragemnt) {
        this.selectedFragemnt = selectedFragemnt;
    }

    @Override
    protected  void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.addActivity(this);
        Fragment mainFragment = new MainFragment();
        MyApplication.setContext(getApplicationContext());
        initStep();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment,mainFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (selectedFragemnt instanceof MainFragment) {
                new AlertDialog.Builder(MainActivity.this).setTitle("Exit").setMessage("Do you want to exit？")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                MyApplication.exit();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();
            } else {
                long curTime= System.currentTimeMillis();
                if ((curTime - preTime) > 1000 * 2) {
                    ToastUtil.showToast("Press twice to return main menu");
                    preTime = curTime;
                }else{
                    Fragment mainFragment = new MainFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.layout_fragment, mainFragment)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }
                return true;
            }
            return true;
        }
         else
            return super.onKeyDown(keyCode, event);
    }

    private void initStep() {
        Intent intent = new Intent(MainActivity.this, BindService.class);
        isBind = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BindService.LcBinder lcBinder = (BindService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new StepUpdateUI() {
                @Override
                public void updateUi(int stepCount) {
               //     Log.i("MainActivity—updateUi","receive:");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = stepCount;
                    MyApplication.getPedofragment().getHandler().sendMessage(message);
              //    Log.i("MainActivity—updateUi","current step is:"+stepCount);
                }
            });
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("MainActivity—updateUi","failed");
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(serviceConnection);
        }
    }
            /*
     * init step count service
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
     */
}