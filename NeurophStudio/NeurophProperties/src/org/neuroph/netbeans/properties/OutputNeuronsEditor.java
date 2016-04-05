package org.neuroph.netbeans.properties;

import java.beans.PropertyEditorSupport;
import org.neuroph.core.learning.LearningRule;

/**
 *
 * @author Ivana
 */
public class OutputNeuronsEditor extends PropertyEditorSupport {
    public String getAsText() {
    LearningRule d =(LearningRule) getValue();
    if (d == null) {
        return "No Learning rule Set";
    }
    return String.valueOf(d.getNeuralNetwork().getOutputNeurons().size());
}

    @Override
    public void setAsText(String s) {
    try {
       // setValue (new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse(s));
    } catch (Exception pe) {
        IllegalArgumentException iae = new IllegalArgumentException ("Could not parse date");
        throw iae;
    }
}

}

