package com.mrizkisaputra.benchmarkjavaapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GrafikExecutionTimeActivity extends AppCompatActivity {
    private LineChart lineChart;
    private TextView algorithmTextView;
    private TextView totalInputDataTextView;
    private TextView averageMillisecondTextView;
    private TextView averageSecondTextView;

    private void initComponents() {
        lineChart = findViewById(R.id.lineChart);
        algorithmTextView = findViewById(R.id.algorithm);
        totalInputDataTextView = findViewById(R.id.totalInputData);
        averageMillisecondTextView = findViewById(R.id.averageMillisecond);
        averageSecondTextView = findViewById(R.id.averageSecond);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grafik_execution_time);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();

        // Atur properti grafik
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        Description description = new Description();
        description.setText("Millisecond");
        description.setPosition(150f, 15f);
        lineChart.setDescription(description);
        lineChart.getAxisRight().setDrawLabels(false);
        YAxis axisLeft = lineChart.getAxisLeft();
        axisLeft.setAxisMinimum(0f);
        axisLeft.setAxisLineWidth(3f);
        axisLeft.setAxisLineColor(Color.BLACK);
        axisLeft.setLabelCount(12);

        ArrayList<Long> executionTimes = (ArrayList<Long>) getIntent().getSerializableExtra("EXTRA_EXECUTION_TIMES");
        String iteration = getIntent().getStringExtra("EXTRA_ITERATION");
        String algorithm = getIntent().getStringExtra("EXTRA_ALGORITHM");
        Integer size = getIntent().getIntExtra("EXTRA_TOTAL_DATA", 0);
        if (executionTimes != null) {
            double total = 0;
            for (long time : executionTimes) {
                total += time;
            }
            double averagesMillis = total / executionTimes.size();
            double averagesSecond = averagesMillis / executionTimes.size() / 1000;
            averageMillisecondTextView.setText("Rata-rata : "+averagesMillis+" ms");
            averageSecondTextView.setText("Rata-rata : "+averagesSecond+" second");
            setDataLineChart(executionTimes);
        }
        algorithmTextView.setText("Algoritma : "+algorithm);
        totalInputDataTextView.setText("Jumlah data : "+size);
    }

    void setDataLineChart(List<Long> executionTimes) {
        List<Entry> entries = new ArrayList<>();
        List<String> valueBottom = new ArrayList<>();

        for (int i = 0; i < executionTimes.size(); i++) {
            // Tambahkan setiap nilai ke dalam Entry
            entries.add(new Entry(i, executionTimes.get(i)));
            valueBottom.add(""+i+1);
        }

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(valueBottom));
        xAxis.setLabelCount(entries.size());
        xAxis.setGranularity(1f);

        LineDataSet executionTime = new LineDataSet(entries, "Waktu Eksekusi");
        executionTime.setColor(Color.BLUE);
        executionTime.setValueTextColor(Color.RED);

        LineData lineData = new  LineData(executionTime);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}