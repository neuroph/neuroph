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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.util.data.sample.Sampling;
import org.neuroph.util.data.sample.SubSampling;

/**
 * This class represents a collection of data rows (DataSetRow instances) used
 * for training and testing neural network.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see DataSetRow
 * http://openforecast.sourceforge.net/docs/net/sourceforge/openforecast/DataSet.html
 */
public class DataSet implements Serializable {

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
     * Creates an instance of new empty training set
     *
     * @param inputSize
     */
    public DataSet(int inputSize) {
        this.rows = new ArrayList();
        this.inputSize = inputSize;
        this.isSupervised = false;
        this.columnNames = new String[inputSize];
    }

    /**
     * Creates an instance of new empty training set
     *
     * @param inputSize  Length of the input vector
     * @param outputSize Length of the output vector
     */
    public DataSet(int inputSize, int outputSize) {
        this.rows = new ArrayList();
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.isSupervised = true;
        this.columnNames = new String[inputSize + outputSize];
    }

    /**
     * Adds new row row to this data set
     *
     * @param row data set row to add
     */
    public void addRow(DataSetRow row)
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
        this.rows.add(row);
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
    public int size() {
        return this.rows.size();
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
        ObjectOutputStream out = null;

        try {
            File file = new File(this.filePath);
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(this);
            out.flush();

        } catch (IOException ioe) {
            throw new NeurophException(ioe);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                }
            }
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
     *
     * @param filePath training set file
     * @return loded training set
     */
    public static DataSet load(String filePath) {
        ObjectInputStream oistream = null;

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException("Cannot find file: " + filePath);

            }

            oistream = new ObjectInputStream(new FileInputStream(filePath));
            DataSet dataSet = (DataSet) oistream.readObject();
            dataSet.setFilePath(filePath);

            return dataSet;

        } catch (IOException ioe) {
            throw new NeurophException("Error reading file!", ioe);
        } catch (ClassNotFoundException ex) {
            throw new NeurophException("Class not found while trying to read DataSet object from the stream!", ex);
        } finally {
            if (oistream != null) {
                try {
                    oistream.close();
                } catch (IOException ioe) {
                }
            }
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
        BufferedReader reader = null;

        if (filePath == null) throw new IllegalArgumentException("File name cannot be null!");
        if (inputsCount <= 0) throw new IllegalArgumentException("Number of inputs cannot be <= 0 : "+inputsCount);
        if (outputsCount < 0) throw new IllegalArgumentException("Number of outputs cannot be < 0 : "+outputsCount);
        if ((delimiter == null) || delimiter.isEmpty())
            throw new IllegalArgumentException("Delimiter cannot be null or empty!");

        try {
            DataSet dataSet = new DataSet(inputsCount, outputsCount);
            dataSet.setFilePath(filePath);            
            reader = new BufferedReader(new FileReader(new File(filePath)));

            String line = null;

            if (loadColumnNames) {
                // get column names from the first line
                line = reader.readLine();
                String[] colNames = line.split(delimiter);
                dataSet.setColumnNames(colNames);
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
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex1) {
                }
            }
            throw new NeurophException("Error reading data set file!", ex);
        } catch (NumberFormatException ex) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex1) {
                }
            }
            ex.printStackTrace();
            throw new NeurophException("Bad number format in data set file!", ex);
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

}