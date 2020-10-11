package com.example.assignment2.rxRetrofit;

import android.util.Log;

import com.example.assignment2.Model.Memo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Utility {
    public static String handleUser(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.get("code").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handleToken(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.get("token").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handleState(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.get("state").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String handleMemo(List<Memo> memos, String response) {
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("memo");
            for (int i = 0; i < jsonArray.length(); i++) {
                String MessageContent = jsonArray.getJSONObject(i).toString();
                Log.d("util",MessageContent);
                Memo m = new Gson().fromJson(MessageContent, Memo.class);
                memos.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
