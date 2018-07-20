/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.LSTM;

import java.util.HashMap;
import java.util.Map;
import org.neuroph.core.data.DataSet;

/**
 *
 * @author manoj.kumar
 */
public class TextEncoder {
    
    public static HashMap<String, Double> getEncoding(String[] data){
        long intvl =1;// Integer.MAX_VALUE / data.length;//This to ensure max sepration
        HashMap<String, Double> unique = new HashMap<>();

        for (int i = 0; i < data.length; i++) {  //This is a limitation to int.max words
            unique.put(data[i], new Double(i));
        }
        
         int j = 0;
        for (Map.Entry<String, Double> entry : unique.entrySet()) {
            entry.setValue(round((double)(intvl * j++)/(double)unique.size(),5));
            //System.out.println("Key = "+entry.getKey()+", Value = " + entry.getValue());
        }
        
        return unique;
    }
    
    public static HashMap< Double, String> getDecoding(HashMap<String, Double> unique) {
        HashMap<Double, String> decode = new HashMap<>();

        unique.entrySet().forEach((entry) -> {
            decode.put(entry.getValue(), entry.getKey());
            //System.out.println("Key = "+entry.getKey()+"  Value = " + entry.getValue());
        });

        return decode;

    }
    /**
     * Generate the encoded data.
     * Limitation is at int.max number of words.
     *
     * @param data
     * @return
     */
    public static double[] generateCategory(String[] data) {
        HashMap<String, Double> unique = getEncoding(data);

        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {  //This is a limitation to int.max words
            result[i] = unique.get(data[i]);
        }

        return result;
    }

    public static double[] generateCategory(String[] data,HashMap<String, Double> unique) {

        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {  //This is a limitation to int.max words
            result[i] = unique.get(data[i])!=null?unique.get(data[i]):-1;
        }

        return result;
    }
    
    public static double[] generateCategory(String data) {
        return generateCategory(data.replaceAll("\n", "").replaceAll(",", "").replace(".", " .").split(" "));
    }

    
    public static double[] generateCategory(String data,HashMap<String, Double> unique) {
        return generateCategory(data.replaceAll("\n", "").replaceAll(",", "").replace(".", " .").split(" "),unique);
    }
    
    public static DataSet getDataSet(String data, int sequenceCount,int outSequence) {
        double catData[] = generateCategory(data);
        DataSet readData = new DataSet(sequenceCount, outSequence);

        for (int i = 0; i < catData.length - sequenceCount; i++) {
            double[] rowData = new double[sequenceCount];
            for (int j = 0; j < rowData.length; j++) {
                rowData[j] = catData[i + j]; //RNN data
            }
             double[] outData = new double[outSequence];
            for (int j = 0; j < outData.length; j++) {
                outData[j] = catData[i +sequenceCount+ j]; //RNN data
            }
            
            readData.addRow(rowData,outData); //GMV
        }
        

        return readData;
    }
    
    public static DataSet getDataSet(String data,HashMap<String, Double> unique, int sequenceCount,int outSequence) {
        double catData[] = generateCategory(data,unique);
        DataSet readData = new DataSet(sequenceCount, outSequence);

        for (int i = 0; i < catData.length - sequenceCount-outSequence; i++) {
            double[] rowData = new double[sequenceCount];
            for (int j = 0; j < rowData.length; j++) {
                rowData[j] = catData[i + j]; //RNN data
            }
            
            double[] outData = new double[outSequence];
            for (int j = 0; j < outData.length; j++) {
                outData[j] = catData[i +sequenceCount+ j]; //RNN data
            }
            
            readData.addRow(rowData,outData); //GMV
        }

        return readData;
    }
    
    public static double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();

    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
}
    
    public static String findClosestMatch(HashMap< Double, String> decode, double myNumber) {
        double distance = Integer.MAX_VALUE;
        int idx = 0;
        String match = "#";
        int c = 0;
        for (Map.Entry<Double, String> entry : decode.entrySet()) {
            //decode.put(entry.getValue(), entry.getKey());
           // System.out.println("Key = " + entry.getKey() + "  Value = " + entry.getValue());

            double cdistance =  Math.abs(entry.getKey() - myNumber);
            if (cdistance < distance) {
                idx = c;
                distance = cdistance;
                match = entry.getValue();
            }
            c++;
        }

        return match;
    }
    
    
}
