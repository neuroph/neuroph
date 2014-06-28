/**
 * *
 * Neuroph http://neuroph.sourceforge.net Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * Neuroph is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neuroph.netbeans.imr.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class provides Iterator for the image files (jpg and png) in the
 * specified directory next() method loads and returns File objects
 *
 * @author Jon Tait
 */
public class ImagesIterator implements Iterator<File> {

    private Iterator<File> imageIterator;
    private String currentFilename = null;

    /**
     * Creates image Iterator for the specified dir
     *
     * @param dir
     * @throws java.io.IOException
     */
    public ImagesIterator(File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IOException(dir + " is not a directory!");
        }

        String[] imageFilenames = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.length() > 4) {
                    String fileExtension = name.substring(name.length() - 4, name.length());
                    return ".jpg".equalsIgnoreCase(fileExtension)
                            || ".png".equalsIgnoreCase(fileExtension);
                }
                return false;
            }
        });

        List<File> imageFiles = new ArrayList<File>();
        for (String imageFile : imageFilenames) {
            imageFiles.add(new File(dir, imageFile));
        }

        imageIterator = imageFiles.iterator();
    }

    public boolean hasNext() {
        return imageIterator.hasNext();
    }

    /**
     * Loads and returns next image
     *
     * @return Retruns next image file from directory as File object
     */
    public File next() {
        File nextFile = imageIterator.next();
        currentFilename = nextFile.getName();
        return nextFile;
    }

    public void remove() {
        imageIterator.remove();
    }

    public String getFilenameOfCurrentImage() {
        return currentFilename;
    }
}
