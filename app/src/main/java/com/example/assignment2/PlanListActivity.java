package com.example.assignment2;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.assignment2.Utils.DateUtil;
import com.example.assignment2.Utils.ToastUtil;
import com.example.assignment2.Model.DayStatus;
import com.example.assignment2.db.DayStatusDao;
import com.example.assignment2.db.PlanListItemDao;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;


public class PlanListActivity extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private View homeTab;
    private ImageView homeImg;
    private TextView homeText;
    private View calendarTab;
    private ImageView calendarImg;
    private TextView calendarText;
    private View graphTab;
    private ImageView graphImg;
    private TextView graphText;
    private PlanListFragment homeFragment;
    private DateFragment calendarFragment;
    private GraphFragment graphFragment;
    private FragmentManager fragmentManager;

    private Button MenuButton;
    private TextView banner;
    private static final String[] FRAGMENT_TAGS=
            new String[]{"homeFragment","calendarFragment","graphFragment"};
    private int savedIndex=0;
    private long preTime;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        fragmentManager=getSupportFragmentManager();
        initLastData();
        initViews();
        //initToolbar();
        initListener();
        initTab();
        if(savedInstanceState!=null){
            savedIndex=savedInstanceState.getInt("savedIndex");
        }
//        setTabSelection(savedIndex);
    }
    private void initLastData(){
        SharedPreferences preferences=getSharedPreferences("list",MODE_PRIVATE);
        String lastVisitTime=preferences.getString("lastVisitTime","");
        Calendar tempCalendar= Calendar.getInstance();
        String todayTime= DateUtil.getYearMonthDayNumberic(tempCalendar.getTime());
        if(!lastVisitTime.equals("")){
            if(!lastVisitTime.equals(todayTime)){
                DayStatus dayStatus= PlanListItemDao.updateNoRecord(lastVisitTime);
                if(dayStatus!=null){
                    DayStatusDao.insertDayStatus(dayStatus);
                }
            }
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("lastVisitTime",todayTime);
        editor.apply();
    }
    private  void initTab(){
        final Fragment[] fragments = {new PlanListFragment(),new DateFragment(),new GraphFragment()};
        tabLayout.addTab(tabLayout.newTab().setText("PLAN"));
        tabLayout.addTab(tabLayout.newTab().setText("CALENDER"));
        tabLayout.addTab(tabLayout.newTab().setText("CHART"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                @Override
                public int getCount() {
                return fragments.length;
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
    private void initViews(){
        MenuButton = findViewById(R.id.menuButton);
        banner = findViewById(R.id.banner);
        Typeface rl = ResourcesCompat.getFont(this,R.font.rl);
        banner.setTypeface(rl);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

    }
    private void initToolbar(){
        setSupportActionBar(toolbar);
    }

    private void hideFragments(FragmentTransaction transaction){
        if(homeFragment!=null){
            transaction.hide(homeFragment);
        }
        if(calendarFragment!=null){
            transaction.hide(calendarFragment);
        }
        if(graphFragment!=null){
            transaction.hide(graphFragment);
        }
    }

        private void initListener(){
        MenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("savedIndex",savedIndex);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long curTime= System.currentTimeMillis();
            if ((curTime - preTime) > 1000 * 2) {
                ToastUtil.showToast("Press twice to return main menu");
                preTime = curTime;
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
