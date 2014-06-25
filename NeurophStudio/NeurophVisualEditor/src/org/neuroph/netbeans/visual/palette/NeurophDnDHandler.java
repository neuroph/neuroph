package org.neuroph.netbeans.visual.palette;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.BeanInfo;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.openide.nodes.Node;
import org.openide.text.ActiveEditorDrop;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author Zoran Sevarac
 */
public class NeurophDnDHandler extends DragAndDropHandler {
    //http://bits.netbeans.org/dev/javadoc/org-openide-text/org/openide/text/ActiveEditorDrop.html
    // http://java2s.com/Open-Source/Java/IDE-Netbeans/spi/org/netbeans/modules/palette/ActiveEditorDropDefaultProvider.java.htm
    //  http://stackoverflow.com/questions/4935136/java-accessing-private-property-via-reflection
    @Override
    public void customize(ExTransferable et, Lookup lkp) {
        
        ActiveEditorDrop drop = lkp.lookup(ActiveEditorDrop.class); // ovaj im aproperty body koji sadzi naziv klase koja se dnd
        // ActiveEditorDropDefaultProvider$ActiveEditorDropDefault is private         
        final Class droppedClass = getDroppedClass(drop);
        Node node = lkp.lookup(Node.class);        

        // old way                
  //      PalleteItem palleteItem = node.getLookup().lookup(PalleteItem.class);  
//        final Class dropClass = palleteItem.getDropClass();

                  
        final Image image = (Image) node.getIcon(2); // BeanInfo.ICON_COLOR_16x16
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
        et.put(new ExTransferable.Single(new DataFlavor(droppedClass, droppedClass.getSimpleName())) {
            
            @Override
            protected Object getData() throws IOException, UnsupportedFlavorException {
                return droppedClass;
            }
        });                     
    }
    
    private Class getDroppedClass(ActiveEditorDrop drop) {
        try {
            // this is body tag from editor_palette_item
            Field field = drop.getClass().getDeclaredField("body");         
            field.setAccessible(true);
            Object value = field.get(drop);            
            return Class.forName(value.toString());
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } 

        return null;           
    }
}