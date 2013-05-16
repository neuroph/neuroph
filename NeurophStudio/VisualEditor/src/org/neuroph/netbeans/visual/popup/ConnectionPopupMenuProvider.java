package org.neuroph.netbeans.visual.popup;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronConnectionWidget;

/**
 *
 * @author remote
 */
public class ConnectionPopupMenuProvider implements PopupMenuProvider{
    JPopupMenu connectionPopupMenu;

    @Override
    public JPopupMenu getPopupMenu(final Widget widget, Point point) {

        connectionPopupMenu = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               ((NeuralNetworkScene)widget.getScene()).getNetworkEditor().removeConnection(widget, null);
            }
        });
        connectionPopupMenu.add(removeItem);


        return connectionPopupMenu;

    }
}
