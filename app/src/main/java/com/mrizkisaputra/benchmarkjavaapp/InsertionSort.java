package com.mrizkisaputra.benchmarkjavaapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InsertionSort {
    public static JSONArray sort(JSONArray data) {
        Log.i("SEBELUM DI SORTING", data.toString());
        int n = data.length();
        for (int pass = 1; pass < n; pass++) {
            try {
                JSONObject key = data.getJSONObject(pass);
                int i = pass - 1;
                while (i >= 0 && data.getJSONObject(i).getInt("nomorAntrian") > key.getInt("nomorAntrian")) {
                    data.put(i + 1, data.getJSONObject(i));
                    i = i - 1;
                }
                data.put(i + 1, key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }
}