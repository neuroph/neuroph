/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.visual.beans;

import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Kasia
 */
public class CategoryNode extends AbstractNode {

    public CategoryNode( Category category ) {
        super( new NetworkComponentChildren(category), Lookups.singleton(category) );
        setDisplayName(category.getName());
    }

}
