package org.neuroph.netbeans.visual.popup;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.NeuralNetworkEditor;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;

/**
 *
 * @author remote
 */
public class ConnectionPopupMenuProvider implements PopupMenuProvider{
    JPopupMenu connectionPopupMenu;
    NeuralNetworkEditor editor;

    
    
    
    @Override
    public JPopupMenu getPopupMenu(final Widget widget, Point point) {
        editor = ((NeuralNetworkScene)widget.getScene()).getNeuralNetworkEditor();
        connectionPopupMenu = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
               editor.removeConnection(widget, null);
            }
        });
        connectionPopupMenu.add(removeItem);


        return connectionPopupMenu;

    }
}
