
package org.neuroph.imgrec;

import java.awt.Color;
import java.awt.image.BufferedImage;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageJ2SE;

/**
 *
 * @author Mihailo Stupar
 */
public class FractionHSLData {
    
    /**
     * Image width
     */
    private int width;
	
    /**
     * Image height
     */
    private int height;

    /**
     * Matrix which contains the hue componenet for each image pixel
     */
    protected double[][] hueValues;

    /**
     * Matrix which contains the saturation componenet for each image pixel
     */
    protected double[][] saturationValues;

    /**
     * Matrix which contains the intensity componenet for each image pixel
     */
    protected double[][] lightnessValues;
	
    /**
     * Single array with the hue, saturation and lightness components for each image pixel
     */
    protected double[] flattenedHSLValues;
        
        
    /**
     * Single array only with hue components for each pixel
     */
    protected double [] flattenedHueValues;
        
    public FractionHSLData(BufferedImage img) {
	
		
	if(img!=null){
		width = img.getWidth();
		height = img.getHeight();
			
		hueValues = new double[height][width];
		saturationValues = new double[height][width];
		lightnessValues = new double[height][width];
		flattenedHSLValues = new double[width * height * 3];
                flattenedHueValues = new double[width * height];
			
		populateHSLArrays(new ImageJ2SE(img));  
	}
	else{//no input image specified so default all values
		width=0;
		height=0;
		hueValues = null;
		saturationValues = null;
		lightnessValues = null;
		flattenedHSLValues = null;
                flattenedHueValues = null;
	}
    }
        
    
	public FractionHSLData(Image img)
	{
		width = img.getWidth();
		height = img.getHeight();
		
		hueValues = new double[height][width];
		saturationValues = new double[height][width];
		lightnessValues = new double[height][width];
		flattenedHSLValues = new double[width * height * 3];
                flattenedHueValues = new double[width * height];
		
		populateHSLArrays(img);
	}    
    
    
    /**
	 * Fills the HSL matrices from image
	 * @param img image to get rgb data from
	 * 
	 */
	
    /**
     * Fills the HSL matrices from image - this is where conversion from RGB to HSL is done
     * @param image image to use    
     */
    protected void populateHSLArrays(Image image) {
            
            double red;
            double green;
            double blue;
            
            double Cmax;
            double Cmin;
            
            double delta;
            
            for (int j = 0; j < width; j++) {
                for (int i = 0; i < height; i++) {
                    
                    Color color= new Color(image.getPixel(j, i)); // it was getRGB
                    
                    red = color.getRed();
                    green = color.getGreen();
                    blue = color.getBlue();
                    
                    red = red/255;
                    green = green/255;
                    blue = blue/255;
                    
                    Cmax = Math.max(red, Math.max(green, blue));
                    Cmin = Math.min(red, Math.min(green, blue));
                    
                    delta = Cmax-Cmin;
                    
                    double hue = 0;
                    if (delta != 0) {
                        if (Cmax == red) 
                            hue = 60*(((green - blue)/delta)%6);                       
                        if (Cmax == green)
                            hue = 60*(((blue-red)/delta)+2);
                        if (Cmax == blue)
                            hue = 60*((red-green)/delta + 4);
                    } else {
                        double a = (2*red-green-blue)/2;
                        double b = (green-blue)*Math.sqrt(3)/2;
                        hue = Math.atan2(b, a);
                    }                      
                    hueValues[i][j] = hue/360; //podeli sa 360 da vrednot bude izmedju 0-1
                    
                    double lightness = (Cmax + Cmin)/2;
                    lightnessValues[i][j] = lightness;
                    
                    double saturation = 0;
                    if (delta == 0)
                        saturation = 0;
                    else
                        saturation = delta/(1-Math.abs(2*lightness-1));                   
                    saturationValues[i][j] = saturation;
                }
            }
        }
        
       	public void fillFlattenedHueValues () {
            
            int position = 0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < height; j++) {
                    flattenedHueValues[position++] = hueValues[i][j];
             }
        }
    }
        
    public void fillFlattenedHSLValues(){
            
        int positionHue = 0;
        int positionSaturation = 1;
        int positionLighteness = 2;
            
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                flattenedHSLValues[positionHue] = hueValues[i][j];
                flattenedHSLValues[positionSaturation] = saturationValues[i][j];
                flattenedHSLValues[positionLighteness] = lightnessValues[i][j];
                positionHue = positionHue + 3;
                positionSaturation = positionSaturation + 3;
                positionLighteness = positionLighteness + 3;
            }
        }
            
            
    }
        
    public double[][] getHueValues() {
    	return hueValues;
    }
    	
    /**
     * Returns saturation component for the entire image
     * @return 2d array in the form: [row][column]
    */
    public double[][] getSaturationValues()
    {
    	return saturationValues;
    }
    	
    /**
    * Returns lightness component for the entire image
    * @return 2d array in the form: [row][column]
    */
    public double[][] getLightnessValues() {
        return lightnessValues;
    }

    public double[] getFlattenedHSLValues() {
        return flattenedHSLValues;
    }
        
        
        
}