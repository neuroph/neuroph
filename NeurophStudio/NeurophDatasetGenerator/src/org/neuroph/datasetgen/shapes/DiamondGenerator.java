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
public class DiamondGenerator extends SquareGenerator {

    public DiamondGenerator(int numberOfPoints, double squareSideSize) {
        super(numberOfPoints, squareSideSize);
    }

    @Override
    protected boolean getCategoryMembership(double randomX, double randomY) {

        double value = 0.75;
//(randomX + randomY) >= value && (randomX + randomY) <= value + 1 && (-randomX + randomY) <= value && (-randomX + randomY) >= -value;
        return (randomX + randomY) <= value && (randomX + randomY) >= -value && (-randomX + randomY) >= -value && (-randomX + randomY) <= value;

    }

    @Override
    public String toString() {
        return "Diamond";
    }
    
}
