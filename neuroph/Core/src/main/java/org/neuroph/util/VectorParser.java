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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Provides methods to parse strings as Integer or Double vectors.
 * 
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
// rename to ArrayUtils
public class VectorParser {

	/**
	 * This method parses input String and returns Integer vector
	 * 
	 * @param str
	 *            input String
	 * @return Integer vector
	 */
	static public ArrayList<Integer> parseInteger(String str) {
		StringTokenizer tok = new StringTokenizer(str);
		ArrayList<Integer> ret = new ArrayList<Integer>();
		while (tok.hasMoreTokens()) {
			Integer d = Integer.valueOf(tok.nextToken());
			ret.add(d);
		}
		return ret;
	}

	/**
	 * This method parses input String and returns double array
         * 
	 * @param inputStr
	 *            input String
	 * @return double array
	 */
	static public double[] parseDoubleArray(String inputStr) {
		String[] inputsArrStr = inputStr.split(" ");

		double[] ret = new double[inputsArrStr.length];
		for (int i = 0; i < inputsArrStr.length; i++) {
			ret[i] = Double.parseDouble(inputsArrStr[i]);
		}

		return ret;
	}

	public static double[] toDoubleArray(List<Double> list) {
		double[] ret = new double[list.size()];
		for (int i = 0; i < list.size(); i++) {
			ret[i] = list.get(i).doubleValue();
		}
		return ret;
	}

	public static ArrayList<Double> convertToVector(double[] array) {
		ArrayList<Double> vector = new ArrayList<Double>(array.length);

		for (double val : array) {
			vector.add(val);
		}

		return vector;
	}

}