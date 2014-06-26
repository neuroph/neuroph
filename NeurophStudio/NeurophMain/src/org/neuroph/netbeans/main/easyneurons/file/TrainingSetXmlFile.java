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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.io.IOException;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.FileUtils;

/**
 * Handles Training Set Xml Serialization
 *
 * @author Zoran Sevarac
 * @author Ivan Jovanovic
 * @author Nemanja Joksovic
 */
public class TrainingSetXmlFile implements FileInterface<DataSet> {

	@Override
	public void save(DataSet trainingSet, String filePath) {
		if (trainingSet == null) {
			throw new IllegalArgumentException("Training set cannot be null");
		}

		if (filePath == null) {
			throw new IllegalArgumentException("File cannot be null");
		}

		try {
			XStream xstream = getXStream();
			String xml = xstream.toXML(trainingSet);
			FileUtils.writeStringToFile(new File(filePath), xml);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public DataSet load(String filePath) {
		if (filePath == null) {
			throw new IllegalArgumentException("File cannot be null");
		}

		try {
			XStream xstream = getXStream();
			String xml = FileUtils.readStringFromFile(new File(filePath));
			DataSet trainingSet = (DataSet) xstream.fromXML(xml);
			return trainingSet;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private XStream getXStream() {
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("dataSet", DataSet.class);
		xstream.alias("dataSetRow", DataSetRow.class);
//		xstream.alias("supervisedTrainingElement", SupervisedTrainingElement.class);
		return xstream;
	}
}
