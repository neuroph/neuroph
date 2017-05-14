/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imgrec.filter.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.filter.ImageFilter;

/**
 *
 * @author Mihailo Stupar
 */
// http://opencv-code.com/quick-tips/implementation-of-thinning-algorithm-in-opencv/
public class ZhangSuenThinFilter implements ImageFilter {

    private BufferedImage originalImage;
    private BufferedImage filteredImage;

    private boolean blackLetters = true;

    int[][] imageM;
    int width;
    int height;

    /**
     *
     * @param image The input image should be binary
     * @return
     */
    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;
        width = originalImage.getWidth();
        height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());
        imageM = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int col = new Color(originalImage.getRGB(i, j)).getRed();
                if (blackLetters) {
                    imageM[i][j] = 1 - (col / 255);
                } else {
                    imageM[i][j] = col / 255;
                }

            }

        }

        
        while (true) {

            int[][] start = new int[width][height];

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    start[i][j] = imageM[i][j];
                }
            }

            thiningIteration(0);
            thiningIteration(1);

            boolean same = true;
            MainforLoop:
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (start[i][j] != imageM[i][j]) {
                        same = false;
                        break MainforLoop;
                    }

                }
            }
            if (same) {
                break;
            }

        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                int col;
                if (blackLetters) {
                    col = 255 - imageM[i][j] * 255;
                } else {
                    col = imageM[i][j] * 255;
                }
                int rgb = ImageUtilities.colorToRGB(alpha, col, col, col);

                filteredImage.setRGB(i, j, rgb);
            }
        }

        return filteredImage;

    }

    public void thiningIteration(int iter) {
        int[][] marker = new int[width][height];

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {

                int p2 = imageM[i - 1][j];
                int p3 = imageM[i - 1][j + 1];
                int p4 = imageM[i][j + 1];
                int p5 = imageM[i + 1][j + 1];
                int p6 = imageM[i + 1][j];
                int p7 = imageM[i + 1][j - 1];
                int p8 = imageM[i][j - 1];
                int p9 = imageM[i - 1][j - 1];

//                int A  = (p2 == 0 && p3 == 1) + (p3 == 0 && p4 == 1) + 
//                     (p4 == 0 && p5 == 1) + (p5 == 0 && p6 == 1) + 
//                     (p6 == 0 && p7 == 1) + (p7 == 0 && p8 == 1) +
//                     (p8 == 0 && p9 == 1) + (p9 == 0 && p2 == 1); 
                int c1 = 0; //p2 == 0 && p3 == 1
                int c2 = 0; //p3 == 0 && p4 == 1
                int c3 = 0; //p4 == 0 && p5 == 1
                int c4 = 0; //p5 == 0 && p6 == 1
                int c5 = 0; //p6 == 0 && p7 == 1
                int c6 = 0; //p7 == 0 && p8 == 1
                int c7 = 0; //p8 == 0 && p9 == 1
                int c8 = 0; //p9 == 0 && p2 == 1

                if (p2 == 0 && p3 == 1) {
                    c1 = 1;
                }
                if (p3 == 0 && p4 == 1) {
                    c2 = 1;
                }
                if (p4 == 0 && p5 == 1) {
                    c3 = 1;
                }
                if (p5 == 0 && p6 == 1) {
                    c4 = 1;
                }
                if (p6 == 0 && p7 == 1) {
                    c5 = 1;
                }
                if (p7 == 0 && p8 == 1) {
                    c6 = 1;
                }
                if (p8 == 0 && p9 == 1) {
                    c7 = 1;
                }
                if (p9 == 0 && p2 == 1) {
                    c8 = 1;
                }

                int A = c1 + c2 + c3 + c4 + c5 + c6 + c7 + c8;
                int B = p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;

                int m1 = iter == 0 ? (p2 * p4 * p6) : (p2 * p4 * p8);
                int m2 = iter == 0 ? (p4 * p6 * p8) : (p2 * p6 * p8);

                if (A == 1 && (B >= 2 && B <= 6) && m1 == 0 && m2 == 0) {
                    marker[i][j] = 1;
                }

            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int tmp = 1 - marker[i][j];
                if (imageM[i][j] == tmp && imageM[i][j] == 1) {
                    imageM[i][j] = 1;
                } else {
                    imageM[i][j] = 0;
                }

            }
        }

    }

    @Override
    public String toString() {
        return "Zhang-Suen Thin Method";
    }

   

    public void setBlackLetters(boolean blackLetters) {
        this.blackLetters = blackLetters;
    }

    

}
