/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.rightclick;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.files.tset.DataSetDataObject;
import org.neuroph.util.DataSetColumnType;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "BpelNodes",
        id = "org.neuroph.netbeans.rightclick.DataSetNominalColumnsAction"
)
@ActionRegistration(
        displayName = "#CTL_DataSetNominalColumnsAction"
)
@ActionReference(path = "Loaders/text/x-tset/Actions", position = 200)
@Messages("CTL_DataSetNominalColumnsAction=Set nominal columns")
public final class DataSetNominalColumnsAction implements ActionListener {

    private final DataSetDataObject context;

    public DataSetNominalColumnsAction(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        DataSet dataSet = context.getDataSet();

        String initialValue = getNominalColumns(dataSet);

        String nominalColumns = (String) JOptionPane.showInputDialog(
                null,
                "Enter nominal column indeces",
                "Nominal columns",
                JOptionPane.PLAIN_MESSAGE,
                null, null, initialValue);

        if (!"".equals(nominalColumns)) {
            setNominalColumns(dataSet, nominalColumns);
        }
    }

    private String getNominalColumns(DataSet dataSet) {
        DataSetColumnType[] columnTypes = dataSet.getColumnTypes();
        StringBuilder sb = new StringBuilder();
        boolean isNominalColumnFound = false;
        for (int i = 0; i < columnTypes.length; i++) {
            if (columnTypes[i] == DataSetColumnType.NOMINAL) {
                if (isNominalColumnFound) {
                    sb.append(", ");
                }
                sb.append(i);
                isNominalColumnFound = true;
            }
        }
        return sb.toString();
    }

    private void setNominalColumns(DataSet dataSet, String nominalColumns) {
        resetColumnTypes(dataSet);
        DataSetColumnType[] columnTypes = dataSet.getColumnTypes();
        String[] items = nominalColumns.replaceAll("\\s", "").split(",");
        for (String item : items) {
            try {
                int index = Integer.parseInt(item);
                if (index >= 0 && index < columnTypes.length) {
                    dataSet.setColumnType(index, DataSetColumnType.NOMINAL);
                }
            }catch (NumberFormatException nfe) {
                //SKIP
            }
        }
    }
    
    private void resetColumnTypes(DataSet dataSet) {
        DataSetColumnType[] columnTypes = dataSet.getColumnTypes();
        for (int i = 0; i < columnTypes.length; i++) {
            dataSet.setColumnType(i, DataSetColumnType.NUMERIC);
        }
    }
}
