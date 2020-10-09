package com.example.assignment2.Utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.example.assignment2.MyApplication;

public class DisplayUtil {
    private static DisplayMetrics displayMetrics=MyApplication.getContext().getResources().getDisplayMetrics();
    public static int dp2px(int dpValue){
        float pxValue= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,displayMetrics);
        return (int)pxValue;
    }
    public static int sp2px(int spValue){
        float pxValue= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,spValue,displayMetrics);
        return (int)pxValue;
    }
    public static int getScreenWidth(){
        return displayMetrics.widthPixels;
    }
    public static int getScreenHeight(){
        return displayMetrics.heightPixels;
    }


}

