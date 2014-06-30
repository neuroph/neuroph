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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of OutputAdapter interface for writing neural network outputs to database.
 * @see OutputAdapter
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class JDBCOutputAdapter implements OutputAdapter {
    Connection connection;
    String tableName;

    /**
     * Creates new JDBCOutputAdapter with specifed database connection and table
     * @param connection database connection
     * @param tableName  table to put data into
     */
    public JDBCOutputAdapter(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Writes specified output to table in database
     * @param output 
     */
    @Override
    public void writeOutput(double[] output) {
        try {
            String sql = "INSERT " + tableName + " VALUES(";
            for (int i = 0; i < output.length; i++) {
                sql += output[i];
                if (i < (output.length - 1)) {
                    sql = ", ";
                }
            }
            sql += ")";

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();

        } catch (SQLException ex) {
            Logger.getLogger(JDBCOutputAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    @Override
    public void close() {
    }
}
