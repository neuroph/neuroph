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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.neuroph.imgrec.ImageUtilities;


import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;
import org.neuroph.imgrec.image.ImageJ2SE;
import org.neuroph.netbeans.project.CurrentProject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;


/**
 *
 * This class is used to draw letters on, it also contains the methods that are used to
 * get the image of an letter
 *
 * @author Boris Horvat
 */
public class DrawingPanel extends JPanel {


    /**
     * The width that the picture of the letter must be
     */
    public static final int FIXED_WIDTH = 40;
    /**
     * The hight that the picture of the letter must be
     */
    public static final int FIXED_HEIGHT = 40;
    /**
     * Represents the image which is used to draw on
     */
    private BufferedImage image;


    

    public DrawingPanel() {
        this.setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            createDrawingArea();
        } else {
            recreateDrawingArea(image, getWidth(), getHeight());
        }
        g.drawImage(image, 0, 0, this);
    }

    /**
     * This method is used to draw on the panel
     *
     * @param start point of the drawing line
     * @param end point of the drawing line
     */
    public void draw(Point start, Point end) {
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(Color.BLACK);
        g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_MITER));
        g2.draw(new Line2D.Double(start, end));
        g2.dispose();

        repaint();
    }

    /**
     * This method clears everything that is drown on the panel
     */
    public void clearDrawingArea() {
        Graphics g = image.getGraphics();

        g.setColor(getBackground());
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();

        repaint();
    }

    /**
     * This method finds out the coordinate of the drawen letter and creates the
     * approporite image from those coordinates
     */
public BufferedImage getDrawnLetter() throws Exception{

        prepareImage();

        int upperCoordinate = 0;
        int bottomCoordinate = 0;
        int leftCoordinate = 0;
        int rightCoordinate = 0;
        try {
            upperCoordinate = getUpperCoordinate();
            bottomCoordinate = getBottomCoordinate(upperCoordinate);
            leftCoordinate = getLeftCoordinate(upperCoordinate, bottomCoordinate);
            rightCoordinate = getRightCoordinate(upperCoordinate, bottomCoordinate, leftCoordinate);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }


        BufferedImage subImage = reSizingImage(image.getSubimage(leftCoordinate, upperCoordinate,
                rightCoordinate - leftCoordinate, bottomCoordinate - upperCoordinate));
        subImage = ImageUtilities.blackAndLightGrayCleaning(subImage);
        
        return subImage;

        /*
        ImageJ2SE neurophSubImage = (ImageJ2SE) ImageFactory.createImage(subImage.getWidth(), subImage.getHeight(), subImage.getType());
        neurophSubImage.setBufferedImage(subImage);
        neurophSubImage = (ImageJ2SE) ocrUtil.blackAndLightGrayCleaning(neurophSubImage);

        return neurophSubImage.getBufferedImage();
*/
//        try {
//            ImageIO.write(subImage, "PNG", new File("letter.png"));
//        } catch (IOException ex) {
//            throw new RuntimeException("No drawing could be founded, try writing again");
//        }
    }

    public void saveDrawnLetter(String letter) throws Exception {

        prepareImage();
        try {
            int upperCoordinate = getUpperCoordinate();
            int bottomCoordinate = getBottomCoordinate(upperCoordinate);
            int leftCoordinate = getLeftCoordinate(upperCoordinate, bottomCoordinate);
            int rightCoordinate = getRightCoordinate(upperCoordinate, bottomCoordinate, leftCoordinate);

            // extractvletter from image and normalize
            BufferedImage subImage = reSizingImage(image.getSubimage(leftCoordinate, upperCoordinate,
                    rightCoordinate - leftCoordinate, bottomCoordinate - upperCoordinate));
             subImage = ImageUtilities.blackAndLightGrayCleaning(subImage);
            
//            ImageJ2SE neurophSubImage = (ImageJ2SE) ImageFactory.createImage(subImage.getWidth(), subImage.getHeight(), subImage.getType());
//            neurophSubImage.setBufferedImage(subImage);
//            neurophSubImage = (ImageJ2SE) ocrUtil.blackAndLightGrayCleaning(neurophSubImage);
         
            
            int fileNumber = numberOfFiles(letter) + 1;

            // ovde puca ak ofolder Letters/Training Set nije kreiran - resenje: kreirati folder u aktuelnom projektu i to bas na ovommestu!
            String imageDir = TreeManager.getPath() + "/"; // get path to directory with letter images
            ImageIO.write(subImage, "PNG", new File(imageDir + letter.toUpperCase() + "_" + fileNumber + ".png"));
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Finds out the number of allready created letters for the traing set
     *
     * @param letter String that represens the wanted letter
     *
     * @return the number of allready existing letters
     */
    public static int numberOfFiles(String letter) {
        File f = new File(TreeManager.getPath()); // "Letters/Training Set/"
        File[] files = f.listFiles();
        int numberOfFiles = 0;
        for (int i = 0; i < files.length; i++) {
            if(files[i].getName().startsWith(letter)) {
                numberOfFiles++;
            }
        }
        return numberOfFiles;
    }

    /**
     * This method creats the image, that is used to draw on
     */
    private void createDrawingArea() {

        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = image.createGraphics();
        g2.setPaint(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    /**
     * This method creates new image with new dimensions based on the old image and provided dimensions
     *
     * @param bufferedImage old image
     * @param width new width of the image
     * @param height new height of the image
     */
    private void recreateDrawingArea(BufferedImage bufferedImage, int width, int height) {

        BufferedImage newImage = new BufferedImage(width, height, bufferedImage.getType());
        Graphics2D g = newImage.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.drawImage(bufferedImage, 0, 0, width, height, 0, 0, width, height, null);
        g.dispose();

        this.image = newImage;
    }

    /**
     * This method resizes image to the appropriate size
     *
     * @param bufferedImage represents an image that needs to be resized
     *
     * @return the resized image
     */
    private BufferedImage reSizingImage(BufferedImage bufferedImage) {
        // TODO: Use ImageUtilities here and check duplicate of this class!
        BufferedImage newImage = new BufferedImage(FIXED_WIDTH, FIXED_HEIGHT, bufferedImage.getType());
        Graphics2D g = newImage.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, FIXED_WIDTH, FIXED_HEIGHT);
        g.drawImage(bufferedImage, 0, 0, FIXED_WIDTH, FIXED_HEIGHT,
                0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
        g.dispose();
        
        return newImage;

//        ImageJ2SE neurophImage = (ImageJ2SE) ImageFactory.createImage(newImage.getWidth(), newImage.getHeight(), newImage.getType());
//        neurophImage.setBufferedImage(newImage);
//
//        return ((ImageJ2SE)ocrUtil.resizeImage(neurophImage, FIXED_WIDTH, FIXED_HEIGHT)).getBufferedImage();
    }

    /**
     * This method finds out what is the upper coordinate of the drawn letter,
     * by gooing through two loops, the first starts from the 0 coordinate until
     * the image height and the second starts from the 0 coordinate until the
     * image widht, searching for the first pixel that has a color diffrent then white
     *
     * @return the intager value of the upper coordinate
     *
     * @throws RuntimeException if on drawing can be founded
     */
    private int getUpperCoordinate() throws Exception{
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (image.getRGB(j, i) != -1) {
                    return i - 1;
                }
            }
        }
        throw new RuntimeException("No drawing could be founded, try writing again");
    }

    /**
     * This method finds out what is the bottom coordinate of the drawn letter,
     * by gooing through two loops, the first starts from the upper coordinate 
     * until the image height and the second starts from the 0 coordinate until 
     * the image widht, searching for the first vertical line of pixels thas has
     * all the white pixels
     *
     * @param upperCoordinate the upper coordinate of the drawn letter
     *
     * @return the intager value of the bottom coordinate
     *
     * @throws RuntimeException if on drawing can be founded
     */
    private int getBottomCoordinate(int upperCoordinate) throws Exception{

        int counter;
        for (int i = upperCoordinate + 1; i < image.getHeight(); i++) {
            counter = 0;
            for (int j = 0; j < image.getWidth(); j++) {
                if (image.getRGB(j, i) == -1) {
                    counter++;
                }
            }
            if (counter == image.getWidth()) {
                return i;
            }
        }
        throw new RuntimeException("No drawing could be founded, try writing again");
    }

    /**
     * This method finds out what is the left coordinate of the drawn letter,
     * by gooing through two loops, the first starts from the 0 coordinate until
     * the image widht and the second starts from the upper coordinate until the
     * image widht, searching for the first pixel that has a color diffrent then white
     *
     * @param upperCoordinate the upper coordinate of the drawn letter
     * @param bottomCoordinate the bottom coordinate of the drawn letter
     *
     * @return the intager value of the left coordinate
     * 
     * @throws RuntimeException if on drawing can be founded
     */
    private int getLeftCoordinate(int upperCoordinate, int bottomCoordinate) throws Exception{

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = upperCoordinate + 1; j < bottomCoordinate; j++) {
                if (image.getRGB(i, j) != -1) {
                    return i - 1;
                }
            }
        }
        throw new RuntimeException("No drawing could be founded, try writing again");
    }

    /**
     * This method finds out what is the bottom coordinate of the drawn letter,
     * by gooing through two loops, the first starts from the left coordinate
     * until the image widht and the second starts from the upper coordinate until
     * the bottom coordinate, searching for the first horizontal line of pixels
     * thas has all the white pixels
     *
     * @param upperCoordinate the upper coordinate of the drawn letter
     * @param bottomCoordinate the bottom coordinate of the drawn letter
     * @param leftCoordinate the left coordinate of the drawn letter
     *
     * @return the intager value of the right coordinate
     *
     * @throws RuntimeException if on drawing can be founded
     */
    private int getRightCoordinate(int upperCoordinate, int bottomCoordinate, int leftCoordinate) throws Exception{

        int counter;
        for (int i = leftCoordinate + 1; i < image.getWidth(); i++) {
            counter = 0;
            for (int j = upperCoordinate + 1; j < bottomCoordinate; j++) {
                if (image.getRGB(i, j) == -1) {
                    counter++;
                }
            }

            if (counter == bottomCoordinate - upperCoordinate - 1) {
                return i;
            }
        }
        throw new RuntimeException("No drawing could be founded, try writing again");
    }

    /**
     * Prepares the image for trimming by setting the white color around the eges
     * in order so that coordinate of the drawen letter stay inside that white space
     */
    private void prepareImage() {
        int height = image.getHeight();
        int width = image.getWidth();

        for (int i = 0; i < height; i++) {
            image.setRGB(0, i, -1);
            image.setRGB(width-1, i, -1);
        }

        for (int i = 0; i < width; i++) {
            image.setRGB(i, 0, -1);
            image.setRGB(i, height-1, -1);
        }
    }
}
