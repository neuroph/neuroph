/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imgrec.filter.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.filter.ImageFilter;

/**
 *
 * @author Mihailo Stupar
 */
public class OCRCropImage implements ImageFilter {

    private BufferedImage originalImage;
    private BufferedImage filteredImage;
    private int width;
    private int height;

    private int newWidth;
    private int newHeight;

    /**
     * Dimension of the cropped image
     *
     * @param width
     * @param height
     */
    public void setDimension(int width, int height) {
        newHeight = height;
        newWidth = width;
    }

    @Override
    public BufferedImage processImage(BufferedImage image) {

        width = image.getWidth();
        height = image.getHeight();

        originalImage = image;
        filteredImage = new BufferedImage(newWidth, newHeight, image.getType());

        int startH = createStartH();
        int startW = createStartW();
        int endH = createEndH();
        int endW = createEndW();

        fillImage(startH, startW, endH, endW);
        

        return filteredImage;
    }

    private int createStartH() {
        int color;
        int black = 0;
        int startH = 0;
        loop:
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                color = new Color(originalImage.getRGB(j, i)).getRed();
                if (color == black) {
                    startH = j;
                    break loop;
                }
            }
        }
        return startH;
    }

    private int createStartW() {
        int color;
        int black = 0;
        int startW = 0;
        loop:
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                color = new Color(originalImage.getRGB(j, i)).getRed();
                if (color == black) {
                    startW = i;
                    break loop;
                }
            }
        }
        return startW;
    }

    private int createEndH() {
        int color;
        int black = 0;
        int endH = 0;
        loop:
        for (int i = height - 1; i >= 0; i--) {
            for (int j = width; j >= 0; j--) {
                color = new Color(originalImage.getRGB(j, i)).getRed();
                if (color == black) {
                    endH = j;
                    break loop;
                }
            }
        }
        return endH;
    }

    private int createEndW() {
        int color;
        int black = 0;
        int endW = 0;
        loop:
        for (int j = width; j >= 0; j--) {
            for (int i = height - 1; i >= 0; i--) {
                color = new Color(originalImage.getRGB(j, i)).getRed();
                if (color == black) {
                    endW = i;
                    break loop;
                }
            }
        }
        return endW;
    }

    private void fillImage(int startH, int startW, int endH, int endW) {

        // fill the image with white color
        int alpha = new Color(originalImage.getRGB(width / 2, height / 2)).getRed();
        int whiteRGB = ImageUtilities.colorToRGB(alpha, 255, 255, 255);
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                filteredImage.setRGB(j, i, whiteRGB);
            }
        }

        // fill black pixels

        int oldCenterH = (startH + endH) / 2;
        int oldCenterW = (startW + endW) / 2;

        int newCenterH = newHeight / 2;
        int newCenterW = newWidth / 2;

        boolean[][] visited = new boolean[newHeight][newWidth];

        LinkedList<String> queue = new LinkedList<>();
        String pos = newCenterH + " " + newCenterW + " " + oldCenterH + " " + oldCenterW;
        queue.addLast(pos);
        try {
            while (!queue.isEmpty()) {
                String tmp = queue.removeFirst();
                int nh = Integer.parseInt(tmp.split(" ")[0]);
                int nw = Integer.parseInt(tmp.split(" ")[1]);
                int oh = Integer.parseInt(tmp.split(" ")[2]);
                int ow = Integer.parseInt(tmp.split(" ")[3]);
                filteredImage.setRGB(nw, nh, originalImage.getRGB(ow, oh));

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int tmpH = nh + i;
                        int tmpW = nw + i;
                        if (!visited[tmpH][tmpW]) {
                            visited[tmpH][tmpW] = true;
                            queue.addLast(nh + " " + nw + " " + oh + " " + ow);
                        }
                    }
                }
            }

        } catch (IndexOutOfBoundsException e) {}
    }
}
