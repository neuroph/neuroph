/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.datasetgen.shapes;

/**
 *
 * @author Milos Randjic
 */
public class CircleGenerator extends ElipseGenerator {


    public CircleGenerator(int numberOfPoints, double x, double y, double radius) {
        super(numberOfPoints, x, y, Math.sqrt(radius), Math.sqrt(radius));

    }

    @Override
    public String toString() {
        return "Circle";
    }
    
    

}
