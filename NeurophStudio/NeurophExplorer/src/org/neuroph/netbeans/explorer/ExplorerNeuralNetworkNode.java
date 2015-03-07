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
import org.openide.nodes.Children;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 * Node for neural network in Explorer window
 *
 * @author Ivana Jovicic
 * @author Zoran Sevarac
 * @author Boris Perović <borisvperovic@gmail.com>
 */
// http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/AbstractNode.html#AbstractNode%28org.openide.nodes.Children,%20org.openide.util.Lookup%29
// http://bits.netbeans.org/dev/javadoc/org-openide-nodes/org/openide/nodes/Node.html#Node%28org.openide.nodes.Children,%20org.openide.util.Lookup%29
// http://platform.netbeans.org/tutorials/nbm-nodesapi2.html#propertysheet
public class ExplorerNeuralNetworkNode extends AbstractNode {

    private final NeuralNetwork<?> neuralNet;
    
    public ExplorerNeuralNetworkNode(NeuralNetwork<?> neuralNet) {
        // synchronous so that selection doesn't miss (if everything was not yet generated)
        super(Children.create(new ExplorerNeuralNetworkChildFactory(neuralNet), false), Lookups.singleton(neuralNet));
        
        this.neuralNet = neuralNet;
        this.setDisplayName(neuralNet.getLabel());
        // TODO add listener for explorer updating
    }

    public NeuralNetwork<?> getNeuralNet() {
        return neuralNet;
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
            Property<?> type = new PropertySupport.Reflection<>(this.neuralNet, Class.class, "getClass", null);
            PropertySupport.Reflection<LearningRule> learningRule = new PropertySupport.Reflection<>(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection<LearningRule> layers = new PropertySupport.Reflection<>(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection<LearningRule> input = new PropertySupport.Reflection<>(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection<LearningRule> output = new PropertySupport.Reflection<>(this.neuralNet, LearningRule.class, "getLearningRule", null);

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
