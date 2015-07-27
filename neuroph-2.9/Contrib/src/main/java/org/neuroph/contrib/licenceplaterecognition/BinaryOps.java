/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.licenceplaterecognition;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.gui.image.ShowImages;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Megi
 */
public class BinaryOps {

    /**
     *
     * Inverts the image colors from negative to positive
     *
     * @return the image with inverted colors
     */
    public static BufferedImage invertImage(String imageName) {

        // read the image file
        BufferedImage inputFile = null;
        try {
            inputFile = ImageIO.read(new File(imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // go through image pixels and reverse their color       
        for (int x = 0; x < inputFile.getWidth(); x++) {
            for (int y = 0; y < inputFile.getHeight(); y++) {
                int rgba = inputFile.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                inputFile.setRGB(x, y, col.getRGB());
            }
        }

        //write the image to a file blackandwhite.png
        try {
            File outputFile = new File("blackandwhite.png");
            ImageIO.write(inputFile, "png", outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputFile;
    }

    public static BufferedImage binary(String textImageFile) {
        // load and convert the image into a usable format
        BufferedImage image = UtilImageIO.loadImage(textImageFile);

        // convert into a usable format
        ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
        ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        ImageSInt32 label = new ImageSInt32(input.width, input.height);

        // Select a global threshold using Otsu's method.
        double threshold = GThresholdImageOps.computeOtsu(input, 0, 256);

        // Apply the threshold to create a binary image
        ThresholdImageOps.threshold(input, binary, (float) threshold, true);

        // remove small blobs through erosion and dilation
        // The null in the input indicates that it should internally declare the work image it needs
        // this is less efficient, but easier to code.
        ImageUInt8 filtered = BinaryImageOps.erode8(binary, 1, null);
        filtered = BinaryImageOps.dilate8(filtered, 1, null);

        // get the binary image
        BufferedImage visualFiltered = VisualizeBinaryData.renderBinary(filtered, null);

        //write the negative image to a file
        File charFile = new File("whiteandblack.png");
        try {
            ImageIO.write(visualFiltered, "png", charFile);
        } catch (IOException ex) {
            Logger.getLogger(BinaryOps.class.getName()).log(Level.SEVERE, null, ex);
        }

        //return the positive image
        return invertImage("whiteandblack.png");
    }
}
