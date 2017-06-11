package app.num.linechart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LineChart lineChart = (LineChart) findViewById(R.id.chart);


        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));


        LineDataSet lineDataSet1;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            lineDataSet1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            lineDataSet1.setValues(entries);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            lineDataSet1 = new LineDataSet(entries, "The year 2017");

            lineDataSet1.setDrawIcons(false);
            lineDataSet1.setDrawValues(false);


            lineDataSet1.setColors(parseColor("#CC9ddeed"));

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(lineDataSet1);

            LineData data = new LineData(dataSets);
            data.setValueTextSize(10f);

            lineChart.setData(data);
        }
        lineChart.animateY(5000);

//        IAxisValueFormatter xAxisFormatter = new MonthAxisValueFormatter(lineChart);
//
//
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTextSize(10f);
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(7);
//        xAxis.setTextColor(Color.BLACK);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(true);
//        xAxis.setTextColor(parseColor("#39b54a"));
//        xAxis.setValueFormatter(xAxisFormatter);


//        IAxisValueFormatter custom = new MyAxisValueFormatter();
//
        YAxis left = lineChart.getAxisLeft();
//        left.setDrawLabels(true);
//        left.setAxisMinimum(0f);
//        left.setDrawAxisLine(false); // no axis line
//        left.setDrawGridLines(true);
//        left.setTextColor(parseColor("#adadad"));
//        left.setGridColor(parseColor("#39b54a"));
        lineChart.getAxisRight().setEnabled(false); // no right axis
//        left.setValueFormatter(custom);

    }
}
