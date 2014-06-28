package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.learning.IterativeLearning;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Maja
 */
public class LearningRuleNode extends AbstractNode {

    LearningRule learningRule;

    public LearningRuleNode(LearningRule learningRule) {
        this(learningRule, new InstanceContent());
    }

    private LearningRuleNode(LearningRule learningRule, InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        content.add(this);
        content.add(learningRule);
        this.learningRule = learningRule;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/explorer/learningRuleIcon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Learning rule properties");

        try {

            Property type = new PropertySupport.Reflection(learningRule, Class.class, "getClass", null);
            type.setShortDescription("Type");
            type.setName("Type");
            set.put(type);

            if (learningRule instanceof IterativeLearning) {
                Property learningRate = new PropertySupport.Reflection((IterativeLearning) learningRule, Double.class, "getLearningRate", null);
                learningRate.setShortDescription("Learning rate");
                learningRate.setName("Learning rate");
                set.put(learningRate);

                // ne postoji getMaxIterations
                //Property maxIterations = new PropertySupport.Reflection((IterativeLearning) learningRule, Integer.class, "getMaxIterations", null);
                //maxIterations.setShortDescription("Max iterations");
                //maxIterations.setName("Max iterations");
                //set.put(maxIterations);

                if (learningRule instanceof SupervisedLearning) {
                    Property maxError = new PropertySupport.Reflection((SupervisedLearning) learningRule, Double.class, "getMaxError", null);
                    maxError.setShortDescription("Max error");
                    maxError.setName("Max error");
                    set.put(maxError);

                    if (learningRule instanceof MomentumBackpropagation) {
                        Property momentum = new PropertySupport.Reflection((MomentumBackpropagation) learningRule, Double.class, "getMomentum", null);
                        momentum.setShortDescription("Momentum");
                        momentum.setName("Momentum");
                        set.put(momentum);
                    }
                }
            }



        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }
}
