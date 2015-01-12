/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Mihailo Stupar
 */
public class OCRUtilities {

    /**
     * Output contains all character labels with probabilities. This method
     * finds the biggest probability and returns character with that
     * probability.
     *
     * @param output output from network
     * @return character as string
     */
    public static String getCharacter(Map<String, Double> output) {
        double maxValue = -1;
        Map.Entry<String, Double> maxElement = null;
        for (Map.Entry<String, Double> element : output.entrySet()) {
            if (maxValue < element.getValue()) {
                maxElement = element;
                maxValue = element.getValue();
            }
        }
        return maxElement.getKey();
    }

    /**
     * Find the center of each row measured in pixels.
     *
     * @param image input image, should be black-white image;
     * @param ignoreSize the height of the sign (dots or trash) that should not
     * be recognized as letter
     * @return list with pixel position of each row
     */
    public static List<Integer> rowPositions(BufferedImage image, int ignoreSize) {
        int[] histogram = colorHistogramHeight(image);
        int[] gradient = gradient(histogram);
        return linePositions(gradient, ignoreSize);
    }

    /**
     * Word is class with two parameters, startPixel and endPixel. This method
     * calculates these pixels for given row and return them as List of Word
     *
     * @param image input image, should be black-white
     * @param row given row
     * @param letterSize predicted letter size
     * @param spaceGap predicted space size, spaces smaller that spaceGap are
     * not spaces between word, they are spaces between letter. Ignore spaces
     * between letters.
     * @return
     */
    public static List<Word> wordsPositions(BufferedImage image, int row, int letterSize, int spaceGap) {
        List<Word> words = new ArrayList<Word>();
        int[] histogram = horizontalHistogram(image, row, letterSize);
        int[] histogramIMS = histogramIgnoreMiniSpaces(histogram, spaceGap);

        int count = 0;
        for (int i = 0; i < histogramIMS.length; i++) {
            if (histogramIMS[i] != 0) {
                count++;
            } else { //(histogram[i] == 0) drugim recima vece je od nule
                if (count > 0) {
                    int start = i - count;
                    int end = i - 1;
                    Word w = new Word(start, end);
                    words.add(w);
                }
                count = 0;
            }

        }
        return words;
    }

    /**
     * Creates image with single character on it. Method which is used is like
     * connected-components extraction. Algorithm that is used is BFS and image
     * is like the graph.
     *
     * @param image input image with text, should be black-white
     * @param visited dimensions of matrix corresponds to dimension of image.
     * Should be same matrix for all characters in the document (image)
     * @param startX pixel position on X coordinate where is black pixel found.
     * This is potentially pixel of the letter
     * @param startY same as start X, but Y coordinate
     * @param newWidth width of the new image, image with character
     * @param newHeight height of the new image
     * @param trashSize characters with number of black pixels smaller than
     * trashSize would not be extracted. Be carefully if you want to recognize
     * dots, comas... it should not be large number
     * @return image with character or null if character is recognized as trash
     */
    public static BufferedImage extraxtCharacterImage(BufferedImage image, boolean[][] visited, int startX, int startY, int newWidth, int newHeight, int trashSize) {
        int gapWidth = newWidth / 5 * 2;  //start x coordinate of letter, 2/5 itended
        int gapHeight = newHeight / 5 * 2;  //start y coordinate of letter 

        LinkedList<String> queue = new LinkedList<String>();

        BufferedImage letter = new BufferedImage(newWidth, newHeight, image.getType());
//        int alpha = new Color(image.getRGB(startX, startY)).getAlpha();
//        int white = ImageUtilities.colorToRGB(alpha, 255, 255, 255);
//        int black = ImageUtilities.colorToRGB(alpha, 0, 0, 0);\
        Color white = Color.WHITE;
        Color black = Color.BLACK;

        // fill all letter image with white pixels
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                letter.setRGB(j, i, white.getRGB());
            }
        }

        int countPixels = 0; // ignore dots
        String positions = startX + " " + startY;
        visited[startX][startY] = true;
        queue.addLast(positions);
        while (!queue.isEmpty()) {
            String pos = queue.removeFirst();
            String[] posArray = pos.split(" ");
            int H = Integer.parseInt(posArray[0]); // H-height
            int W = Integer.parseInt(posArray[1]); // W-width
            visited[H][W] = true;

            int posW = W - startY + gapWidth;
            int posH = H - startX + gapHeight;

            countPixels++;
            
            letter.setRGB(posW, posH, black.getRGB());
           
            int color;
            int blackInt = 0;
            for (int i = H - 1; i <= H + 1; i++) {
                for (int j = W - 1; j <= W + 1; j++) {
                    if (i >= 0 && j >= 0 && i < image.getHeight() && j < image.getWidth()) {
                        if (!visited[i][j]) {
                            color = new Color(image.getRGB(j, i)).getRed();
                            if (color == blackInt) {
                                visited[i][j] = true;
                                String tmpPos = i + " " + j;
                                queue.addLast(tmpPos);
                            }
                        }
                    }
                }
            }
        }
        if (countPixels < trashSize) { //da ne bi uzimao male crtice, tacke

            return null;
        }

        return letter;
    }

    /**
     * Save the image to the file
     * @param image should be cropped before the saving. Use OCRCropImage class
     * @param path path to the folder, ie C:/Users/.../ it should ended with /
     * @param letterName letter of the name
     * @param extension some of .png .jpg ...
     */
    public static void saveToFile(BufferedImage image, String path, String letterName, String extension) {
        String imagePath = path + letterName +"."+ extension;
        File outputfile = new File(imagePath);   
        try {
            ImageIO.write(image, extension, outputfile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static String createImageName(String character) {
        int number = character.hashCode()*(new Random().nextInt(100));
        return character+"_"+number;
    }
    

    private static int[] histogramIgnoreMiniSpaces(int[] histogram, int spaceGap) {
        int count = 0;
        for (int i = 0; i < histogram.length; i++) {
            if (histogram[i] == 0) {
                count++;
            } else { //(histogram[i] != 0) drugim recima vece je od nule
//                System.out.println(i+"-"+count);
                if (count > 0 && count < spaceGap) {
                    for (int j = i - count; j < i; j++) {
                        histogram[j] = 1;
                    }
                }
                count = 0;
            }

        }
        return histogram;
    }

    private static int[] horizontalHistogram(BufferedImage image, int row, int letterSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        int color;
        int black = 0;
        int[] histogram = new int[width];
        for (int i = 0; i < width; i++) {
            for (int j = row - (letterSize / 2); j <= row + (letterSize / 2); j++) {
                if (j < 0 || j >= height) {
                    continue;
                }
                color = new Color(image.getRGB(i, j)).getRed();
                if (color == black) {
                    histogram[i]++;
                }
            }

        }
        return histogram;
    }

    private static int[] colorHistogramHeight(BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();

        int[] blackHistogram = new int[height];

        int color;
        int black = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                color = new Color(image.getRGB(j, i)).getRed();
                if (color == black) {
                    blackHistogram[i]++;
                }
            }
        }
        return blackHistogram;
    }

    // calculates radient as a difference between two neighbouring elemements (lines)
    private static int[] gradient(int[] blackHistogram) {
        int[] gradient = new int[blackHistogram.length];
        for (int i = 1; i < gradient.length; i++) {
            gradient[i] = blackHistogram[i] - blackHistogram[i - 1];
        }
        return gradient;
    }

    private static List<Integer> linePositions(int[] gradient, int ignoredSize) {
        ArrayList<Integer> lines = new ArrayList<Integer>();
        int sum = 0;
        int count = 0;
        for (int row = 0; row < gradient.length; row++) {
            sum += gradient[row];
            if (sum != 0) {
                count++;
                continue;
            }
            if (sum == 0) {
                if (count < ignoredSize) {
                    count = 0;
                } else { //count >= lineHeightThresh // found line!
                    int startLetter = row - count;
                    int endLetter = row;
                    int line = (startLetter + endLetter) / 2;
                    lines.add(line);
                    count = 0;
                }
            }
        }
        return lines;

    }

}
