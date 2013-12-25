/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.properties;

import java.beans.PropertyEditorSupport;
import org.neuroph.core.learning.LearningRule;

/**
 *
 * @author Ana
 */
public class NeuronLabelEditor extends PropertyEditorSupport{
    
    public String getAsText() {
        String s = String.valueOf(getValue());
        if (s == null) {
            return "Null";
        }
        return s;
    }
    
    public void setAsText(String text){
        setValue(text);
    }
}
