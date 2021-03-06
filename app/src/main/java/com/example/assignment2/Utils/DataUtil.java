package com.example.assignment2.Utils;

import android.content.ContentValues;

import com.example.assignment2.Model.DayStatus;
import com.example.assignment2.Model.Memo;
import com.example.assignment2.Model.PlanListItem;

public class DataUtil {
    public static ContentValues getDayStatusCV(DayStatus dayStatus){
        ContentValues values=new ContentValues();
        values.put("time",dayStatus.getTime());
        values.put("status",dayStatus.getStatus());
        values.put("ratio",dayStatus.getRatio());
        values.put("list_num",dayStatus.getListNum());
        values.put("finish_num",dayStatus.getFinishNum());
        values.put("unfinish_num",dayStatus.getUnFinishNum());
        values.put("year",dayStatus.getYear());
        values.put("month",dayStatus.getMonth());
        values.put("day",dayStatus.getDay());
        return values;
    }
    public static DayStatus getDayStatus(String time, int listNum, int finishNum, int unFinishNum) {
        int year = Integer.valueOf(time.substring(0, 4));
        int month = Integer.valueOf(time.substring(5, 7));
        int day = Integer.valueOf(time.substring(8, 10));
        // LogUtil.e("year="+year+"\tmonth="+month+"\tday="+day);
        float ratio = finishNum * 1.0f / listNum;
        int status;
        if (ratio >= 0.9f) {
            status = DayStatus.GOOD;
        } else if (ratio >= 0.6f) {
            status = DayStatus.ORDINARY;
        } else if (ratio >= 0.4f) {
            status = DayStatus.NOT_WELL;
        } else{
            status = DayStatus.BAD;
    }
        return new DayStatus(time,status,ratio,listNum,finishNum,unFinishNum,year,month,day);
    }
    public static ContentValues getListItemCV(PlanListItem listItem){
        ContentValues values=new ContentValues();
        values.put("content",listItem.getContent());
        values.put("status",listItem.getStatus());
        values.put("time",listItem.getTime());
        return values;
    }
    public static ContentValues generateCV(String content, int status){
        ContentValues values=new ContentValues();
        values.put("content",content);
        values.put("status",status);
        return values;
    }
    public static ContentValues generateCV(int status){
        ContentValues values=new ContentValues();
        values.put("status",status);
        return values;
    }

    /*
    /**
     *  Memo
          //has changed to firebase

    public static ContentValues getMemoCV(Memo memo){
        ContentValues values=new ContentValues();
        values.put("title",memo.getTitle());
        values.put("content",memo.getContent());
        values.put("time",memo.getTime());
        return values;
    }

    public static ContentValues generateMemoCV(String title, String content){
        ContentValues values=new ContentValues();
        values.put("title",title);
        values.put("content",content);
        return values;
    }
*/
}
