package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.Neuron;
import org.neuroph.core.input.InputFunction;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.netbeans.properties.InputFunctionEditor;
import org.neuroph.netbeans.properties.TransferFunctionEditor;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Ivana
 */
public class NeuronNode extends AbstractNode {
    Neuron neuron;
    
    public NeuronNode(Neuron neuron) {
        super(new NeuronChildren(neuron), Lookups.singleton(neuron));
        this.neuron = neuron;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/explorer/neuronIcon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Neuron properties");

        try {
            Property type = new PropertySupport.Reflection(neuron, Class.class, "getClass", null);
            PropertySupport.Reflection transferfunction = new PropertySupport.Reflection(neuron, TransferFunction.class, "getTransferFunction", null);
            PropertySupport.Reflection inputfunction = new PropertySupport.Reflection(neuron, InputFunction.class, "getInputFunction", null);

            Property netInput = new PropertySupport.Reflection(neuron, Double.class, "getNetInput", null);
            Property output = new PropertySupport.Reflection(neuron, Double.class, "getOutput", null);
            Property error = new PropertySupport.Reflection(neuron, Double.class, "getError", null);
            Property label = new PropertySupport.Reflection(neuron, String.class, "getLabel", null);

           
            type.setShortDescription("Neuron type/class");
            inputfunction.setShortDescription("Input function");
            transferfunction.setShortDescription("Transfer function");
            netInput.setShortDescription("Total net input for this neuron");
            output.setShortDescription("Neuron's output");
            error.setShortDescription("Neuron's error");
            label.setShortDescription("Neuron's label");

            
            inputfunction.setPropertyEditorClass(InputFunctionEditor.class);
            transferfunction.setPropertyEditorClass(TransferFunctionEditor.class);
                       
            type.setName("Type");
            inputfunction.setName("Input function");
            transferfunction.setName("Transfer function");
            netInput.setName("Net Input");
            output.setName("Output");
            error.setName("Error");
            label.setName("Label");

            set.put(type);
            set.put(inputfunction);
            set.put(transferfunction);
            set.put(netInput);
            set.put(output);
            set.put(error);
            set.put(label);
            
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }
}
