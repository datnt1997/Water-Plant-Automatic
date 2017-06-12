package app.num.linechart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;

public class MainActivity extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = (LineChart) findViewById(R.id.chart);

        //set interaction
        lineChart.setDragEnabled(true);


        //set description
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        //add data to drawn linechart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 20));
        entries.add(new Entry(5f, 40));
        entries.add(new Entry(6f, 80));
        entries.add(new Entry(7f, 40));
        entries.add(new Entry(8f, 20));
        entries.add(new Entry(9f, 90));
        entries.add(new Entry(10f, 20));
        entries.add(new Entry(11f, 40));
        entries.add(new Entry(12f, 80));
        entries.add(new Entry(13f, 40));
        entries.add(new Entry(14f, 20));
        entries.add(new Entry(15f, 90));


        LineDataSet chartDataSet1;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            chartDataSet1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            chartDataSet1.setValues(entries);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            chartDataSet1 = new LineDataSet(entries, "The year 2017");

            chartDataSet1.setDrawIcons(false);
            chartDataSet1.setDrawValues(false);


            chartDataSet1.setColors(parseColor("#CC9ddeed"));
            chartDataSet1.setDrawValues(true);
            chartDataSet1.setLineWidth(2f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(chartDataSet1);

            LineData data = new LineData(dataSets);
            data.setValueTextSize(10f);

            lineChart.setData(data);
        }

        IAxisValueFormatter xAxisFormatter = new MonthAxisValueFormatter(lineChart);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(parseColor("#39b54a"));
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis left = lineChart.getAxisLeft();
        left.setDrawLabels(true);
        left.setAxisMinimum(0f);
        left.setAxisMaximum(100f);
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(true);
        left.setTextColor(parseColor("#adadad"));
        left.setGridColor(parseColor("#39b54a"));
        lineChart.getAxisRight().setEnabled(false); // no right axis
        left.setValueFormatter(custom);

    }
}
