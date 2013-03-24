package org.neuroph.netbeans.visual.popup;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.dialogs.AddCustomLayerDialog;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;

/**
 *
 * @author remote
 */
public class MainPopupMenuProvider implements PopupMenuProvider {

    JPopupMenu mainPopupMenu;

    @Override
    public JPopupMenu getPopupMenu(final Widget widget, Point point) {

        mainPopupMenu = new JPopupMenu();
        JMenuItem refreshItem = new JMenuItem("Refresh");
        JMenuItem addLayerItem = new JMenuItem("Add Layer");
        JMenuItem showConnections = new JMenuItem("Show/Hide Connections");
        refreshItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });
//        addLayerItem.addActionListener(new ActionListener() {
//          // add index field to this dialog
//            public void actionPerformed(ActionEvent e) {
//                AddCustomLayerDialog addLayers = new AddCustomLayerDialog(null, true, ((NeuralNetworkScene) widget.getScene()));
//                addLayers.setVisible(true);
//                ((NeuralNetworkScene) widget.getScene()).refresh();
//            }
//        });

        showConnections.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NeuralNetworkScene scene = (NeuralNetworkScene) widget.getScene();
                scene.setShowConnections(!scene.isShowConnections());
                scene.refresh();
            }
        });
        mainPopupMenu.add(addLayerItem);
        mainPopupMenu.add(refreshItem); 
        mainPopupMenu.add(showConnections);


        return mainPopupMenu;

    }
}
