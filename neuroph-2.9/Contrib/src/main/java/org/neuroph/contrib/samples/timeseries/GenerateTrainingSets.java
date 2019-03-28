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

package org.neuroph.contrib.samples.timeseries;

import java.io.IOException;

/**
 * Very simple class which just wraps up the generation of the time-series predicition training sets.
 * You can alter this to generate differing data sets. I.e. different samplig rates (frequency) and differing sizes of memory. 
 * See http://neuroph.sourceforge.net/TimeSeriesPredictionTutorial.html
 *
 * @author Laura Ellen Carter-Greaves
 */

public class GenerateTrainingSets {


        /**
         * Generates datasets used in time series prediction tutorial
         */
	public static void main(String[] args) {
		
		//change this directory as required for your installation...
		final String directory="C:\\java\\Neuroph_2.3\\Neuroph\\tutorial\\";
		
		try {
			GenerateSet gs=new GenerateSet(directory+"BSW15", true, 1, 5);
			gs.doIt();
			
			gs=new GenerateSet(directory+"BSW120", true, 1, 20);
			gs.doIt();
			
			gs=new GenerateSet(directory+"BSW210", true, 2, 10);
			gs.doIt();
			
			gs=new GenerateSet(directory+"BSW220", true, 2, 20);
			gs.doIt();
			
			gs=new GenerateSet(directory+"BSW55", true, 5, 5);
			gs.doIt();
			
			gs=new GenerateSet(directory+"SSW15", false, 1, 5);
			gs.doIt();
			
			gs=new GenerateSet(directory+"SSW120", false, 1, 20);
			gs.doIt();
			
			gs=new GenerateSet(directory+"SSW210", false, 2, 10);
			gs.doIt();
			
			gs=new GenerateSet(directory+"SSW540", false, 5, 40);
			gs.doIt();
			
			gs=new GenerateSet(directory+"SSW1010", false, 10, 10);
			gs.doIt();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to generate all data sets.");
			System.exit(1);
		}
		
		System.out.println("Generated data sets.");
	}

}
