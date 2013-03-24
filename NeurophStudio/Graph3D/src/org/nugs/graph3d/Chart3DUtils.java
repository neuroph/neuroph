/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.graph3d;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;

/**
 *
 * @author vedrana
 */
public class Chart3DUtils {

    /**
     * Opens chart in Chart launcher
     *
     * @param chart Chart
     * @return void
     */
    public static void openInChartLauncher(Chart chart) { //prvo nije najlakse, pokazacu ti sto, a drugo ja sam stavila da prima panel pa si me ti ispravio, a evo sto nije najlakse
        ChartLauncher.openChart(chart);
    }

    /**
     * Create a new JPanel Chart
     *
     * @param chart Chart
     * @param panel JPanel
     * @return void
     */
    public static void createChartPanel(Chart chart,JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add((Component) chart.getCanvas());
    }
}
