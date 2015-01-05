/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imgrec.filter.impl;



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.filter.ImageFilter;

/**
 *
 * @author Mihailo Stupar
 */
public class OCRSeparationFilter implements ImageFilter {

    private BufferedImage originalImage;
    private int width;
    private int height;

    private int cropHeight;
    private int cropWidth;
    
    private boolean[][] visited;

    private int letterWidth;
    private int letterHeight;
    private String location;

    private int[] counts;

    private int[] linePositions = null;
//    private ArrayList<String> letterLabels;

    private String text;
    private int seqNum = 0;

    

    public OCRSeparationFilter() {
        letterWidth = 0;
        letterHeight = 0;
//        letterLabels = new ArrayList<>(); 
        cropHeight = 0;
        cropWidth = 0;
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;
        width = originalImage.getWidth();
        height = originalImage.getHeight();

        prepare();

        visited = new boolean[height][width];

        int color;
        int white = 255;

        for (int line = 0; line < linePositions.length; line++) {
            for (int k = -1; k <= 1; k++) {
                int i = linePositions[line]+k;
                if (i == -1 || i == height)
                    continue;;
//        for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    color = new Color(originalImage.getRGB(j, i)).getRed();
                    if (color == white) {
                        visited[i][j] = true;
                    } else {
                        BFStraverseAndSave(i, j);

                    }

                }
            }
        }
        return originalImage;
    }

    private void BFStraverseAndSave(int startI, int startJ) {

        int gapWidth = letterWidth / 5 * 2;  //start x coordinate of letter, 2/5 itended
        int gapHeight = letterHeight / 5 * 2;  //start y coordinate of letter 

        LinkedList<String> queue = new LinkedList<>();

        BufferedImage letter = new BufferedImage(letterWidth, letterHeight, originalImage.getType());
        int alpha = new Color(originalImage.getRGB(startJ, startI)).getAlpha();
        int white = ImageUtilities.colorToRGB(alpha, 255, 255, 255);
        int black = ImageUtilities.colorToRGB(alpha, 0, 0, 0);

        // fill all letter image with white pixels
        for (int i = 0; i < letterHeight; i++) {
            for (int j = 0; j < letterWidth; j++) {
                letter.setRGB(j, i, white);
            }
        }

        int countPixels = 0; // ignore dots
        String positions = startI + " " + startJ;
        visited[startI][startJ] = true;
        queue.addLast(positions);
        while (!queue.isEmpty()) {
            String pos = queue.removeFirst();
            String[] posArray = pos.split(" ");
            int H = Integer.parseInt(posArray[0]); // H-height
            int W = Integer.parseInt(posArray[1]); // W-width
            visited[H][W] = true;

            int posW = W - startJ + gapWidth;
            int posH = H - startI + gapHeight;

            countPixels++;

            letter.setRGB(posW, posH, black);

            int color;
            int blackInt = 0;
            for (int i = H - 1; i <= H + 1; i++) {
                for (int j = W - 1; j <= W + 1; j++) {
                    if (i >= 0 && j >= 0 && i < originalImage.getHeight() && j < originalImage.getWidth()) {
                        if (!visited[i][j]) {
                            color = new Color(originalImage.getRGB(j, i)).getRed();
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

        if (countPixels < 35) { //da ne bi uzimao male crtice, tacke
            return;
        }

        String name = createName();
        saveToFile(letter, name); //potrebno je izbaciti seqNum i ostaviti samo name
        
        seqNum++;

    }

    private void saveToFile(BufferedImage img, String letterName) {
        File outputfile = new File(location + letterName + ".png");
        BufferedImage crop = img;
        if (cropHeight != 0 || cropWidth != 0) {
            OCRCropImage ci = new OCRCropImage();
            ci.setDimension(cropWidth, cropHeight);
            crop = ci.processImage(img);
        }
        
        
        try {
            ImageIO.write(crop, "png", outputfile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  pretopstavka da s ekoriste samo slova, mala i velika
     *  26 mali i 26 velikih, zato je counts[52]
     */
    private void prepare() {
        counts = new int[52];
        for (int i = 0; i < counts.length; i++) {
            counts[i] = 1;
        }
        String pom = "";
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i))) {
                pom += text.charAt(i);
            }
        }
        text = pom;
        //====================================================
        // ako nije setovan linepostions proci kroz sve linije
        if (linePositions == null) {
            linePositions = new int[height];

            for (int i = 0; i < linePositions.length; i++) {
                linePositions[i] = i;
            }
        }

    }

    
    /**
     * trenutno radi samo sa slovima, malim i velikim
     * promeniti da prepoznaje i druge karaktere
     * @return naziv slova, npr A ili c
     */
    private String createName() {

        int offsetBIG = 65;
        int offsetSMALL = 97;
        int offsetARRAY = 26;

        char c = text.charAt(seqNum);
        int key = c;
//        System.out.println(key+" "+c);
        int number;
        if (key < 95) { //smallLetter
            number = counts[key - offsetBIG];
            counts[key - offsetBIG]++;
        } else { //big letter
            number = counts[key - offsetSMALL + offsetARRAY];
            counts[key - offsetSMALL + offsetARRAY]++;
        }
        String name = c + "_" + number;
//        letterLabels.add(c+"");
        return name;
    }

    public void setLinePositions(int[] linePositions) {
        this.linePositions = linePositions;
    }

//    public ArrayList<String> getLetterLabels() {
//        return letterLabels;
//    }

    /**
     * The dimension of the image with a single letter
     * Letter will be in the center of the image
     * If the dimension is too small, the letter will be cropped
     * treba dodati preporucene velicine za svaki font
     * @param cropHeight
     * @param cropWidth 
     */
    public void setDimension(int cropHeight, int cropWidth) {
        this.cropHeight = cropHeight;
        this.cropWidth = cropWidth;
        letterWidth = 3*cropWidth;
        letterHeight = 3*cropHeight;
    } 

    /**
     * @param location Location path/folder where the images with letters
     * will be saved
     */
    public void setLocationFolder(String location) {
        this.location = location;
    }

    
    /**
     * The text that corresponds to the text on image
     * Used for name of each image
     * @param text 
     */
    public void setText(String text) {
        this.text = text;
    }
    
    
    
}
