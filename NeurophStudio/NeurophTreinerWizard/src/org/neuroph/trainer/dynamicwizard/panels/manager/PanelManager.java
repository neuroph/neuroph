/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.trainer.dynamicwizard.panels.manager;

/**
 * Class that handles setting wizard type
 * 
 * @author Sanja
 */
public class PanelManager {
    private static PanelManager instance;

    public static final int SIMPLE_WIZARD = 0;
    public static final int ADVANCE_WIZARD = 1;
    
    private int wizardType;
    
    private PanelManager() {
    }
    
    public static PanelManager getInstance(){
        if (instance == null) {
            instance = new PanelManager();
        }
        return instance;
    }
    
    public void setType(int type){
        switch(type){
            case SIMPLE_WIZARD: wizardType= SIMPLE_WIZARD; break;
            case ADVANCE_WIZARD: wizardType= ADVANCE_WIZARD; break;
           
        }
    }

    public int getType(){
        return wizardType;
    }
    
    
}
