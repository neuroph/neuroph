/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pca;

import Jama.Matrix;
import java.awt.image.BufferedImage;

/**
 *
 * @author Sanja
 */
public class PCATest {

    /**
     * Get image using specified number of principal components
     * @param image
     * @param rank
     * @return 
     */
    public static BufferedImage getImage(BufferedImage image,int rank) {
        
        double[][] niz = ImageConverter.convertTo2DArray(image);
        Matrix A = Matrix.constructWithCopy(niz);
        PCA pca = new PCA(A);
        pca.pca();
        Matrix B = pca.recreateOriginalDataFromPrincipalComponents(rank);
        BufferedImage bi = ImageConverter.imageFromArray(B.getArray());
        return bi;

       
    }

}
