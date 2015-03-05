package org.neuroph.netbeans.visual.palette;

import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 * This class provides a method to create a PaletteController which manages
 * pallete support
 *
 * @author Zoran Sevarac
 * @author Boris Perović
 */
public class PaletteSupport {

    public static PaletteController pc = null;

    public static PaletteController getPalette() {
        if (pc == null) {
            AbstractNode paletteRoot = new AbstractNode(Children.create(new PaletteCategoryChildFactory(), true));
            paletteRoot.setName("Neuroph Palette");
            pc = PaletteFactory.createPalette(paletteRoot, new EmptyPaletteActions());
        }
        return pc;
    }

    public static class EmptyPaletteActions extends PaletteActions {

        @Override
        public Action[] getImportActions() {
            return null;
        }

        @Override
        public Action[] getCustomPaletteActions() {
            return null;
        }

        @Override
        public Action[] getCustomCategoryActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action[] getCustomItemActions(Lookup lookup) {
            return null;
        }

        @Override
        public Action getPreferredAction(Lookup lookup) {
            return null;
        }
    }
}
