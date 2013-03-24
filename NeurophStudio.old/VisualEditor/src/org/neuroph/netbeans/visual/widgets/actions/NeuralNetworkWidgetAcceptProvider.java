package org.neuroph.netbeans.visual.widgets.actions;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.neuroph.core.Layer;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.core.learning.SupervisedLearning;
import org.neuroph.core.learning.UnsupervisedLearning;
import org.neuroph.netbeans.visual.dialogs.AddCompetitiveLayerDialog;
import org.neuroph.netbeans.visual.dialogs.AddCustomLayerDialog;
import org.neuroph.netbeans.visual.dialogs.AddInputLayerDialog;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkWidget;
import org.neuroph.nnet.comp.layer.CompetitiveLayer;
import org.neuroph.nnet.comp.layer.InputLayer;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.nnet.learning.PerceptronLearning;
import org.neuroph.nnet.learning.SigmoidDeltaRule;
import org.neuroph.nnet.learning.UnsupervisedHebbianLearning;
import org.openide.windows.WindowManager;

/**
 *
 * @author hrza
 */
public class NeuralNetworkWidgetAcceptProvider implements AcceptProvider {

    private NeuralNetworkWidget neuralNetworkWidget;
    private NeuralNetworkScene scene;
    private Graphics2D graphics;

    public NeuralNetworkWidgetAcceptProvider(NeuralNetworkWidget neuralNetworkWidget) {
        this.neuralNetworkWidget = neuralNetworkWidget;
        this.scene = ((NeuralNetworkScene)neuralNetworkWidget.getScene());
    }

    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
        DataFlavor flavor = t.getTransferDataFlavors()[2];
        JComponent view = widget.getScene().getView();
        Class droppedClass = flavor.getRepresentationClass();

        return canAccept(droppedClass) ? ConnectorState.ACCEPT : ConnectorState.REJECT;
    }

    public void accept(Widget widget, Point point, Transferable t) {
        DataFlavor flavor = t.getTransferDataFlavors()[2];
        Class droppedClass = flavor.getRepresentationClass();
   
        int dropIdx = 0;
        for (int i = 0; i < neuralNetworkWidget.getChildren().size(); i++) {
            double layerWidgetPosition = neuralNetworkWidget.getChildren().get(i).getLocation().getY();
            if (point.getY() < layerWidgetPosition) {
                dropIdx = i - 1; // 
                break;
            } else {
                dropIdx = neuralNetworkWidget.getChildren().size();
            }
        }      
        
        if (droppedClass.equals(Layer.class) || droppedClass.getSuperclass().equals(Layer.class)) {
            try {
                //IMPORTANT: it only adds WeightSum layer, also it only adds layer at the end                             
                      boolean hasInputLayer = false;
                for (int i = 0; i < scene.getNeuralNetwork().getLayersCount(); i++) {
                    if (scene.getNeuralNetwork().getLayerAt(i) instanceof InputLayer) {
                        hasInputLayer = true;
                        break;
                    }
                }
                if(droppedClass.equals(InputLayer.class)&&!hasInputLayer==false) {
                    JOptionPane.showMessageDialog(null, "Network already has input layer!");
                }
                
                if (droppedClass.equals(InputLayer.class)&&hasInputLayer==false) {
                    AddInputLayerDialog dialog = new AddInputLayerDialog(null, true, scene.getNeuralNetwork(), (NeuralNetworkScene) widget.getScene(), dropIdx);
                    dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                    dialog.setVisible(true);
                    scene.refresh();
                } else if (droppedClass.equals(CompetitiveLayer.class)) {
                    AddCompetitiveLayerDialog dialog = new AddCompetitiveLayerDialog(null, true, scene.getNeuralNetwork(), (NeuralNetworkScene) widget.getScene(), dropIdx);
                    dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                    dialog.setVisible(true);
                    scene.refresh();
                }                  
                else {
                    Layer newLayer = (Layer) droppedClass.newInstance();
                    scene.getNeuralNetwork().addLayer(dropIdx, newLayer);
                    scene.refresh();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }  else if (droppedClass.getSuperclass().equals(LearningRule.class)
                || droppedClass.getSuperclass().equals(SupervisedLearning.class)
                || droppedClass.getSuperclass().equals(PerceptronLearning.class)
                || droppedClass.getSuperclass().equals(BackPropagation.class)
                || droppedClass.getSuperclass().equals(MomentumBackpropagation.class)
                || droppedClass.getSuperclass().equals(UnsupervisedLearning.class)
                || droppedClass.getSuperclass().equals(UnsupervisedHebbianLearning.class)
                || droppedClass.getSuperclass().equals(SigmoidDeltaRule.class)
                || droppedClass.getSuperclass().equals(LMS.class)) {
                    //HopfieldLearning, IterativeLearning, KohonenLearning mogu da se nabace na neuralNetwork                                   
            try{             
            neuralNetworkWidget.getNeuralNetwork().setLearningRule((LearningRule)droppedClass.newInstance());
            } catch (Exception e) {
            e.printStackTrace();
            }
                       
        }else if (droppedClass.equals(AddCustomLayerDialog.class)) {

            // showDialogClass 
            AddCustomLayerDialog dialog = new AddCustomLayerDialog(null, true,scene, dropIdx);
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setVisible(true);
            scene.refresh();
        }
    }

    public boolean canAccept(Class droppedClass) {
        return droppedClass.equals(Layer.class) || droppedClass.getSuperclass().equals(Layer.class)
                || droppedClass.equals(LearningRule.class)
                || droppedClass.getSuperclass().equals(LearningRule.class)
                || droppedClass.getSuperclass().equals(SupervisedLearning.class)
                || droppedClass.getSuperclass().equals(PerceptronLearning.class)
                || droppedClass.getSuperclass().equals(BackPropagation.class)
                || droppedClass.getSuperclass().equals(MomentumBackpropagation.class)
                || droppedClass.getSuperclass().equals(UnsupervisedLearning.class)
                || droppedClass.getSuperclass().equals(UnsupervisedHebbianLearning.class)
                || droppedClass.getSuperclass().equals(SigmoidDeltaRule.class)
                || droppedClass.getSuperclass().equals(LMS.class)
                || droppedClass.equals(AddCustomLayerDialog.class); // FIX: why this!? this shoud be removed
    }
}
