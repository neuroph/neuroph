/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.tcr.top.settings;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.neuroph.imgrec.filter.ImageFilter;
import org.neuroph.imgrec.filter.impl.AdaptiveThresholdBinarizeFilter;
import org.neuroph.imgrec.filter.impl.Dilation;
import org.neuroph.imgrec.filter.impl.GrayscaleFilter;
import org.neuroph.imgrec.filter.impl.MeanFilter;
import org.neuroph.imgrec.filter.impl.MedianFilter;
import org.neuroph.imgrec.filter.impl.NormalizationFilter;
import org.neuroph.imgrec.filter.impl.OtsuBinarizeFilter;
import org.neuroph.imgrec.filter.impl.ZhangSuenThinFilter;

/**
 *
 * @author Mihailo
 */
public class FilterTableModel extends AbstractTableModel{

    private List<ImageFilter> filters;

    public FilterTableModel() {
        filters = new ArrayList<ImageFilter>();
        filters.add(new GrayscaleFilter());
        filters.add(new MedianFilter());
        filters.add(new MeanFilter());
        filters.add(new Dilation());
        filters.add(new OtsuBinarizeFilter());
        filters.add(new ZhangSuenThinFilter());
        filters.add(new NormalizationFilter());
        filters.add(new AdaptiveThresholdBinarizeFilter());
    }
    
    
    
    
    
    public int getRowCount() {
        return filters.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return filters.get(rowIndex).toString();
        if (columnIndex == 1) {
            if (rowIndex == 1) {
                MedianFilter mf = (MedianFilter)filters.get(rowIndex);
                return mf.getRadius();
            }
            if (rowIndex == 2) {
                MeanFilter mf = (MeanFilter)filters.get(rowIndex);
                return mf.getRadius();
            }
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            if (rowIndex == 1)
                return true;
            if (rowIndex == 2)
                return true;
        }
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            System.out.println(aValue);
            if (rowIndex == 1) {
                MedianFilter mf = (MedianFilter) filters.get(rowIndex);
                Integer radius = (Integer) aValue;
                mf.setRadius(radius);
            }
            if (rowIndex == 2) {
                MeanFilter mf = (MeanFilter) filters.get(rowIndex);
                Integer radius = (Integer) aValue;
                mf.setRadius(radius);
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0)
            return "Filter";
        if (column == 1)
            return "Radius";
        return null;
    }

  
    public ImageFilter getImageFilter(int index) {
        return filters.get(index);
    }
    
    
    
    
}
