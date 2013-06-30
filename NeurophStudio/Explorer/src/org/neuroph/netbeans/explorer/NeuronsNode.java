package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.Neuron;
import org.neuroph.core.transfer.TransferFunction;
import org.neuroph.netbeans.properties.TransferFunctionEditor;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 * can be deleted
 * @author Ivana
 */
public class NeuronsNode extends AbstractNode {
    Neuron neuroncl;
    
    public NeuronsNode(Neuron neuron) {
        super(new Children.Keys() {

            @Override
            protected Node[] createNodes(Object t) {
                Neuron obj = (Neuron) t;

                return new Node[]{new NeuronsNode(obj)};
            }
        }, Lookups.singleton(neuron));
        neuroncl = neuron;
    }


    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/explorer/neuronsIcon.png");
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

            Property error = new PropertySupport.Reflection(neuroncl, Double.class, "getError", null);
            Property netinput = new PropertySupport.Reflection(neuroncl, Double.class, "getNetInput", null);
            Property netoutput = new PropertySupport.Reflection(neuroncl, Double.class, "getOutput", null);
            PropertySupport.Reflection transferfunction = new PropertySupport.Reflection(neuroncl, TransferFunction.class, "getTransferFunction", null);
           // PropertySupport.Reflection inputfunction = new PropertySupport.Reflection(neuroncl, InputFunction.class, "getInputFunction", null);
            error.setShortDescription("Error");
            netinput.setShortDescription("Net input");
            netinput.setShortDescription("output");
            transferfunction.setPropertyEditorClass(TransferFunctionEditor.class);
         //   inputfunction.setPropertyEditorClass(InputFunctionEditor.class);
         //   inputfunction.setName("Input function");
            error.setName("Error");
            netinput.setName("Net Input");
            netoutput.setName("Output");
            transferfunction.setName("Transfer function");
            set.put(error);
            set.put(transferfunction);
            set.put(netinput);
            set.put(netoutput);
         //   set.put(inputfunction);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }
}
