/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.tcr.wizards;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Mihailo
 */
public class ImageTableModel extends AbstractTableModel{

    private List<DocumentWrapper> list;

    public ImageTableModel() {
        list = new ArrayList<DocumentWrapper>();
        list.add(new DocumentWrapper());
    }
    
    
    
    public int getRowCount() {
        return list.size();
    }

    public int getColumnCount() {
        return 2;
    }

    
    public Object getValueAt(int rowIndex, int columnIndex) {
        DocumentWrapper dw = list.get(rowIndex);
        if (columnIndex == 0) {
            if (dw.getImage() != null) 
                return dw.getImageName();
            else
                return "";
        }
        if (columnIndex == 1) {
            if (dw.getTextPath() != null) 
                return dw.getTextName();
            else
                return "";
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0)
            return "Image";
        if (column == 1)
            return "Text";
        return null;
    }
    
    
    public void addNewRow() {
        list.add(new DocumentWrapper());
        fireTableDataChanged();
    }
    
    public void updateRowWithImage(int row, String imageName, BufferedImage image) {
        DocumentWrapper dw = list.get(row);
        dw.setImageName(imageName);
        dw.setImage(image);
        fireTableDataChanged();
    }
    
    public void updateRowWithText(int row, String textPath, String textName) {
         DocumentWrapper dw = list.get(row);
        dw.setTextName(textName);
        dw.setTextPath(textPath);
        fireTableDataChanged();
    }

    public List<DocumentWrapper> getList() {
        return list;
    }

    public void deleteRow(int row) {
        list.remove(row);
        fireTableDataChanged();
    }
    
    
}
