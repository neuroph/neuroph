package org.neuroph.netbeans.visual.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * 
 */
public class PalleteItemNode extends AbstractNode {

    private PalleteItem palleteItem;

    public PalleteItemNode(PalleteItem key) {
        super(Children.LEAF, Lookups.singleton(key) );
        this.palleteItem = key;
        setIconBaseWithExtension(key.getIcon());
        setDisplayName(key.getTitle());
    }

    public PalleteItem getNetItem() {
        return palleteItem;
    }


}
