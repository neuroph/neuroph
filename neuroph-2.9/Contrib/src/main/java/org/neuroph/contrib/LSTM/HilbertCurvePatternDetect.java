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
import java.util.ArrayList;
import java.util.HashMap;
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
        
        
           final File folderObjects = new File("G:/bkp/$AVG/baseDir/Imports/Sprites/OBJECTS/");
           ArrayList<String> ObjectFiles=new ArrayList<>();
    for (final File fileEntry : folderObjects.listFiles()) {
        if (fileEntry.isDirectory()) {
           // listFilesForFolder(fileEntry);
        } else {
            ObjectFiles.add(fileEntry.getAbsolutePath());
        }}
        
       final File folder = new File("G:/bkp/$AVG/baseDir/Imports/Sprites/WOMEN-III/");
    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
           // listFilesForFolder(fileEntry);
        } else {
            if(Math.random()>.01)continue;
            System.out.println(fileEntry.getName());
//            Result result=getMatchingRegion(new String[]{
//               // "C:/Users/Maneesh/Desktop/face.png",
//                //"C:/Users/Maneesh/Desktop/face2.png",
//                "C:/Users/Maneesh/Desktop/face5.png",
//                "C:/Users/Maneesh/Desktop/Face4.png",
//                "C:/Users/Maneesh/Desktop/face3.png"},fileEntry.getAbsolutePath(), null);
            String[] strArr = null;

            strArr = ObjectFiles.toArray(new String[ObjectFiles.size()]);

            //Result result = getMatchingRegion(strArr, fileEntry.getAbsolutePath(), null);
            Result result = getMatchingRegionV2(strArr, fileEntry.getAbsolutePath(), null);

            BufferedImage img = getTheImage(fileEntry.getAbsolutePath(),false);
            Graphics2D graph = img.createGraphics();
            
            int bestScored=0;
            for(int i=0;i<result.getRectangle().length;i++)
            {
                
                graph.setColor(Color.GREEN);
                graph.draw(result.rectangle[i]);
                graph.setColor(Color.WHITE);
                graph.fill(new Rectangle(result.rectangle[i].x-10, result.rectangle[i].y-10,100,10));
                graph.setColor(Color.BLUE);
                graph.drawString(">>"+result.difference[i], result.rectangle[i].x, result.rectangle[i].y);
                graph.drawString(">>"+result.Tags[i], result.rectangle[i].x, result.rectangle[i].y+20);
                
                bestScored=result.difference[i]<result.difference[bestScored]?i:bestScored;
                
            }
            
            
            graph.setColor(Color.MAGENTA);
                graph.draw(result.rectangle[bestScored]);
                graph.setColor(Color.WHITE);
                graph.fill(new Rectangle(result.rectangle[bestScored].x-10, result.rectangle[bestScored].y-10,100,10));
                graph.setColor(Color.BLACK);
                graph.drawString(">>"+result.difference[bestScored], result.rectangle[bestScored].x, result.rectangle[bestScored].y);
                graph.drawString(">>"+result.Tags[bestScored], result.rectangle[bestScored].x, result.rectangle[bestScored].y+20);
                
            //graph.drawImage(targetScaled, rect.x, rect.y, null);
            graph.dispose();
            displayImage(img,"img");
            displayImage(cropImage(img, result.getRectangle()[result.getRectangle().length-1]),"img");
        }
        
    }



        getMatchingRegion(new String[]{"C:/Users/Maneesh/Desktop/face2.png"},"C:/Users/Maneesh/Desktop/Emiko_1513270388079.png", null);
        //getMatchingRegion("C:/Users/Maneesh/Desktop/SeirenFace.png","C:/Users/Maneesh/Desktop/Seiren.png");
        //SeirenFace

    }
      public static BufferedImage getMatchedRegion(String imagePattern, String TargetImage, java.awt.image.BufferedImage orTheBufferedImage) {
        java.awt.Rectangle[] result = getMatchingRegion(new String[]{imagePattern}, TargetImage, orTheBufferedImage).getRectangle();
        BufferedImage img = orTheBufferedImage!=null?orTheBufferedImage:HilbertCurvePatternDetect.getTheImage(TargetImage, false);
        //Graphics2D graph = img.createGraphics();
        //displayImage(cropImage(img, result[result.length-1]),"img");
        return cropImage(img, result[result.length - 1]);

    }
      
    public static BufferedImage getMatchedRegionMarked(String imagePattern, String TargetImage, java.awt.image.BufferedImage orTheBufferedImage) {
        Result result = getMatchingRegion(new String[]{imagePattern}, TargetImage, orTheBufferedImage);
        BufferedImage img = orTheBufferedImage != null ? orTheBufferedImage : HilbertCurvePatternDetect.getTheImage(TargetImage, false);
        Graphics2D graph = img.createGraphics();

        for (int i = 0; i < result.getRectangle().length; i++) {
            graph.setColor(Color.GREEN);
            graph.draw(result.rectangle[i]);
            graph.setColor(Color.WHITE);
            graph.fill(new Rectangle(result.rectangle[i].x - 10, result.rectangle[i].y - 10, 100, 10));
            graph.setColor(Color.BLUE);
            graph.drawString(">>" + result.difference[i], result.rectangle[i].x, result.rectangle[i].y);

        }
//Graphics2D graph = img.createGraphics();
        //displayImage(cropImage(img, result[result.length-1]),"img");
        return img;

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
    
    public static BufferedImage getTheImageblackWhite(BufferedImage img) {


       // BufferedImage blackWhite = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        BufferedImage blackWhite = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = blackWhite.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return blackWhite ;
    }
    /**
     * 
     * @param imagePattern
     * @param TargetImage
     * @param orTheBufferedImage
     * @return 
     */
    public static Result getMatchingRegion(String[] imagePattern, String TargetImage, BufferedImage orTheBufferedImage){
        
        /**
         * 1. Read pattern image
         * 2. Read the target image
         * 3. scale both to 2px by 2px
         * 4. With hilbert curve find close matching pixels
         * 5. Find the scale difference of pattern image and the Target image and then set the bits target from 1 to scale
         * 6. select those pixels that are closest match and then keep recording the matches. 
         */
     


        //BufferedImage imgT = orTheBufferedImage!=null?orTheBufferedImage:getTheImage(TargetImage, true);displayImage(imgT,"imgT");
        BufferedImage imgT = getTheImageblackWhite(BoofImageHelpers.getCannyEdges(orTheBufferedImage!=null?orTheBufferedImage:getTheImage(TargetImage, true)));displayImage(imgT,"imgT");

        int thePixelsToTraverse = 5;
        int bitsForObject = thePixelsToTraverse + 2;

        Rectangle result[] = new Rectangle[thePixelsToTraverse - 1];
        double differences[]=new double[thePixelsToTraverse - 1];
        String Tags[]=new String[thePixelsToTraverse - 1];
        Result resultObject=new Result(thePixelsToTraverse - 1);
        ArrayList<TrackingParameters> TrackingParametersList=new ArrayList<>();
        BufferedImage img[] = new BufferedImage[imagePattern.length ];// getTheImage(imagePattern[0], true);//displayImage(img,"img");
        //HashMap<String, BufferedImage> cache=new HashMap<>();
        HashMap<String, ArrayList<PointScore>> cachedScores=new HashMap<>();
        for (int i = 0; i < imagePattern.length; i++) {
           // img[i] =getTheImage(imagePattern[i], true);displayImage( img[i],"imgT");
            img[i] =getTheImageblackWhite(BoofImageHelpers.getCannyEdges( getTheImage(imagePattern[i], true)));//displayImage( img[i],imagePattern[i]);
        }
        
        for (int i = 1; i < thePixelsToTraverse; i++) {
            
            BufferedImage patternScaled = resizeImage(img[0], img[0].getWidth() * (i + i) / thePixelsToTraverse, img[0].getHeight() * (i + i) / thePixelsToTraverse);//displayImage(patternScaled,"patternScaled");

            BufferedImage targetScaled = imgT;//resizeImage(imgT,imgT.getWidth()*i/thePixelsToTraverse, imgT.getHeight()*i/thePixelsToTraverse);//displayImage(targetScaled,"targetScaled");

          //  HilbertCurve cForpattern = HilbertCurve.bits(thePixelsToTraverse).dimensions(2);
            HilbertCurve cForTarget = HilbertCurve.bits(bitsForObject).dimensions(2);

            //Very important calculation
            int pointsToTravese = (int) Math.pow(2, thePixelsToTraverse * 2);
            int pointsToTraveseC = (int) Math.pow(2, (bitsForObject) * 2);
            
            int pointBestMatched = 0, tempMatchCount = 0;
            int difference = Integer.MAX_VALUE;
            String matchedFile="";
            System.out.println("Loop to run for>>" + pointsToTraveseC + "x" + pointsToTravese + " at scale>>" + ((double) (i + i) / thePixelsToTraverse));
            //for(int traverseStart=0;traverseStart<pointsToTravese-pointsToTraveseC;traverseStart++){//Limit by target size
            for (int traverseStart = 0; traverseStart < pointsToTraveseC - pointsToTravese; traverseStart = (int) (traverseStart + pointsToTravese * .3))
            {//Limit by target size
                double count = pointsToTravese - 5;
                
                for(int k=0;k<imagePattern.length;k++){
                    TrackingParameters trackingParameters=new TrackingParameters();
                    trackingParameters.count=count;
                    trackingParameters.setObjectName(imagePattern[k].substring(imagePattern[k].lastIndexOf("\\")+1)+ "_" + (i + i));
                    ArrayList<PointScore> dataTomatch;
                    ArrayList<PointScore> dataToTarget=new ArrayList<>();
                    double stdDev=BoofImageHelpers.getImageStatistics(img[k]).getStandardDeviation();
                    
                    if (cachedScores.containsKey(k + "_" + (i + i)))
                        dataTomatch = cachedScores.get(k + "_" + (i + i));
                    else {
                        //patternScaled = resizeImage(img[k], img[k].getWidth() * (i + i) / thePixelsToTraverse, img[k].getHeight() * (i + i) / thePixelsToTraverse);//displayImage(patternScaled,"patternScaled");
                        dataTomatch=getHilbertValues(img[k], ((double)(i + i) / thePixelsToTraverse), thePixelsToTraverse);
                       
                        cachedScores.put(k + "_" + (i + i), dataTomatch);
                    }
                    int traverseStartp=0;
                    int skipCount=0;
                    int intersections=0;
                  //  for (int traverseStartp = 0; traverseStartp < pointsToTravese; traverseStartp++) {//Compare every point
                        for(PointScore pointScore:dataTomatch){
                        try {
                            long[] pointT = cForTarget.point(traverseStart);
                            pointT[0] = (long) (pointT[0] * targetScaled.getWidth() / Math.sqrt(pointsToTraveseC)) + pointScore.getX();
                            pointT[1] = (long) (pointT[1] * targetScaled.getHeight() / Math.sqrt(pointsToTraveseC)) + pointScore.getY();
                            PointScore targetPointScore=new PointScore();
                            targetPointScore.setX(pointScore.getX());
                            targetPointScore.setY(pointScore.getY());
                            dataToTarget.add(targetPointScore);
                            if (pointT[0] >= targetScaled.getWidth() || pointT[1] >= targetScaled.getHeight()) {

                                break;
                            }

                            //get pixel value
                            //System.out.println(pointT[0]+" /"+targetScaled.getWidth()+" "+pointT[1]+" /"+targetScaled.getHeight());
                             int pT = targetScaled.getRGB((int) (long) pointT[0], (int) (long) pointT[1]);
                             targetPointScore.setScore(pT);
                             if(Math.abs(pT-pointScore.getScore())<3*stdDev){
                                 intersections++;
                             }
                            if (!getColor(pointScore.getScore()).equals(Color.black)) { //Dont match exact black
                                if (count < skipCount) //Moving average method
                                {
                                    trackingParameters.maP = (trackingParameters.maP + pointScore.getScore()) / trackingParameters.count;
                                    trackingParameters.maT = (trackingParameters.maT + pT) / trackingParameters.count;
                                   
                                } else {
                                    trackingParameters.maP = (trackingParameters.maP + pointScore.getScore());
                                    trackingParameters.maT = (trackingParameters.maT + pT);
                                    
                                }

                                if (pointScore.getScore() < Color.GRAY.getRGB()) {//Compute shiny area better
                                    if (count < skipCount) //Moving average method
                                    {
                                        trackingParameters.maP2 = (trackingParameters.maP2 + pointScore.getScore()) / trackingParameters.count;
                                        trackingParameters.maT2 = (trackingParameters.maT2 + pT) / trackingParameters.count;
                                        
                                    } else {
                                        trackingParameters.maP2 = (trackingParameters.maP2 + pointScore.getScore());
                                        trackingParameters.maT2 = (trackingParameters.maT2 + pT);
                                        
                                    }
                                }
                                skipCount++;
                               
                            }else if (skipCount==0 && 100*(traverseStartp)/dataTomatch.size()>30){
                                break;
                            }
                            
                            trackingParameters.didLoopComplete = traverseStartp == dataTomatch.size() - 1;
                            traverseStartp++;
                            //
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            //break;
                        }
                         
                    }
                    
                     long[] pointT = cForTarget.point(traverseStart);
                     //Scale up the location
                     pointT[0] = (long) (pointT[0] * targetScaled.getWidth() / Math.sqrt(pointsToTraveseC));
                     pointT[1] = (long) (pointT[1] * targetScaled.getHeight() / Math.sqrt(pointsToTraveseC));    
                     trackingParameters.setRectUnderObservation(new Rectangle((int) (long) pointT[0], (int) (long) pointT[1], patternScaled.getWidth(), patternScaled.getHeight()));
                    
                    //Go for min difference region
                    if (trackingParameters.didLoopComplete) {
                        //System.out.println("Epoch >>"+traverseStart+" maP>>"+maP+"\t maT>>"+maT);
                        //double tempDecisionmaker = Math.sqrt(Math.pow(Math.abs(trackingParameters.maP - trackingParameters.maT),2) + 0.3*Math.pow(Math.abs(trackingParameters.maP2 - trackingParameters.maT2) ,2)) ;//=Math.abs(samePointsCountTarget-samePointsCount)
                        double tempDecisionmaker =(double) (intersections/(dataTomatch.size()+dataToTarget.size()-intersections)) ;//=Math.abs(samePointsCountTarget-samePointsCount)
                        
                        
                        pointBestMatched = difference < tempDecisionmaker ? pointBestMatched : traverseStart;
                        matchedFile=difference < tempDecisionmaker ?matchedFile:imagePattern[k].substring(imagePattern[k].lastIndexOf("\\")+1);
                        //Then >> update difference
                        difference = (int) (difference < tempDecisionmaker ? difference : tempDecisionmaker);
                        
                    }
                   TrackingParametersList.add(trackingParameters);
                }
                //
            }
            bitsForObject = bitsForObject + 1;

             System.out.println("Epoch >>" + i + " pointBestMatched>>" + pointBestMatched + "\t difference>>" + difference+"\t matchedFile>>"+matchedFile);
            //For global compute
//            differenceGlobal = difference < differenceGlobal ? difference : differenceGlobal;
//            pointBestMatchedGlobal = difference < differenceGlobal ? pointBestMatched : pointBestMatchedGlobal;
//            scaleSelected = difference < differenceGlobal ? bitsForObject - 1 : scaleSelected;
//            scaleSelectedForPattern = difference < differenceGlobal ? (i + i) : scaleSelectedForPattern;
//            System.out.println("Epoch >>" + i + " difference>>" + difference + "\t differenceGlobal>>" + differenceGlobal);

            try {
                long[] pointT = cForTarget.point(pointBestMatched);
                //Scale up the location
                pointT[0] = (long) (pointT[0] * targetScaled.getWidth() / Math.sqrt(pointsToTraveseC));
                pointT[1] = (long) (pointT[1] * targetScaled.getHeight() / Math.sqrt(pointsToTraveseC));
//                System.out.println(pointT[0] + " " + pointT[1] + " " + patternScaled.getWidth() + " " + patternScaled.getHeight());
//                System.out.println(" Target>> " + targetScaled.getWidth() + " " + targetScaled.getHeight());

                Rectangle rect = new Rectangle((int) (long) pointT[0], (int) (long) pointT[1], patternScaled.getWidth(), patternScaled.getHeight());
                result[i - 1] = rect;
                differences[i - 1]=difference;
                Tags[i-1]=matchedFile;
//                BufferedImage framedImage = new BufferedImage(targetScaled.getWidth(), targetScaled.getHeight(), targetScaled.getType());

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
        
        //Print Tracking parameters:
        //TrackingParametersList.forEach(s->System.out.println(s.toStringNameAndScore()));
        resultObject.setRectangle(result);
        resultObject.setDifference(differences);
        resultObject.setTags(Tags);

        return resultObject;
}
    
    public static Result getMatchingRegionV2(String[] imagePattern, String TargetImage, BufferedImage orTheBufferedImage){
        
        /**
         * 1. Read pattern image
         * 2. Read the target image
         * 3. scale both to 2px by 2px
         * 4. With hilbert curve find close matching pixels
         * 5. Find the scale difference of pattern image and the Target image and then set the bits target from 1 to scale
         * 6. select those pixels that are closest match and then keep recording the matches. 
         */
     


        BufferedImage imgT = orTheBufferedImage!=null?orTheBufferedImage:getTheImage(TargetImage, true);displayImage(imgT,"imgT");
        BufferedImage imgTEdges = getTheImageblackWhite(BoofImageHelpers.getCannyEdges(orTheBufferedImage!=null?orTheBufferedImage:getTheImage(TargetImage, true)));displayImage(imgTEdges,"imgT");

        int thePixelsToTraverse = 5;
         //Very important calculation
        int pointsToTravese = (int) Math.pow(2, thePixelsToTraverse*2);
        
        int bitsForObject = thePixelsToTraverse + 2;

        Rectangle result[] = new Rectangle[thePixelsToTraverse - 1];
        double differences[]=new double[thePixelsToTraverse - 1];
        String Tags[]=new String[thePixelsToTraverse - 1];
        Result resultObject=new Result(thePixelsToTraverse - 1);
        ArrayList<TrackingParameters> TrackingParametersList=new ArrayList<>();
        BufferedImage img[] = new BufferedImage[imagePattern.length ];// getTheImage(imagePattern[0], true);//displayImage(img,"img");
        BufferedImage imgEdges[] = new BufferedImage[imagePattern.length ];// getTheImage(imagePattern[0], true);//displayImage(img,"img");
        
        HashMap<String, BufferedImage> cache=new HashMap<>();
        HashMap<String, Statistic> cacheOfStats=new HashMap<>();
        HashMap<String, BufferedImage> cacheEdges=new HashMap<>();
        //HashMap<String, ArrayList<PointScore>> cachedScores=new HashMap<>();
        for (int i = 0; i < imagePattern.length; i++) {
            img[i] =getTheImage(imagePattern[i], true);//displayImage( img[i],"imgT");
            imgEdges[i] =getTheImageblackWhite(BoofImageHelpers.getCannyEdges( getTheImage(imagePattern[i], true)));//displayImage( img[i],imagePattern[i]);
        }
        
       
        for (int i = 1; i < thePixelsToTraverse; i++) {
            
            BufferedImage patternScaled = resizeImage(img[0], img[0].getWidth() * (i + i) / thePixelsToTraverse, img[0].getHeight() * (i + i) / thePixelsToTraverse);//displayImage(patternScaled,"patternScaled");
            Statistic statistic=new Statistic();
            BufferedImage patternScaledEdges = resizeImage(imgEdges[0], imgEdges[0].getWidth() * (i + i) / thePixelsToTraverse, imgEdges[0].getHeight() * (i + i) / thePixelsToTraverse);//displayImage(patternScaled,"patternScaled");

            HilbertCurve cForTarget = HilbertCurve.bits(bitsForObject).dimensions(2);

            
            //int pointsToTraveseC = (int) Math.pow(2, (bitsForObject) * 2);
            
            int pointBestMatched = 0;
            int difference = Integer.MAX_VALUE;
            String matchedFile="";
            //System.out.println("Loop to run for>>" + pointsToTraveseC + "x" + pointsToTravese + " at scale>>" + ((double) (i + i) / thePixelsToTraverse));
            //for(int traverseStart=0;traverseStart<pointsToTravese-pointsToTraveseC;traverseStart++){//Limit by target size
            for (int traverseStart = 0; traverseStart < pointsToTravese ; traverseStart ++)
            {
                for (int k = 0; k < imagePattern.length; k++) {
                    TrackingParameters trackingParameters = new TrackingParameters();
                   
                    trackingParameters.setObjectName(imagePattern[k].substring(imagePattern[k].lastIndexOf("\\") + 1) + "_" + (i + i));
                    if (cache.containsKey(k + "_" + (i + i))) {
                        patternScaled = cache.get(k + "_" + (i + i));
                        statistic=cacheOfStats.get(k + "_" + (i + i));
                        patternScaledEdges=cacheEdges.get(k + "_" + (i + i));
                    } else {
                        patternScaled = resizeImage(img[k], img[k].getWidth() * (i + i) / thePixelsToTraverse, img[k].getHeight() * (i + i) / thePixelsToTraverse);//displayImage(patternScaled,"patternScaled");
                        statistic=BoofImageHelpers.getImageStatistics(patternScaled);
                        patternScaledEdges= resizeImage(imgEdges[k], imgEdges[k].getWidth() * (i + i) / thePixelsToTraverse, imgEdges[k].getHeight() * (i + i) / thePixelsToTraverse);//displayImage(patternScaled,"patternScaled");
                        
                        cache.put(k + "_" + (i + i), patternScaled);
                        cacheOfStats.put(k + "_" + (i + i), statistic);
                        cacheEdges.put(k + "_" + (i + i), patternScaledEdges);
                    }

                    long[] pointT = cForTarget.point(traverseStart);
                    //Scale up the location
                    pointT[0] = (long) (pointT[0] * imgT.getWidth() / Math.sqrt(pointsToTravese));
                    pointT[1] = (long) (pointT[1] * imgT.getHeight() / Math.sqrt(pointsToTravese));

                    Rectangle rect = new Rectangle((int) (long) pointT[0], (int) (long) pointT[1], patternScaled.getWidth(), patternScaled.getHeight());

                    BufferedImage targetSection = cropImage(imgT, rect);
                    BufferedImage targetSectionEdges = cropImage(imgTEdges, rect);
                    Statistic statsTarget=BoofImageHelpers.getImageStatistics(targetSection);

                    //Go for min difference region
                    if ( /*Math.abs(statistic.mean-statsTarget.mean)<2*(statistic.standardDeviation+statsTarget.standardDeviation)&&*/
                            patternScaled.getWidth() == targetSection.getWidth() && patternScaled.getHeight() == targetSection.getHeight()) {
//                                           System.out.println(pointT[0] + " " + pointT[1] + " " + patternScaled.getWidth() + " " + patternScaled.getHeight());
//                    System.out.println(" Target>> " + targetSection.getWidth() + " " + targetSection.getHeight());
//                   
//Check mean and variances as well.
                        double tempDecisionmaker = BoofImageHelpers.getMeanDiffSq(patternScaled, targetSection);//+.5*BoofImageHelpers.getMeanDiffSq(patternScaledEdges, targetSectionEdges);
                        // System.out.println(" tempDecisionmaker>> " + tempDecisionmaker);
                        pointBestMatched = difference < tempDecisionmaker ? pointBestMatched : traverseStart;
                        matchedFile = difference < tempDecisionmaker ? matchedFile : imagePattern[k].substring(imagePattern[k].lastIndexOf("\\") + 1);
                        //Then >> update difference
                        difference = (int) (difference < tempDecisionmaker ? difference : tempDecisionmaker);
                    }
                }
                //
            }
            bitsForObject = bitsForObject + 1;

             System.out.println("Epoch >>" + i + " pointBestMatched>>" + pointBestMatched + "\t difference>>" + difference+"\t matchedFile>>"+matchedFile);

            try {
                long[] pointT = cForTarget.point(pointBestMatched);
                //Scale up the location
                pointT[0] = (long) (pointT[0] * imgT.getWidth() / Math.sqrt(pointsToTravese));
                pointT[1] = (long) (pointT[1] * imgT.getHeight() / Math.sqrt(pointsToTravese));
//                System.out.println(pointT[0] + " " + pointT[1] + " " + patternScaled.getWidth() + " " + patternScaled.getHeight());
//                System.out.println(" Target>> " + targetScaled.getWidth() + " " + targetScaled.getHeight());

                Rectangle rect = new Rectangle((int) (long) pointT[0], (int) (long) pointT[1], patternScaled.getWidth(), patternScaled.getHeight());
                result[i - 1] = rect;
                differences[i - 1]=difference;
                Tags[i-1]=matchedFile;

            } catch (Exception ex) {
                Logger.getLogger(HilbertCurvePatternDetect.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Print Tracking parameters:
        //TrackingParametersList.forEach(s->System.out.println(s.toStringNameAndScore()));
        resultObject.setRectangle(result);
        resultObject.setDifference(differences);
        resultObject.setTags(Tags);

        return resultObject;
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

    // first check if we need to bitsForObject width
    if (original_width > bound_width) {
        //scale width to fit
        new_width = bound_width;
        //scale height to maintain aspect ratio
        new_height = (new_width * original_height) / original_width;
    }

    // then check if we need to bitsForObject even with the new height
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
    /**
     * 
     * @param imageUnderObservation
     * @param scale
     * @param bits
     * @return 
     */
    public static ArrayList<PointScore> getHilbertValues(BufferedImage imageUnderObservation, double scale, int bits) {
        int pointsToTravese = (int) Math.pow(2, (bits) * 2);
        BufferedImage patternScaled = resizeImage(imageUnderObservation, (int)(imageUnderObservation.getWidth() * scale), (int)(imageUnderObservation.getHeight() * scale));//displayImage(patternScaled,"patternScaled");
        HilbertCurve cForpattern = HilbertCurve.bits(bits).dimensions(2);
        ArrayList<PointScore> result=new ArrayList<>();
        for (int traverseStartp = 0; traverseStartp < pointsToTravese; traverseStartp++) {//Compare every point
            PointScore pointScore=new PointScore();
            try {
                //Get the XY in images to match
                long[] pointP = cForpattern.point(traverseStartp);
                pointP[0] = (long) (pointP[0] * patternScaled.getWidth() / Math.sqrt(pointsToTravese));
                pointP[1] = (long) (pointP[1] * patternScaled.getHeight() / Math.sqrt(pointsToTravese));

                if (pointP[0] >= patternScaled.getWidth() || pointP[1] >= patternScaled.getHeight()) {
                    break;
                }
                //get pixel value
                int p = patternScaled.getRGB((int) (long) pointP[0], (int) (long) pointP[1]);
                //System.out.println(getColor(p).equals(Color.BLACK));
                if(getColor(p).equals(Color.BLACK)){continue;}//Ignore the backrgound.
                if(getColor(p).equals(Color.TRANSLUCENT)){continue;}//Ignore the backrgound.
                ;
                pointScore.setX(pointP[0]);
                pointScore.setY(pointP[1]);
                pointScore.setScore(p);
                result.add(pointScore);
                //
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                //break;
            }
        }
        return result;
    }

   

}

class PointScore{

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    
    long x;
    long y;
    int score;
}

class Result{
    
    Result(){}
    
    public Result(int size){
    }
    
    public Rectangle[] getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle[] rectangle) {
        this.rectangle = rectangle;
    }

    public long[] getPointBestMatched() {
        return pointBestMatched;
    }

    public void setPointBestMatched(long[] pointBestMatched) {
        this.pointBestMatched = pointBestMatched;
    }

    public double[] getDifference() {
        return difference;
    }

    public void setDifference(double[] difference) {
        this.difference = difference;
    }
    String[] Tags;

    public String[] getTags() {
        return Tags;
    }

    public void setTags(String[] Tags) {
        this.Tags = Tags;
    }
    
    Rectangle[] rectangle;
    long[] pointBestMatched;
    double[] difference;
   
}

 class TrackingParameters {

        public double getMaP() {
            return maP;
        }

        public void setMaP(double maP) {
            this.maP = maP;
        }

        public double getMaP2() {
            return maP2;
        }

        public void setMaP2(double maP2) {
            this.maP2 = maP2;
        }

        public double getMaT() {
            return maT;
        }

        public void setMaT(double maT) {
            this.maT = maT;
        }

        public double getMaT2() {
            return maT2;
        }

        public void setMaT2(double maT2) {
            this.maT2 = maT2;
        }

        public double getCount() {
            return count;
        }

        public void setCount(double count) {
            this.count = count;
        }

        public boolean isDidLoopComplete() {
            return didLoopComplete;
        }

        public void setDidLoopComplete(boolean didLoopComplete) {
            this.didLoopComplete = didLoopComplete;
        }
        
        
        String ObjectName;

    public String getObjectName() {
        return ObjectName;
    }

    public void setObjectName(String ObjectName) {
        this.ObjectName = ObjectName;
    }

        double maP = 0, maP2 = 0;
        double maT = 0, maT2 = 0;
        double count = 5;
        double score=1;
        Rectangle rectUnderObservation;

    public Rectangle getRectUnderObservation() {
        return rectUnderObservation;
    }

    public void setRectUnderObservation(Rectangle rectUnderObservation) {
        this.rectUnderObservation = rectUnderObservation;
    }

    public double getScore() {
        return Math.sqrt(Math.pow(Math.abs(maP - maT),2) + Math.pow(Math.abs(maP2 - maT2) ,2)) ;//=Math.abs(samePointsCountTarget-samePointsCount)
                       
    }

    public void setScore(double score) {
        this.score = score;
    }
        boolean didLoopComplete = false;
        
        public void printValues(){
                 
            System.out.println("score>>"+score);
            System.out.println("maP>>"+maP+" maP2>>"+maP2);
            System.out.println("maT>>"+maT+" maT2>>"+maT2);
            System.out.println("count>>"+count);
            System.out.println("ObjectName>>"+ObjectName);
        }
        
        @Override
        public String toString(){
             double tempDecisionmaker = Math.sqrt(Math.pow(Math.abs(maP - maT),2) + Math.pow(Math.abs(maP2 - maT2) ,2)) ;//=Math.abs(samePointsCountTarget-samePointsCount)
              StringBuilder sb=new StringBuilder();
            sb.append("\n").append("tempDecisionmaker>>").append(tempDecisionmaker);
            sb.append("\n").append("maP>>").append(maP).append(" maP2>>").append(maP2);
            sb.append("\n").append("maT>>").append(maT).append(" maT2>>").append(maT2);
            sb.append("\n").append("count>>").append(count);
            sb.append("\n").append("ObjectName>>").append(ObjectName);
            
            return sb.toString();
        }
        
        public String toStringNameAndScore(){
             double tempDecisionmaker = Math.sqrt(Math.pow(Math.abs(maP - maT),2) + Math.pow(Math.abs(maP2 - maT2) ,2)) ;//=Math.abs(samePointsCountTarget-samePointsCount)
              StringBuilder sb=new StringBuilder();
            
           // sb.append("\n").append("maP>>").append(maP).append(" maP2>>").append(maP2);
            //sb.append("\n").append("maT>>").append(maT).append(" maT2>>").append(maT2);
           // sb.append("\n").append("count>>").append(count);
            sb.append("ObjectName>>").append(ObjectName);
            sb.append("tempDecisionmaker>>").append(tempDecisionmaker);
            
            return sb.toString();
        }
    }