package org.neuroph.netbeans.stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.neuroph.netbeans.stock.wizard.TransferFunction;
import org.openide.util.Exceptions;

/**
 *
 * @author Tomek
 */
public class StockPredictionManager {

    public StockPredictionManager() {
        stockData = new ArrayList<Double>(); 
    }

    private List<Double> stockData;
    private double normalizationScale;

    private static StockPredictionManager instance;
    public static StockPredictionManager getInstance(){
        if(instance == null) instance = new StockPredictionManager();
        return instance;
    }

    // load data from file
    public void loadStockData(String filePath, String delimiter) {
        try {
            stockData.clear();
            if (delimiter.isEmpty()) {
                delimiter = " ";
            }           
            
         String line = "";
         FileReader fileReader = new FileReader(new File(filePath));
         BufferedReader reader = new BufferedReader(fileReader);
          
          while((line = reader.readLine())!=null) {
            String[] values = line.split(delimiter);

            if (values[0].equals("")) continue; // skip if line was empty

            for (int i = 0; i < values.length; i++) {
                if (values[i].isEmpty()) continue;
                stockData.add( new Double( Double.parseDouble(values[i])) );
            }

          }
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } 
      
    }

    public List<Double> getStockData() {
        return stockData;
    }

    public double getNormalizationScale() {
        return normalizationScale;
    }

    public void setNormalizationScale(double normalizationScale) {
        this.normalizationScale = normalizationScale;
    }
    
    

    // remove this from here
    public List<TransferFunction> getTransferFunctions() {
        List<TransferFunction> listOfFunctions = new ArrayList<TransferFunction>();
        listOfFunctions.add(new TransferFunction("Sigmoid"));
        listOfFunctions.add(new TransferFunction("Tanh"));
        
        return listOfFunctions;
    }

}
