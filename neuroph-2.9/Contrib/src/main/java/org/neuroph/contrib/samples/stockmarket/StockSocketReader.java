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

package org.neuroph.contrib.samples.stockmarket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides method to reads stock data from the socket.
 * See http://neuroph.sourceforge.net/tutorials/StockMarketPredictionTutorial.html
 * @author Dr.V.Steinhauer
 */
public class StockSocketReader implements Runnable {

    private int maxCounter;
    private long tsleep = 5000;
    private String[] valuesRow;

    public String[] getValuesRow() {
        return valuesRow;
    }

    public void setValuesRow(String[] valuesRow) {
        this.valuesRow = valuesRow;
    }

    public int getMaxCounter() {
        return maxCounter;
    }

    public void setMaxCounter(int maxCounter) {
        this.maxCounter = maxCounter;
    }

    public long getTsleep() {
        return tsleep;
    }

    public void setTsleep(long tsleep) {
        this.tsleep = tsleep;
    }

    public StockSocketReader() {
        //this.setTsleep(10000);
        this.setMaxCounter(100);
    }

    public StockSocketReader(int maxCounter) {
        //this.setTsleep(10000);
        this.setMaxCounter(maxCounter);
    }

    @SuppressWarnings("static-access")
    public void run() {
        valuesRow = new String[this.getMaxCounter()];
        for (int i = 0; i < this.getMaxCounter(); i++) {
            InputStream is = null;
            try {
                String surl = "http://download.finance.yahoo.com/d/quotes.csv?s=^GDAXI&f=sl1d1t1c1ohgv&e=.csv";
                URL url = new URL(surl);
                is = url.openStream();
                BufferedReader dis = new BufferedReader(new InputStreamReader(is));
                String s = dis.readLine();
                System.out.println(s);
                valuesRow[i] = s;
                is.close();
            } catch (MalformedURLException mue) {
                System.out.println("Ouch - a MalformedURLException happened.");
                mue.printStackTrace();
                System.exit(1);
            } catch (IOException ioe) {
                System.out.println("Oops- an IOException happened.");
                ioe.printStackTrace();
                System.exit(1);
            }
            try {
                Thread.currentThread().sleep(this.getTsleep());
            } catch (InterruptedException e) {
            }
        }//end of for
        System.out.println("valuesRow.length=" + valuesRow.length);

    }
}
