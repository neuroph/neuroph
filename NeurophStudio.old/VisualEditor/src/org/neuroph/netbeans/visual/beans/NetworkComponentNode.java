/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.visual.beans;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kasia
 */
public class NetworkComponentNode extends AbstractNode {

    private NetworkComponent netItem;

    /** Creates a new instance of InstrumentNode */
    public NetworkComponentNode(NetworkComponent key) {
        super(Children.LEAF, Lookups.fixed( new Object[] {key} ) );
        this.netItem = key;
        setIconBaseWithExtension(key.getIcon());
        setDisplayName(key.getTitle());
    }

    public NetworkComponent getNetItem() {
        return netItem;
    }


}
