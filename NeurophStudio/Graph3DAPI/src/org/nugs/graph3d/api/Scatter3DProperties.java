package org.nugs.graph3d.api;

//import org.jzy3d.maths.Range;

/**
 *
 * @author Vedrana Gajic
 */
public class Scatter3DProperties extends Chart3DProperties {

    private Range xRange = new Range(0, 5);
    private Range yRange = new Range(0, 5);
    private Range zRange = new Range(0, 5);
    private float dotSize = 1;

    
    public Scatter3DProperties() {
    }
    
    public Scatter3DProperties(Range xRange, Range yRange, float dotSize) {
        this.xRange = xRange;
        this.yRange = yRange;
        this.dotSize = dotSize;
    }
    
    public Scatter3DProperties(Range xRange, Range yRange, Range zRange, float dotSize) {
        this.xRange = xRange;
        this.yRange = yRange;
        this.zRange = zRange;
        this.dotSize = dotSize;
    }

    public Range getxRange() {
        return xRange;
    }

    public void setxRange(Range xRange) {
        this.xRange = xRange;
    }

    public Range getyRange() {
        return yRange;
    }

    public void setyRange(Range yRange) {
        this.yRange = yRange;
    }
    
    public Range getzRange() {
        return zRange;
    }

    public void setzRange(Range zRange) {
        this.zRange = zRange;
    }

    public float getDotSize() {
        return dotSize;
    }

    public void setDotSize(float dotSize) {
        this.dotSize = dotSize;
    }

}
