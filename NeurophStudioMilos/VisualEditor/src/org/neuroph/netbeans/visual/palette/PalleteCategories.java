package org.neuroph.netbeans.visual.palette;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Zoran Sevarac
 */
public class PalleteCategories extends Children.Keys {

    private String[] categories = new String[]{
        "Layers", 
        "Neurons",
        "Input Functions",
        "Transfer Functions",
        "Connections",        
        "Learning Rules"};

    public PalleteCategories() {    }

    @Override
    protected Node[] createNodes(Object key) {
        PalleteCategory obj = (PalleteCategory) key;
        return new Node[] { new PalleteCategoryNode(obj) };
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        PalleteCategory[] objs = new PalleteCategory[categories.length];
        for (int i = 0; i < objs.length; i++) {
            PalleteCategory cat = new PalleteCategory();
            cat.setName(categories[i]);
            objs[i] = cat;
        }
        setKeys(objs);
    }

}