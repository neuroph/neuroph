package org.neuroph.netbeans.visual.palette;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.lookup.Lookups;

/**
 *
 * http://netbeans.dzone.com/nb-how-to-drag-drop-with-nodes-api
 */
public class PalleteItemNode extends AbstractNode {

    private PalleteItem palleteItem; // ovo staviti u lookup

    public PalleteItemNode(PalleteItem key) {
        super(Children.LEAF, Lookups.singleton(key));
        this.palleteItem = key;
        setIconBaseWithExtension(key.getIcon());
        setDisplayName(key.getTitle());
    }

    @Override
    public Transferable clipboardCopy() throws IOException {
        return new ExTransferable.Single(new DataFlavor( PalleteItem.class, "NeurophPalleteItem")) {
            protected Class getData() {
                return palleteItem.getDropClass();
            }
        };
    }
    
    @Override
    public Transferable drag() throws IOException {

        return new ExTransferable.Single(new DataFlavor( PalleteItem.class, "NeurophPalleteItem")) {
            protected Class getData() {
                return palleteItem.getDropClass();
            }
        };
    }

}

