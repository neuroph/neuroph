package org.nugs.graph3d;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;

/**
 *
 * @author Vedrana Gajic
 */
public class Chart3DUtils {

    /**
     * Opens chart in Chart launcher (with possibility to zoom etc.)
     *
     * @param chart Chart
     * @return void
     */
    public static void openInChartLauncher(Chart chart) {
        ChartLauncher.openChart(chart);
    }

    /**
     * Place a chart on the panel
     *
     * @param chart Chart
     * @param panel JPanel
     * @return void
     */
    public static void addChartToPanel(Chart chart, JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add((Component) chart.getCanvas());
    }
}
