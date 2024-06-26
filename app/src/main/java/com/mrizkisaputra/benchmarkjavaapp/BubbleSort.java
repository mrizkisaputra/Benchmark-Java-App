package com.mrizkisaputra.benchmarkjavaapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BubbleSort {
    public static JSONArray sort(JSONArray data) {
        Log.i("SEBELUM DI SORTING", data.toString());
        int n = data.length();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                try {
                    JSONObject jsonObject1 = data.getJSONObject(j);
                    JSONObject jsonObject2 = data.getJSONObject(j + 1);
                    if (jsonObject1.getInt("nomorAntrian") > jsonObject2.getInt("nomorAntrian")) {
                        data.put(j, jsonObject2); // Menukar elemen secara ascending
                        data.put(j + 1, jsonObject1);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return data;
    }
}
