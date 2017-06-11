package app.num.groupbarchart;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import static android.graphics.Color.parseColor;
import static app.num.groupbarchart.R.id.chart;

public class MainActivity extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barChart = (BarChart) findViewById(chart);
        barChart.setFitBars(true);
        barChart.setBackgroundColor(Color.TRANSPARENT);

        Description description = new Description();
        description.setText("");
        description.setTextColor(parseColor("#adadad"));
        barChart.setDescription(description);

        ArrayList<BarEntry> group1 = new ArrayList<>();
        group1.add(new BarEntry(4f, 7.5f));
        group1.add(new BarEntry(6f, 1));


        BarDataSet barDataSet1;
        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            barDataSet1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            barDataSet1.setValues(group1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            barDataSet1 = new BarDataSet(group1, "The year 2017");

            barDataSet1.setDrawIcons(false);
            barDataSet1.setDrawValues(false);


            barDataSet1.setColors(parseColor("#CC9ddeed"));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(barDataSet1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            barChart.setData(data);
        }

        //show animation
        barChart.animateXY(2000, 2000);
        barChart.invalidate();

        //hide legend
        barChart.getLegend().setEnabled(false);

        IAxisValueFormatter xAxisFormatter = new MonthAxisValueFormatter(barChart);


        XAxis xAxis = barChart.getXAxis();
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

        YAxis left = barChart.getAxisLeft();
        left.setDrawLabels(true);
        left.setAxisMinimum(0f);
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(true);
        left.setTextColor(parseColor("#adadad"));
        left.setGridColor(parseColor("#39b54a"));
        barChart.getAxisRight().setEnabled(false); // no right axis
        left.setValueFormatter(custom);

    }
}
