package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.data.DataSet;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Ivana
 */
public class ExplorerDataSetNode extends AbstractNode {

    private final DataSet dataSet;
    
    public ExplorerDataSetNode(DataSet dataSet) {
        super(Children.LEAF, Lookups.singleton(dataSet));
        this.dataSet = dataSet;
        this.setDisplayName(dataSet.getLabel());
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/files/tset/tsetIcon.png");
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Data Set Properties");

        try {
            Property<String> label = new PropertySupport.Reflection<>(dataSet, String.class, "getLabel", null);
            Property<Integer> inputSize = new PropertySupport.Reflection<>(dataSet, Integer.class, "getInputSize", null);
            Property<Integer> outputSize = new PropertySupport.Reflection<>(dataSet, Integer.class, "getOutputSize", null);
            Property<Integer> size = new PropertySupport.Reflection<>(dataSet, Integer.class, "size", null);

            label.setName("Label");
            inputSize.setName("Input size");
            outputSize.setName("Output size");
            size.setName("Number of elements");

            set.put(label);
            set.put(inputSize);
            set.put(outputSize);
            set.put(size);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }
}
