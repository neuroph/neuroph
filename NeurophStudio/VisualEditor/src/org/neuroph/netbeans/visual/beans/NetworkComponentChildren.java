/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.visual.beans;

import java.util.ArrayList;

import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.openide.util.Utilities;

/**
 *
 * @author Kasia
 */
public class NetworkComponentChildren extends Index.ArrayChildren {

    private Category category;

    private String[][] items = new String[][]{
        {"0", "Components", "org/neuroph/netbeans/visual/support/circle.png", "neuron"},
        {"1", "Components", "org/neuroph/netbeans/visual/support/rectangle.png", "layer"},
    };

    public NetworkComponentChildren(Category Category) {
        this.category = Category;
    }

    @Override
    protected java.util.List<Node> initCollection() {
        ArrayList childrenNodes = new ArrayList( items.length );
        for( int i=0; i<items.length; i++ ) {
            if( category.getName().equals( items[i][1] ) ) {
                NetworkComponent item = new NetworkComponent();
                item.setId(new Integer(items[i][0]));
                item.setCategory(items[i][1]);
                item.setIcon(items[i][2]);
                item.setTitle(items[i][3]);
                childrenNodes.add( new NetworkComponentNode( item ) );
            }
        }
        return childrenNodes;
    }

}
