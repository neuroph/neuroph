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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Very simple basic class which wraps the data generation process for the Time-Series
 * Prediction tutorial. Internally handles writing the data out to a system file. Also
 * uses a buffer to simulate stepping along the time series data.
 * See http://neuroph.sourceforge.net/TimeSeriesPredictionTutorial.html
 *
 * @author Laura Ellen Carter-Greaves
 */

public class GenerateSet {

	private String filename;
	private boolean simple;
	private int sampleEvery;
	private int memorySize;
	

        /**
         * Contructor for datase set generator which takes filename and data set settings as arguments
         * @param filename
         * @param simple
         * @param sampleEvery
         * @param memory
         */
	public GenerateSet(String filename, boolean simple, int sampleEvery, int memory) {
		super();
		this.filename = filename;
		this.simple = simple;
		this.sampleEvery = sampleEvery;
		this.memorySize = memory;
	}

        /**
         * Does the data set generation
         * @throws IOException
         */
	public void doIt() throws IOException {
		if(simple){
			doItSimple();
		}
		else{
			doItSupposition();
		}
	}

        /**
         * Generates simple sine wave data set
         * @throws IOException
         */
	private void doItSimple() throws IOException {
		
		double buffer[]=new double[memorySize+1]; //the extra one is for the output value
		int bufferSize=buffer.length;
		int bufferFull=buffer.length;
		
		GenerateSineWave gsw=new GenerateSineWave(1,1);
		
		BufferedWriter out=new BufferedWriter(new FileWriter(filename));
		
		for(int samples=0;samples<1000;samples++){
			
			//run through next samples so we take samples EVERY 'sampleEvery' steps
			for(int j=0;j<sampleEvery;j++){
				gsw.getNextSample();
			}
			
			//shift buffer down by one
			for(int i=1;i<bufferSize;i++){
				buffer[i-1]=buffer[i];
			}
			
			//load latest into end of buffer
			buffer[bufferSize-1]=gsw.getNextSample();
			
			//check to see when buffer fills up
			bufferFull--;
			if(bufferFull<=0){ //if buffer full write out the buffer
				for(int i=0;i<bufferSize;i++){
					out.write(buffer[i]+"\t");
				}
				out.write("\n");
			}
		}
		
		out.flush();
		out.close();
	}

        /**
         * Generates supposition sine wave data set
         * @throws IOException
         */
	private void doItSupposition() throws IOException{
		
		BufferedWriter out=new BufferedWriter(new FileWriter(filename));
		
		double buffer[]=new double[memorySize+1]; //the extra one is for the output value
		int bufferSize=buffer.length;
		int bufferFull=buffer.length;
		
		GenerateSineWave gsw1=new GenerateSineWave(1,0.5);
		GenerateSineWave gsw2=new GenerateSineWave(4,0.5);
		
		for(int samples=0;samples<1000;samples++){
			
			//run through next samples so we take samples EVERY
			for(int j=0;j<sampleEvery;j++){
				gsw1.getNextSample();
				gsw2.getNextSample();
			}
			
			//shift buffer down by one
			for(int i=1;i<bufferSize;i++){
				buffer[i-1]=buffer[i];
			}
			
			//load latest into end of buffer
			buffer[bufferSize-1]=gsw1.getNextSample()+gsw2.getNextSample();
			
			//check to see when buffer fills up
			bufferFull--;
			if(bufferFull<=0){ //if buffer full write out the buffer
				for(int i=0;i<bufferSize;i++){
					out.write(buffer[i]+"\t");
				}
				out.write("\n");
			}
		}
		
		out.flush();
		out.close();
	}
	
}
