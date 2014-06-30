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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class can be used for large training sets, which are partialy read from
 * file during the training. It loads bufferSize rows from file into DataSet,
 * and when it iterates all of them, it takes next bufferSize rows. It can be
 * used everywhere where DataSet class is used since it extends it. The rows
 * should be iterated with iterator() interface.
 *
 * @author Zoran Sevarac
 */
public class BufferedDataSet extends DataSet implements Iterator<DataSetRow> {

    /**
     * Buffer size determines how many data rows will be loaded from file at once
     */
    private int bufferSize = 1000;
    
    /**
     * File with data set rows
     */
    private File file;
    
    /**
     * Number of lines in the file
     */
    private long fileLinesNumber;
    
    /**
     * Current line number that we're reading
     */
    private long currentFileLineNumber;    
    
    /**
     * Number of rows loaded
     */
    private int rowsLoaded;
    
    /**
     * Delimiter character for values in line
     */
    private String delimiter;
    
    /**
     * File reader used to read from file
     */
    FileReader fileReader = null;
    
    /**
     * Buffered reader used to read from file
     */
    BufferedReader bufferedReader;
    
    /**
     * A reference to the buffered rows (a List collection in superclass)
     */
    List<DataSetRow> bufferedRows;
    
    /**
     * Iterator for buffered rowa
     */
    Iterator<DataSetRow> bufferIterator;


    public BufferedDataSet(File file, int inputSize, String delimiter) {
        super(inputSize);
    }

    /**
     * Creates new buffered data set with specified file, input and output size.
     * Data set file is assumed to be txt value with data set rows in a single line,
     * with input and output vector values delimited by delimiter.
     * 
     * @param file datas et file
     * @param inputSize size of input vector
     * @param outputSize size of outut vector
     * @param delimiter delimiter for vector values
     * @throws FileNotFoundException 
     */
    public BufferedDataSet(File file, int inputSize, int outputSize, String delimiter) throws FileNotFoundException {
        super(inputSize, outputSize);

        this.delimiter = delimiter;
        this.file = file;
        this.fileReader = new FileReader(file);
        this.bufferedReader = new BufferedReader(fileReader);
        fileLinesNumber = countFileLines();
        
        // load first chunk of data into buffer
        loadNextBuffer();
    }

    /**
     * Counts and returns number of lines in a file
     * @return number of lines in a file
     * @throws FileNotFoundException 
     */
    private long countFileLines() throws FileNotFoundException {
        LineNumberReader lnr = new LineNumberReader(new FileReader(file));
        try {
          //  lnr.skip(Long.MAX_VALUE);
           while(lnr.skip(Long.MAX_VALUE) > 0) {}; 
        } catch (IOException ex) {
            Logger.getLogger(BufferedDataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lnr.getLineNumber() + 1;
    }

    /**
     * Returns iterator for buffered data set
     * @return 
     */
    @Override
    public Iterator<DataSetRow> iterator() {
        return this;
    }

    /**
     * Returns true if there are more rows, false otherwise
     * @return true if there are more rows, false otherwise
     */
    @Override
    public boolean hasNext() {
        if (currentFileLineNumber < fileLinesNumber) {
            return true;
        }

        return false;
    }

    /**
     * Returns next data set row. Note that if there are no more buffered rows, 
     * this mthod will load next bufferSize rows into buffer.
     * @return next data set row
     */
    @Override
    public DataSetRow next() {
        // if we have reached the end of buffered data
        if (!bufferIterator.hasNext()) {
            this.loadNextBuffer(); // load next chunk from file into buffer
        }
        
        currentFileLineNumber++; // increase line counter
        return bufferIterator.next(); // and return next data row
    }

    @Override
    public void remove() { // what should be done here?
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Loads next bufferSize rows from file into buffer
     */
    private void loadNextBuffer() {
        try {
            String line = "";
            this.clear();    // data set buffer

            rowsLoaded = 0;
            while (rowsLoaded < bufferSize) { // 

                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }

                rowsLoaded++;
                double[] inputs = new double[getInputSize()];
                double[] outputs = new double[getOutputSize()];
                String[] values = line.split(delimiter);

                if (values[0].equals("")) {
                    continue; // skip if line was empty
                }
                for (int i = 0; i < getInputSize(); i++) {
                    inputs[i] = Double.parseDouble(values[i]);
                }

                for (int i = 0; i < getOutputSize(); i++) {
                    outputs[i] = Double.parseDouble(values[getInputSize() + i]);
                }

                if (getOutputSize() > 0) {
                    this.addRow(new DataSetRow(inputs, outputs));
                } else {
                    this.addRow(new DataSetRow(inputs));
                }
            }

            bufferedRows = this.getRows();
            bufferIterator = bufferedRows.iterator();

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
    }
}
