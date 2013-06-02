package org.neuroph.netbeans.properties;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.neuroph.core.transfer.Gaussian;
import org.neuroph.core.transfer.Linear;
import org.neuroph.core.transfer.Log;
import org.neuroph.core.transfer.Ramp;
import org.neuroph.core.transfer.Sgn;
import org.neuroph.core.transfer.Sigmoid;
import org.neuroph.core.transfer.Sin;
import org.neuroph.core.transfer.Step;
import org.neuroph.core.transfer.Tanh;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.core.transfer.Trapezoid;
import org.neuroph.util.Neuroph;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import sun.awt.RequestFocusController;

/**
 *
 * @author Ivana
 */
public class TransferFunctionEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    @Override
    public String getAsText() {
        TransferFunction p = (TransferFunction) getValue();
        if (p == null) {
            return "No Learning rule Set";
        }
        return p.getClass().toString().substring(6);
    }
    
 
        public void setAsText(String text) {
        
        if (text.equals("Linear")){
            Linear linear = new Linear();
            setValue(linear);
            
        }else if (text.equals("Gaussian")){
            Gaussian gaussian = new Gaussian();
            setValue(gaussian);
        }else if (text.equals("Log")){
            Log log = new Log();
            setValue(log);
        }else if (text.equals("Ramp")){
            Ramp ramp = new Ramp();
            setValue(ramp);
        }else if (text.equals("Sgn")){
            Sgn sgn = new Sgn();
            setValue(sgn);
        }else if (text.equals("Sigmoid")){
            Sigmoid sigmoid = new Sigmoid();
            setValue(sigmoid);
        }else if (text.equals("Sin")){
            Sin sin = new Sin();
            setValue(sin);
        }else if (text.equals("Step")){
            Step step = new Step();
            setValue(step);
        }else if (text.equals("Tanh")){
            Tanh tanh = new Tanh();
            setValue(tanh);
        }else {
            Trapezoid trapezoid = new Trapezoid();
            setValue(trapezoid);
        }
                
        
    }

    public void attachEnv(PropertyEnv env) {
        env.registerInplaceEditorFactory(this);
    }
    private InplaceEditor ed = null;

    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new TransferFunctionEditor.Inplace();
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private final JComboBox transferFunctions = new JComboBox(Neuroph.getInstance().getTransferFunctions().toArray());
        private PropertyEditor editor = null;

        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        public JComponent getComponent() {
            return transferFunctions;
        }

        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }

        public Object getValue() {
            return transferFunctions.getSelectedItem();
        }

        public void setValue(Object object) {
            transferFunctions.setSelectedItem(object);
        }

        public boolean supportsTextEntry() {
            return true;
        }

        public void reset() {
            Object o = editor.getValue();
            if (o != null) {
                transferFunctions.setSelectedItem(o);
            }
        }

        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        public PropertyEditor getPropertyEditor() {
            return editor;
        }

        public PropertyModel getPropertyModel() {
            return model;
        }
        private PropertyModel model;

        public void setPropertyModel(PropertyModel propertyModel) {
            this.model = propertyModel;
        }

        public boolean isKnownComponent(Component component) {
            return component == transferFunctions || transferFunctions.isAncestorOf(component);
        }

        public void addActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }

        public void removeActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }
    }
}
