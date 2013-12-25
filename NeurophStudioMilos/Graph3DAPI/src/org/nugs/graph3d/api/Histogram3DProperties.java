package org.nugs.graph3d.api;

/**
 *
 * @author Vedrana Gajic
 */
public class Histogram3DProperties extends Chart3DProperties {

    private float radius = 0.35f;
    public Histogram3DProperties() {
    }
    

    public Histogram3DProperties(float radius) {
        this.radius = radius;
    }

    /**
     * Setter for radius of bars
     *
     * @param radius
     * @return void
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
}
