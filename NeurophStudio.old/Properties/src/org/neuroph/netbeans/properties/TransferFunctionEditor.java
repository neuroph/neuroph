package org.neuroph.netbeans.properties;

import java.beans.PropertyEditorSupport;
import org.neuroph.core.transfer.TransferFunction;

/**
 *
 * @author Ivana
 */
public class TransferFunctionEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        TransferFunction p = (TransferFunction) getValue();
        if (p == null) {
            return "No Learning rule Set";
        }
        return p.getClass().toString().substring(6);
    }
}
