/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.imageexplorer.imagevalidator;

import java.io.File;
import javax.swing.filechooser.FileFilter;
/**
 *
 * @author Djordje
 */
public class ImageFileFilter extends FileFilter {

    private final String[] okFileExtensions =
            new String[]{"jpg", "png", "gif", "jpeg", "tif", "tiff"};

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Images";
    }
}
