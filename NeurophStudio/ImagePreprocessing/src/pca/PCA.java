/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pca;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 *
 * @author Sanja
 */
public class PCA {

    //original data
    private Matrix A;
    //data after svd
    private Matrix U;
    private Matrix S;
    private Matrix V;
    //means of original data
    private double[] mean;

    public PCA(Matrix A) {
        this.A = A;
     }
    
    
    /**
     * Standardize matrix and set parameters for PCA using SVD from jama
     * A* = USV'
     * @param A matrix for pca
     */
    public void pca() {
        mean = getMean(A);
        A = standardizeMatrix(A, mean);
        //svd
        SingularValueDecomposition s = A.svd();
        //set matrixis from svd values
        setU(s.getU());
        setS(s.getS());
        setV(s.getV());
        
    }
    
    /**
     * Gets i-th principal component
     * PC = sigma(i)*U(:,i)
     * @param i index of principal component
     * @return one column matrix which represents principal component 
     */
    public Matrix getPrincipalComponent(int i){
     
        Matrix u = U.getMatrix(0, U.getRowDimension() - 1, i, i);
        Matrix pc = u.times(S.get(i, i));
        return pc;
    }
    
    /**
     * Returns matrix with specified numbers of principal components
     * @param numberOfComponents number of principal components
     * @return matrix of principal components
     */
    public Matrix getPrincipalComponents(int numberOfComponents) {

        Matrix pc = new Matrix(U.getRowDimension(), numberOfComponents, 0);
        
        for (int i = 0; i < numberOfComponents; i++) {
            Matrix u = U.getMatrix(0, U.getRowDimension() - 1, i, i);
            Matrix pci = u.times(S.get(i, i));
            pc.setMatrix(0, U.getRowDimension()-1, i, i, pci);

        }
        return pc;
    }
    
    /**
     * Recreates original data with specified number of principal components
     * @param numberOfComponents
     * @return 
     */
    public Matrix recreateOriginalDataFromPrincipalComponents(int numberOfComponents){
        
        Matrix e = new Matrix(A.getRowDimension(),A.getColumnDimension(),  0);
        for (int i = 0; i < numberOfComponents; i++) {
            Matrix v1 = V.getMatrix(0, V.getRowDimension() - 1, i, i).transpose();
            Matrix e1 = getPrincipalComponent(i).times(v1);
            e = e.plus(e1);
        }
        
        return unstandardizedMatrix(e,mean);
        
    }
    
    
    /**
     * Standardizes matrix by subtracting mean from all data
     *
     * @param m matrix to be standardize
     * @param mean vector of means for every column
     * @return standardize matrix
     */
    private Matrix standardizeMatrix(Matrix m, double[] mean) {

        // subtract the mean from the original data                              
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < mean.length; j++) {
                m.set(i, j, m.get(i, j) - mean[j]);
            }
        }
        return m;
    }

    /**
     * Returns matrix to original state
     * @param m matrix to be unstandardized
     * @param mean vector of means for every column
     * @return unstandardized matrix
     */
    private static Matrix unstandardizedMatrix(Matrix m, double[] mean) {

        // add the mean to get original data                       
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < mean.length; j++) {
                m.set(i, j, m.get(i, j) + mean[j]);
            }
        }
        return m;
    }

    /**
     * Get mean for every column in matrix
     * @param m matrix
     * @return vector of means by column
     */
    private static double[] getMean(Matrix m) {
        
        double[] mean = new double[m.getColumnDimension()];
        // compute the mean of all the samples
        for (int i = 0; i < m.getRowDimension(); i++) {            
            for (int j = 0; j < mean.length; j++) {
                mean[j] += m.get(i, j);
            }
        }
        for (int j = 0; j < mean.length; j++) {
            mean[j] /= m.getRowDimension();
        }
        return mean;

    }

    public Matrix getU() {
        return U;
    }

    public void setU(Matrix U) {
        this.U = U;
    }

    public Matrix getS() {
        return S;
    }

    public void setS(Matrix S) {
        this.S = S;
    }

    public Matrix getV() {
        return V;
    }

    public void setV(Matrix V) {
        this.V = V;
    }
    
}
