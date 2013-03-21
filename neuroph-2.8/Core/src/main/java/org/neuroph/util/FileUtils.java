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

package org.neuroph.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility methods for working with files.
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class FileUtils {

    public static void writeStringToFile(File file, String xml) throws IOException {
      BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(xml);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            writer.close();
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public static String readStringFromFile(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = null;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            reader = new BufferedReader(new FileReader(file));
            String line = "";

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);
            }

            return stringBuffer.toString();

        } catch (FileNotFoundException ex) {
           throw ex;
        } catch (IOException ex) {
            throw ex;
        } finally {
              if (reader != null ) {
                  reader.close();
              }
        }
    }
}
