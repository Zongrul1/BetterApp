package com.example.assignment2.Adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.assignment2.StatsFragment;

import java.util.List;

public class FocusAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    List<String> appList;
    StatsFragment mCurrentFragment;
    public FocusAdapter(FragmentManager fm, int NumOfTabs, List<String> applist) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.appList = applist;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        StatsFragment statsFragment = new StatsFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("period", position);
        statsFragment.setArguments(args);
        return statsFragment;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (StatsFragment) object;
        super.setPrimaryItem(container, position, object);
    }
    public StatsFragment getCurrentFragment() {
        return mCurrentFragment;
    }

}