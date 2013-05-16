package org.neuroph.netbeans.visual.popup;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuronWidget;

/**
 *
 * @author hrza
 */
public class NeuronConnectionPopupMenuProvider implements PopupMenuProvider {

    private NeuronWidget srcWidget;
    private NeuronWidget trgWidget;

    public NeuronConnectionPopupMenuProvider(NeuronWidget srcWidget, NeuronWidget trgWidget) {
        this.srcWidget = srcWidget;
        this.trgWidget = trgWidget;
    }

    public JPopupMenu getPopupMenu(Widget widget, Point point) {

        JPopupMenu menu = new JPopupMenu();
        JMenuItem removeConnection = new JMenuItem("Remove Connection"); 
        removeConnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((NeuralNetworkScene) trgWidget.getScene()).getNetworkEditor().removeConnection(srcWidget, trgWidget);
                       
                ((NeuralNetworkScene) trgWidget.getScene()).refresh();
            }
        }); 
       menu.add(removeConnection);
        return menu;
    }
}
