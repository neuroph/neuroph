package org.nugs.graph3d;

/**
 *
 * @author vedrana
 */
public class Hist3DProperties extends Chart3DProperties{


    private float radius = 0.45f;


    public Hist3DProperties() {
    }

 

    /**
     * Setter for radius of bars
     * @param radius
     * @return void
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Returns the radius of bars
     * @return float
     */
    public float getRadius() {
        return radius;
    }

}
