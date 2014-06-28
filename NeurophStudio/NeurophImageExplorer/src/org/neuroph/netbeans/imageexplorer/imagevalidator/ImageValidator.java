/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.imageexplorer.imagevalidator;

import org.openide.filesystems.FileObject;

/**
 *
 * @author Djordje
 */
public class ImageValidator implements FileObjectValidator {

    public boolean validateFileObjects(FileObject f) {
        if (f.isFolder()) {
            return true;
        }

        String extension = f.getNameExt();
        if (extension != null) {
            if (extension.equals(Utils.tiff)
                    || extension.equals(Utils.tif)
                    || extension.equals(Utils.gif)
                    || extension.equals(Utils.jpeg)
                    || extension.equals(Utils.jpg)
                    || extension.equals(Utils.png)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
