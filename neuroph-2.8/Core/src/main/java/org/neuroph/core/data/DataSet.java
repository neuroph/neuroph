/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.core.data.norm.MaxNormalizer;
import org.neuroph.core.data.norm.Normalizer;
import org.neuroph.core.data.sample.Sampling;
import org.neuroph.core.data.sample.SubSampling;

/**
 * This class represents a collection of data rows (DataSetRow instances) used
 * for training and testing neural network.
 *
 * @author Zoran Sevarac <sevarac@gmail.com>
 * @see DataSetRow
 * http://openforecast.sourceforge.net/docs/net/sourceforge/openforecast/DataSet.html
 */
public class DataSet implements Serializable /*
 * , EngineIndexableSet
 */ {

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
    }

    /**
     * Creates an instance of new empty training set
     *
     * @param inputSize Length of the input vector
     * @param outputSize Length of the output vector
     */
    public DataSet(int inputSize, int outputSize) {
        this.rows = new ArrayList();
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.isSupervised = true;
    }

    /**
     * Adds new row row to this data set
     *
     * @param row data set row to add
     */
    public void addRow(DataSetRow row)
            throws VectorSizeMismatchException {
        
        if (row == null) {
                    throw new NeurophException("Training dta row cannot be null!");
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

        // if everything went ok add training row
        this.rows.add(row);
    }

    public void addRow(double[] input) {
        this.addRow(new DataSetRow(input));
    }

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
    
    public void setColumnName(String columnName, int idx) {
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
     * Returns label of this training set
     *
     * @return label of this training set
     */
    @Override
    public String toString() {  
        StringBuilder sb = new StringBuilder();
        sb.append("Dataset").append(System.lineSeparator());
        sb.append("Label:").append(label).append(System.lineSeparator());
               
        if (columnNames != null) {
            sb.append("Columns: ");
            for(String columnName : columnNames) {
                sb.append(columnName).append(", ");
            }
            sb.delete(sb.length()-2, sb.length()-1);
            sb.append(System.lineSeparator());
        }    
        
        sb.append("Rows:");
        for(DataSetRow row : rows) {
            sb.append(row);
            sb.append(System.lineSeparator());
        }
        
        return sb.toString();
    }
    
    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        
        if (columnNames != null) {
            for(String columnName : columnNames) {
                sb.append(columnName).append(", ");
            }
            sb.delete(sb.length()-2, sb.length()-1);
            sb.append(System.lineSeparator());
        }
        
        sb.append("Rows:");
        for(DataSetRow row : rows) {
            sb.append(row);
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

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
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
        if ((delimiter == null) || delimiter.equals("")) {
            delimiter = " ";
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(new File(filePath)))) {

            for (DataSetRow row : this.rows) {
                double[] input = row.getInput();
                for (int i = 0; i < input.length; i++) {
                    out.print(input[i] + delimiter);
                }

                if (row.isSupervised()) {
                    double[] output = row.getDesiredOutput();
                    for (int j = 0; j < output.length; j++) {
                        out.print(output[j] + delimiter);
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

    public static DataSet createFromFile(String filePath, int inputsCount, int outputsCount, String delimiter) {
        FileReader fileReader = null;

        try {
            DataSet dataSet = new DataSet(inputsCount, outputsCount);
            fileReader = new FileReader(new File(filePath));
            BufferedReader reader = new BufferedReader(fileReader);

            String line = "";

            while ((line = reader.readLine()) != null) {
                double[] inputs = new double[inputsCount];
                double[] outputs = new double[outputsCount];
                String[] values = line.split(delimiter);

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

            return dataSet;

        } catch (FileNotFoundException ex) {
           throw new NeurophException("Could not find data set file!", ex);
        } catch (IOException ex) {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex1) {
                }
            }
            throw new NeurophException("Error reading data set file!", ex);
        } catch (NumberFormatException ex) {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex1) {
                }
            }
            ex.printStackTrace();
            throw new NeurophException("Bad number format in data set file!", ex);
        }

    }

    public void normalize() {
        this.normalize(new MaxNormalizer());
    }

    public void normalize(Normalizer normalizer) {
        normalizer.normalize(this);
    }

    /**
     * Returns output vector size of training elements in this training set This
     * method is implementation of EngineIndexableSet interface, and it is added
     * to provide compatibility with Encog data sets and FlatNetwork
     */
//    @Override
//    public int getIdealSize() {
//        return this.outputVectorSize;
//    }
//    public void setInputSize(int inputVectorSize) {
//        this.inputSize = inputVectorSize;
//    }
//
//    public void setOutputSize(int outputVectorSize) {
//        this.outputSize = outputVectorSize;
//    }

    // http://java.about.com/od/javautil/a/uniquerandomnum.htm
    /**
     * 
     * @param trainSetPercent
     * @param testSetPercent
     * @return 
     * @deprecated Use sample(int size) instead
     */
    public DataSet[] createTrainingAndTestSubsets(int trainSetPercent, int testSetPercent) {
        DataSet[] trainAndTestSet = new DataSet[2];

        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            randoms.add(i);
        }

        Collections.shuffle(randoms);

        // create training set
        trainAndTestSet[0] = new DataSet(inputSize, outputSize);
        int trainingElementsCount = this.size() * trainSetPercent / 100;
        for (int i = 0; i < trainingElementsCount; i++) {
            int idx = randoms.get(i);
            trainAndTestSet[0].addRow(this.rows.get(idx));
        }


        // create test set
        trainAndTestSet[1] = new DataSet(inputSize, outputSize);
        int testElementsCount = this.size() - trainingElementsCount;
        for (int i = 0; i < testElementsCount; i++) {
            int idx = randoms.get(trainingElementsCount + i);
            trainAndTestSet[1].addRow(this.rows.get(idx));
        }

        return trainAndTestSet;
    }
    
    public DataSet[] sample(int percent) {
        Sampling sampling = new SubSampling(percent);
        return sampling.sample(this);
    }

    public DataSet[] sample(Sampling sampling) {
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
    /**
     * Returns true if training set contains supervised training elements This
     * method is implementation of EngineIndexableSet interface, and it is added
     * to provide compatibility with Encog data sets and FlatNetwork
     */
//    @Override
//    public boolean isSupervised() {
//        return this.outputVectorSize > 0;
//    }
    /**
     * Gets training data/record at specified index position. This method is
     * implementation of EngineIndexableSet interface. It is added for
     * Encog-Engine compatibility.
     */
//    @Override
//    public void getRecord(long index, EngineData pair) {
//        EngineData item = this.elements.get((int) index);
//        pair.setInputArray(item.getInputArray());
//        pair.setIdealArray(item.getIdealArray());
//    }
    /**
     * Returns training elements/records count This method is implementation of
     * EngineIndexableSet interface. It is added for Encog-Engine compatibility.
     */
//    @Override
//    public long getRecordCount() {
//        return this.elements.size();
//    }
    /**
     * This method is implementation of EngineIndexableSet interface, and it is
     * added to provide compatibility with Encog data sets and FlatNetwork.
     *
     * Some datasets are not memory based, they may make use of a SQL connection
     * or a binary flat file. Because of this these datasets need to be cloned
     * for multi-threaded training or performance will greatly suffer. Because
     * this is a memory-based dataset, no cloning takes place and the "this"
     * object is returned.
     */
//    @Override
//    public EngineIndexableSet openAdditional() {
//        return this;
//    }
}