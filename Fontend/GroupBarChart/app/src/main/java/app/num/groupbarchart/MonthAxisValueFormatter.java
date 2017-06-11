package app.num.groupbarchart;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Datnt on 6/1/2017.
 */

public class MonthAxisValueFormatter implements IAxisValueFormatter {

    protected String[] mMonths = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
    };

    private BarLineChartBase<?> chart;

    public MonthAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mMonths[(int) value];
    }
}
