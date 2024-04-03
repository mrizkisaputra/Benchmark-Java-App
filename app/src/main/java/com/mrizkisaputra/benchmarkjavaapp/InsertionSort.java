package com.mrizkisaputra.benchmarkjavaapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InsertionSort {
    public static JSONArray sort(JSONArray data) {
        Log.i("SEBELUM DI SORTING", data.toString());
        int n = data.length();
        for (int i = 1; i < n; i++) {
            try {
                JSONObject key = data.getJSONObject(i);
                int j = i - 1;

                while (j >= 0 && key.getInt("nomorAntrian") < data.getJSONObject(j).getInt("nomorAntrian")) {
                    data.put(j+1, data.getJSONObject(j));
                    --j;
                }
                data.put(j+1, key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }
}
