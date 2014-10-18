package org.neuroph.imgrec.filter.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.filter.ImageFilter;

/**
* Adaptive threshold binarization filter is primarily used for binarizing images 
* with text. If the image has such a brightness that one piece of text looks good 
* and the other part is in the dark, calculation of threshold based on the whole 
* image will not give a good result.This filter for each pixel in the image counts 
* a special threshold, and then decides whether the pixel transforms into black 
* or white color. Another purpose of this filter is to edge detection, especially 
* if the following filters that can be used is MedianFilter. In both cases, the 
* input should be a grayscale image.
 * 
 * @param wSize size of window for calculating treshold, default value is 31
 * @param k constant k, values between 0 and 1, default value is 0.02
 * 
 * reference to: http://arxiv.org/ftp/arxiv/papers/1201/1201.5227.pdf
 * 
 * @author Mihailo Stupar
 */
public class AdaptiveThresholdBinarizeFilter implements ImageFilter, Serializable {

    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;

    /**
     * Size of window for calculating treshold, default value is 31
     */    
    private int windowSize = 31;
    
    /**
     * Constant k, values between 0 and 1, default value is 0.02
     */
    private double k = 0.02;

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());

        int alpha;
        double gray;

        double[][] G = new double[width][height]; //Integral sum G

        gray = new Color(originalImage.getRGB(0, 0)).getRed();
        G[0][0] = gray / 255;

        for (int i = 1; i < width; i++) {
            gray = new Color(originalImage.getRGB(i, 0)).getRed();
            G[i][0] = G[i - 1][0] + gray / 255;
        }
        for (int j = 1; j < height; j++) {
            gray = new Color(originalImage.getRGB(0, j)).getRed();
            G[0][j] = G[0][j - 1] + gray / 255;
        }
        for (int i = 1; i < width; i++) {
            for (int j = 1; j < height; j++) {
                gray = new Color(originalImage.getRGB(i, j)).getRed();
                G[i][j] = gray / 255 + G[i][j - 1] + G[i - 1][j] - G[i - 1][j - 1];
            }
        }

        int d = windowSize / 2;

        int A = 0;
        int B = 0;
        int C = 0;
        int D = 0;

        double s;
        double m;
        double delta;
        double treshold;

        int newColor;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (i + d - 1 >= width) {
                    A = width - 1;
                } else {
                    A = i + d - 1;
                }

                if (j + d - 1 >= height) {
                    B = height - 1;
                } else {
                    B = j + d - 1;
                }

                if (i - d < 0) {
                    C = 0;
                } else {
                    C = i - d;
                }

                if (j - d < 0) {
                    D = 0;
                } else {
                    D = j - d;
                }

                s = (G[A][B] + G[C][D]) - (G[C][B] + G[A][D]);
                m = s / (windowSize * windowSize);

                gray = new Color(originalImage.getRGB(i, j)).getRed();

                delta = gray / 255 - m;

                treshold = m * (1 + k * (delta / (1.0 - delta) - 1));

                if (gray / 255 > treshold) {
                    newColor = 255;
                } else {
                    newColor = 0;
                }

                alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                newColor = ImageUtilities.colorToRGB(alpha, newColor, newColor, newColor);

                filteredImage.setRGB(i, j, newColor);
            }
        }

        return filteredImage;
    }

    public void setK(double k) {
        this.k = k;
    }
    
    public void setWindowSize(int wSize) {
        this.windowSize = wSize;
    }

    @Override
    public String toString() {
        return "Adaptive Treshold Binarize Filter";
    }

   
}
