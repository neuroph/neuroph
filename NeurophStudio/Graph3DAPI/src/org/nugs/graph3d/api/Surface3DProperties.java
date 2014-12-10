package org.nugs.graph3d.api;


/**
 *
 * @author Vedrana Gajic
 */
public class Surface3DProperties extends Chart3DProperties {

    private int xSteps = 20;
    private int ySteps = 20;
    private Range xRange = new Range(0, 5);
    private Range yRange = new Range(0, 5);

    public Surface3DProperties() {
    }

    public Surface3DProperties(int xSteps, int ySteps, Range xRange, Range yRange) {
        this.xSteps = xSteps;
        this.ySteps = ySteps;
        this.xRange = xRange;
        this.yRange = yRange;
    }

    /**
     * Returns the function steps of x values
     *
     * @return int
     */
    public int getXSteps() {
        return xSteps;
    }

    /**
     * Setter for the steps of x values
     *
     * @param xSteps function steps for x values
     * @return void
     */
    public void setXSteps(int xSteps) {
        this.xSteps = xSteps;
    }

    /**
     * Returns the function steps of y values
     *
     * @return int
     */
    public int getYSteps() {
        return ySteps;
    }

    /**
     * Setter for the steps of y values
     *
     * @param ySteps function steps for y values
     * @return void
     */
    public void setYSteps(int ySteps) {
        this.ySteps = ySteps;
    }

    /**
     * Getter for the range of the values on x-Axis
     *
     * @return Range
     */
    public Range getXRange() {
        return xRange;
    }

    /**
     * Setter for the range of the values on x-Axis
     *
     * @param xRange (Example: new Range(1,10))
     * @return void
     */
    public void setXRange(Range xRange) {
        this.xRange = xRange;
    }

    /**
     * Getter for the range of the values on y-Axis
     *
     * @return Range
     */
    public Range getYRange() {
        return yRange;
    }

    /**
     * Setter for the range of the values on y-Axis
     *
     * @param yRange (Example: new Range(1,10))
     * @return void
     */
    public void setYRange(Range yRange) {
        this.yRange = yRange;
    }
}
