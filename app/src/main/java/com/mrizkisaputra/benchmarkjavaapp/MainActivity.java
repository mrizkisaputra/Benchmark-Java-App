package com.mrizkisaputra.benchmarkjavaapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Long> executionTimes;
    private ArrayAdapter<String> executionTimesAdapter;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    private MaterialAutoCompleteTextView optionAlgorithmMaterialAutoComplete;
    private TextInputLayout inputLayoutAlgorithmToBenchmark;
    private TextInputLayout inputLayoutIteration;
    private TextInputEditText iterationsEditText;
    private Button startBenchmarkButton;
    private Button lineChartExecutionTimeButton;
    private ProgressBar benchmarkProgressBar;
    private ListView listview;

    private String algorithm;
    private String iteration;

    private JSONArray data;

    private void initComponents() {
        optionAlgorithmMaterialAutoComplete = findViewById(R.id.algorithmToBenchmark);
        inputLayoutAlgorithmToBenchmark = findViewById(R.id.inputLayoutAlgorithmToBenchmark);
        iterationsEditText = findViewById(R.id.iterations);
        inputLayoutIteration = findViewById(R.id.inputLayoutIteration);
        startBenchmarkButton = findViewById(R.id.startBenchmark);
        lineChartExecutionTimeButton = findViewById(R.id.lineChartExecutionTime);
        benchmarkProgressBar = findViewById(R.id.benchmarkProgress);
        listview = findViewById(R.id.listview);
    }

    private JSONArray readJsonFromAsset(String algorithm) {
        InputStream inputStream = null;
        try {
            if (Objects.equals(algorithm, "Insertion Sorting") || Objects.equals(algorithm, "Bubble Sorting")) {
                inputStream = getAssets().open("bubble_insertion.json"); // Membaca file JSON
            } else {
                inputStream = getAssets().open("quick_shell.json"); // Membaca file JSON
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String json = stringBuilder.toString();
            return new JSONArray(json);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearBenchmarkResults() {
        executionTimes.clear();
        executionTimesAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();

        executionTimes = new ArrayList<>();
        executionTimesAdapter = new ArrayAdapter<>(this, R.layout.item_list, R.id.text);
        listview.setAdapter(executionTimesAdapter);

        startBenchmarkButton.setOnClickListener(this);
        lineChartExecutionTimeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.startBenchmark) {
            clearBenchmarkResults();
            this.algorithm = optionAlgorithmMaterialAutoComplete.getText().toString();
            this.iteration = iterationsEditText.getText().toString();
            if (algorithm.isEmpty()) {
                inputLayoutAlgorithmToBenchmark.setError("please select one algorithm");
                return;
            } else {
                inputLayoutAlgorithmToBenchmark.setError(null);
            }

            if (iteration.isEmpty()) {
                inputLayoutIteration.setError("iteration is not be empty");
                return;
            } else {
                inputLayoutIteration.setError(null);
            }

            executor.execute(() -> {
                handler.post(() -> {
                    benchmarkProgressBar.setVisibility(View.VISIBLE);
                    startBenchmarkButton.setText("Benchmark in progress...");
                    startBenchmarkButton.setEnabled(false);
                    lineChartExecutionTimeButton.setVisibility(View.GONE);
                });
                switch (algorithm) {
                    case "Bubble Sorting":
                        startBenchmarkBubbleSort(Integer.valueOf(iteration));
                        break;
                    case "Insertion Sorting":
                        startBenchmarkInsertionSort(Integer.valueOf(iteration));
                        break;
                    case "Quick Sorting":
                        startBenchmarkQuickSort(Integer.valueOf(iteration));
                        break;
                    case "Shell Sorting":
                        startBenchmarkShellSort(Integer.valueOf(iteration));
                        break;
                }
                handler.post(() -> {
                    benchmarkProgressBar.setVisibility(View.GONE);
                    startBenchmarkButton.setText("Start Benchmark");
                    startBenchmarkButton.setEnabled(true);
                    lineChartExecutionTimeButton.setVisibility(View.VISIBLE);
                });
            });
        } else if (v.getId() == R.id.lineChartExecutionTime) {
            Intent intent = new Intent(this, GrafikExecutionTimeActivity.class);
            intent.putExtra("EXTRA_EXECUTION_TIMES", executionTimes);
            intent.putExtra("EXTRA_ITERATION", iteration);
            intent.putExtra("EXTRA_ALGORITHM", algorithm);
            intent.putExtra("EXTRA_TOTAL_DATA", data.length());
            startActivity(intent);
        }
    }

    private void startBenchmarkBubbleSort(Integer iteration) {
        for (int i = 1; i <= iteration; i++) {
            data = readJsonFromAsset(algorithm);
            long start = System.currentTimeMillis();
            JSONArray sorted = BubbleSort.sort(data);
            long measureTimeMills = System.currentTimeMillis() - start;
            executionTimes.add(measureTimeMills); // menyimpan data execution time
            final int iterationIndex = i;
            handler.post(() -> {
                executionTimesAdapter.add("Pengujian iterasi " + iterationIndex + " data berhasil diurutkan");
            });
            Log.i("SETELAH DI SORTING", String.valueOf(sorted));
        }
    }

    private void startBenchmarkInsertionSort(Integer iteration) {
        for (int i = 1; i <= iteration; i++) {
            data = readJsonFromAsset(algorithm);
            long start = System.currentTimeMillis();
            JSONArray sorted = InsertionSort.sort(data);
            long measureTimeMills = System.currentTimeMillis() - start;
            executionTimes.add(measureTimeMills); // menyimpan data execution time
            final int iterationIndex = i;
            handler.post(() -> {
                executionTimesAdapter.add("Pengujian iterasi " + iterationIndex + " data berhasil diurutkan");
            });
            Log.i("SETELAH DI SORTING", String.valueOf(sorted));
        }
    }

    private void startBenchmarkShellSort(Integer iteration) {
        for (int i = 1; i <= iteration; i++) {
            data = readJsonFromAsset(algorithm);
            long start = System.currentTimeMillis();
            JSONArray sorted = ShellSort.sort(data);
            long measureTimeMills = System.currentTimeMillis() - start;
            executionTimes.add(measureTimeMills); // menyimpan data execution time
            final int iterationIndex = i;
            handler.post(() -> {
                executionTimesAdapter.add("Pengujian iterasi " + iterationIndex + " data berhasil diurutkan");
            });
            Log.i("SETELAH DI SORTING", String.valueOf(sorted));
        }
    }

    private void startBenchmarkQuickSort(Integer iteration) {
        for (int i = 1; i <= iteration; i++) {
            data = readJsonFromAsset(algorithm);
            Log.i("SEBELUM DI SORTING", data.toString());
            long start = System.currentTimeMillis();
            QuickSort.sort(data, 0, data.length() - 1);
            long measureTimeMills = System.currentTimeMillis() - start;
            executionTimes.add(measureTimeMills); // menyimpan data execution time
            final int iterationIndex = i;
            handler.post(() -> {
                executionTimesAdapter.add("Pengujian iterasi " + iterationIndex + " data berhasil diurutkan");
            });
            Log.i("SETELAH DI SORTING", String.valueOf(data));
        }
    }


}