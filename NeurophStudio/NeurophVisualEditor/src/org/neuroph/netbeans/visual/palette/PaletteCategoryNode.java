package org.neuroph.netbeans.visual.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author Zoran Sevarac
 * @author Boris Perović
 */
public class PaletteCategoryNode extends AbstractNode {

    public PaletteCategoryNode(PaletteCategory category) {
        super(Children.create(new PaletteItemChildFactory(category), true));
        setDisplayName(category.getName());
    }
}
