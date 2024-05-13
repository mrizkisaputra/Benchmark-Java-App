package com.mrizkisaputra.benchmarkjavaapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuickSort {
    // metode untuk menemukan posisi partisi
    private static int partition(JSONArray data, int low, int high) throws JSONException {
        // Pilih elemen pivot
        JSONObject pivot = data.getJSONObject(high);
        int pivotValue = pivot.getInt("nomorAntrian");
        int i = (low - 1); // Indeks dari elemen yang lebih kecil
        for (int j = low; j < high; j++) {
            // Jika elemen saat ini lebih kecil atau sama dengan pivot
            if (data.getJSONObject(j).getInt("nomorAntrian") <= pivotValue) {
                i++;

                // Tukar arr[i] dan arr[j]
                JSONObject temp = data.getJSONObject(i);
                data.put(i, data.getJSONObject(j));
                data.put(j, temp);
            }
        }

        // Tukar arr[i+1] dan arr[high] (atau pivot)
        JSONObject temp = data.getJSONObject(i + 1);
        data.put(i + 1, data.getJSONObject(high));
        data.put(high, temp);

        return i + 1;
    }

    public static void sort(JSONArray data, int low, int high) {
        if (low < high) {
            try {
                int pi = partition(data, low, high); // Membagi array dan mendapatkan indeks pivot

                // Memanggil quickSort() rekursif untuk dua subarray sebelum dan setelah pivot
                sort(data, low, pi - 1);
                sort(data, pi + 1, high);
            } catch (JSONException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
