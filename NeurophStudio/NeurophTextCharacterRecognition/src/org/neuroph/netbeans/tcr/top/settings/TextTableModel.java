/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.tcr.top.settings;


import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mihailo
 */
public class TextTableModel extends AbstractTableModel{

    private String [] textArray;
    private List<String> imageList;

    public TextTableModel() {
        textArray = new String[0];
        imageList = new ArrayList<String>();
    }
    
    
    
    public int getRowCount() {
        return imageList.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return imageList.get(rowIndex);
        if (columnIndex == 1)
            return textArray[rowIndex];
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1)
            return true;
        return false;
    }

    

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
        textArray = new String[imageList.size()];
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0)
            return "Training image";
        if (column == 1)
            return "Training text";
        return null;
    }
    
    

    void setTextFile(String textFile,int row) {
        textArray[row] = textFile;
        fireTableDataChanged();
    }
        
    public void addImage(String imageName) {
        imageList.add(imageName);
        String [] tmp = new String[textArray.length+1];
        for (int i = 0; i < textArray.length; i++) {
            tmp[i] = textArray[i];
        }
        fireTableDataChanged();
    }
    
}
