/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.imgrec.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.neuroph.imgrec.ImageUtilities;

/**
 * This class represents image on J2SE platform, and the Image interface is used to provide compatibility with Android platform.
 * It is a wrapper around BufferedImage and implementation of Image interface.
 * @author dmicic
 */
public class ImageJ2SE implements Image {

    private BufferedImage bufferedImage;

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    private ImageJ2SE() {
    }

    private ImageJ2SE(Integer width, Integer height, Integer imageType) {
        bufferedImage = new BufferedImage(width, height, checkImageType(imageType));
    }

    private ImageJ2SE(File imageFile) throws IOException {
        bufferedImage = ImageIO.read(imageFile);
    }

    private ImageJ2SE(String filePath) throws IOException {
        bufferedImage = ImageIO.read(new File(filePath));
    }

    private ImageJ2SE(URL imageUrl) throws IOException {
        bufferedImage = ImageIO.read(imageUrl);
    }
    
    public ImageJ2SE(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;      
    }

    @Override
    public int getPixel(int x, int y) {
        return bufferedImage.getRGB(x, y);
    }

    @Override
    public void setPixel(int x, int y, int color) {
        bufferedImage.setRGB(x, y, color);
    }

    @Override
    public int[] getPixels(int offset, int stride, int x, int y, int width, int height) {
        int[] pixels = new int[width * height];
        bufferedImage.getRGB(x, y, width, height, pixels, offset, stride);
        return pixels;
    }

    @Override
    public void setPixels(int[] pixels, int offset, int stride, int x, int y, int width, int height) {
        bufferedImage.setRGB(x, y, width, height, pixels, offset, stride);
    }

    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public Image resize(int width, int height) {
        BufferedImage resizedImage = ImageUtilities.resizeImage(bufferedImage, width, height);
        return new ImageJ2SE(resizedImage);
    }

    @Override
    public Image crop(int x1, int y1, int x2, int y2) {
        BufferedImage croppedImage = ImageUtilities.cropImage(bufferedImage, x1, y1, x2, y2);
        return new ImageJ2SE(croppedImage);
//        ((ImageJ2SE) image).setBufferedImage(((ImageJ2SE) image).getBufferedImage().getSubimage(x1, y1, x2 - x1, y2 - y1));
//        return image;
    }

    private int checkImageType(int imageType) throws IllegalArgumentException {
        if (imageType < ImageType.J2SE_TYPE_CUSTOM || imageType > ImageType.J2SE_TYPE_BYTE_INDEXED) {
            throw new IllegalArgumentException("Illegal image type, image type: " + imageType);
        } else {
            return imageType;
        }
    }

    public int getType() {
        return bufferedImage.getType();
    }
}
