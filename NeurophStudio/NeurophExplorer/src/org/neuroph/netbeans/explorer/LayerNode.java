package org.neuroph.netbeans.explorer;

import java.awt.Image;
import java.util.concurrent.Callable;
import org.neuroph.core.Layer;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Ivana
 */
public class LayerNode extends AbstractNode {

    private final Layer layer;

    public LayerNode(Layer layer) {
        super(Children.createLazy(new ChildrenSetCallable(layer)), Lookups.singleton(layer));
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/explorer/icons/layerIcon.png");
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
            Property label = new PropertySupport.Reflection(layer, String.class, "getLabel", "setLabel");

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

    // To enable creating leaf nodes at start, otherwise we need to pass a factory,
    // which can expand only manually and then check that there are no children nodes
    private static class ChildrenSetCallable implements Callable<Children> {

        private final Layer key;

        private ChildrenSetCallable(Layer key) {
            this.key = key;
        }

        @Override
        public Children call() throws Exception {
            if (key.getNeurons().size() == 0) {
                return Children.LEAF;
            } else {
                // synchronous so that selection of members doesn't miss (if everything was not yet generated)
                return Children.create(new LayerChildFactory(key), false);
            }
        }
    }
}
