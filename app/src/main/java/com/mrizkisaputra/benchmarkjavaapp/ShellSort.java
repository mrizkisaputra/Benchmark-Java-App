package com.mrizkisaputra.benchmarkjavaapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShellSort {
    public static JSONArray sort(JSONArray data) {
        Log.i("SEBELUM DI SORTING", data.toString());
        int n = data.length();
        for (int interval = n / 2; interval > 0; interval /= 2) {
            for (int i = interval; i < n; i++) {
                try {
                    JSONObject key = data.getJSONObject(i);
                    int j = i;

                    while (j >= interval && data.getJSONObject(j-interval).getInt("nomorAntrian") > key.getInt("nomorAntrian")) {
                        data.put(j, data.getJSONObject(j - interval));
                        j = j - interval;
                    }
                    data.put(j, key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}
