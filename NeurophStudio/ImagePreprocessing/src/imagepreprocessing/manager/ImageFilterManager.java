/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagepreprocessing.manager;

import imagepreprocessing.filter.ImageFilterChain;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing list of filterChains
 * @author Aleksandar
 */
public class ImageFilterManager {
    private static ImageFilterManager instance;
    List<ImageFilterChain> listOfFilters;
    
    private ImageFilterManager() {
        listOfFilters = new ArrayList<ImageFilterChain>();
    }
    
    public static ImageFilterManager getObject(){
        if (instance == null) {
            instance = new ImageFilterManager();
        }
        return instance;
    }
    
    public List<ImageFilterChain> getListOfFilters() {
        return listOfFilters;
    }
    
    public void addChain(ImageFilterChain ifc){
        listOfFilters.add(ifc);
    }
    
}
