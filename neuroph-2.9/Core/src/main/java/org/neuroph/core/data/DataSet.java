/**
 * Copyright 2014 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.neuroph.core.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.util.DataSetColumnType;
import org.neuroph.util.data.sample.Sampling;
import org.neuroph.util.data.sample.SubSampling;

/**
 * This class represents a collection of data rows (DataSetRow instances) used
 * for training and testing neural network.
 * TODO: add logging
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see DataSetRow
 * http://openforecast.sourceforge.net/docs/net/sourceforge/openforecast/DataSet.html
 */
public class DataSet implements List<DataSetRow>, Serializable { // implements 

    /**
     * The class fingerprint that is set to indicate serialization compatibility
     * with a previous version of the class
     */
    private static final long serialVersionUID = 2L;
    /**
     * Collection of data rows
     */
    private List<DataSetRow> rows;

    /**
     * Size of the input vector in data set rows
     */
    private int inputSize = 0;

    /**
     * Size of output vector in data set rows
     */
    private int outputSize = 0;

    /**
     * Column names/labels
     */
    private String[] columnNames;

    /**
     * Flag which indicates if this data set containes data rows for supervised training
     */
    private boolean isSupervised = false;

    /**
     * Label for this training set
     */
    private String label;

    /**
     * Full file path including file name
     */
    private transient String filePath;
    
    /**
     * Column types for data set
     */
    private DataSetColumnType[] columnTypes;

    /**
     * Creates an instance of new empty training set
     *
     * @param inputSize
     */
    public DataSet(int inputSize) {
        this.rows = new ArrayList();
        this.inputSize = inputSize;
        this.isSupervised = false;
        //this.columnNames = new String[inputSize];
        setDefaultColumnNames();
        setDefaultColumnTypes();
    }

    /**
     * Creates an instance of new empty training set
     *
     * @param inputSize  Length of the input vector
     * @param outputSize Length of the output vector
     */
    public DataSet(int inputSize, int outputSize) {
        this.rows = new ArrayList();
        this.inputSize = inputSize; // > 0
        this.outputSize = outputSize; // > 0
        this.isSupervised = true;
      //  this.columnNames = new String[inputSize + outputSize];
        setDefaultColumnNames();
        setDefaultColumnTypes();
    }

    /**
     * Adds new row row to this data set
     *
     * @param row data set row to add
     */
    public boolean addRow(DataSetRow row)
            throws VectorSizeMismatchException {

        if (row == null) {
            throw new IllegalArgumentException("Data set row cannot be null!");
        }

        // check input vector size if it is predefined
        if ((this.inputSize != 0)
                && (row.getInput().length != this.inputSize)) {
            throw new VectorSizeMismatchException("Input vector size does not match data set input size!");
        }

        if ((this.outputSize != 0)
                && (row.getDesiredOutput().length != this.outputSize)) {
            throw new VectorSizeMismatchException("Output vector size does not match data set output size!");
        }

        // if everything was ok add training row
        return rows.add(row);
    }

    /**
     * Adds a new dataset row with specified input
     *
     * @param input
     */
    public void addRow(double[] input) {
        if (input == null)
            throw new IllegalArgumentException("Input for dataset row cannot be null!");

        if (input.length != inputSize)
            throw new NeurophException("Input size for given row is different from the data set size!");

        if (isSupervised)
            throw new NeurophException("Cannot add unsupervised row to supervised data set!");

        this.addRow(new DataSetRow(input));
    }

    /**
     * Adds a new dataset row with specified input and output
     *
     * @param input
     * @param output
     */
    public void addRow(double[] input, double[] output) {
        this.addRow(new DataSetRow(input, output));
    }

    /**
     * Removes training row at specified index position
     *
     * @param idx position of row to remove
     */
    public void removeRowAt(int idx) {
        this.rows.remove(idx);
    }

    /**
     * Returns Iterator for iterating training elements collection
     *
     * @return Iterator for iterating training elements collection
     */
    public Iterator<DataSetRow> iterator() {
        return this.rows.iterator();
    }

    /**
     * Returns elements of this training set
     *
     * @return training elements
     */
    public List<DataSetRow> getRows() {
        return this.rows;
    }

    /**
     * Returns training row at specified index position
     *
     * @param idx index position of training row to return
     * @return training row at specified index position
     */
    public DataSetRow getRowAt(int idx) {
        return this.rows.get(idx);
    }

    /**
     * Removes all alements from training set
     */
    public void clear() {
        this.rows.clear();
    }

    /**
     * Returns true if training set is empty, false otherwise
     *
     * @return true if training set is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.rows.isEmpty();
    }

    /**
     * Returns true if data set is supervised,  false otherwise
     *
     * @return
     */
    public boolean isSupervised() {
        return this.isSupervised;
    }

    /**
     * Returns number of training elements in this training set set
     *
     * @return number of training elements in this training set set
     */
    @Override
    public int size() {
        return rows.size();
    }

    /**
     * Returns label for this training set
     *
     * @return label for this training set
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets label for this training set
     *
     * @param label label for this training set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public String getColumnName(int idx) {
        return columnNames[idx];
    }

    public void setColumnName(int idx, String columnName) {
        columnNames[idx] = columnName;
    }
    
    public DataSetColumnType[] getColumnTypes() {
        return this.columnTypes;
    }
    
    public DataSetColumnType getColumnType(int index) {
        return this.columnTypes[index];
    }
    
    /**
     * Sets column type for the given index.
     * 
     * @param index Index of the column in the row.
     * @param columnType Column type to set, nominal or numeric.
     */
    public void setColumnType(int index, DataSetColumnType columnType) {
        this.columnTypes[index] = columnType;
    }
    
    /**
     * Sets full file path for this training set
     *
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Returns full file path for this training set
     *
     * @return full file path for this training set
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Returns string representation of this data set
     *
     * @return string representation of this data set
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dataset Label: ").append(label).append(System.lineSeparator());

        if (columnNames != null) {
            sb.append("Columns: ");
            for (String columnName : columnNames) {
                sb.append(columnName).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
            sb.append(System.lineSeparator());
        }

        for (DataSetRow row : rows) {
            sb.append(row).append(System.lineSeparator());
        }

        return sb.toString();
    }

    /**
     * Returns enire dataset in csv format
     *
     * @return
     */
    public String toCSV() {
        StringBuilder sb = new StringBuilder();

        if ((columnNames != null) && (columnNames.length > 0)) {
            for (String columnName : columnNames) {
                sb.append(columnName).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length() - 1);
            sb.append(System.lineSeparator());
        }

        // promeniti
        for (DataSetRow row : rows) {
            sb.append(row.toCSV()); // nije dobro jer lepi input i desired output; treba bez toga mozda dodati u toCSV
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    /**
     * Saves this training set to the specified file
     *
     * @param filePath
     */
    public void save(String filePath) {
        this.filePath = filePath;
        this.save();
    }

    /**
     * Saves this training set to file specified in its filePath field
     */
    public void save() {
        
        if (filePath == null) throw new NeurophException("filePath is null! It must be specified in order to save file!");
        
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filePath)))) {
            out.writeObject(this);
            out.flush();
            out.close();
        } catch (IOException ioe) {
            throw new NeurophException(ioe);
        }
    }

    public void saveAsTxt(String filePath, String delimiter) {

        if (filePath == null) throw new IllegalArgumentException("File path is null!");

        // default delimiter is space if other is not specified
        if ((delimiter == null) || delimiter.equals("")) {
            delimiter = " ";
        }


        try (PrintWriter out = new PrintWriter(new FileWriter(new File(filePath)))) {

            int columnCount = inputSize + outputSize;
            if ((columnNames != null) && (columnNames.length > 0)) {
                for (int i = 0; i < columnNames.length; i++) {
                    out.print(columnNames[i]);
                    if (i < columnCount - 1) out.print(delimiter);
                }
                out.println();
            }

            for (DataSetRow row : this.rows) {
                double[] input = row.getInput();
                for (int i = 0; i < input.length; i++) {
                    out.print(input[i]);
                    if (i < columnCount - 1) out.print(delimiter);
                }

                if (row.isSupervised()) {
                    double[] output = row.getDesiredOutput();
                    for (int j = 0; j < output.length; j++) {
                        out.print(output[j]);
                        if (inputSize + j < columnCount - 1) out.print(delimiter);
                    }
                }
                out.println();
            }

            out.flush();

        } catch (IOException ex) {
            throw new NeurophException("Error saving data set file!", ex);
        }
    }

    /**
     * Loads training set from the specified file
     * TODO:  throw checked exceptionse here
     * 
     * @param filePath training set file
     * @return loded training set
     */
    public static DataSet load(String filePath) {

        try (ObjectInputStream oistream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filePath))) ) {
 
            DataSet dataSet = (DataSet) oistream.readObject();
            dataSet.setFilePath(filePath);

            return dataSet;

        } catch(FileNotFoundException fnfe) {
           throw new NeurophException("Could not find file: '" + filePath + "'!", fnfe); 
        } catch (IOException ioe) {
            throw new NeurophException("Error reading file: '" + filePath + "'!", ioe);
        } catch (ClassNotFoundException ex) {
            throw new NeurophException("Class not found while trying to read DataSet object from the stream!", ex);
        } 
    }

    /**
     * Creates and returns data set from specified csv file
     *
     * @param filePath        path to csv dataset file to import
     * @param inputsCount     number of inputs
     * @param outputsCount    number of outputs
     * @param delimiter       delimiter of values
     * @param loadColumnNames true if csv file contains column names in first line, false otherwise
     * @return instance of dataset with values from specified file
     * 
     * TODO: try with resources, provide information on exact line of error if format is not good in NumberFormatException
     */
    public static DataSet createFromFile(String filePath, int inputsCount, int outputsCount, String delimiter, boolean loadColumnNames) {
       
        if (filePath == null) throw new IllegalArgumentException("File name cannot be null!");
        if (inputsCount <= 0) throw new IllegalArgumentException("Number of inputs cannot be <= 0 : "+inputsCount);
        if (outputsCount < 0) throw new IllegalArgumentException("Number of outputs cannot be < 0 : "+outputsCount);
        if ((delimiter == null) || delimiter.isEmpty())
            throw new IllegalArgumentException("Delimiter cannot be null or empty!");

        try ( BufferedReader reader = new BufferedReader(new FileReader(filePath)) ) {
            DataSet dataSet = new DataSet(inputsCount, outputsCount);
            dataSet.setFilePath(filePath);            

            String line = null;

            if (loadColumnNames) {
                // get column names from the first line
                line = reader.readLine();
                String[] colNames = line.split(delimiter);
                dataSet.setColumnNames(colNames);
            } else {
                dataSet.setDefaultColumnNames();
            }

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(delimiter);

                double[] inputs = new double[inputsCount];
                double[] outputs = new double[outputsCount];

                if (values[0].equals("")) {
                    continue; // skip if line was empty
                }
                for (int i = 0; i < inputsCount; i++) {
                    inputs[i] = Double.parseDouble(values[i]);
                }

                for (int i = 0; i < outputsCount; i++) {
                    outputs[i] = Double.parseDouble(values[inputsCount + i]);
                }

                if (outputsCount > 0) {
                    dataSet.addRow(new DataSetRow(inputs, outputs));
                } else {
                    dataSet.addRow(new DataSetRow(inputs));
                }
            }

            reader.close();
            
            return dataSet;

        } catch (FileNotFoundException ex) {
            throw new NeurophException("Could not find data set file!", ex);
        } catch (IOException ex) {
             throw new NeurophException("Error reading data set file!", ex);
        } catch (NumberFormatException ex) {
             ex.printStackTrace();
            throw new NeurophException("Bad number format in data set file!", ex); // TODO: add line number!
        }

    }

    /**
     * Creates and returns data set from specified csv file
     *
     * @param filePath        path to csv dataset file to import
     * @param inputsCount     number of inputs
     * @param outputsCount    number of outputs
     * @param delimiter       delimiter of values
     * @return instance of dataset with values from specified file
     */    
    public static DataSet createFromFile(String filePath, int inputsCount, int outputsCount, String delimiter) {    
        return createFromFile(filePath, inputsCount, outputsCount, delimiter, false);
    }
    
    
    
    // http://java.about.com/od/javautil/a/uniquerandomnum.htm

    /**
     * Returns training and test subsets in the specified percent ratio
     * @param trainSetPercent
     * @param testSetPercent
     * @return
     */
    public DataSet[] createTrainingAndTestSubsets(int trainSetPercent, int testSetPercent) {
        SubSampling sampling = new SubSampling(trainSetPercent, testSetPercent);
        DataSet[] trainAndTestSet =  new DataSet[2];
        sampling.sample(this).toArray(trainAndTestSet);
        return trainAndTestSet;
    }

    public List<DataSet> split(int ... sizePercents) {
        SubSampling sampling = new SubSampling(sizePercents);
        return sampling.sample(this);    
    }
    

    public List<DataSet> sample(Sampling sampling) {
        return sampling.sample(this);
    }


    /**
     * Returns output vector size of training elements in this training set.
     */
    public int getOutputSize() {
        return this.outputSize;
    }

    /**
     * Returns input vector size of training elements in this training set This
     * method is implementation of EngineIndexableSet interface, and it is added
     * to provide compatibility with Encog data sets and FlatNetwork
     */
    public int getInputSize() {
        return this.inputSize;
    }

    public void shuffle() {
        Collections.shuffle(rows);
    }

    @Override
    public boolean contains(Object o) {
        return rows.contains(o);// TODO: implement equals for DataSetRow
    }

    @Override
    public Object[] toArray() {
       return rows.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return rows.toArray(a);
    }

    @Override
    public boolean add(DataSetRow row) {
        return rows.add(row); // better  call to addRow instead
    }

    @Override
    public boolean remove(Object row) {
        return rows.remove(row);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
       return rows.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends DataSetRow> c) {
        return rows.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends DataSetRow> c) {
        return rows.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return rows.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return rows.retainAll(c);
    }

    @Override
    public DataSetRow get(int index) {
        return rows.get(index);
    }

    @Override
    public DataSetRow set(int index, DataSetRow row) {
        return rows.set(index, row);
    }

    @Override
    public void add(int index, DataSetRow row) {
        rows.add(index, row);
    }

    @Override
    public DataSetRow remove(int index) {
        return rows.remove(index);
    }

    @Override
    public int indexOf(Object row) {
        return rows.indexOf(row);
    }

    @Override
    public int lastIndexOf(Object row) {
        return rows.lastIndexOf(row);
    }

    @Override
    public ListIterator<DataSetRow> listIterator() {
        return rows.listIterator();
    }

    @Override
    public ListIterator<DataSetRow> listIterator(int index) {
        return rows.listIterator(index);
    }

    @Override
    public List<DataSetRow> subList(int fromIndex, int toIndex) {
        return rows.subList(fromIndex, toIndex);
    }

    private void setDefaultColumnNames() {
        columnNames = new String[inputSize + outputSize];
        
        for (int i = 0; i < inputSize; i++) {
            columnNames[i] = "Input" + (i+1);
        }
        for (int i = 0; i < outputSize; i++) {
            columnNames[inputSize + i] = "Output" + (i+1);
        }
    }
    
    private void setDefaultColumnTypes() {
        columnTypes = new DataSetColumnType[inputSize + outputSize];
        for (int i = 0; i < inputSize; i++) {
            columnTypes[i] = DataSetColumnType.NUMERIC;
        }
        for (int i = 0; i < outputSize; i++) {
            columnTypes[inputSize + i] = DataSetColumnType.NUMERIC;
        }
    }
    
}