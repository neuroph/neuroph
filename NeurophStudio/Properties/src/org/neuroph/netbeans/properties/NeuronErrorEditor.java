package org.neuroph.netbeans.properties;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Ana
 */
public class NeuronErrorEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        double d = Double.parseDouble(String.valueOf(getValue()));
        return String.valueOf(d);
    }

    @Override
    public void setAsText(String text) {
        double d = Double.parseDouble(text);
        setValue(d);
    }
}
