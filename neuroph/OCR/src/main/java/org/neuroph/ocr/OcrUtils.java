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

package org.neuroph.ocr;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;


/**
 * Contains various utility methods used for OCR.
 * 
 * @author Ivana Jovicic, Vladimir Kolarevic, Marko Ivanovic, Zoran Sevarac
 */
public class OcrUtils {

    /**
     * This method cleans input image by replacing
     * all non black pixels with white pixels
     * @param image - input image that will be cleaned
     * @return - cleaned input image as BufferedImage
     */
    public static Image blackAndWhiteCleaning(Image image) {
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                if (image.getPixel(i, j) != -16777216) {
                    image.setPixel(i, j, -1);
                }
            }
        }
        return image;
    }

    /**
     * This method cleans input image by replacing all pixels with RGB values
     * from -4473925 (gray) to -1 (white) with white pixels and
     * from -4473925 (gray) to -16777216 (black) with black pixels
     * @param image - input image that will be cleaned
     * @return - cleaned input image as BufferedImage
     */
    public static Image blackAndGrayCleaning(Image image) {
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                if (image.getPixel(i, j) > -4473925) {
                    image.setPixel(i, j, -1);
                } else {
                    image.setPixel(i, j, -16777216);
                }
            }
        }
        return image;
    }

    /**
     * This method cleans input image by replacing all pixels with RGB values
     * from -3092272 (light gray) to -1 (white) with white pixels and
     * from -3092272 (light gray) to -16777216 (black) with black pixels
     * @param image - input image that will be cleaned
     * @return - cleaned input image as BufferedImage
     */
    public static Image blackAndLightGrayCleaning(Image image) {
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                if (image.getPixel(i, j) > -4473925) {
                    image.setPixel(i, j, -1);
                } else {
                    image.setPixel(i, j, -16777216);
                }
            }
        }
        return image;
    }

    /**
     * This method cleans input image by replacing all pixels with RGB values
     * from RGBcolor input (the input color) to -1 (white) with white pixels and
     * from RGBcolor input (the input color) to -16777216 (black) with black pixels
     * @param image - input image that will be cleaned
     * @param RGBcolor - input RGB value of wanted color as reference for celaning
     * @return - cleaned input image as BufferedImage
     */
    public static Image colorCleaning(Image image, int RGBcolor) {  	
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                if (image.getPixel(i, j) == RGBcolor) {
                    image.setPixel(i, j, -16777216);
                } else {
                    image.setPixel(i, j, -1);
                }
            }
        }
        return image;
    }

    /**
     * This method loads the input Image and returns the cleaned version
     * @param f - input file that will be loaded as image
     * @return - return cleaned loaded image as BufferedImage
     */  
    public static Image loadAndCleanImage(File f) {
    	Image image = ImageFactory.getImage(f);
        return blackAndLightGrayCleaning(image);
    }

    /**
     * Loads image from the file.
     * @param file image file
     * @return loaded image
     */ 
    public static Image loadImage(File file) {
    	Image image = ImageFactory.getImage(file);
        return image;
    }

    /**
     * This method reads the image pixels until it reads the first black pixel
     * by height and then returns that value
     * @param Img - input image that will be read
     * @return - returns the value of height when conditions are true
     */
    private static int trimLockup(Image img) {
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                if (img.getPixel(i, j) == -16777216) {
                    return j;
                }
            }
        }
        return 0;
    }

    /**
     * This method reads the input image from the input from
     * start pixel height (y1) until it reads the first next row
     * where all pixel are white by height and return that value
     * @param Img - input image that will be read
     * @param y1 - input start height pixel of image
     * @return - returns the value of height when conditions are true
     */
    private static int trimLockdown(Image img, int y1) {
        for (int j = y1 + 1; j < img.getHeight(); j++) {
            int counterWhite = 0;
            for (int i = 0; i < img.getWidth(); i++) {
                if (img.getPixel(i, j) == -1) {
                    counterWhite++;
                }
            }
            if (counterWhite == img.getWidth()) {
                //this is a chek for dots over the letters i and j
                //so they wont be missread as dots
                if (j > (img.getHeight() / 2)) {
                    return j;
                }
            }
            if (j == img.getHeight() - 1) {
                return j + 1;
            }
        }
        return 0;
    }

    /**
     * This method trims the input image and returns it as a BufferedImage
     * @param imageToTrim input image that will be trimed
     * @return return trimed input image as BufferedImage
     */
//    public static Image trimImage(Image imageToTrim) {
//        int y1 = trimLockup(imageToTrim);
//        int y2 = trimLockdown(imageToTrim, y1);
//        int x1 = 0;
//        int x2 = imageToTrim.getWidth();
//        return cropImage(imageToTrim, x1, y1, x2, y2);
//    }

    /**
     * Resize image to specified dimensions
     * @param image image to resize
     * @param width new image width
     * @param height new image height
     * @return resized image
     */
//    public static Image resizeImage(Image image, int width, int height) {
//    	return image.resize(width, height);
//    }

    /**
     * Crops (returns subimage) of specified input image at specified points.
     *
     * @param image image to crop
     * @param x1 top left x coordinate
     * @param y1 top left y coordinate
     * @param  x2 bottom right x coordinate
     * @param  y2 bottom right y coordinate
     *
     * @return image croped at specified points
     */
//    public static Image cropImage(Image image, int x1, int y1, int x2, int y2) {
//    	return image.crop(x1, y1, x2 - x1, y2 - y1);
//    }

    /**
     * Creates and returns image from the given text.
     * @param text input text
     * @param font text font
     * @return image with input text
     */
//    public static BufferedImage createImageFromText(String text, Font font) {
//        //You may want to change these setting, or make them parameters
//        boolean isAntiAliased = true;
//        boolean usesFractionalMetrics = false;
//        FontRenderContext frc = new FontRenderContext(null, isAntiAliased, usesFractionalMetrics);
//        TextLayout layout = new TextLayout(text, font, frc);
//        Rectangle2D bounds = layout.getBounds();
//        int w = (int) Math.ceil(bounds.getWidth());
//        int h = (int) Math.ceil(bounds.getHeight()) + 2;
//        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB); //for example;
//        Graphics2D g = image.createGraphics();
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, w, h);
//        g.setColor(Color.BLACK);
//        g.setFont(font);
//        Object antiAliased = isAntiAliased
//                ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
//        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAliased);
//        Object fractionalMetrics = usesFractionalMetrics
//                ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
//        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics);
//        g.drawString(text, (float) -bounds.getX(), (float) -bounds.getY());
//        g.dispose();
//
//        return image;
//    }
    
//    public static Bitmap createImageFromText(String text) {
//		TextView txtView = new TextView(null);
//		txtView.setBackgroundColor(Color.WHITE);
//		txtView.setTextColor(Color.BLACK);
//		txtView.setText(text);
//		txtView.setWidth(LayoutParams.WRAP_CONTENT);
//		txtView.setHeight(LayoutParams.WRAP_CONTENT);
//        Bitmap image = Bitmap.createBitmap(txtView.getWidth(), txtView.getHeight(), Bitmap.Config.ALPHA_8);
//        Canvas canvas = new Canvas(image);
//        txtView.draw(canvas);
//        
//        return image;
//    }

    /**
     * Returns RGB data for all input images
     *
     * @param imagesData data map with characters as keys and charcter images as values
     * @return data map with characters as keys and image rgb data as values
     */
    public static Map<String, FractionRgbData> getFractionRgbDataForImages(HashMap<String, Image> imagesData) {

        Map<String, FractionRgbData> rgbDataMap = new HashMap<String, FractionRgbData>();

        for (String character : imagesData.keySet()) {
            StringTokenizer st = new StringTokenizer(character, ".");
            Image image = imagesData.get(character);
            rgbDataMap.put(st.nextToken(), new FractionRgbData(image));
        }

        return rgbDataMap;
    }

}
