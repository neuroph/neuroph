/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.graph3d;

/**
 *
 * @author vedrana
 */
public class Surface3DProperties extends Chart3DProperties{

    private int xSteps = 20;
    private int ySteps = 20;

    public Surface3DProperties() {
    }

  
    /**
     * Returns the function steps of x values
     *
     * @return int
     */
    public int getxSteps() {
        return xSteps;
    }

    /**
     * Setter for the steps of x values
     *
     * @param xSteps function steps for x values
     * @return void
     */
    public void setxSteps(int xSteps) {
        this.xSteps = xSteps;
    }

    /**
     * Returns the function steps of y values
     *
     * @return int
     */
    public int getySteps() {
        return ySteps;
    }

    /**
     * Setter for the steps of y values
     *
     * @param ySteps function steps for y values
     * @return void
     */
    public void setySteps(int ySteps) {
        this.ySteps = ySteps;
    }
}
