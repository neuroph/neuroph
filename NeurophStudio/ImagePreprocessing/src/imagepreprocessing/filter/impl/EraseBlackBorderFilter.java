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
import java.util.LinkedList;

/**
 * Erase Black border filter removes black border from the image. It assumes
 * that the most important part of image is included in rectangle in the central
 * part of the image. The rectangle is placed horizontally.All parts outside
 * rectangle will be deleted from the image. The entrance to the filter must be
 * binarized image. Good results are shown in images for lung cancer/cross
 * section.
 *
 * @author Mihailo Stupar
 */
public class EraseBlackBorderFilter implements ImageFilter,Serializable {

    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;

    @Override
    public BufferedImage processImage(BufferedImage image) {

        originalImage = image;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        filteredImage = new BufferedImage(width, height, originalImage.getType());

        int centerI = width / 2;
        int centerJ = height / 2;

        int lengthI = width / 4;
        int lengthJ = height / 6;

        int startI = centerI - lengthI / 2;
        int goalI = centerI + lengthI / 2;

        int startJ = centerJ - lengthJ / 2;
        int goalJ = centerJ + lengthJ / 2;
        boolean[][] visited;
        visited = new boolean[width][height];
        int color;

        for (int i = startI; i < goalI; i++) {
            for (int j = startJ; j < goalJ; j++) {

                color = new Color(originalImage.getRGB(i, j)).getRed();
                if (color == 0) {
                    if (!visited[i][j]) {
                        BFS(i, j, visited);
                    }
                }
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int alpha = new Color(originalImage.getRGB(i, j)).getAlpha();
                if (!visited[i][j]) {
                    int white = 255;
                    color = PreprocessingHelper.colorToRGB(alpha, white, white, white);
                    filteredImage.setRGB(i, j, color);
                } else {
                    int black = 0;
                    color = PreprocessingHelper.colorToRGB(alpha, black, black, black);
                    filteredImage.setRGB(i, j, color);
                }
            }
        }

        return filteredImage;
    }

    public void BFS(int startI, int startJ, boolean[][] visited) {
        LinkedList<String> queue = new LinkedList<String>();

        String positions = startI + " " + startJ;
        visited[startI][startJ] = true;
        queue.addLast(positions);

        while (!queue.isEmpty()) {
            String pos = queue.removeFirst();
            String[] posArray = pos.split(" ");
            int x = Integer.parseInt(posArray[0]);
            int y = Integer.parseInt(posArray[1]);
            visited[x][y] = true;
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i >= 0 && j >= 0 && i < originalImage.getWidth() && j < originalImage.getHeight() && i != x && j != y) {
                        if (!visited[i][j]) {
                            int color = new Color(originalImage.getRGB(i, j)).getRed();
                            if (color == 0) {
                                visited[i][j] = true;
                                String tmpPos = i + " " + j;
                                queue.addLast(tmpPos);
                            }
                        }
                    }
                } //i
            } //j
        }

    }
    
    @Override
    public String toString() {
        return "Erase Black Border Filter";
    }

}
