package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.learning.DataSet;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;

/**
 *
 * @author Ivana
 */
public class TrainingSetNode extends AbstractNode {

 private DataSet trainingSet;

    public TrainingSetNode(DataSet trainingSet) {
        super(Children.LEAF);
        this.trainingSet = trainingSet;
        this.setDisplayName(trainingSet.getLabel());
    }

 @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/files/dset/iconTs.png");
    }
 
    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Training set properties");

        try {
            Property label = new PropertySupport.Reflection(trainingSet, String.class, "getLabel", null);
            Property inputSize = new PropertySupport.Reflection(trainingSet, Integer.class, "getInputSize", null);
            Property outputSize = new PropertySupport.Reflection(trainingSet, Integer.class, "getOutputSize", null);
            Property size = new PropertySupport.Reflection(trainingSet, Integer.class, "size", null);
             

            label.setName("Label");
            inputSize.setName("Input size");
            outputSize.setName("Output size");
            size.setName("Number of elements");

            set.put(label);
            set.put(inputSize);
            set.put(outputSize);
            set.put(size);
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }

}
