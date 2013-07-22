/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.graph3d.api;

/**
 *
 * @author zoran
 */
public class Point3D {

    double x, y, z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
