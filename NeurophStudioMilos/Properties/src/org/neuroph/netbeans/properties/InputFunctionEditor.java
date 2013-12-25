package org.neuroph.netbeans.properties;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.neuroph.core.Connection;
import org.neuroph.core.input.Difference;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.input.Max;
import org.neuroph.core.input.Min;
import org.neuroph.core.input.Or;
import org.neuroph.core.input.Product;
import org.neuroph.core.input.Sum;
import org.neuroph.core.input.SumSqr;
import org.neuroph.core.input.WeightedSum;
import org.neuroph.util.Neuroph;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;

/**
 *
 * @author Ivana
 */
public class InputFunctionEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    public String getAsText() {
        InputFunction p = (InputFunction) getValue();
        if (p == null) {
            return "No Learning rule Set";
        }
        return p.getClass().toString().substring(6);
    }

    public void setAsText(String text) {
     
        if (text.equals("Sum")) {
            Sum input = new Sum();
            setValue(input);
        } else if (text.equals("WeightedSum")) {
            WeightedSum wSum = new WeightedSum();
            setValue(wSum);
        } else if (text.equals("Difference")) {
            Difference diff = new Difference();
            setValue(diff);
        }else if (text.equals("Max")){
            Max max = new Max();
            setValue(max);
        }else if (text.equals("Min")){
            Min min = new Min();
            setValue(min);
        }else if (text.equals("Or")){
            Or or = new Or();
            setValue(or);
        }else if (text.equals("Product")){
            Product product = new Product();
            setValue(product);
        }else if (text.equals("SumSqr")){
            SumSqr sumSqr = new SumSqr();
            setValue(sumSqr);
        }

    }

    public void attachEnv(PropertyEnv env) {
        env.registerInplaceEditorFactory(this);
    }
    private InplaceEditor ed = null;

    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace();
        }
        return ed;
    }

    private static class Inplace implements InplaceEditor {

        private final JComboBox inputFunctions = new JComboBox(Neuroph.getInstance().getInputFunctions().toArray());
        private PropertyEditor editor = null;

        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        public JComponent getComponent() {
            return inputFunctions;
        }

        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }

        public Object getValue() {
            return inputFunctions.getSelectedItem();
        }

        public void setValue(Object object) {
          
            inputFunctions.setSelectedItem(object);
        }

        public boolean supportsTextEntry() {
            return true;
        }

        public void reset() {
            Object o = editor.getValue();
            if (o != null) {
                inputFunctions.setSelectedItem(o);
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
            return component == inputFunctions || inputFunctions.isAncestorOf(component);
        }

        public void addActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }

        public void removeActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }
    }
}
