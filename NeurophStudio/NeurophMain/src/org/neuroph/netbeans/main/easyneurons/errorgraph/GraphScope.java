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

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author Ivan Trnavac
 */
public class GraphScope {
    double pixValueX, pixValueY;
    double maxX, minX;    
    double maxY, minY;    
    
    public GraphScope() {               
        pixValueX = 0.16;
        pixValueY = 0.00525;
        maxX = 100; // 600 oldvalue
        minX =  0;
        maxY = 1.5;      
        minY = 0;    
    }
    
    public GraphScope clone() {
        GraphScope s = new GraphScope();
        s.setPixValueX(pixValueX);
        s.setPixValueY(pixValueY);
        s.setMaxX(maxX);
        s.setMaxY(maxY);
        s.setMinX(minX);
        s.setMinY(minY);
        return s;
    }
    public Point2D getPoint2D(Point p, int westGap, int southGap) {        
        return new Point2D.Double((p.x - westGap - calculateX0())*pixValueX, (p.y - southGap - calculateY0())*pixValueY);
    }
    
    public void crop(Point p1, Point p2, int westGap, int southGap) {
        Point2D max = getPoint2D(p1, westGap, southGap);       
        Point2D min = getPoint2D(p2, westGap, southGap);        
        setMaxX(max.getX());
        setMaxY(max.getY());
        setMinX(min.getX());
        setMinY(min.getY());
    }

    protected void zoomX(double value) {
        pixValueX *= value;
    }
    
    protected void zoomY(double value) {
        pixValueY *= value;
    }
    
    protected void zoom(double valX, double valY) {
        zoomX(valX);
        zoomY(valY);
    }
    
    public void calculatePixelValue(int width, int height) {
        pixValueX = (maxX - minX) / width;
        pixValueY = (maxY - minY) / height;
    }

    public Point getPoint(Point2D point2d, int gapX, int gapY) {
        if (point2d == null) {
            return null;
        }
        if (Double.isNaN(point2d.getX()) || Double.isNaN(point2d.getY())) {
            return null;
        }
        return new Point((int)Math.round(point2d.getX()/pixValueX ) + calculateX0() + gapX, 
                (int)Math.round(point2d.getY()/pixValueY /*+ minY*/) + calculateY0() + gapY);
    }
    
    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getPixValueX() {
        return pixValueX;
    }

    public void setPixValueX(double pixValueX) {
        this.pixValueX = pixValueX;
    }
    
    public double getPixValueY() {
        return pixValueY;
    }

    public void setPixValueY(double pixValueY) {
        this.pixValueY = pixValueY;
    }
    //********************************************************/
    public int calculateHeight() {       
        return (int) ((maxY - minY) / pixValueY);
    }

    public int calculateWidth() {
        return (int) ((maxX - minX) / pixValueX);// + 1;
    }
    
    public int calculateY0() {
        return (int) Math.round(-minY / pixValueY);// *+ 1;
    }
    
    public int calculateX0() {
        return (int) Math.round(-minX / pixValueX);// + 1;
    }            
}
