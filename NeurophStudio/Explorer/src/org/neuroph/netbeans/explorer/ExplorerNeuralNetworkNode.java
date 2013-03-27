package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.properties.InputNeuronsEditor;
import org.neuroph.netbeans.properties.LayerEditor;
import org.neuroph.netbeans.properties.LearningRuleEditor;
import org.neuroph.netbeans.properties.OutputNeuronsEditor;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Node for neural network in Explorer window
 * @author Ivana Jovicic
 * @author Zoran Sevarac
 */
public class ExplorerNeuralNetworkNode extends AbstractNode  {
    NeuralNetwork neuralNet;
    
    
    public ExplorerNeuralNetworkNode(NeuralNetwork neuralNet) {
        this(neuralNet, new InstanceContent());
    }

// http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/AbstractNode.html#AbstractNode%28org.openide.nodes.Children,%20org.openide.util.Lookup%29
// http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/Node.html#Node%28org.openide.nodes.Children,%20org.openide.util.Lookup%29
    private ExplorerNeuralNetworkNode(NeuralNetwork neuralNet, InstanceContent content ) {
     // http://platform.netbeans.org/tutorials/nbm-nodesapi2.html#propertysheet
        
      super( new ExplorerNeuralNetworkChildren(neuralNet), new AbstractLookup(content) );     
      content.add(this);
      this.setDisplayName(neuralNet.getLabel());
      this.neuralNet = neuralNet;
    }


    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/files/nnet/neuralNetIcon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();

        try {
            Property type = new PropertySupport.Reflection(this.neuralNet, Class.class, "getClass", null);
            PropertySupport.Reflection learningRule = new PropertySupport.Reflection(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection layers = new PropertySupport.Reflection(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection input = new PropertySupport.Reflection(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection output = new PropertySupport.Reflection(this.neuralNet, LearningRule.class, "getLearningRule", null);

            type.setShortDescription("Neural Network Type");
            learningRule.setPropertyEditorClass(LearningRuleEditor.class);
            input.setPropertyEditorClass(InputNeuronsEditor.class);
            output.setPropertyEditorClass(OutputNeuronsEditor.class);
            layers.setPropertyEditorClass(LayerEditor.class);

            type.setName("Type");
            learningRule.setName("Learning Rule");
            layers.setName("Layers");
            input.setName("Input Neurons");
            output.setName("Output Neurons");


            set.put(type);
            set.put(learningRule);
            set.put(layers);
            set.put(input);
            set.put(output);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }
    
}