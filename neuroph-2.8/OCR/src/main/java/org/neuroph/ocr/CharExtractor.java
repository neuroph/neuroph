package org.neuroph.ocr;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.image.Dimension;

/**
 * This class provides methods to extract single character images from text image
 * 
 * @author Vladimir Kolarevic
 * @author Zoran Sevarac
 */
public class CharExtractor {

    private int cropTopY = 0;//up locked coordinate
    private int cropBottomY = 0;//down locked coordinate
    private int cropLeftX = 0;//left locked coordinate
    private int cropRightX = 0;//right locked coordinate
    private BufferedImage imageWithChars = null;
    private boolean endOfImage;//end of picture
    private boolean endOfRow;//end of current reading row

    /**
     * Creates new char extractor with soecified text image
     * TODO: add background and foregrounf color
     * @param imageWithChars - image with text
     */
    public CharExtractor(BufferedImage imageWithChars) {
        this.imageWithChars = imageWithChars;
    }

    public void setImageWithChars(BufferedImage imageWithChars) {
        this.imageWithChars = imageWithChars;
    }

    /**
     * This method scans image pixels until it finds the first black pixel (TODO: use foreground color which is black by default).
     * When it finds black pixel, it sets cropTopY and returns true. if it reaches end of image and does not find black pixels, 
     * it sets endOfImage flag and returns false.
     * @return - returns true when black pixel is found and cropTopY value is changed, and false if cropTopY value is not changed
     */
    private boolean findCropTopY() {
        for (int y = cropBottomY; y < imageWithChars.getHeight(); y++) { // why cropYDown? - for multiple lines of text using cropBottomY from previous line above; for first line its zero
            for (int x = cropLeftX; x < imageWithChars.getWidth(); x++) { // scan starting from the previous left crop position - or it shoud be right???
                if (imageWithChars.getRGB(x, y) == -16777216) { // if its black rixel (also consider condition close to black or not white or different from background)
                    this.cropTopY = y;   // save the current y coordiante
                    return true;        // and return true
                }
            }
        }
        endOfImage = true;  //sets this flag if no black pixels are found
        return false;       // and return false
    }

    /**
     * This method scans image pixels until it finds first row with white pixels. (TODO: background color which is white by default).
     * When it finds line whith all white pixels, it sets cropBottomY and returns true
     * @return - returns true when cropBottomY value is set, false otherwise
     */
    private boolean findCropBottomY() {
        for (int y = cropTopY + 1; y < imageWithChars.getHeight(); y++) { // scan image from top to bottom           
            int whitePixCounter = 0; //counter of white pixels in a row
            for (int x = cropLeftX; x < imageWithChars.getWidth(); x++) { // scan all pixels to right starting from left crop position
                if (imageWithChars.getRGB(x, y) == -1) {    // if its white pixel
                    whitePixCounter++;                      // increase counter
                }
            }
            if (whitePixCounter == imageWithChars.getWidth()) { // if we have reached end of line counting white pixels (x pos)
                cropBottomY = y;                                // that means that we've found white line, so set current y coordinate minus 1
                return true;                                    // as cropBottomY and finnish with true
            }
            if (y == imageWithChars.getHeight() - 1) {  // if we have reached end of image 
                cropBottomY = y;                        // set crop bottom
                endOfImage = true;                      // set corresponding endOfImage flag
                return true;                            // and return true
            }
        }
        return false;                                   // this should never happen, however its possible if image has non white bg
    }

    /**
     * This method scans image pixels to the right  until it finds first black pixel, or reach end of row.
     * When black pixel is found it sets cropLeftX and returns true. It should set cropLeftX to the next letter in a row.
     * @return - return true when true when black pixel is found and cropLeftX is changed, false otherwise
     */
    private boolean findCropLeftX() {        
        int whitePixCounter = 0;                                            // white pixel counter between the letters
        for (int x = cropRightX; x < imageWithChars.getWidth(); x++) {      // start from previous righ crop position (previous letter), and scan following pixels to the right
            for (int y = cropTopY; y <= cropBottomY; y++) {             // vertical pixel scan at current x coordinate
                if (imageWithChars.getRGB(x, y) == -16777216) {             // when we find black pixel
                    cropLeftX = x;                                          // set cropLeftX
                    return true;                                            // and return true
                }
            }
            
            // BUG?: this condition looks strange.... we might not need whitePixCounter at all, it might be used for 'I' letter
            whitePixCounter++;                                              // if its not black pixel assume that its white pixel
            if (whitePixCounter == 3) {                                     // why 3 pixels? its hard coded for some case and does not work in general...!!!
                whitePixCounter = 0;                                        // why does it sets to zero, this has no purporse at all...
            }
        }
        endOfRow = true;        // if we have reached end of row and we have not found black pixels, set the endOfRow flag
        return false;           // and return false
    }

    /**
     * This method scans image pixels to the right until it finds next row where all pixel are white, y1 and y2.
     * TODO: resiti broblem tolerancije n aboju bacgrounda kada ima prelaz...
     * @return - return true  when x2 value is changed and false when x2 value is not changed
     */
    private boolean findCropRightX() {
        for (int x = cropLeftX + 1; x < imageWithChars.getWidth(); x++) {   // start from current cropLeftX position and scan pixels to the right
            int whitePixCounter = 0;
            for (int y = cropTopY; y <= cropBottomY; y++) {             // vertical pixel scan at current x coordinate
                if (imageWithChars.getRGB(x, y) == -1) {                    // if we have white pixel at current (x, y)
                    whitePixCounter++;                                      // increase whitePixCounter
                }
            }
            
            // this is for space!
            int heightPixels = cropBottomY - cropTopY;                      // calculate crop height
            if (whitePixCounter == heightPixels+1) {                         // if white pixel count is equal to crop height+1  then this is white vertical line, means end of current char/ (+1 is for case when there is only 1 pixel; a 'W' bug fix)
                cropRightX = x;                                             // so set cropRightX    
                return true;                                                // and return true
            }
            
            // why we need this when we allready have condiiton in the for loop? - for the last letter in the row.
            if (x == imageWithChars.getWidth() - 1) {                       // if we have reached end of row with x position    
                cropRightX = x;                                             // set cropRightX
                endOfRow = true;                                            // set endOfRow flag
                return true;                                                // and return true
            }
        }
        
//        endOfRow = true;                                                    // da li ovo treba , nije ga bilo?
        return false;                                                       // return false if we have not found vertical white line (end of letter) to the right
    }

    /**
     * Creates HashMap with characters as keys and BufferedImages as values
     * @param Dimensions to which output char images has to be resized to
     * @param list of letters which are names of images
     * @return HashMap with characters as keys and char images as values
     * This method returns HashMap with characters as keys and char images as values
     */
    public HashMap<String, BufferedImage> extractCharImagesToLearn(BufferedImage imageWithText, List<String> chars, Dimension dim) {
        this.imageWithChars = imageWithText;
        
        HashMap<String, BufferedImage> charImages = new HashMap<String, BufferedImage>();

        // scans image from top to botoom, and left to right and seeks nonwhite/black pixels
        
        int i = 0;
        while (endOfImage == false) {               // until the end of the image is reached do the following
            endOfRow = false;                       // reset endOfRow flag on each new text row start
            boolean foundTop = findCropTopY();      // find top coordinate for crop
            boolean foundBottom = false;            // reset foundBottom flag on each new text row start, after the top coordinate is found
            if (foundTop == true) {                 // make sure we have top position and proceed. Otherwise we have reached end of image
                foundBottom = findCropBottomY();    // find bottom coordinate for crop
                if (foundBottom == true) {          // make sure we have bottom coordinateand  proceed. At this point we have top and bottom crop coordinates.
                    while (endOfRow == false) {     // while we have not reached the end of row
                        boolean foundLeft = false;      // at  begining of the row set left and right crop flags to false
                        boolean foundRight = false;
                        foundLeft = findCropLeftX();    // first find left crop position
                        if (foundLeft == true) {        // make sure we have left crop position and proceed
                            foundRight = findCropRightX();  // find right crop position
                            if (foundRight == true) {       // make sure we have right position, and proceed
                                // crop, trim and resize character image - next two lines extracts characers, this should be fixed to deal with variable spacing between chars ...
                                BufferedImage charImage = ImageUtilities.cropImage(imageWithText, cropLeftX, cropTopY, cropRightX, cropBottomY);  // first crop  image at specified positions
                                charImage =  ImageUtilities.trimImage(charImage);   // then trim image just in case
                                // deal with 'I' 'i' 'l' 'j' chars - ako su manji zalepi ih centrirane na blok...
//                                if (charImage.getWidth() < (charImage.getHeight() /2)) {
//                                    BufferedImage bi = new BufferedImage(dim.getWidth(), dim.getHeight(), BufferedImage.TYPE_INT_RGB);
//                                    bi.getGraphics().setColor(Color.WHITE);
//                                    bi.getGraphics().fillRect(0, 0, bi.getWidth(), bi.getHeight());
//                                    bi.getGraphics().drawImage(charImage, bi.getWidth()/2 - charImage.getWidth()/2, 0, null);
//                                    charImage = bi;
//                                }    else 
                                
                                // and at the end scale image to specified size
                                charImage = ImageUtilities.resizeImage(charImage, dim.getWidth(), dim.getHeight());                               
                                charImages.put(chars.get(i), charImage);    // add char image to collection which will be returned
                                // we can write images to file here to see what comes out, for debuging
//                                try {
//                                    ImageIO.write(charImage  , "PNG", new File(chars.get(i)+".png"));
//                                } catch (IOException ex) {
//                                    Exceptions.printStackTrace(ex);
//                                }
                                i++;

                            } // found right
                        } // found left
                    } // end of row
                    cropLeftX = 0;  // at th eend of each row reset left and right x coordinates
                    cropRightX = 0;
                } //foundBottom
            } // foundTop
        } // end of image
        
        cropTopY = 0;
        cropBottomY = 0;
        endOfImage = false;

        return charImages;
    }

    /**
     * Extracts and returns char images to recognize as list of images
     * @return 
     */
    public List<BufferedImage> extractCharImagesToRecognize() {
        List<BufferedImage> trimedImages = new ArrayList<BufferedImage>();
        int i = 0;

        while (endOfImage == false) {
            endOfRow = false;
            boolean foundTop = findCropTopY();
            boolean foundBottom = false;
            if (foundTop == true) {
                foundBottom = findCropBottomY();
                if (foundBottom == true) {
                    while (endOfRow == false) {
                        boolean foundLeft = false;
                        boolean foundRight = false;
                        foundLeft = findCropLeftX();
                        if (foundLeft == true) {
                            foundRight = findCropRightX();
                            if (foundRight == true) {
                                BufferedImage image = ImageUtilities.trimImage(ImageUtilities.cropImage(imageWithChars, cropLeftX, cropTopY, cropRightX, cropBottomY));
                                trimedImages.add(image);                                
                                i++;                                                                               
                            }
                        }
                    }
                    cropLeftX = 0;
                    cropRightX = 0;
                }
            }
        }
        cropTopY = 0;
        cropBottomY = 0;
        endOfImage = false;

        return trimedImages;
    }
}
