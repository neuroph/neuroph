/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.ConnectionWidget;

/**
 *
 * @author Maja
 */
public class RouterConnection implements Router {

    Point first;
    Point last;
    private static final int MAX_DISTANCE_UP = 33;
    private static final int MAX_DISTANCE_DOWN = 40;
    private static final int MIN_DISTANCE_UP = 26;
    private static final int MIN_DISTANCE_DOWN = 26;

    public RouterConnection(Point first, Point last) {
        this.first = first;
        this.last = last;
    }

    public List<Point> routeConnection(ConnectionWidget cw) {
        int firstX = first.x;
        int lastX = last.x;
        int distance = lastX - firstX;
        List<Point> points = new ArrayList<Point>();
        
        if(first.equals(last)){
            points.add(new Point(first.x-22, first.y-10));
            points.add(new Point(first.x-37, first.y-10));
            points.add(new Point(first.x-37, first.y+10));
            points.add(new Point(first.x-22, first.y+10));
            return points;
        }
        points.add(first);
        if (Math.abs(distance) > 20) {
            if (firstX < lastX) {
                int height = (int) (distance * (MAX_DISTANCE_UP - MIN_DISTANCE_UP) / 1000);
                points.add(new Point(firstX + distance / 20, first.y - MIN_DISTANCE_UP - height));
                points.add(new Point(lastX - distance / 20, first.y - MIN_DISTANCE_UP - height));
            } else {
                int height = (int) (distance * (MAX_DISTANCE_DOWN - MIN_DISTANCE_DOWN) / 1000);
                points.add(new Point(firstX + distance / 20, first.y + MIN_DISTANCE_DOWN - height));
                points.add(new Point(lastX - distance / 20, first.y + MIN_DISTANCE_DOWN - height));
            }
        }
        points.add(last);
        return points;
    }
}
