package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.Layer;
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
public class LayerNode extends AbstractNode {

    Layer layer;

    public LayerNode(Layer layer) {
//        super(new Children.Keys() {
//
//            @Override
//            protected Node[] createNodes(Object t) {
//                Layer obj = (Layer) t;
//        return new Node[]{new LayerNode(obj)};
//            }
//        }, Lookups.singleton(layer));

        super(new LayerChildren(layer), Lookups.singleton(layer));

        this.layer = layer;

    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/explorer/layerIcon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {

        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Layer properties");

        try {

            Property neuronCount = new PropertySupport.Reflection(layer, Integer.class, "getNeuronsCount", null);
            Property label = new PropertySupport.Reflection(layer, String.class, "getLabel", null);

            neuronCount.setShortDescription("Number of neurons");
            label.setShortDescription("Layer's label");

            neuronCount.setName("Number of neurons");
            label.setName("Label");

            set.put(neuronCount);
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
