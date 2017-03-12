/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.ocr.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * Extraction of the letter in the text.
 * Finds the pixel which corresponds to letter and whole letter extract to the new image. 
 * @author Mihailo
 */
public class OCRExtractLetter {

    private int cropWidth;
    private int cropHeight;
    private int trashSize;

    /**
     * @param cropWidth width of the cropped image, should be greater of the letter
     * @param cropHeight height of the cropped image, should be greater of the letter
     * @param trashSize number of pixels that is not recognized as letter
     */
    public OCRExtractLetter(int cropWidth, int cropHeight, int trashSize) {
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
        this.trashSize = trashSize;
    }

    /**
     * You <b>must</b> set three parameters<br/>
     * 1. cropWidth<br/>
     * 2. cropHeight<br/>
     * 3. trashSize
     */
    public OCRExtractLetter() {
    }

    /**
     * 
     * @param cropHeight height of the cropped image, should be greater than letter height
     */
    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }

    /**
     * 
     * @param cropWidth width of the cropped image, should be greater than letter width
     */
    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    /**
     * 
     * @param trashSize number of pixels that is not recognized as letter, 
     * number smaller than the number of pixels in some little letter, for example i
     */
    public void setTrashSize(int trashSize) {
        this.trashSize = trashSize;
    }

    
    /**
     * 
     * @param image image with whole text
     * @param visited matrix of boolean, size of the matrix should correspond to size of the image with text. This matrix is used like in BFS traversal.
     * @param startX starting point on X coordinate where the black pixel is found
     * @param startY starting point on Y coordinate where the black pixel is found
     * @return new image with extracted letter only if number of pixel in that letter is greater than trashSize 
     */
    public BufferedImage extraxtLetter(BufferedImage image, boolean[][] visited, int startX, int startY) {
        int gapWidth = cropWidth / 5 * 2;  //start x coordinate of letter, 2/5 itended
        int gapHeight = cropHeight / 5 * 2;  //start y coordinate of letter 
        LinkedList<String> queue = new LinkedList<String>();
        BufferedImage letter = new BufferedImage(cropWidth, cropHeight, image.getType());
        Color white = Color.WHITE;
        Color black = Color.BLACK;

        // fill all letter image with white pixels
        for (int i = 0; i < cropHeight; i++) {
            for (int j = 0; j < cropWidth; j++) {
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

}
