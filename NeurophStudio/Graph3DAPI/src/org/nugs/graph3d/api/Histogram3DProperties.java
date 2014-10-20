package org.nugs.graph3d.api;

/**
 *
 * @author Vedrana Gajic
 */
public class Histogram3DProperties extends Chart3DProperties {

    private float radius = 0.35f;
    private int maxBarsSize;
    private int numberOfBarRows;
    
    public Histogram3DProperties() {
    }
    

    public Histogram3DProperties(float radius) {
        this.radius = radius;
    }

    /**
     * Setter for radius of bars
     *
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Returns the radius of bars
     *
     * @return float
     */
    public float getRadius() {
        return radius;
    }

    public int getMaxBarsSize() {
        return maxBarsSize;
    }

    public void setMaxBarsSize(int maxBarsSize) {
        this.maxBarsSize = maxBarsSize;
    }

    public int getNumberOfBarRows() {
        return numberOfBarRows;
    }

    public void setNumberOfBarRows(int numberOfBarRows) {
        this.numberOfBarRows = numberOfBarRows;
    }
}
