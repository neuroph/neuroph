package org.neuroph.netbeans.main.easyneurons.samples.perceptron;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import javax.swing.JPanel;

/**
 *
 * @author Marko
 */
public class PatternSpacePanel extends JPanel {

    public final static double EE = 1e-10;
    private PerceptronSampleFrameTopComponent perceptronSampleView;
    private int x1, x2, y1, y2;
    private int width = 200;
    private int height = 200;
    private Image offImage;
    private Graphics offGraphics;

    public PatternSpacePanel(PerceptronSampleFrameTopComponent pa, int w, int h) {
        super();
        perceptronSampleView = pa;
        width = w;
        height = h;
        setSize(width, height);
    }

    @Override
    public void paintComponent(Graphics g) {

        if (offImage == null) {
            offImage = createImage(width, height);   // izicijalizacija Image objekta
        }

        offGraphics = offImage.getGraphics();       // uzimanje grafike za crtanje po Imageu

        offGraphics.translate(50, 50);

        // Erase the previous image and reset the drawing colour
        offGraphics.setColor(getBackground());
        offGraphics.fillRect(-50, -50, width, height);

        offGraphics.setColor(Color.black);

        // Draw vertical axis and label
        drawArrow(offGraphics, 0, 125, 0, -25);
        offGraphics.setColor(Color.BLACK);
        offGraphics.drawString("x2", -20, -10);

        // Draw horizontal axis and label - perceptronSampleView
        drawArrow(offGraphics, -25, 100, 125, 100);
        offGraphics.drawString("x1", 110, 120);


        // Draw the 4 points (green if on, red if off)
        if (perceptronSampleView.outputs[0][0] > 0) {
            offGraphics.setColor(Color.GREEN);
            offGraphics.fillArc(-5, 95, 10, 10, 0, 360);
            offGraphics.drawArc(-5, 95, 10, 10, 0, 360);
        } else {
            offGraphics.setColor(Color.RED);
            offGraphics.fillArc(-5, 95, 10, 10, 0, 360);
            offGraphics.drawArc(-5, 95, 10, 10, 0, 360);
        }

        if (perceptronSampleView.outputs[1][0] > 0) {
            offGraphics.setColor(Color.green);
            offGraphics.fillArc(95, 95, 10, 10, 0, 360);
            offGraphics.drawArc(95, 95, 10, 10, 0, 360);
        } else {
            offGraphics.setColor(Color.RED);
            offGraphics.fillArc(95, 95, 10, 10, 0, 360);
            offGraphics.drawArc(95, 95, 10, 10, 0, 360);
        }


        if (perceptronSampleView.outputs[2][0] > 0) {
            offGraphics.setColor(Color.green);
            offGraphics.fillArc(-5, -5, 10, 10, 0, 360);
            offGraphics.drawArc(-5, -5, 10, 10, 0, 360);
        } else {
            offGraphics.setColor(Color.red);
            offGraphics.fillArc(-5, -5, 10, 10, 0, 360);
            offGraphics.drawArc(-5, -5, 10, 10, 0, 360);
        }
        offGraphics.setColor(Color.BLACK);


        if (perceptronSampleView.outputs[3][0] > 0) {
            offGraphics.setColor(Color.green);
            offGraphics.fillArc(145 - 50, 45 - 50, 10, 10, 0, 360);
            offGraphics.drawArc(145 - 50, 45 - 50, 10, 10, 0, 360);
        } else {
            offGraphics.setColor(Color.red);
            offGraphics.fillArc(145 - 50, 45 - 50, 10, 10, 0, 360);
            offGraphics.drawArc(145 - 50, 45 - 50, 10, 10, 0, 360);
        }
        offGraphics.setColor(Color.BLACK);

        // Draw the new line(s)
        drawLine(offGraphics);


        g.drawImage(offImage, 0, 0, 200, 200, this);    //crtanje grafike na ekranu


    }

    public void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        Polygon p;  // Polygon used for arrow head

        p = new Polygon();
        p.addPoint(x2, y2);

        // Vertical arrow
        if (x2 == x1) {
            if (y2 > y1) {
                p.addPoint(x2 - 5, y2 - 10);
                p.addPoint(x2 + 5, y2 - 10);
            } else {
                p.addPoint(x2 - 5, y2 + 10);
                p.addPoint(x2 + 5, y2 + 10);
            }
        } // Horizontal arrow
        else if (y2 == y1) {
            if (x2 > x1) {
                p.addPoint(x2 - 10, y2 - 5);
                p.addPoint(x2 - 10, y2 + 5);
            } else {
                p.addPoint(x2 + 10, y2 - 5);
                p.addPoint(x2 + 10, y2 + 5);
            }
        } // Now we need to rotate the arrow head about the origin
        else {
            // Calculate the angle of rotation and adjust for the quadrant
            double t1 = Math.abs(new Integer(y2 - y1).doubleValue());
            double t2 = Math.abs(new Integer(x2 - x1).doubleValue());
            double theta = Math.atan(t1 / t2);
            if (x2 < x1) {
                if (y2 < y1) {
                    theta = Math.PI + theta;
                } else {
                    theta = -(Math.PI + theta);
                }
            } else if (x2 > x1 && y2 < y1) {
                theta = 2 * Math.PI - theta;
            }
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);

            // Create the other points and translate the arrow to the origin
            Point p2 = new Point(-10, -5);
            Point p3 = new Point(-10, +5);

            // Rotate the points (without using matrices!)
            int x = new Long(Math.round((cosTheta * p2.x) - (sinTheta * p2.y))).intValue();
            p2.y = new Long(Math.round((sinTheta * p2.x) + (cosTheta * p2.y))).intValue();
            p2.x = x;
            x = new Long(Math.round((cosTheta * p3.x) - (sinTheta * p3.y))).intValue();
            p3.y = new Long(Math.round((sinTheta * p3.x) + (cosTheta * p3.y))).intValue();
            p3.x = x;

            // Translate back to desired location and add to polygon
            p2.translate(x2, y2);
            p3.translate(x2, y2);
            p.addPoint(p2.x, p2.y);
            p.addPoint(p3.x, p3.y);
        }
        g.fillPolygon(p);
        g.drawLine(x1, y1, x2, y2);
    }

    public void updatePoints() {
      //  update(getGraphics());
        repaint();
    }

    public void updateLine(double w0, double w1, double w2) {
        calculateLineCoords(w0, w1, w2);
        //update(getGraphics());
         repaint();
    }

    private void drawLine(Graphics g) {
        g.setColor(Color.blue);
        g.drawLine(x1, y1, x2, y2);
    }

    public void calculateLineCoords(double w0, double w1, double w2) {
        if (Math.abs(w2) < EE) {
            try {
                x1 = (int) ((((w0 * (-1)) / w1) * 100) + 50);     
            } catch (ArithmeticException e) {
            }
            x2 = x1;                              //System.out.println("  x2 = "+ x2);
            y1 = 0;
            y2 = height;                 
        } else {
            // Calulate y (x2) value at x1 points on canvas boundary
            x1 = -50; //convertX(-1);                    
            x2 = 150; //convertX(2);                     

            double Yv1 = 100 - (((w0 - w1 * (-0.5)) / w2) * 100);
            double Yv2 = 100 - (((w0 - w1 * 1.5) / w2) * 100);   

            y1 = (int) Yv1;
            y2 = (int) Yv2;  
        }
    }
}
