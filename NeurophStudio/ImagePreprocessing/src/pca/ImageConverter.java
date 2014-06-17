/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pca;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Class that handles conversion from image to array and from array back to
 * image
 *
 * @author Sanja
 */
public class ImageConverter {

    /**
     * Converts image to 2D array where every filed is one pixel, number of rows
     * equals image height number if columns equals image width NOTE: Image is
     * grayscale.
     *
     * @param image image to be converted
     * @return 2d array representation
     */
    public static double[][] convertTo2DArray(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        double[][] result = new double[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int rgb = image.getRGB(col, row);

                result[row][col] = rgb & 0xff;
            }
        }

        return result;
    }

    /**
     * Convert 2d array to image
     *
     * @param a array to be converted
     * @return image
     */
    public static BufferedImage imageFromArray(double[][] a) {

        BufferedImage img = new BufferedImage(a[0].length, a.length, BufferedImage.TYPE_BYTE_GRAY);

        //use this because img.setRGB doesn't work
        WritableRaster raster = img.getRaster();
        for (int r = 0; r < img.getHeight(); r++) {
            for (int c = 0; c < img.getWidth(); c++) {

                int pixel = (int) a[r][c];
                //set red, blue, grean 
                int p = pixel | (pixel << 8) | (pixel << 16);
                raster.setSample(c, r, 0, pixel);

            }
        }
        return img;
    }

}
