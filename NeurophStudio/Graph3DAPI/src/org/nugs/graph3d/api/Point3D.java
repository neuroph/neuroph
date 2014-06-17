/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.graph3d.api;

/**
 *
 * @author zoran / 
 * @author Milos Randjic
 */
public class Point3D {

    public static class Float {

        private float x;
        private float y;
        private float z;

        public Float(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getZ() {
            return z;
        }

        public void setZ(float z) {
            this.z = z;
        }

        public void setLocation(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static class Double {

        private double x;
        private double y;
        private double z;

        public Double(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public void setLocation(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
    
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
