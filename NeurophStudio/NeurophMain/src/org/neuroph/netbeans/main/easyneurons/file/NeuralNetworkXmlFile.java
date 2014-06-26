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

import java.io.File;
import java.io.IOException;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.util.FileUtils;
import org.neuroph.util.NeuralNetworkType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Handles Neural Network Xml Serialization
 *
 * @author Zoran Sevarac
 * @author Ivan Jovanovic
 * @author Nemanja Joksovic
 */
public class NeuralNetworkXmlFile implements FileInterface<NeuralNetwork> {

    @Override
    public void save(NeuralNetwork neuralNetwork, String filePath) {
        if (neuralNetwork == null) {
            throw new IllegalArgumentException("Neural Network cannot be null");
        }

        if (filePath == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        try {
            XStream xstream = getXStream();
            String xml = xstream.toXML(neuralNetwork);
            FileUtils.writeStringToFile(new File(filePath), xml);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public NeuralNetwork load(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        try {
            XStream xstream = getXStream();
            String xml = FileUtils.readStringFromFile(new File(filePath));
            NeuralNetwork neuralNetwork = (NeuralNetwork) xstream.fromXML(xml);
            return neuralNetwork;
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private XStream getXStream() {
//		XStream xstream = new XStream(new DomDriver());
//		xstream.alias("neuralNetwork", NeuralNetwork.class);
//		xstream.alias("type", NeuralNetworkType.class);
//		xstream.alias("labelsPlugin", LabelsPlugin.class);
//		xstream.alias("neuralNetworkViewFrame", NeuralNetworkViewFrame.class);
//		xstream.alias("adaline", org.neuroph.nnet.Adaline.class);
//		xstream.alias("layer", org.neuroph.core.Layer.class);
//		xstream.alias("neuron", org.neuroph.core.Neuron.class);
//		xstream.alias("connection", org.neuroph.core.Connection.class);
//		xstream.registerConverter(new SwingXmlConverter());
////        xstream.omitField(java.util.Observable.class, "obs");
////        xstream.omitField(java.util.Observable.class, "changed");
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("neuralNetwork", NeuralNetwork.class);
		xstream.alias("type", NeuralNetworkType.class);
//		xstream.alias("neuralNetworkViewFrame", NeuralNetworkTopComponent.class);
		xstream.alias("adaline", org.neuroph.nnet.Adaline.class);
		xstream.alias("layer", org.neuroph.core.Layer.class);
		xstream.alias("neuron", org.neuroph.core.Neuron.class);
		xstream.alias("connection", org.neuroph.core.Connection.class);
		xstream.registerConverter(new SwingXmlConverter());
		return xstream;
    }
}
