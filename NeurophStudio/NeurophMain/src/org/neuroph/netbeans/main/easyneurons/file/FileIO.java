/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neuroph.netbeans.main.easyneurons.file;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

/**
 * This class provides all file IO operations for neural networks and training sets
 *
 * @author Marko Koprivica
 * @author Zoran Sevarac
 * @author Ivan Jovanovic
 * @author Nemanja Joksovic
 */
public class FileIO {

	public static void saveNeuralNetwork(NeuralNetwork nnet, String filename, String filePath) {
		String fileExtension = FileUtils.getExtension(filePath);
		if (fileExtension == null) {
			fileExtension = FileUtils.nn;
			filePath += "." + FileUtils.nn;
		}

		nnet.setLabel(filename);

		try {
			if (fileExtension.equals(FileUtils.nn)) {
				nnet.save(filePath);
			} else if (fileExtension.equals(FileUtils.nxml)) {
                NeuralNetworkXmlFile xmlFile = new NeuralNetworkXmlFile();
                xmlFile.save(nnet, filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NeuralNetwork loadNeuralNetwork(String filePath) {
		String fileExtension = FileUtils.getExtension(filePath);
		if (fileExtension == null) fileExtension = FileUtils.nn;

		try {
			if (fileExtension.equals(FileUtils.nn)) {
				return NeuralNetwork.load(filePath);
			} else if (fileExtension.equals(FileUtils.nxml)) {
                NeuralNetworkXmlFile xmlFile = new NeuralNetworkXmlFile();
                return xmlFile.load(filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void saveDataSet(DataSet dataSet, String filename, String filePath) {
		String fileExtension = FileUtils.getExtension(filePath);
		if (fileExtension == null) {
			fileExtension = FileUtils.ts;
			filePath += "." + FileUtils.ts;
		}

		dataSet.setLabel(filename);

		try {
			if (fileExtension.equals(FileUtils.ts)) {
				dataSet.save(filePath);
			} else if (fileExtension.equals(FileUtils.txml)) {
                TrainingSetXmlFile xmlFile = new TrainingSetXmlFile();
				xmlFile.save(dataSet, filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DataSet loadTrainingSet(String filePath) {
		String fileExtension = FileUtils.getExtension(filePath);
		if (fileExtension == null) fileExtension = FileUtils.ts;

		try {
			if (fileExtension.equals(FileUtils.ts)) {
				return DataSet.load(filePath);
			} else if (fileExtension.equals(FileUtils.txml)) {
                TrainingSetXmlFile xmlFile = new TrainingSetXmlFile();
				return xmlFile.load(filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
