package org.neuroph.netbeans.visual.popup;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Layer;
import org.neuroph.netbeans.visual.NeuralNetworkEditor;
import org.neuroph.netbeans.visual.dialogs.AddCustomLayerDialog;
import org.neuroph.netbeans.visual.dialogs.ScenePreferencesDialog;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkWidget;
import org.openide.windows.WindowManager;

/**
 *
 * @author remote
 */
public class MainPopupMenuProvider implements PopupMenuProvider {

    JPopupMenu mainPopupMenu;
    NeuralNetworkEditor editor;

    @Override
    public JPopupMenu getPopupMenu(final Widget widget, final Point point) {
        editor = ((NeuralNetworkScene) widget.getScene()).getNeuralNetworkEditor();
        mainPopupMenu = new JPopupMenu();
        JMenuItem refreshItem = new JMenuItem("Refresh");
        JMenu addLayer = new JMenu("Add Layer");
        JMenuItem addEmptyLayerItem = new JMenuItem("Empty Layer");
        JMenuItem addCustomLayerItem = new JMenuItem("Custom Layer");
        JMenuItem scenePreferences = new JMenuItem("Display Preferences");
        refreshItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((NeuralNetworkScene) widget.getScene()).refresh();
            }
        });

        addEmptyLayerItem.addActionListener(new ActionListener() {
            int dropIdx = 0;

            public void actionPerformed(ActionEvent e) {
                Widget neuralNetworkWidget = widget.getChildren().get(0).getChildren().get(1);
                for (int i = 0; i < (neuralNetworkWidget.getChildren().size()); i++) {
                    double layerWidgetPosition = neuralNetworkWidget.getChildren().get(i).getLocation().getY();
                    if (point.getY() < layerWidgetPosition) {
                        dropIdx = i - 1; // 
                        break;
                    } else {
                        dropIdx = neuralNetworkWidget.getChildren().size();
                    }
                }

                Layer layer = new Layer();
                NeuralNetworkScene scene = (NeuralNetworkScene) widget.getScene();
                editor.addEmptyLayer(dropIdx, layer);
                scene.refresh();
            }
        });

        addCustomLayerItem.addActionListener(new ActionListener() {
            int dropIdx = 0;

            public void actionPerformed(ActionEvent e) {
                Widget neuralNetworkWidget = widget.getChildren().get(0).getChildren().get(1);
                for (int i = 0; i < (neuralNetworkWidget.getChildren().size()); i++) {
                    double layerWidgetPosition = neuralNetworkWidget.getChildren().get(i).getLocation().getY();
                    if (point.getY() < layerWidgetPosition) {
                        dropIdx = i - 1;
                        break;
                    } else {
                        dropIdx = neuralNetworkWidget.getChildren().size();
                    }
                }
                NeuralNetworkScene scene = (NeuralNetworkScene) widget.getScene();
                AddCustomLayerDialog dialog = new AddCustomLayerDialog(null, true, scene, dropIdx);
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
                scene.refresh();
            }
        });
        
        scenePreferences.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScenePreferencesDialog dialog = new ScenePreferencesDialog((NeuralNetworkScene) widget.getScene(), null, true);
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
            }
        });

        addLayer.add(addEmptyLayerItem);
        addLayer.add(addCustomLayerItem);
        mainPopupMenu.add(addLayer);
        mainPopupMenu.add(refreshItem);
        mainPopupMenu.add(scenePreferences);

        return mainPopupMenu;

    }
}
