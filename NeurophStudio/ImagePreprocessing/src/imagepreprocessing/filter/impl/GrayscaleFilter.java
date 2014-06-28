/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagepreprocessing.filter.impl;

import imagepreprocessing.helper.PreprocessingHelper;
import imagepreprocessing.filter.ImageFilter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Grayscale filter from image in RGB format makes grayscale image in way that
 * for each pixel, using value of red, green and blue color, calculates new
 * value using formula: gray = 0.21*red + 0.71*green + 0.07*blue Grayscale
 * filter is commonly used as first filter in Filter Chain and on that grayscale
 * image other filters are added.
 *
 * @author Mihailo Stupar
 */
public class GrayscaleFilter implements ImageFilter,Serializable {

    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;

        int alpha;
        int red;
        int green;
        int blue;

        int gray;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                red = new Color(originalImage.getRGB(i, j)).getRed();
                green = new Color(originalImage.getRGB(i, j)).getGreen();
                blue = new Color(originalImage.getRGB(i, j)).getBlue();

                gray = (int) (0.21 * red + 0.71 * green + 0.07 * blue);

                gray = PreprocessingHelper.colorToRGB(alpha, gray, gray, gray);

                filteredImage.setRGB(i, j, gray);

            }
        }

        return filteredImage;
    }

    @Override
    public String toString() {
        return "Grayscale Filter";
    }
    
    
}
