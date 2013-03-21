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

/**
 * This class provides file extensions for neural networks and training sets
 * and related operations
 *
 * @author Marko Koprivica
 * @author Zoran Sevarac
 * @author Ivan Jovanovic
 * @author Nemanja Joksovic
 */
public class FileUtils {

	public final static String nn = "nnet";
	public final static String ts = "tset";
	public final static String nxml = "nxml";
	public final static String txml = "txml";

	/*
	 * Get the extension of a file.
	 */
	public static String getExtension(String s) {
		String ext = null;
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}

		return ext;
	}

	public static String getExtension(File f) {
		return getExtension(f.getName());
	}

	public static boolean isNeuralNetwork(String extension) {
		return extension.equals(nxml) || extension.equals(nn);
	}

	public static boolean isTrainingSet(String extension) {
		return extension.equals(txml) || extension.equals(ts);
	}

}
