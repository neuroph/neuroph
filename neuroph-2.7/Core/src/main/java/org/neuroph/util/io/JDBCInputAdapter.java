/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.util.io;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementation of InputAdapter interface for reading neural network inputs from database.
 * @see InputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class JDBCInputAdapter implements InputAdapter {
    Connection connection;
    ResultSet resultSet;
    int inputSize;

    public JDBCInputAdapter(Connection connection, String sql) {
        this.connection = connection;        
        try {
            Statement stmt = connection.createStatement(); // Get a statement from the connection
            resultSet = stmt.executeQuery(sql);  // Execute the query  
            ResultSetMetaData rsmd = resultSet.getMetaData();
            inputSize = rsmd.getColumnCount();
        } catch (SQLException ex) {
            throw new NeurophInputException("Error executing query at JdbcInputAdapter", ex);
        }
    }

    /**
     * Reads next row from result set and returns input for neural network as array of doubles.
     * @return neural network input as array of doubles
     */    
    @Override
    public double[] readInput() {
        try {
            while (resultSet.next()) {
                double[] inputBuffer = new double[inputSize];
                for (int i = 1; i <= inputSize; i++) {
                    inputBuffer[i - 1] = resultSet.getDouble(i);
                }
                return inputBuffer;
            }
        } catch (SQLException ex) {
            throw new NeurophInputException("Error reading input value from the result set!", ex);
        }

        return null;
    }

    /**
     * Closes result set used as data source.
     */
    @Override
    public void close() {
        try {
             if (resultSet != null)
                  resultSet.close();
        } catch (SQLException ex) {
            throw new NeurophInputException("Error closing database connection!", ex);
        }
    }
}