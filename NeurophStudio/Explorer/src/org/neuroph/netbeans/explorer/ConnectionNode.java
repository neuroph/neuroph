package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.Connection;
import org.neuroph.core.Weight;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author zoran
 */
public class ConnectionNode extends AbstractNode {
    Connection connection;

    public ConnectionNode(Connection connection) {
        super(Children.LEAF, Lookups.singleton(connection));

        this.connection = connection;
    }

    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/explorer/connectionIcon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Connection properties");

        try {
            Weight weight = connection.getWeight();
            Property weightProp = new PropertySupport.Reflection(weight, Double.class, "getValue", null);
            weightProp.setShortDescription("Connection Weight");
            weightProp.setName("Weight");
            set.put(weightProp);
        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault().log(ex.getMessage());
        } catch (Exception ex) {
           ErrorManager.getDefault().log(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }

}
