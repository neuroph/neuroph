package org.neuroph.netbeans.properties;

import java.beans.PropertyEditorSupport;
import org.neuroph.core.input.InputFunction;

/**
 *
 * @author Ivana
 */
public class InputFunctionEditor extends PropertyEditorSupport{
public String getAsText() {
    InputFunction p =(InputFunction) getValue();
    if (p == null) {
        return "No Learning rule Set";
    }
    return p.getClass().toString().substring(6);
}

}
