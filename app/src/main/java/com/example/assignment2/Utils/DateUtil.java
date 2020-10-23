package com.example.assignment2.Utils;

import android.content.res.Resources;

import com.example.assignment2.MyApplication;
import com.example.assignment2.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static Resources resources= MyApplication.getContext().getResources();
    private static Calendar calendar= Calendar.getInstance();
    public static String getYearMonthDayNumberic(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(resources.getString(R.string.year_month_day_numberic),year,month,day);
    }
    public static String getYearAndMonth(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        return String.format(resources.getString(R.string.year_and_month),year,month);
    }
    public static String getYearMonthDay(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(resources.getString(R.string.year_month_day),year,month,day);
    }

    public static String getCompareTodayText(Date date){
        Date todayDate=new Date();
        calendar.setTime(todayDate);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        todayDate=calendar.getTime();
        long intervals=date.getTime()-todayDate.getTime();
        long oneDay=24*60*60*1000;
        long twoDay=2*oneDay;
        long threeDay=3*oneDay;
        String text;
        if(intervals>=0){
            if(intervals<oneDay){
                text=resources.getString(R.string.today);
            }else if(intervals<twoDay){
                text=resources.getString(R.string.tomorrow);
            }else if(intervals<threeDay){
                text=resources.getString(R.string.after_tomorrow);
            }else{
                int num=(int)(intervals/oneDay);
                text= String.format(resources.getString(R.string.after_day_num),num);
            }
        }else{
            intervals= Math.abs(intervals);
            if(intervals<oneDay){
                text=resources.getString(R.string.yesterday);
            }else if(intervals<twoDay){
                text=resources.getString(R.string.before_yesterday);
            }else{
                int num=(int)(intervals/oneDay)+1;
                text= String.format(resources.getString(R.string.before_day_num),num);
            }
        }
        return text;
    }

    public static Date getNextMonthDate(Date date){
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,1);
        return calendar.getTime();
    }

    public static Date getLastMonthDate(Date date){
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,-1);
        return calendar.getTime();
    }
}
