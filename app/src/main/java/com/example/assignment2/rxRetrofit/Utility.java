package com.example.assignment2.rxRetrofit;

import org.json.JSONObject;

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
}
