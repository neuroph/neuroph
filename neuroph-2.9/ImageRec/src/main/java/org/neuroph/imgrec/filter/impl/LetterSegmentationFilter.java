/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.imgrec.filter.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.filter.ImageFilter;

/**
 *
 * @author Mihailo Stupar
 */
public class LetterSegmentationFilter implements ImageFilter{
    
    private BufferedImage originalImage;

    private int width;
    private int height;

    private boolean[][] visited;

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;
        width = originalImage.getWidth();
        height = originalImage.getHeight();

        visited = new boolean[width][height];

        int name = 1;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int color = new Color(originalImage.getRGB(i, j)).getRed();
                if (color == 255) {
                    visited[i][j] = true;
                } else {
                    if (name > 3000) {
                        return originalImage;
                    }
                    BFS(i, j, name + "");
                    name++;
                }

            }
        }

        return originalImage;
    }

    public void BFS(int startI, int startJ, String imageName) {
        LinkedList<String> queue = new LinkedList<String>();

        //=============================================================================
        int letterWidth = 80;
        int letterHeight = 80;
        int gapX = 30;
        int gapY = 30;
        BufferedImage letter = new BufferedImage(letterWidth, letterHeight, BufferedImage.TYPE_BYTE_BINARY);
        int alpha = new Color(originalImage.getRGB(startI, startJ)).getAlpha();
        int white = ImageUtilities.colorToRGB(alpha, 255, 255, 255);
        int black = ImageUtilities.colorToRGB(alpha, 0, 0, 0);
        for (int i = 0; i < letterWidth; i++) {
            for (int j = 0; j < letterHeight; j++) {
                letter.setRGB(i, j, white);

            }
        }
        //=============================================================================

        int count = 0;
        String positions = startI + " " + startJ;
        visited[startI][startJ] = true;
        queue.addLast(positions);

        while (!queue.isEmpty()) {
            String pos = queue.removeFirst();
            String[] posArray = pos.split(" ");
            int x = Integer.parseInt(posArray[0]);
            int y = Integer.parseInt(posArray[1]);
            visited[x][y] = true;

            //set black pixel to letter image===================================
            int posX = startI - x + gapX;
            int posY = startJ - y + gapY;

            count++;
            try {
                letter.setRGB(posX, posY, black);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("posX " + posX);
                System.out.println("posY " + posY);
                System.out.println("letterWidth " + letter.getWidth());
                System.out.println("letterHeight " + letter.getHeight());
                throw e;
            }
            //==================================================================
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i >= 0 && j >= 0 && i < originalImage.getWidth() && j < originalImage.getHeight()) {
                        if (!visited[i][j]) {
                            int color = new Color(originalImage.getRGB(i, j)).getRed();
                            if (color < 10) {
                                visited[i][j] = true;
                                String tmpPos = i + " " + j;
                                queue.addLast(tmpPos);
                            }
                        }
                    }
                } //i
            } //j
        }

        System.out.println("count = " + count);
        //save letter=========================================================== 
        if (count < 3) {
            return;
        }
        try {
            saveToFile(letter, imageName);
            //
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveToFile(BufferedImage img, String name) throws FileNotFoundException, IOException {

        File outputfile = new File("C:/Users/Mihailo/Documents/NetBeansProjects/ImagePreprocessing/Segmented_letters/" + name + ".jpg");
        ImageIO.write(img, "jpg", outputfile);
    }

    @Override
    public String toString() {
        return "Letter Segmentation filter";
    }
    
}
