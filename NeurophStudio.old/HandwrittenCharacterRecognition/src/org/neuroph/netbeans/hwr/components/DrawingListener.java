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

package org.neuroph.netbeans.hwr.components;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

/**
 * This class represents the listener for the drawing events
 *
 * @author Damir Kocic
 */
public class DrawingListener extends MouseInputAdapter {

    private DrawingPanel dp;
    private Point start;

    public DrawingListener(DrawingPanel dp) {
        this.dp = dp;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        start = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        dp.draw(start, p);
        start = p;
    }
}
