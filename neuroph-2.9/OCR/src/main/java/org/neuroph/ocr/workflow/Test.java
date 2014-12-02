/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.workflow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.neuroph.imgrec.FractionHSLData;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.filter.impl.GrayscaleFilter;
import org.neuroph.imgrec.filter.impl.LetterSeparationFilter;
import org.neuroph.imgrec.image.Dimension;

/**
 *
 * @author Mihailo
 */
public class Test {

    public static void main(String[] args) throws IOException {

        String pathText = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.txt";
        String pathImage = "C:/Users/Mihailo/Desktop/OCR workflow/part_75.png";
        Reader.readImage(pathImage);
        Reader.readText(pathText);

        Filters filters = new Filters();
        filters.addFilter(new GrayscaleFilter());
        filters.addFilter(new LetterSeparationFilter());
        filters.processImage();

        System.out.println("Creating letters...");
        String locationFolder = "C:\\Users\\Mihailo\\Desktop\\OCR workflow\\letters";
        LetterCreator letterCreator = new LetterCreator(locationFolder);
        letterCreator.createLetterImage(80, 80); //dimensions of the image

        ArrayList<String> letterLabels = Share.getInstance().getLetterLabels();

        Map<String, FractionHSLData> map = ImageRecognitionHelper.getFractionHSLDataForDirectory(new File(locationFolder), new Dimension(20, 20));
    }

}
