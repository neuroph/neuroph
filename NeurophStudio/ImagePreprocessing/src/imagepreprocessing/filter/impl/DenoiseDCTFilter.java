/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.filter.impl;

import imagepreprocessing.helper.PreprocessingHelper;
import imagepreprocessing.filter.ImageFilter;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 *
 * @author Mihailo Stupar
 */

//http://www.lokminglui.com/dct.pdf
//http://www.ipol.im/pub/art/2011/ys-dct/
public class DenoiseDCTFilter implements ImageFilter,Serializable{
    
    
    private transient BufferedImage originalImage;
    private transient BufferedImage filteredImage;
    
    
    private double sigma;
    private int N;
    private int qualityLevel;

    public DenoiseDCTFilter() {
        sigma = 20;
        N = 16;
        qualityLevel = 95;
    }

    
    
    
     @Override
    public BufferedImage processImage(BufferedImage image) {
		
        int width = image.getWidth();
	int height = image.getHeight();

        while (width % N != 0) {
            width--;
        }
        
        while (height % N != 0) {
            height--;
        }
        
        originalImage = resize(image, width, height);

        filteredImage = new BufferedImage(width, height, originalImage.getType());
    

        int numXpatches = width/N;
        int numYpatches = height/N;
        
        double treshold = 3*sigma;
        

        double [][] T = createT();
        
        
        double [][] Tinv = null;
        
        if (N==16) {
            Tinv = createTinv16X16();
        }
        if (N==8) {
            Tinv = createTinv();
        }
        
         for (int i = 0; i < numXpatches; i++) {
             for (int j = 0; j < numYpatches; j++) {
                 
                 double [][] M = createM(i, j);
                 double[][] D = multiply(multiply(T, M), Tinv);
                 tresholdDmatrix(D, treshold);
                 
                 int [][] Q = null;
                 
                 if (N==16) {
                     Q = createQ16X16();
                 }
                 if (N==8){
                     Q = createQ50(); 
                     updateQ(Q);
                 }
                 int [][] C = createC(D, Q);
                 double [][] R = createR(Q, C);
                 int [][] Nmatrix = createN(Tinv, R, T);
                 fillFilteredImage(i, j, Nmatrix);
             }
         }
        return filteredImage;
    }
    
    public  BufferedImage resize(BufferedImage img, int newW, int newH) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
    }
    
    public double[][] createT () { 
        double [][] T = new double [N][N];
        for (int i = 0; i < N; i++) {
            T[0][i] = roundFourDecimals(1.0/Math.sqrt(N));
        }     
        for (int i = 1; i < N; i++) {
            for (int j = 0; j < N; j++) {
                
                T[i][j] = roundFourDecimals(Math.sqrt(2.0/N)*Math.cos(((2.0*j+1)*i*Math.PI)/(2.0*N)));
            }
        }
        return T;
    }
    
    public double [][] createM (int i, int j) {
        double [][] M = new double [N][N];
        int xx = 0;
        int yy = 0;
        for (int x = i*N; x < i*N+N; x++) {
            for (int y = j*N; y < j*N+N; y++) {    
                M[xx][yy] = new Color(originalImage.getRGB(x, y)).getRed() - 128;
                yy++;   
            }
            xx++;
            yy=0;
        }
        return M;    
    }
     
    public double roundFourDecimals(double d) {
        DecimalFormat fourDForm = new DecimalFormat("#.####");
        return Double.valueOf(fourDForm.format(d));
    }
    
    public double [][] createTinv() {
        double [][] Tinv = {{ 0.3536,0.4904,0.4619,0.4157,0.3536,0.2778,0.1913,0.0975},
                            {0.3536,0.4157,0.1913,-0.0975,-0.3536,-0.4904,-0.4619,-0.2778},
                            {0.3536,0.2778,-0.1913,-0.4904,-0.3536,0.0975,0.4619,0.4157},
                            {0.3536,0.0975,-0.4619,-0.2778,0.3536,0.4157,-0.1913,-0.4904},
                            {0.3536,-0.0975,-0.4619,0.2778,0.3536,-0.4157,-0.1913,0.4904},
                            {0.3536,-0.2778,-0.1913,0.4904,-0.3536,-0.0975,0.4619,-0.4157},
                            {0.3536,-0.4157,0.1913,0.0975,-0.3536,0.4904,-0.4619,0.2778},
                            {0.3536,-0.4904,0.4619,-0.4157,0.3536,-0.2778,0.1913,-0.0975}
                           };
        
        return Tinv;
    }
    
     public double[][] createTinv16X16() {
        double [][] Tinv = 
        { {0.2500,0.3518,0.3467,0.3384,0.3267,0.3118,0.2939,0.2733,0.2500,0.2243,0.1964,0.1667,0.1353,0.1026,0.0690,0.0346},
                {0.2500,0.3384,0.2939,0.2243,0.1353,0.0346,-0.0690,-0.1667,-0.2500,-0.3118,-0.3467,-0.3518,-0.3267,-0.2733,-0.1964,-0.1026},
                {0.2500,0.3118,0.1964,0.0346,-0.1353,-0.2733,-0.3467,-0.3384,-0.2500,-0.1026,0.0690,0.2243,0.3267,0.3518,0.2939,0.1667},
                {0.2500,0.2733,0.0690,-0.1667,-0.3267,-0.3384,-0.1964,0.0346,0.2500,0.3518,0.2939,0.1026,-0.1353,-0.3118,-0.3467,-0.2243},
                {0.2500,0.2243,-0.0690,-0.3118,-0.3267,-0.1026,0.1964,0.3518,0.2500,-0.0346,-0.2939,-0.3384,-0.1353,0.1667,0.3467,0.2733},
                {0.2500,0.1667,-0.1964,-0.3518,-0.1353,0.2243,0.3467,0.1026,-0.2500,-0.3384,-0.0690,0.2733,0.3267,0.0346,-0.2939,-0.3118},
                {0.2500,0.1026,-0.2939,-0.2733,0.1353,0.3518,0.0690,-0.3118,-0.2500,0.1667,0.3467,0.0346,-0.3267,-0.2243,0.1964,0.3384},
                {0.2500,0.0346,-0.3467,-0.1026,0.3267,0.1667,-0.2939,-0.2243,0.2500,0.2733,-0.1964,-0.3118,0.1353,0.3384,-0.0690,-0.3518},
                {0.2500,-0.0346,-0.3467,0.1026,0.3267,-0.1667,-0.2939,0.2243,0.2500,-0.2733,-0.1964,0.3118,0.1353,-0.3384,-0.0690,0.3518},
                {0.2500,-0.1026,-0.2939,0.2733,0.1353,-0.3518,0.0690,0.3118,-0.2500,-0.1667,0.3467,-0.0346,-0.3267,0.2243,0.1964,-0.3384},
                {0.2500,-0.1667,-0.1964,0.3518,-0.1353,-0.2243,0.3467,-0.1026,-0.2500,0.3384,-0.0690,-0.2733,0.3267,-0.0346,-0.2939,0.3118},
                {0.2500,-0.2243,-0.0690,0.3118,-0.3267,0.1026,0.1964,-0.3518,0.2500,0.0346,-0.2939,0.3384,-0.1353,-0.1667,0.3467,-0.2733},
                {0.2500,-0.2733,0.0690,0.1667,-0.3267,0.3384,-0.1964,-0.0346,0.2500,-0.3518,0.2939,-0.1026,-0.1353,0.3118,-0.3467,0.2243},
                {0.2500,-0.3118,0.1964,-0.0346,-0.1353,0.2733,-0.3467,0.3384,-0.2500,0.1026,0.0690,-0.2243,0.3267,-0.3518,0.2939,-0.1667},
                {0.2500,-0.3384,0.2939,-0.2243,0.1353,-0.0346,-0.0690,0.1667,-0.2500,0.3118,-0.3467,0.3518,-0.3267,0.2733,-0.1964,0.1026},
                {0.2500,-0.3518,0.3467,-0.3384,0.3267,-0.3118,0.2939,-0.2733,0.2500,-0.2243,0.1964,-0.1667,0.1353,-0.1026,0.0690,-0.0346}
        };

        return Tinv;
    }
    
    public double[][] multiply(double[][] m1, double[][] m2) {
    int m1rows = m1.length;
    int m1cols = m1[0].length;
    int m2rows = m2.length;
    int m2cols = m2[0].length;
    if (m1cols != m2rows)
      throw new IllegalArgumentException("matrices don't match: " + m1cols + " != " + m2rows);
    double[][] result = new double[m1rows][m2cols];
 
    // multiply
    for (int i=0; i<m1rows; i++)
      for (int j=0; j<m2cols; j++)
        for (int k=0; k<m1cols; k++)
        result[i][j] += m1[i][k] * m2[k][j];
    return result;
  }
    
    public int[][] createQ50 () {
        int [][] Q = { {16, 11, 10, 16, 24, 40, 51, 61},
                          {12, 12, 14, 19, 26, 58, 60, 55},
                          {14, 13, 16, 24, 40, 57, 69, 56},
                          {14, 17, 22, 29, 51, 87, 80, 62},
                          {18, 22, 37, 56, 68, 109, 103, 77},
                          {24, 35, 55, 64, 81, 104, 113, 92},
                          {49, 64, 78, 87, 103, 121, 120, 101},
                          {72, 92, 95, 98, 112, 100, 103, 99}
                        };
     return Q;
    }
    
    public int [][] createC (double [][] D, int [][] Q) {
        int [][] C = new int[N][N];     
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {       
                C[i][j] = (int) Math.round(D[i][j]/Q[i][j]);
            }
        }     
        return C;
    }
    
    public double [][] createR (int [][] Q, int [][] C) {
        double [][] R = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                R[i][j] = Q[i][j]*C[i][j];
            }
        }   
        return R;
    }    
    
    public int [][] createN (double [][] Tinv, double [][] R, double [][] T) {
        int [][] Nmatrix = new int [N][N];
        double [][] tmp = multiply(multiply(Tinv, R), T);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Nmatrix[i][j] = (int) (Math.round(tmp[i][j])+128);
            }
        }
        return Nmatrix;
    }
    
    public void fillFilteredImage (int i, int j, int[][] Nmatrix) {
        int xx =0;
        int yy =0;
        for (int x = i*N; x < i*N+N; x++) {
            for (int y = j*N; y < j*N+N; y++) {
                int alpha = new Color(originalImage.getRGB(x, y)).getAlpha();
                int color = Nmatrix[xx][yy];
                int rgb = PreprocessingHelper.colorToRGB(alpha, color, color, color);  
                yy++;
                filteredImage.setRGB(x, y, rgb);                        
            }
            xx++;
            yy=0;
        } 
    }
    
    public void tresholdDmatrix (double [][] D, double treshold) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double coef = D[i][j];
                if (Math.abs(coef) < treshold) 
                    D[i][j] = 0;
            }
        }
    }

    public void updateQ(int [][] Q) {
        if (qualityLevel == 50)
            return;
        if (qualityLevel > 50) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    Q[i][j] = (int) Math.round(Q[i][j]*(100-qualityLevel)*1.0/50);
                }
            }
        }
        if (qualityLevel < 50) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    Q[i][j] = (int)Math.round(Q[i][j]*50.0/qualityLevel);
                }
            }
        }
    }
    
     public int [][] createQ16X16 () {
        int [][] Q = { {8,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                     {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,30},
                     {8,1,1,1,1,1,1,1,1,1,1,1,1,1,30,28},
                     {1,1,1,1,1,1,1,1,1,1,1,1,1,32,35,29},
                     {1,1,1,1,1,1,1,1,1,1,1,1,32,35,32,28},
                     {1,1,1,1,1,1,1,1,1,1,1,35,40,42,40,35},
                     {1,1,1,1,1,1,1,1,1,1,35,44,42,40,35,31},
                     {1,1,1,1,1,1,1,1,1,35,44,44,50,53,52,45},
                     {1,1,1,1,1,1,1,1,31,34,44,55,53,52,45,39},
                     {1,1,1,1,1,1,1,31,34,40,41,47,52,45,52,50},
                     {1,1,1,1,1,1,30,32,36,41,47,52,54,57,50,46},
                     {1,1,1,1,1,36,32,36,44,47,52,57,60,57,55,47},
                     {1,1,1,1,36,39,42,44,48,52,57,61,60,60,55,51},
                     {1,1,1,39,42,47,48,46,49,57,56,55,52,61,54,51},
                     {1,1,42,46,47,48,48,49,53,56,53,50,51,52,51,50},
                     {1,45,46,47,48,49,57,56,56,50,52,52,51,51,51,50},
              }; 
        return Q;
    }
    
    
    @Override
    public String toString() {
        return "Denoise DCT - grayscale";
    }

    
    //za denoise siga mora biti ista kao i kod GaussianNoise
    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public void setQualityLevel(int qualityLevel) {
        if (qualityLevel > 97) {
            this.qualityLevel = 97;
            return;
        }
        if (qualityLevel < 1) {
            this.qualityLevel = 1;
            return;
        }
        this.qualityLevel = qualityLevel;
    }

    public void setN(int N) {
        if (N >= 12 ) {
            this.N = 16;
            return;
        }
        this.N = 8;
    }
    
    
    
}
