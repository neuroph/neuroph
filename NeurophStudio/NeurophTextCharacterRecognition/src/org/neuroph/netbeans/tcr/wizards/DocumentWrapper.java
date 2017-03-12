/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.tcr.wizards;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Mihailo
 */
public class DocumentWrapper implements Serializable{
    
    private BufferedImage image;
    private String imageName;
    
    
    private String textPath;
    private String textName;
            
            
    
    
    public BufferedImage getImage() {
        return image;
    }

    public String getTextPath() {
        return textPath;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setTextPath(String textPath) {
        this.textPath = textPath;
    }

    public String getImageName() {
        return imageName;
    }

    public String getTextName() {
        return textName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setTextName(String textName) {
        this.textName = textName;
    }
    
    
    
}
