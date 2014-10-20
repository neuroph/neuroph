/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.jmevisualization;

import com.jme3.math.ColorRGBA;
import java.util.ArrayList;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author Milos Randjic
 */
public class DataSetVisualizationParameters {
       
    
    private DataSet dataSet;    
    private int[] inputs;//indexes of 3 chosen inputs   
    private ArrayList<ColorRGBA> dominantOutputColors;//calculated output colors for each dataSet row during training iteration

    public DataSetVisualizationParameters(DataSet dataSet, int[] inputs, ArrayList<ColorRGBA> dominantOutputColors) {
        this.dataSet = dataSet;
        this.inputs = inputs;
        this.dominantOutputColors = dominantOutputColors;
    }
    
    public DataSetVisualizationParameters() {
        
    }
    
    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public ArrayList<ColorRGBA> getDominantOutputColors() {
        return dominantOutputColors;
    }

    public void setDominantOutputColors(ArrayList<ColorRGBA> dominantOutputColors) {
        this.dominantOutputColors = dominantOutputColors;
    }

    public int[] getInputs() {
        return inputs;
    }

    public void setInputs(int[] inputs) {
        this.inputs = inputs;
    }

}
