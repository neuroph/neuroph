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

package org.neuroph.netbeans.hwr.wiz;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This class represnts the image panel on wich the drawing of the letter is done
 *
 * @author Boris Horvat
 */
public class JImagePanel extends JPanel {

    /** Holds the reference to the BuffedImage objec on which the drawing is done */
    private BufferedImage image;

    /** The value of the X coordinate of the pannel */
    private final int X = 150;
    /** The value of the Y coordinate of the pannel */
    private final int Y = 150;

    /**
     * Creates the new image pannel by colling the constructor of the JPanel
     */
    public JImagePanel() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            //g.drawImage(resize(image, X, Y), (getWidth()-X)/2, (getHeight()-Y)/2, null);
            g.drawImage(image, 0, 0, null);
        } catch (NullPointerException e) {}
    }

    /**
     * Sets the image to the given location
     *
     * @param imagePath the location of the image
     */
    public void setImage(String imagePath) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(imagePath));
            this.image = img;
            repaint();
        } catch (IOException ex) { }
    }

    /**
     * Resizes the given image to the given size
     * 
     * @param img the image that needs to be resized
     * @param newW the number that representd the new width of the image
     * @param newH the number that representd the new hight of the image
     *
     * @return the resized image
     */
    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, 1);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }
}
