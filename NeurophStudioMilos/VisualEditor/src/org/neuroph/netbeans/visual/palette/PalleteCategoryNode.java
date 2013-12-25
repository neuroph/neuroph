package org.neuroph.netbeans.visual.palette;

import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Zoran Sevarac
 */
public class PalleteCategoryNode extends AbstractNode {

    public PalleteCategoryNode( PalleteCategory category ) {
        super( new PalleteItems(category), Lookups.singleton(category) );
        setDisplayName(category.getName());
        
    }

}
