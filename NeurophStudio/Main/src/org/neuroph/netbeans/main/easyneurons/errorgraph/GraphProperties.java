/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neuroph.netbeans.main.easyneurons.errorgraph;

import java.awt.Color;
import java.text.DecimalFormat;

/**
 *
 * @author Ivan Trnavac
 */
public class GraphProperties {
    private Color background = Color.white;
    private Color frameColor = Color.pink;
    private Color pointColor = Color.blue;
    private Color lineColor = Color.red;
    private Color axisColor = Color.green.darker();
    private Color gridColor = Color.gray;
    private Color numColor = Color.pink.darker();
    private int westGap = 30;
    private int eastGap = 10;
    private int northGap = 10;
    private int southGap = 25;
    private boolean visibleGrid = true;
    private boolean visiblePoints = true;
    private boolean visibleLine = true;
    private double numPeriodX = 5000;
    private double numPeriodY = 0.5;
    private int fixPixNumPeriodX = 50; 
    private int fixPixNumPeriodY = 20;
    private DecimalFormat decimalFormatX;
    private DecimalFormat decimalFormatY;
    private int decimalX;
    private int decimalY;
    
    public GraphProperties() {
        setDecimals(0, 2);
    }
    
    public int getHGap() {
        return westGap + eastGap;
    }
    
    public int getVGap() {
        return northGap + southGap;
    }
    
    public GraphProperties clone() {
        GraphProperties v = new GraphProperties();
        v.setBackground(background);
        v.setFrameColor(frameColor);
        v.setPointColor(pointColor);
        v.setLineColor(lineColor);
        v.setAxisColor(axisColor);
        v.setNumColor(numColor);
        v.setGridColor(gridColor);
        v.setWestGap(westGap);
        v.setEastGap(eastGap);
        v.setNorthGap(northGap);
        v.setSouthGap(southGap);
        v.setVisibleGrid(visibleGrid);
        v.setVisiblePoints(visiblePoints);
        v.setVisibleLine(visibleLine);
        v.setNumPeriodX(numPeriodX);
        v.setNumPeriodY(numPeriodY);
        v.setDecimalFormatX(decimalFormatX);
        v.setDecimalFormatY(decimalFormatY);
        v.setDecimalX(decimalX);
        v.setDecimalY(decimalY);
        return v;
    }
    
    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getFrameColor() {
        return frameColor;
    }

    public double getNumPeriodX() {
        return numPeriodX;
    }

    public void setNumPeriodX(double numPeriodX) {
        this.numPeriodX = numPeriodX;
    }

    public double getNumPeriodY() {
        return numPeriodY;
    }

    public void setNumPeriodY(double numPeriodY) {
        this.numPeriodY = numPeriodY;
    }

    public void setFrameColor(Color frameColor) {
        this.frameColor = frameColor;
    }

    public Color getPointColor() {
        return pointColor;
    }

    public void setPointColor(Color pointColor) {
        this.pointColor = pointColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(Color axisColor) {
        this.axisColor = axisColor;
    }

    public Color getGridColor() {
        return gridColor;
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
    }

    public Color getNumColor() {
        return numColor;
    }

    public void setNumColor(Color numColor) {
        this.numColor = numColor;
    }

    public int getWestGap() {
        return westGap;
    }

    public void setWestGap(int westGap) {
        this.westGap = westGap;
    }

    public int getEastGap() {
        return eastGap;
    }

    public void setEastGap(int eastGap) {
        this.eastGap = eastGap;
    }

    public int getNorthGap() {
        return northGap;
    }

    public void setNorthGap(int northGap) {
        this.northGap = northGap;
    }

    public int getSouthGap() {
        return southGap;
    }

    public void setSouthGap(int southGap) {
        this.southGap = southGap;
    }

    public boolean isVisibleGrid() {
        return visibleGrid;
    }

    public void setVisibleGrid(boolean visibleGrid) {
        this.visibleGrid = visibleGrid;
    }

    public boolean isVisiblePoints() {
        return visiblePoints;
    }

    public void setVisiblePoints(boolean visiblePoints) {
        this.visiblePoints = visiblePoints;
    }

    public boolean isVisibleLine() {
        return visibleLine;
    }

    public void setVisibleLine(boolean visibleLine) {
        this.visibleLine = visibleLine;
    }    

    public int getDecimalX() {
        return decimalX;
    }

    public void setDecimalX(int decimalX) {
        this.decimalFormatX = getDecimalFormat(decimalX);
        this.decimalX = decimalX;
    }

    public int getDecimalY() {
        return decimalY;
    }

    public void setDecimalY(int decimalY) {
        this.decimalFormatY = getDecimalFormat(decimalY);
        this.decimalY = decimalY;
    }

    public void setDecimals(int dx, int dy) {
        setDecimalX(dx);
        setDecimalY(dy);
    }
    
    public DecimalFormat getDecimalFormatX() {
        return decimalFormatX;
    }

    public void setDecimalFormatX(DecimalFormat decimalFormatX) {
        this.decimalFormatX = decimalFormatX;
    }

    public DecimalFormat getDecimalFormatY() {
        return decimalFormatY;
    }

    public void setDecimalFormatY(DecimalFormat decimalFormatY) {
        this.decimalFormatY = decimalFormatY;
    }
    
    private DecimalFormat getDecimalFormat(int decimal) {
        String s = "#,##0";
        if (decimal > 0) {
            s += ".";
            for (int i = 0; i < decimal; i++) {
                s = s + "0";
            }
        }
        return new DecimalFormat(s);
    }

    public int getFixPixNumPeriodX() {
        return fixPixNumPeriodX;
    }

    public void setFixPixNumPeriodX(int fixPixNumPeriodX) {
        this.fixPixNumPeriodX = fixPixNumPeriodX;
    }

    public int getFixPixNumPeriodY() {
        return fixPixNumPeriodY;
    }

    public void setFixPixNumPeriodY(int fixPixNumPeriodY) {
        this.fixPixNumPeriodY = fixPixNumPeriodY;
    }
}
