/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imgrec.filter;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Process images by applying all filters in chain 
 * @author Sanja
 */
public class ImageFilterChain implements ImageFilter, Serializable {

    private List<ImageFilter> filters = new ArrayList<>();
    private String chainName;
    /**
     * Add filter to chain
     * @param filter filter to be added
     */
    public void addFilter(ImageFilter filter){
        filters.add(filter);
    }
    /**
     * Remove filter from chain
     * @param filter filter to be removed
     * @return true if filter is removed 
     */
    public boolean removeFilter(ImageFilter filter){
        return filters.remove(filter);
    }

    /**
     * Apply all filters from a chain on image 
     * @param image image to process
     * @return processed image
     */
    @Override
    public BufferedImage processImage(BufferedImage image) {
        
        BufferedImage tempImage = image;
        for (ImageFilter filter : filters) {
            BufferedImage filteredImage = filter.processImage(tempImage);
            tempImage = filteredImage;
        }

        return tempImage;

    }
    /**
     * Returns images of all stages in processing
     * Used for testing 
     * @param image
     * @return 
     */
    public List<FilteredImage> processImageTest(BufferedImage image){
        List<FilteredImage> list = new ArrayList<FilteredImage>();
        BufferedImage tempImage = image;
        for (ImageFilter filter : filters) {
            BufferedImage processedImage = filter.processImage(tempImage);
            String filterName = filter.toString();
            FilteredImage filteredImage = new FilteredImage(processedImage,filterName);
            list.add(filteredImage);
            tempImage = processedImage;
        }

        return list;
    }
    /**
     * Get filters from chain
     * @return 
     */
    public List<ImageFilter> getFilters(){
        return filters;
    }

    public void setFilters(List<ImageFilter> filters) {
        this.filters = filters;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    @Override
    public String toString() {
        return chainName;
    }
    
}
