/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.datasetgen;

/**
 * @author Milos Randjic
 * @param <C>
 */
public class Point <C>{
    

    private double[] values;
    
    private C category;

    public Point(double ... values) {
        this.values = values;
    }
    
    public C getCategory() {
        return category;
    }

    public void setCategory(C category) {
        this.category = category;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    
}
