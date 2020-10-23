package com.example.assignment2.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.assignment2.MyApplication;
import com.example.assignment2.Utils.DataUtil;
import com.example.assignment2.Utils.LogUtil;
import com.example.assignment2.Model.DayStatus;

import java.util.Calendar;
import java.util.Date;

public class DayStatusDao {
    private static MyOpenHelper dbHelper=new MyOpenHelper(MyApplication.getContext(),"list.db",null,4);
    private static Calendar calendar= Calendar.getInstance();
    public static long insertDayStatus(DayStatus dayStatus){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        ContentValues values= DataUtil.getDayStatusCV(dayStatus);
        return database.insert("DayStatus",null,values);
    }
    public static int queryStatus(String time){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("DayStatus",new String[]{"status"},"time=?", new String[]{time},null,null,null);
        int status=-1;
        while(cursor.moveToNext()){
            status=cursor.getInt(cursor.getColumnIndex("status"));
        }
        cursor.close();
        return status;
    }
    public static int[] queryMonthStatus(Date date){
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        int dayNum=calendar.get(Calendar.DAY_OF_MONTH);
        int recordDay=0,goodDay=0,ordinaryDay=0,badDay=0,notwellDay=0,notRecordDay=0;
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("DayStatus",null,"year=? and month=?",new String[]{year+"",month+""},null,null,null);
        float bestRatio=0,worstRatio=1;
        while(cursor.moveToNext()){
            recordDay+=1;
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            float ratio=cursor.getFloat(cursor.getColumnIndex("ratio"));
          //  LogUtil.e("day="+day);
            if(ratio>bestRatio){
                bestRatio=ratio;
            }
            if(ratio<worstRatio){
                worstRatio=ratio;
            }
            if(status == DayStatus.GOOD){
                goodDay+=1;
            }else if(status == DayStatus.ORDINARY){
                ordinaryDay+=1;
            }else if(status == DayStatus.BAD){
                badDay+=1;
            }
            else if(status == DayStatus.NOT_WELL){
                notwellDay+=1;
            }
        }
        cursor.close();
        notRecordDay=dayNum-recordDay;
        int[]monthSort=new int[7];
        monthSort[0]=dayNum;
        monthSort[1]=recordDay;
        monthSort[2]=goodDay;
        monthSort[3]=ordinaryDay;
        monthSort[4]=badDay;
        monthSort[5]=notRecordDay;
        monthSort[6]=notwellDay;
        return monthSort;
    }
}
