/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.LSTM;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.davidmoten.hilbert.HilbertCurve;
import org.davidmoten.hilbert.HilbertCurveRenderer;
import org.davidmoten.hilbert.HilbertCurveRenderer.Option;
import org.davidmoten.hilbert.Range;
import org.davidmoten.hilbert.SmallHilbertCurve;

/**
 *
 * @author Maneesh
 */
public class HilbertCurvePatternDetect {

    public static void main(String... args) {

        HilbertCurve c1 = HilbertCurve.bits(4).dimensions(2);
        BigInteger index = c1.index(3, 4);
        System.out.println(index);
        long[] point =c1.point(53);
        System.out.println(point[0]+" "+point[1]);

        SmallHilbertCurve c = HilbertCurve.small().bits(5).dimensions(3);
        long[] point1 = new long[]{3, 3,3};
        long[] point2 = new long[]{8, 10,3};
        int splitDepth = 1;
        List<Range> ranges = c.query(point1, point2, splitDepth);
        ranges.stream().forEach(System.out::println);
        
        
        HilbertCurveRenderer.renderToFile(4, 800, "target/image.png");
        
        HilbertCurveRenderer.renderToFile(4, 1000, "target/imageColor4.png", Option.COLORIZE, Option.LABEL);
        
       final File folder = new File("G:/bkp/$AVG/baseDir/Imports/Sprites/MEN/");
    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
           // listFilesForFolder(fileEntry);
        } else {
            if(Math.random()>.1)continue;
            System.out.println(fileEntry.getName());
            Rectangle[] result=getMatchingRegion("C:/Users/Maneesh/Desktop/face.png",fileEntry.getAbsolutePath());
            BufferedImage img = getTheImage(fileEntry.getAbsolutePath(),false);
            Graphics2D graph = img.createGraphics();
            graph.setColor(Color.GREEN);
            for(Rectangle rect:result)
             graph.draw(rect);
            //graph.drawImage(targetScaled, rect.x, rect.y, null);
            graph.dispose();
            displayImage(img,"img");
            displayImage(cropImage(img, result[result.length-1]),"img");
        }
        
    }



        getMatchingRegion("C:/Users/Maneesh/Desktop/face2.png","C:/Users/Maneesh/Desktop/Emiko_1513270388079.png");
        //getMatchingRegion("C:/Users/Maneesh/Desktop/SeirenFace.png","C:/Users/Maneesh/Desktop/Seiren.png");
        //SeirenFace

    }
      public static BufferedImage getMatchedRegion(String imagePattern, String TargetImage) {
        java.awt.Rectangle[] result = getMatchingRegion(imagePattern, TargetImage);
        BufferedImage img = HilbertCurvePatternDetect.getTheImage(TargetImage, false);
        //Graphics2D graph = img.createGraphics();
        //displayImage(cropImage(img, result[result.length-1]),"img");
        return cropImage(img, result[result.length - 1]);

    }
    public static BufferedImage getTheImage(String imageFullPath, boolean blackAndWhite) {
        BufferedImage img = null;
        File f = null;

        //read image
        try {
            f = new File(imageFullPath);
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }

       // BufferedImage blackWhite = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        BufferedImage blackWhite = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = blackWhite.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return blackAndWhite ? blackWhite : img;
    }
    
    public static Rectangle[] getMatchingRegion(String imagePattern,String TargetImage){
        
        /**
         * 1. Read pattern image
         * 2. Read the target image
         * 3. scale both to 2px by 2px
         * 4. With hilbert curve find close matching pixels
         * 5. Find the scale difference of pattern image and the Target image and then set the bits target from 1 to scale
         * 6. select those pixels that are closest match and then keep recording the matches. 
         */
     
      BufferedImage img = getTheImage(imagePattern,true);//displayImage(img,"img");
      BufferedImage imgT = getTheImage(TargetImage,true);//displayImage(imgT,"imgT");

    //get image width and height
    int width = img.getWidth();
    int height = img.getHeight();
    
    int scale=imgT.getHeight()*imgT.getWidth()/(img.getWidth()*img.getHeight());
   // scale=4;//yet to find optimum value
    int thePixelsToTraverse=5;
    scale=thePixelsToTraverse+2;
    int startOPixelToTraverse=3;
    int pointBestMatchedGlobal=0;
    int differenceGlobal=Integer.MAX_VALUE;
    int scaleSelected=1;
    int scaleSelectedForPattern=1;
   // for(int j=0;j<img.getWidth();j++)
   Rectangle result[] = new Rectangle[thePixelsToTraverse-1];
    for(int i=1;i<thePixelsToTraverse;i++){
        
        BufferedImage patternScaled= resizeImage(img,img.getWidth()*(i+i)/thePixelsToTraverse, img.getHeight()*(i+i)/thePixelsToTraverse);//displayImage(patternScaled,"patternScaled");
        
        BufferedImage targetScaled= imgT;//resizeImage(imgT,imgT.getWidth()*i/thePixelsToTraverse, imgT.getHeight()*i/thePixelsToTraverse);//displayImage(targetScaled,"targetScaled");
        
        
        HilbertCurve cForpattern = HilbertCurve.bits(thePixelsToTraverse).dimensions(2);
        //HilbertCurveRenderer.renderToFile(i, 1000, "target/cForpattern_"+i+".png", Option.COLORIZE, Option.LABEL);
        //displayImage(HilbertCurveRenderer.render(i, patternScaled.getWidth(), Option.LABEL),"target/cForpattern_"+i+".png");
        HilbertCurve cForTarget = HilbertCurve.bits(scale).dimensions(2);
        //HilbertCurveRenderer.renderToFile((scale*i), 1000, "target/cForTarget_"+(i+scale)+".png", Option.COLORIZE, Option.LABEL);
        //displayImage(HilbertCurveRenderer.render(i+scale, targetScaled.getWidth(), Option.LABEL),"target/cForTarget_"+i+".png");
        
        
        
        int pointsToTravese=(int) Math.pow(2, thePixelsToTraverse*2);
        int pointsToTraveseC=(int) Math.pow(2, (scale)*2);
        scale=scale+1;
        int pointBestMatched=0,tempMatchCount=0;
        int difference=Integer.MAX_VALUE;
        long[] pointFinal;
        System.out.println("Loop to run for>>"+pointsToTraveseC+"x"+pointsToTravese+" at scale>>"+((double)(i+i)/thePixelsToTraverse));
        //for(int traverseStart=0;traverseStart<pointsToTravese-pointsToTraveseC;traverseStart++){//Limit by target size
        for(int traverseStart=0;traverseStart<pointsToTraveseC-pointsToTravese;traverseStart=(int) (traverseStart+pointsToTravese*.1)){//Limit by target size
           int samePointsCount=0;
           int samePointsCountTarget=0;
           double maP=0,maP2=0;
           double maT=0,maT2=0;
           double count=pointsToTravese-5;
           boolean didLoopComplete=false;
           for(int traverseStartp=0;traverseStartp<pointsToTravese;traverseStartp++){//Compare every point
               
               try {
                   //Get the XY in images to match
                   long[] pointP = cForpattern.point(traverseStartp);
                   pointP[0] = (long) (pointP[0] * patternScaled.getWidth() / Math.sqrt(pointsToTravese));
                   pointP[1] = (long) (pointP[1] * patternScaled.getHeight() / Math.sqrt(pointsToTravese));
                   long[] pointT = cForTarget.point(traverseStart );
                   pointT[0] = (long) (pointT[0] * targetScaled.getWidth() / Math.sqrt(pointsToTraveseC))+pointP[0];
                   pointT[1] = (long) (pointT[1] * targetScaled.getHeight() / Math.sqrt(pointsToTraveseC))+pointP[1] ;

                   if (pointT[0] >= targetScaled.getWidth() || pointT[1] >= targetScaled.getHeight()) {
                       
                       break;
                   }
                   if (pointP[0] >= patternScaled.getWidth() || pointP[1] >= patternScaled.getHeight()) {
                       break;
                   }
                   //get pixel value
                   //System.out.println(pointT[0]+" /"+targetScaled.getWidth()+" "+pointT[1]+" /"+targetScaled.getHeight());
                   int p = patternScaled.getRGB((int) (long) pointP[0], (int) (long) pointP[1]);
                   int pT = targetScaled.getRGB((int) (long) pointT[0], (int) (long) pointT[1]);
                   
                   if (!getColor(p).equals(Color.black)) { //Dont match exact black
                       if (count < traverseStartp) //Moving average method
                       {
                           maP = (maP + p) / count;
                           maT = (maT + pT) / count;
                       } else {
                           maP = (maP + p);
                           maT = (maT + pT);
                       }
                       
                       if(p>Color.GRAY.getRGB()){//Compute shiny area better
                           if (count < traverseStartp) //Moving average method
                       {
                           maP2 = (maP2 + p) / count;
                           maT2 = (maT2 + pT) / count;
                       } else {
                           maP2 = (maP2 + p);
                           maT2 = (maT2 + pT);
                       }
                       }
                   }
                   //For same count in region
//                   Color col =getColor(p);
//                   if (!col.equals(Color.black)) {
//                       samePointsCount++;
//                       
//                   }
//                   
//                   col =getColor(pT);
//                   if (!col.equals(Color.black)) {
//                       samePointsCountTarget++;
//                   }
                   //For same pixel matches
//                   if (getColor(p).equals(getColor(pT))) {
//                       samePointsCountTarget++;
//                       samePointsCount++;
//                   }
                   
//                   System.out.println("targetScaled>> " + pointT[0] + " /" + targetScaled.getWidth() + " " + pointT[1] + " /" + targetScaled.getHeight());
//                   System.out.println("patternScaled>> " + pointP[0] + " /" + patternScaled.getWidth() + " " + pointP[1] + " /" + patternScaled.getHeight());
//
//                   System.out.println(" samePointsCount>> " + samePointsCount + " samePointsCountTarget>> " + samePointsCountTarget);

//                   if (p == pT) {
//                       samePointsCount++;
//                       
//                       System.out.println("Epoch traverseStart>>"+traverseStart+" traverseStartp >> "+traverseStartp+" samePointsCount>>"+samePointsCount);
//                       System.out.println("targetScaled>> "+pointT[0]+" /"+targetScaled.getWidth()+" "+pointT[1]+" /"+targetScaled.getHeight());
//                       System.out.println("patternScaled>> "+pointP[0]+" /"+patternScaled.getWidth()+" "+pointP[1]+" /"+patternScaled.getHeight());
//                   }
                   //get alpha
                   int a = (p >> 24) & 0xff;
                   //get red
                   int r = (p >> 16) & 0xff;
                   //get green
                   int g = (p >> 8) & 0xff;
                   //get blue
                   int b = p & 0xff;
                   didLoopComplete=traverseStartp==pointsToTravese-1?true:false;
                   //
               } catch (Exception ex) {
                  System.out.println( ex.getMessage());
                   //break;
               }
           }
//           pointBestMatched=tempMatchCount>samePointsCount?pointBestMatched:traverseStart;
//           tempMatchCount=tempMatchCount>samePointsCount?tempMatchCount:samePointsCount;
           //Go for min difference region
            if (didLoopComplete) {
                //System.out.println("Epoch >>"+traverseStart+" maP>>"+maP+"\t maT>>"+maT);

                double tempDecisionmaker =( Math.abs(maP - maT)*.4+Math.abs(maP2 - maT2)*.6)/2;//=Math.abs(samePointsCountTarget-samePointsCount)
                pointBestMatched = difference < tempDecisionmaker ? pointBestMatched : traverseStart;
                difference = (int) (difference < tempDecisionmaker ? difference : tempDecisionmaker);

            }
           //
        }
        
        System.out.println("Epoch >>"+i+" pointBestMatched>>"+pointBestMatched+"\t difference>>"+difference);
                       //For global compute
        differenceGlobal=difference<differenceGlobal?difference:differenceGlobal;
        pointBestMatchedGlobal=difference<differenceGlobal?pointBestMatched:pointBestMatchedGlobal;
        scaleSelected=difference<differenceGlobal?scale-1:scaleSelected;
        scaleSelectedForPattern=difference<differenceGlobal?(i+i):scaleSelectedForPattern;
        System.out.println("Epoch >>"+i+" difference>>"+difference+"\t differenceGlobal>>"+differenceGlobal);
       
          try {
            long[] pointT = cForTarget.point(pointBestMatched);
            //Scale up the location
            pointT[0]=(long) (pointT[0]*targetScaled.getWidth()/Math.sqrt(pointsToTraveseC));
            pointT[1]=(long) (pointT[1]*targetScaled.getHeight()/Math.sqrt(pointsToTraveseC));
            System.out.println(pointT[0] + " " + pointT[1] + " " + patternScaled.getWidth() + " " + patternScaled.getHeight());
            System.out.println(" Target>> " + targetScaled.getWidth() + " " + targetScaled.getHeight());

            Rectangle rect = new Rectangle((int) (long) pointT[0], (int) (long) pointT[1], patternScaled.getWidth(), patternScaled.getHeight());
            result[i-1]=rect;
            BufferedImage framedImage = new BufferedImage(targetScaled.getWidth(), targetScaled.getHeight(), targetScaled.getType());

            //For testing
//            Graphics2D graph = targetScaled.createGraphics();
//            graph.setColor(Color.GREEN);
//            graph.draw(rect);
//            //graph.drawImage(targetScaled, rect.x, rect.y, null);
//            graph.dispose();
//            displayImage(resizeImage(targetScaled,800,800), "extract");
            //displayImage(patternScaled, "patternScaled");
            //System.out.println(ImageIO.write(patternScaled, ".png", new File("patternScaled"+i+".png")));
            //ImageIO.write(targetScaled, ".png", new File("targetScaled"+i+".png"));

            //BufferedImage extract = cropImage(targetScaled, rect);
            //displayImage(extract, "extract");
            //ImageIO.write(extract, ".png", new File("cropContent"+i+".png"));
        } catch (Exception ex) {
            Logger.getLogger(HilbertCurvePatternDetect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    HilbertCurve cForTarget = HilbertCurve.bits(scaleSelected).dimensions(2);
    long[] pointT = cForTarget.point(pointBestMatchedGlobal);
    //Scale up the location
    BufferedImage patternScaled= resizeImage(img,img.getWidth()*(scaleSelectedForPattern)/thePixelsToTraverse, img.getHeight()*(scaleSelectedForPattern)/thePixelsToTraverse);//displayImage(patternScaled,"patternScaled");
        
//    int pointsToTraveseC=(int) Math.pow(2, (scaleSelected)*2);
//    pointT[0]=(long) (pointT[0]*imgT.getWidth()/Math.sqrt(pointsToTraveseC));
//    pointT[1]=(long) (pointT[1]*imgT.getHeight()/Math.sqrt(pointsToTraveseC));
//    Rectangle rect = new Rectangle((int) (long) pointT[0], (int) (long) pointT[1], patternScaled.getWidth(), patternScaled.getHeight());
//    System.out.println(pointT[0] + " " + pointT[1] + " " + patternScaled.getWidth() + " " + patternScaled.getHeight());
//            System.out.println("Global Target>> " + imgT.getWidth() + " " + imgT.getHeight());
//    BufferedImage extract = cropImage(imgT, rect);        
//    displayImage(extract, "extract");
   

    //some code goes here...
        return result;
}
    
    
public static BufferedImage resizeImage(Image image, int width, int height) {
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = bufferedImage.createGraphics();

    // Increase quality if needed at the expense of speed
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    AffineTransform scaleTransform = AffineTransform.getScaleInstance(
            width / (double) image.getWidth(null), height / (double) image.getHeight(null));
    g.drawImage(image, scaleTransform, null);

    // Release resources
    g.dispose();

    return bufferedImage;
}
    
    public static BufferedImage cropImage(BufferedImage src, Rectangle rect) {
      BufferedImage dest = src.getSubimage(rect.x, rect.y,
              src.getWidth()<rect.width+rect.x?(rect.width-(rect.width+rect.x- src.getWidth())):rect.width,
              src.getHeight()<rect.height+rect.y?(rect.height-(rect.height+rect.y- src.getHeight())):rect.height);
      return dest; 
   }
    
    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

    int original_width = imgSize.width;
    int original_height = imgSize.height;
    int bound_width = boundary.width;
    int bound_height = boundary.height;
    int new_width = original_width;
    int new_height = original_height;

    // first check if we need to scale width
    if (original_width > bound_width) {
        //scale width to fit
        new_width = bound_width;
        //scale height to maintain aspect ratio
        new_height = (new_width * original_height) / original_width;
    }

    // then check if we need to scale even with the new height
    if (new_height > bound_height) {
        //scale height to fit instead
        new_height = bound_height;
        //scale width to maintain aspect ratio
        new_width = (new_height * original_width) / original_height;
    }

    return new Dimension(new_width, new_height);
}
    public static void displayImage(BufferedImage bimage,String message ){
        Icon icon = new ImageIcon(bimage);
              JLabel picLabel = new JLabel(icon);
              JOptionPane.showMessageDialog(null, picLabel,message, JOptionPane.PLAIN_MESSAGE, null);
    }
    
    public static Color getColor(int c) {

        int red = (c & 0x00ff0000) >> 16;
        int green = (c & 0x0000ff00) >> 8;
        int blue = c & 0x000000ff;
      //  System.out.println(red+" "+green+" "+blue);
        return new Color(red, blue, green);

    }
}
