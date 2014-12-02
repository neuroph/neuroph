/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.ocr.workflow;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Mihailo
 */
public class Reader {
    
    public static void readImage (String pathImage) {
        try {
            BufferedImage image = ImageIO.read(new File(pathImage));
            Share.getInstance().setImage(image);
        }catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    public static void readText (String pathText) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathText));
            String text = "";
            String tmp;
            while ((tmp = br.readLine()) != null) {
                text = text + tmp + " ";
            }
            br.close();
            Share.getInstance().setText(text);
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    
}
