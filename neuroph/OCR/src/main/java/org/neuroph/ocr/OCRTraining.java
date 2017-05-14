/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr;

import org.neuroph.ocr.util.Letter;
import org.neuroph.ocr.util.Text;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.neuroph.ocr.filter.OCRCropLetter;
import org.neuroph.ocr.filter.OCRExtractLetter;
import org.neuroph.ocr.properties.OCRProperties;
import org.neuroph.ocr.util.OCRUtilities;

/**
 *
 * @author Mihailo Stupar
 */
public class OCRTraining extends OCRProperties {

    private String folderPath;
    private String trainingText;
    private String imageExtension;

    private List<String> characterLabels;

    public OCRTraining(Letter letterInformation, Text textInformation) {
        super(letterInformation, textInformation);
        imageExtension = "png";
    }

    /**
     * Path to the text (.txt file) with letters that corresponds to the letters
     * on the image
     *
     * @param trainingTextPath path to the .txt file
     */
    public void setTrainingTextPath(String trainingTextPath) {
        try {
            Path path = FileSystems.getDefault().getPath(trainingTextPath);
            trainingText = new String(Files.readAllBytes(path));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
    
    /**
     * Path to the folder where the images (each with the single letter) will be
     * stored
     *
     * <P>
     * Example: "C:/Users...../OCR/"</p>
     * <p>
     * Important thing is character '/' at the end of string</p>
     *
     * @param lettersPath path to the folder
     */
    public void setFolderPath(String lettersPath) {
        this.folderPath = lettersPath;

    }

    /**
     * Extension of the images with letters. Ie: png, jpg
     *
     * @param imageExtension imageExtension of images
     */
    public void setImageExtension(String imageExtension) {
        this.imageExtension = imageExtension;
    }

    /**
     * Text used for training. It should be formated as image.
     * <p>
     * Recommendations:</p>
     * <p>
     * -Use space, ie A B C a b c</p>
     * <p>
     * -Don't use A A A A, letters should be shaken</p>
     * <p>
     * -Use equal number of repetitions of each letter, as much as you can</p>
     *
     * @return text for training
     */
    public String getTrainingText() {
        return trainingText;
    }

    /**
     *
     * @param trainingText text used for training, for labeling the images
     */
    public void setTrainingText(String trainingText) {
        this.trainingText = trainingText;
    }

    /**
     *
     * @return Folder for storing training images
     */
    public String getFolderPath() {
        return folderPath;
    }

    /**
     * Unique characters in the list. Extracted from the training text.
     * Characters are represented as string
     *
     * @return list of characters (unique)
     */
    public List<String> getCharacterLabels() {
        return characterLabels;
    }

    /**
     * <p>
     * 1. create character labels using text for training</p>
     * <p>
     * 2. create images, each with single letter and named correctly</p>
     * <p>
     * 3. store these image to the folder set before</p>
     */
    public void prepareTrainingSet() {
        prepateText();
        createCharacterLabels();
        createImagesWithLetters();
    }

    //=========================================================================
    private void createCharacterLabels() {
        characterLabels = new ArrayList<String>();
        for (int i = 0; i < trainingText.length(); i++) {
            String c = trainingText.charAt(i) + "";
            if (!characterLabels.contains(c)) {
                characterLabels.add(trainingText.charAt(i) + "");
            }
        }
    }

    private void prepateText() {
        String tmp = "";
        for (int i = 0; i < trainingText.length(); i++) {
            char c = trainingText.charAt(i);
            if ((!Character.isSpaceChar(c)) && (!Character.isWhitespace(c))) {
                tmp += c;
            }
        }
        trainingText = tmp;
    }

    private void createImagesWithLetters() {
        int cropWidth = letterInformation.getCropWidth();
        int cropHeight = letterInformation.getCropHeight();
        int tmpWidth = 3 * cropWidth;
        int tmpHeight = 3 * cropHeight;
        int trashSize = letterInformation.getTrashSize();
        
        OCRExtractLetter extractionLetter = new OCRExtractLetter(tmpWidth, tmpHeight, trashSize);
        
        int letterSize = letterInformation.getLetterSize();
        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();
        boolean[][] visited = new boolean[imageHeight][imageWidth];
        Color white = Color.WHITE;
        Color color;
        int seqNum = 0;

        for (int line = 0; line < textInformation.numberOfRows(); line++) {

//==============================================================================
            for (int j = 0; j < imageWidth; j++) {
                for (int k = -(letterSize / 4); k < (letterSize / 4); k++) {
                    int rowPixel = textInformation.getRowAt(line);
                    int i = rowPixel + k;
                    if (i < 0 || i >= imageHeight) {
                        continue;
                    }
//==============================================================================
//                   fornja verzija radi, ova ima gresku 
//            for (int k = -(letterSize / 4); k < (letterSize / 4); k++) {
//                int rowPixel = textInformation.getRowAt(line);
//                int i = rowPixel + k;
//                if (i < 0 || i >= imageHeight) {
//                    continue;
//                }
//                for (int j = 0; j < imageWidth; j++) {
//==============================================================================
                    color = new Color(image.getRGB(j, i));
                    if (color.equals(white)) {
                        visited[i][j] = true;
                    } else if (visited[i][j] == false) {
                        BufferedImage letter = extractionLetter.extraxtLetter(image, visited, i, j); // OCRUtilities.extraxtCharacterImage(image, visited, i, j, tmpWidth, tmpHeight, letterInformation.getTrashSize());
                        if (letter != null) {
                            OCRCropLetter crop = new OCRCropLetter(letter, cropWidth, cropHeight);
                            BufferedImage croped = crop.processImage();
                            String character = trainingText.charAt(seqNum) + "";
                            String name = character + "_" + seqNum;
                            OCRUtilities.saveToFile(croped, folderPath, name, imageExtension);
                            seqNum++;
                        }
                    }

                }
            }

        }

    }

}
