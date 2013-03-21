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
package org.neuroph.core.learning;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.neuroph.core.exceptions.NeurophException;
import org.neuroph.core.exceptions.VectorSizeMismatchException;
import org.neuroph.util.norm.MaxNormalizer;
import org.neuroph.util.norm.Normalizer;

/**
 * A set of training elements for training neural network.
 *
 * @author Zoran Sevarac <sevarac@gmail.com> extends AbstractCollection or
 * Implements Collection
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
    private int inputSize = 0;
    private int outputSize = 0;
    private String[] columnNames;
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
     * Adds new row element to this data set
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

        // if everything went ok add training element
        this.rows.add(row);
    }

    public void addRow(double[] input) {
        this.addRow(new DataSetRow(input));
    }

    public void addRow(double[] input, double[] output) {
        this.addRow(new DataSetRow(input, output));
    }

    /**
     * Removes training element at specified index position
     *
     * @param idx position of element to remove
     */
    public void removeRowAt(int idx) {
        this.rows.remove(idx);
    }

    /**
     * Returns Iterator for iterating training elements collection
     *
     * @return Iterator for iterating training elements collection
     */
    public Iterator iterator() {
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
     * Returns training element at specified index position
     *
     * @param idx index position of training element to return
     * @return training element at specified index position
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
        return this.label;
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

        PrintWriter out = null;

        try {
            out = new PrintWriter(new FileWriter(new File(filePath)));

            for (DataSetRow element : this.rows) {
                double[] input = element.getInput();
                for (int i = 0; i < input.length; i++) {
                    out.print(input[i] + delimiter);
                }

                if (element instanceof DataSetRow) {
                    double[] output = ((DataSetRow) element).getDesiredOutput();
                    for (int j = 0; j < output.length; j++) {
                        out.print(output[j] + delimiter);
                    }
                }
                out.println();
            }

            out.flush();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
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
            DataSet tSet = (DataSet) oistream.readObject();

            return tSet;

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } finally {
            if (oistream != null) {
                try {
                    oistream.close();
                } catch (IOException ioe) {
                }
            }
        }

        return null;
    }

    public static DataSet createFromFile(String filePath, int inputsCount, int outputsCount, String delimiter) {
        FileReader fileReader = null;

        try {
            DataSet trainingSet = new DataSet(inputsCount, outputsCount);
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
                    trainingSet.addRow(new DataSetRow(inputs, outputs));
                } else {
                    trainingSet.addRow(new DataSetRow(inputs));
                }
            }

            return trainingSet;

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex1) {
                }
            }
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex1) {
                }
            }
            ex.printStackTrace();
            throw ex;
        }

        return null;
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
    public DataSet[] createTrainingAndTestSubsets(int trainSetPercent, int testSetPercent) {
        DataSet[] trainAndTestSet = new DataSet[2];

        ArrayList<Integer> randoms = new ArrayList<Integer>();
        for (int i = 0; i < this.size(); i++) {
            randoms.add(i);
        }

        Collections.shuffle(rows);

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