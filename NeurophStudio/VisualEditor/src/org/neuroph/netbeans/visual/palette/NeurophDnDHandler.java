package org.neuroph.netbeans.visual.palette;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.BeanInfo;
import java.io.IOException;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author Zoran Sevarac
 */
public class NeurophDnDHandler extends DragAndDropHandler {
    
    @Override
    public void customize(ExTransferable et, Lookup lkp) {
        Node node = lkp.lookup(Node.class);
        PalleteItem palleteItem = node.getLookup().lookup(PalleteItem.class);  
        final Class dropClass = palleteItem.getDropClass();
        
        final Image image = (Image) node.getIcon(BeanInfo.ICON_COLOR_16x16);
        et.put(new ExTransferable.Single(DataFlavor.imageFlavor) {
             @Override
            protected Object getData() throws IOException, UnsupportedFlavorException {
                return image;
            }
        });
//        final String title = node.getDisplayName();
//        et.put(new ExTransferable.Single(DataFlavor.stringFlavor) {
//            
//            @Override
//            protected Object getData() throws IOException, UnsupportedFlavorException {
//                return title;
//            }
//        });
        et.put(new ExTransferable.Single(new DataFlavor(dropClass, dropClass.getSimpleName())) {
            
            @Override
            protected Object getData() throws IOException, UnsupportedFlavorException {
                return dropClass;
            }
        });                     
    }
}