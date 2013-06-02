/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.properties;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Ana
 */
public class NetOutputEditor extends PropertyEditorSupport{
    
     public String getAsText() {
        double d = Double.parseDouble(String.valueOf(getValue()));
        return String.valueOf(d);
    }

    public void setAsText(String text) {
        double d = Double.parseDouble(text);
        setValue(d);
    }
}
