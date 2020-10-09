package com.example.assignment2.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.assignment2.MyApplication;
import com.example.assignment2.Utils.DataUtil;
import com.example.assignment2.Utils.LogUtil;
import com.example.assignment2.bean.DayStatus;
import com.example.assignment2.bean.PlanListItem;

import java.util.ArrayList;
import java.util.List;

public class PlanListItemDao {
    private static MyOpenHelper dbHelper=new MyOpenHelper(MyApplication.getContext(),"list.db",null,4);
    public static long insertListItem(PlanListItem listItem){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        long id=database.insert("ListItem",null, DataUtil.getListItemCV(listItem));
        return id;
    }
    public static DayStatus updateNoRecord(String time){
        LogUtil.e("我是真正的，开始更新后台数据了");
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("ListItem",null,"time=? and status!=?",new String[]{time,"101"},null,null,"l_id asc");
        int recordNum=0,finishNum=0,unFinishNum=0;
        while(cursor.moveToNext()){
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            recordNum+=1;
            if(status==PlanListItem.NO_RECORD){
                long id=cursor.getInt(cursor.getColumnIndex("l_id"));
                updateItem(id,PlanListItem.UNFINISH);
                unFinishNum+=1;
            }else if(status==PlanListItem.UNFINISH){
                unFinishNum+=1;
            }else if(status==PlanListItem.FINISH){
                finishNum+=1;
            }
        }
        cursor.close();
        if(recordNum==0)
            return null;
        return DataUtil.getDayStatus(time,recordNum,finishNum,unFinishNum);
    }
    public static List<PlanListItem> queryAllItems(String time){
        List<PlanListItem> list=new ArrayList<>();
        PlanListItem listItem;
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("ListItem",null,"time=?",new String[]{time},null,null,"l_id asc");
        while(cursor.moveToNext()){
            long id=cursor.getLong(cursor.getColumnIndex("l_id"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            listItem=new PlanListItem(content,status,time);
            listItem.setId(id);
            list.add(listItem);
        }
        cursor.close();
        return list;
    }
    public static List<PlanListItem> queryAllItemsExceptNoContent(String time){
        List<PlanListItem> list=new ArrayList<>();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.query("ListItem",null,"time=? and status!=?",new String[]{time,"101"},null,null,"l_id asc");
        PlanListItem listItem;
        while(cursor.moveToNext()){
            long id=cursor.getLong(cursor.getColumnIndex("l_id"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            int status=cursor.getInt(cursor.getColumnIndex("status"));
            listItem=new PlanListItem(content,status,time);
            listItem.setId(id);
            list.add(listItem);
        }
        cursor.close();
        return  list;
    }
    public static void updateItem(long id, int status, String content){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=DataUtil.generateCV(content,status);
        db.update("ListItem",values,"l_id=?",new String[]{id+""});
    }
    public static void updateItem(long id,int status){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues values=DataUtil.generateCV(status);
        db.update("ListItem",values,"l_id=?",new String[]{id+""});
    }
    public static void deleteItem(long id){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.delete("ListItem","l_id=?",new String[]{id+""});
    }
}
