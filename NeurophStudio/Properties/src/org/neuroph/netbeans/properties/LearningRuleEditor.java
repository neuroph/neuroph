package org.neuroph.netbeans.properties;

import java.beans.PropertyEditorSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.core.learning.DataSet;
import org.neuroph.core.learning.LearningRule;

/**
 *
 * @author zoran
 */
public class LearningRuleEditor extends PropertyEditorSupport {

    
    public String getAsText() {
        LearningRule p = (LearningRule) getValue();
        if (p == null) {
            return "No Learning rule Set";
        }
        return p.getClass().toString().substring(6);
    }

   
    
}
