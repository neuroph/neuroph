package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.data.DataSet;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Ivana
 */
public class ExplorerDataSetNode extends AbstractNode {

    private DataSet dataSet;

    public ExplorerDataSetNode(DataSet dataSet) {
        this(dataSet, new InstanceContent());
    }

    private ExplorerDataSetNode(DataSet dataSet, InstanceContent content) {
        super(Children.LEAF, new AbstractLookup(content));
        content.add(this);

        this.dataSet = dataSet;
        this.setDisplayName(dataSet.getLabel());
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/files/dset/iconTs.png");
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Data Set Properties");

        try {
            Property label = new PropertySupport.Reflection(dataSet, String.class, "getLabel", null);
            Property inputSize = new PropertySupport.Reflection(dataSet, Integer.class, "getInputSize", null);
            Property outputSize = new PropertySupport.Reflection(dataSet, Integer.class, "getOutputSize", null);
            Property size = new PropertySupport.Reflection(dataSet, Integer.class, "size", null);

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