/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets;


/**
 *
 * @author Ana
 */
public interface Selectable {
     
    public void setSelected();
    
    public void unselect();
    
    public boolean isSelected();
}