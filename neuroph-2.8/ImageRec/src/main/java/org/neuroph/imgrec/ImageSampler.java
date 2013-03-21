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

package org.neuroph.imgrec;

import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;
import org.neuroph.imgrec.image.ImageType;

/**
 * This class uses a given Robot object to sample images from the screen at an 
 * arbitrary sampling resolution.  The coordinates of the scanning rectangle 
 * is (x, y) expressed as a fraction of the screen resolution, with the width also being
 * a fraction.
 * 
 * For example, when the screen is 800x600 resolution, to scan a rectangle with
 * xy coordinates (400,300) to xy (600, 400), use the following rectangle as an argument:
 * 
 * new Rectangle2D.Double(0.5, 0.5, 0.25, 0.16667)
 * 
 * Scanning using sampling is faster than scanning using a screenshot, but is
 * prone to introduce tearing and shearing into the scanned image.  Scanning
 * from a screenshot has no tearing or shearing at the cost of speed.
 *
 * Also provides method for downsampling (scaling) image.
 * This class is based on the code from tileclassification by Jon Tait.
 * 
 * @author Jon Tait
 *
 */
public class ImageSampler
{
//	/**
//	 * Scans screen location using screenshot
//	 * @param robot
//	 * @param rectangleAsDecimalPercent
//	 * @param samplingResolution
//	 * @return image sample from the specified location
//	 */
//	public static BufferedImage scanLocationUsingScreenshot(Robot robot,
//			Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution)
//	{
//		return scanLocationUsingScreenshot(robot, rectangleAsDecimalPercent, samplingResolution, BufferedImage.TYPE_INT_RGB);
//	}
//	
//	/**
//	 * Scans screen location using screenshot
//	 * @param robot
//	 * @param rectangleAsDecimalPercent
//	 * @param samplingResolution
//	 * @param imageType
//	 * @return image sample from the specified location
//	 */
//	public static BufferedImage scanLocationUsingScreenshot(Robot robot,
//			Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution,
//			int imageType)
//	{
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		
//		int inspectX = (int) Math.round((screenSize.getWidth() * rectangleAsDecimalPercent.x));
//		int inspectY = (int) Math.round((screenSize.getHeight() * rectangleAsDecimalPercent.y));
//		int inspectWidth = (int) Math.round((screenSize.getWidth() * rectangleAsDecimalPercent.width));
//		int inspectHeight = (int) Math.round((screenSize.getHeight() * rectangleAsDecimalPercent.height));
//		
//		Rectangle screenRect = new Rectangle(inspectX, inspectY, inspectWidth, inspectHeight);
//		BufferedImage screenCapture = robot.createScreenCapture(screenRect);
//		
//		
//		return downSampleImage(samplingResolution, screenCapture, imageType);
//	}
//
//        /**
//         * Scales image to the specified dimension
//         * @param samplingResolution sampling resolution/image size
//         * @param bigImg image to scale
//         * @return image scaled/downsampled to the specified dimension/resolution
//         */
//	public static BufferedImage downSampleImage(
//			Dimension samplingResolution, BufferedImage bigImg) {
//		return downSampleImage(samplingResolution, bigImg, BufferedImage.TYPE_INT_RGB);
//	}
//
//
//        /**
//         * Scales image to the specified dimension
//         * @param samplingResolution sampling resolution/image size
//         * @param bigImg image to scale
//         * @param returnImageType type of the return image
//         * @return image scaled/downsampled to the specified dimension/resolution
//         */
//	public static BufferedImage downSampleImage(Dimension samplingResolution,
//			BufferedImage bigImg, int returnImageType) {
//		int inspectX;
//		int inspectY;
//		int numberOfSamplesAcross = samplingResolution.getWidth();
//		int numberOfSamplesDown = samplingResolution.getHeight();
//		
//		if(bigImg.getWidth() <= numberOfSamplesAcross || bigImg.getHeight() <= numberOfSamplesDown) {
//			return bigImg;
//		}
//		
//		BufferedImage img = new BufferedImage(
//				numberOfSamplesAcross, numberOfSamplesDown, 
//				returnImageType);
//		
//		double samplingIncrementX = bigImg.getWidth() / (samplingResolution.getWidth() - 1);
//		double samplingIncrementY = bigImg.getHeight() / (samplingResolution.getHeight() - 1);
//		
//		double sampleX = 0;
//		double sampleY = 0;
//		for(int y=0; y < numberOfSamplesDown; y++) {
//			for(int x=0; x < numberOfSamplesAcross; x++) {
//				inspectX = (int) Math.round(sampleX);
//				inspectY = (int) Math.round(sampleY);
//				
//				if(inspectX >= bigImg.getWidth()) {
//					inspectX = bigImg.getWidth() - 1;
//				}
//				if(inspectY >= bigImg.getHeight()) {
//					inspectY = bigImg.getHeight() - 1;
//				}
//				int color = bigImg.getRGB(inspectX, inspectY);
//				img.setRGB(x, y, color);
//				sampleX+=samplingIncrementX;
//			}
//			sampleX=0;
//			sampleY+=samplingIncrementY;
//		}
//		
//		return img;
//	}
//	
//	/**
//	 * Scans screen location using sampling
//	 * @param robot an instance of java.awt.Robot for the scaned screen
//	 * @param rectangleAsDecimalPercent
//	 * @param samplingResolution
//	 * @return image sample from the specified location
//	 */
//	public static BufferedImage scanLocationUsingSampling(Robot robot,
//			Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution)
//	{
//		return scanLocationUsingSampling(robot, rectangleAsDecimalPercent, samplingResolution, BufferedImage.TYPE_INT_RGB);
//	}
//	
//	/**
//	 * Scans screen location using sampling
//	 * @param robot an instance of java.awt.Robot for the scaned screen
//	 * @param rectangleAsDecimalPercent
//	 * @param samplingResolution
//	 * @param imageType
//	 * @return image sample from the specified location
//	 */
//	public static BufferedImage scanLocationUsingSampling(Robot robot,
//			Rectangle2D.Double rectangleAsDecimalPercent, Dimension samplingResolution,
//			int imageType)
//	{
//		double slotULXAsDecimalPercent = rectangleAsDecimalPercent.x; 
//		double slotULYAsDecimalPercent = rectangleAsDecimalPercent.y;
//		double sampleIncrementAcrossAsDecimalPercent = rectangleAsDecimalPercent.width / (samplingResolution.getWidth()-1);
//		double sampleIncrementDownAsDecimalPercent = rectangleAsDecimalPercent.height / (samplingResolution.getHeight()-1);
//		
//		int numberOfSamplesAcross = samplingResolution.width;
//		int numberOfSamplesDown = samplingResolution.height;
//		BufferedImage img = new BufferedImage(
//				numberOfSamplesAcross, numberOfSamplesDown, 
//				imageType);
//		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		
//		waitForVerticalScreenRefresh();
//		
//		double inspectXAsDecimalPercent = slotULXAsDecimalPercent;
//		double inspectYAsDecimalPercent = slotULYAsDecimalPercent;
//		for(int y=0; y < numberOfSamplesDown; y++) {
//			for(int x=0; x < numberOfSamplesAcross; x++) {
//				int inspectX = (int) Math.round((screenSize.width * inspectXAsDecimalPercent));
//				int inspectY = (int) Math.round((screenSize.height * inspectYAsDecimalPercent));
//				Color color = robot.getPixelColor(inspectX, inspectY);
//				img.setRGB(x, y, color.getRGB());
//				inspectXAsDecimalPercent+=sampleIncrementAcrossAsDecimalPercent;
//			}
//			inspectXAsDecimalPercent = slotULXAsDecimalPercent;
//			inspectYAsDecimalPercent+=sampleIncrementDownAsDecimalPercent;
//		}
//		return img;
//	}
//
//	/**
//	 * Another name for this is "Vertical Sync".  Minimizes frame shearing.
//	 */
//	private static void waitForVerticalScreenRefresh()
//	{
//		Toolkit.getDefaultToolkit().sync();
//	}
    
	public static Image downSampleImage(Dimension samplingResolution,
			Image bigImage, int returnImageType) {
		int inspectX;
		int inspectY;
		int numberOfSamplesAcross = samplingResolution.getWidth();
		int numberOfSamplesDown = samplingResolution.getHeight();
		
		if(bigImage.getWidth() <= numberOfSamplesAcross || bigImage.getHeight() <= numberOfSamplesDown) {
			return bigImage;
		}
		
		Image image = ImageFactory.createImage(new Integer(numberOfSamplesAcross), new Integer(numberOfSamplesDown), new Integer(returnImageType));
		
		double samplingIncrementX = bigImage.getWidth() / (samplingResolution.getWidth() - 1);
		double samplingIncrementY = bigImage.getHeight() / (samplingResolution.getHeight() - 1);
		
		double sampleX = 0;
		double sampleY = 0;
		for(int y=0; y < numberOfSamplesDown; y++) {
			for(int x=0; x < numberOfSamplesAcross; x++) {
				inspectX = (int) Math.round(sampleX);
				inspectY = (int) Math.round(sampleY);
				
				if(inspectX >= bigImage.getWidth()) {
					inspectX = bigImage.getWidth() - 1;
				}
				if(inspectY >= bigImage.getHeight()) {
					inspectY = bigImage.getHeight() - 1;
				}
				int color = bigImage.getPixel(inspectX, inspectY);
				image.setPixel(x, y, color);
				sampleX+=samplingIncrementX;
			}
			sampleX=0;
			sampleY+=samplingIncrementY;
		}
		
		return image;
	}    
        
	public static Image downSampleImage(Dimension samplingResolution, Image image) {
		return downSampleImage(samplingResolution, image, ImageType.J2SE_TYPE_INT_RGB);
	}        
    
}
