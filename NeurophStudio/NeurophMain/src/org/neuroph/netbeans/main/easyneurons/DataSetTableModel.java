package org.neuroph.netbeans.main.easyneurons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class DataSetTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	public int HIDDEN_INDEX;
	
     protected ArrayList<String> columnIdentifiers;
     protected ArrayList<ArrayList>dataVector;

	public DataSetTableModel() {
		columnIdentifiers = new ArrayList<String>();
		dataVector = new ArrayList<ArrayList>();
                setDefaultTableValues();
	}
	
	
	public DataSetTableModel(DataSet dataSet) {
		columnIdentifiers = new ArrayList<String>();
		dataVector = new ArrayList<ArrayList>();
				                
                HIDDEN_INDEX = dataSet.getInputSize();

		String[] colNames =  dataSet.getColumnNames();
                
                if (colNames.length > 0) {
                   columnIdentifiers = new ArrayList<String>(Arrays.asList(colNames));
                } else {
                    for (int i = 0; i < dataSet.getInputSize(); i++) {
                        columnIdentifiers.add("Input " + (i + 1));
                    }

                    if (dataSet.isSupervised()) {
                        for (int i = 0; i < dataSet.getOutputSize(); i++) {
                            columnIdentifiers.add("Output " + (i + 1));
                        }
                    }

                }
                
                 if (dataSet.isSupervised()) {
                       HIDDEN_INDEX += dataSet.getOutputSize();
                 }
                 
                 columnIdentifiers.add(""); // hidden col

                
		// columns = inputs + outputs
		Iterator<DataSetRow> iterator = dataSet.iterator();
		while (iterator.hasNext()) {
			DataSetRow trainingElement = iterator.next();
                        //inputVector = VectorParser.convertToVector(trainingElement.getInput());
			double[] inputVector = trainingElement.getInput();

			ArrayList rowVector = new ArrayList();
                        for(double d : inputVector) {
                            rowVector.add(d);
                        }

			if (trainingElement.isSupervised()) {				
                                //outputVector = VectorParser.convertToVector(((DataSetRow) trainingElement)
				//		.getDesiredOutput());
				double[] outputVector = ((DataSetRow) trainingElement)
						.getDesiredOutput();
                                for(double d : outputVector) {
                                rowVector.add(d);
                            }
			}
			
			rowVector.add(new String());

			dataVector.add(rowVector);
		}
	}

	public DataSetTableModel(int inputs, int outputs) {
		columnIdentifiers = new ArrayList<String>();
		dataVector = new ArrayList<ArrayList>();
		
		HIDDEN_INDEX = inputs + outputs;
		
		for (int i = 0; i < inputs; i++) {
			this.columnIdentifiers.add("Input " + (i + 1));
		}

		for (int j = 0; j < outputs; j++) {
			this.columnIdentifiers.add("Output " + (j + 1));
		}
		this.columnIdentifiers.add(""); // hidden col

                setDefaultTableValues();
		
	}
	
	@Override
	public String getColumnName(int column) {
		return this.columnIdentifiers.get(column);
	}	
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == HIDDEN_INDEX) return false;
		else return true;
	}
	
	@Override
	public Class getColumnClass(int column) {
		return Object.class;
	}	

        @Override
	public Object getValueAt(int row, int column) {
		return dataVector.get(row).get(column);
	}	
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		ArrayList rowVector = dataVector.get(row);
		rowVector.set(column, value);
		fireTableCellUpdated(row, column);
	}	

        @Override
	public int getRowCount() {
		return dataVector.size();
	}

        @Override
	public int getColumnCount() {
		return columnIdentifiers.size();
	}	
	

	public void addEmptyRow() {
		int columns = columnIdentifiers.size();
		ArrayList<String> newRow = new ArrayList<String>();
		for (int c = 0; c < columns; c++) {
			newRow.add(new String());
		}		
		
		dataVector.add(newRow);
		fireTableRowsInserted(
			dataVector.size() - 1,
			dataVector.size() - 1);
	}
	
	public boolean hasEmptyRow() {
		if (dataVector.isEmpty()) {
                    return false;
                  }
		ArrayList rowVector = dataVector.get(dataVector.size() - 1);
	 
		Iterator i=rowVector.iterator();
		while(i.hasNext()) {
			if (!i.next().toString().trim().equals("")) {
                           return false;
                        }
		}
	
		return true;
	}	
	
	public ArrayList<ArrayList> getDataVector() {
		return dataVector;
	}
		
	public void removeLastEmptyRow() {
		dataVector.remove(dataVector.size() - 1);
		fireTableRowsInserted(
			dataVector.size() - 1,
			dataVector.size() - 1);		
	}
	
	public void removeRow(int row) {
		if (row < 0 || row > getRowCount()){ //may need to be >=
			return;
		}
           
		dataVector.remove(row);
		fireTableRowsDeleted(row, row);
	}

        //id dataVectorIsEmpty it adds a default values in table
        private void setDefaultTableValues(){
            int columns = columnIdentifiers.size();
            System.out.println("za dattable: "+dataVector.isEmpty());
            if(dataVector.isEmpty()){
		ArrayList<Double> newRow = new ArrayList<Double>();
		for (int c = 0; c < columns; c++) {
			newRow.add(0.0);
		}
                dataVector.add(newRow);
            }
        }

}