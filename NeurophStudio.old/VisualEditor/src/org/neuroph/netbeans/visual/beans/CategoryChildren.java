/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.visual.beans;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Kasia
 */
public class CategoryChildren extends Children.Keys {

    private String[] Categories = new String[]{
        "Components", "Layers", "Neurons", "Transfer Functions", "Input Functions", "Learning Rules"};

    public CategoryChildren() {
    }

    @Override
    protected Node[] createNodes(Object key) {
        Category obj = (Category) key;
        return new Node[] { new CategoryNode(obj) };
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        Category[] objs = new Category[Categories.length];
        for (int i = 0; i < objs.length; i++) {
            Category cat = new Category();
            cat.setName(Categories[i]);
            objs[i] = cat;
        }
        setKeys(objs);
    }

}
