package org.neuroph.netbeans.visual.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * http://netbeans.dzone.com/nb-how-to-drag-drop-with-nodes-api
 */
public class PaletteItemNode extends AbstractNode {

    private final PaletteItem paletteItem;

    public PaletteItemNode(PaletteItem key) {
        super(Children.LEAF, Lookups.singleton(key));
        this.paletteItem = key;
        setIconBaseWithExtension(key.getIcon());
        setDisplayName(key.getTitle());
    }

    public PaletteItem getPaletteItem() {
        return paletteItem;
    }
}

