/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.contrib.LSTM;

import java.io.IOException;
import java.util.ArrayList;
import marvin.image.MarvinImage;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;
import org.bytedeco.javacpp.avcodec;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 *
 * @author Maneesh
 */
public class MarvinVideoProcessing implements Runnable{

    private MarvinVideoInterface    videoAdapter;
    private MarvinImage             videoFrame;

    public MarvinVideoProcessing(){
        try{
            // Create the VideoAdapter used to load the video file
            videoAdapter = new MarvinJavaCVAdapter();
            videoAdapter.loadResource("G:/bkp/$AVG/baseDir/Imports/taxi.mp4");

            // Start the thread for requesting the video frames 
            new Thread(this).start();
        }
        catch(MarvinVideoInterfaceException e){e.printStackTrace();}
    }

    @Override
    public void run() {
        try{
            while(true){
                // Request a video frame
                videoFrame = videoAdapter.getFrame();
                HilbertCurvePatternDetect.displayImage(videoFrame.getBufferedImage(), "SomeFrame");
                
            }
        }catch(MarvinVideoInterfaceException e){e.printStackTrace();}
    }

    public static void main(String[] args) throws FrameGrabber.Exception, IOException, FrameRecorder.Exception {
        //     MarvinVideoProcessing m = new MarvinVideoProcessing();

        FFmpegFrameGrabber g = new FFmpegFrameGrabber("G:/bkp/$AVG/baseDir/Imports/test2.mp4");
         g.start();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("G:/bkp/$AVG/baseDir/Imports/taxiProcessed.mp4", g.getImageWidth(), g.getImageHeight());
       // recorder.setFrameRate(1);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
         //   recorder.setVideoBitrate(9000);
            recorder.setFormat("mp4");
          //  recorder.setVideoQuality(0); // maximum quality  
            recorder.start();
            
       
        Java2DFrameConverter converter = new Java2DFrameConverter();
        for (int i = 0; i < g.getLengthInTime(); i++) {
           //if(Math.random()>.01){g.grabImage();continue;}
            //HilbertCurvePatternDetect.displayImage(HilbertCurvePatternDetect.getMatchedRegionMarked("C:/Users/Maneesh/Desktop/face2.png", " ", converter.convert(g.grabImage())), ">> " + i);
            //ImageIO.write(converter.convert(g.grabImage()), "png", new File("frame-dump/video-frame-" + System.currentTimeMillis() + ".png"));
           recorder.record(converter.convert(HilbertCurvePatternDetect.getMatchedRegionMarked("C:/Users/Maneesh/Desktop/face2.png", " ", converter.convert(g.grabImage()))));
        }

        g.stop();
        recorder.stop();

    }
    
    
    public static void convertJPGtoMovie(ArrayList<String> links, String vidPath) {
        OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(vidPath, 640, 720);
        try {
            recorder.setFrameRate(1);
            recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
            recorder.setVideoBitrate(9000);
            recorder.setFormat("mp4");
            recorder.setVideoQuality(0); // maximum quality  
            recorder.start();
            for (int i = 0; i < links.size(); i++) {
                recorder.record(grabberConverter.convert(cvLoadImage(links.get(i))));
            }
            recorder.stop();
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }  
}
