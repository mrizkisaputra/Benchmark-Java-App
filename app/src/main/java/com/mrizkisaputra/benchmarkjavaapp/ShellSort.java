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
                    JSONObject temp = data.getJSONObject(i);
                    int j;
                    for (j = i; j >= interval && data.getJSONObject(j - interval).getInt("nomorAntrian") > temp.getInt("nomorAntrian"); j -= interval) {
                        data.put(j, data.getJSONObject(j - interval));
                    }
                    data.put(j, temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
}
