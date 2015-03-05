package org.neuroph.netbeans.visual.palette;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Zoran Sevarac
 * @author Boris Perović
 */
public class PaletteCategoryChildFactory extends ChildFactory<PaletteCategory> {

    private static final String[] CATEGORIES = new String[]{
        "Layers",
        "Neurons",
        "Input Functions",
        "Transfer Functions",
        "Connections",
        "Learning Rules"
    };

    @Override
    protected boolean createKeys(List<PaletteCategory> toPopulate) {
        for (String category : CATEGORIES) {
            PaletteCategory cat = new PaletteCategory(category);
            toPopulate.add(cat);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(PaletteCategory key) {
        PaletteCategoryNode pcn = new PaletteCategoryNode(key);
//        pcn.setValue(PaletteController.ATTR_IS_EXPANDED, true);
        return pcn;
    }
}
