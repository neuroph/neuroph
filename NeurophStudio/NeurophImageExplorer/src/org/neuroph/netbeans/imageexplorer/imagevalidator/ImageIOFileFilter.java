/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.imageexplorer.imagevalidator;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author Djordje
 */
public class ImageIOFileFilter implements FileFilter {

    private final String[] okFileExtensions =
            new String[]{"jpg", "png", "gif", "jpeg", "tif", "tiff"};

    public boolean accept(File pathname) {


        if (pathname.isDirectory()) {
            return true;
        }

        for (String extension : okFileExtensions) {
            if (pathname.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;

    }
}
