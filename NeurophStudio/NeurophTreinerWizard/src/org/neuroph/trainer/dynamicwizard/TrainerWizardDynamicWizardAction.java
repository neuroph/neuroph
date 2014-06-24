/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.trainer.dynamicwizard;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.neuroph.core.data.DataSet;
import org.neuroph.training.DataSetLoaderTask;
import org.neuroph.training.LoopTask;
import org.neuroph.training.MultiLayerPerceptronFactoryTask;
import org.neuroph.training.NormalizationTask;
import org.neuroph.training.ReportTask;
import org.neuroph.training.SamplingTask;
import org.neuroph.training.SaveNeuralNetworkTask;
import org.neuroph.training.SetTrainingPropertiesTask;
import org.neuroph.training.TestTask;
import org.neuroph.training.TrainingPropertiesGeneratorTask;
import org.neuroph.training.TrainingTask;
import org.neuroph.util.data.norm.MaxNormalizer;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;

// An example action demonstrating how the wizard could be called from within
// your code. You can move the code below wherever you need, or register an action:
@ActionID(category = "Training Wizard", id = "org.neuroph.trainer.dynamicwizard.TrainerWizardDynamicWizardAction")
@ActionRegistration(displayName = "Training Wizard")
@ActionReference(path = "Menu/Tools", position = 4)
public final class TrainerWizardDynamicWizardAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        TrainerWizardDynamicWizardPanel1 tp1 = new TrainerWizardDynamicWizardPanel1();
        try {
            //if project is not selected TrainerWizardDynamicWizardPanel1 will not be created
            //so wizard is not started
            if (tp1 == null) {
                throw new Exception("Project with data set required");
            }
            panels.add(tp1);
            panels.add(new TrainerWizardDynamicWizardPanel2());
            panels.add(new TrainerWizardDynamicWizardPanel3());
            String[] steps = new String[panels.size()];
            for (int i = 0; i < panels.size(); i++) {
                Component c = panels.get(i).getComponent();
                // Default step name to component name of panel.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
                }
            }
            final WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
            // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
            wiz.setTitleFormat(new MessageFormat("{0}"));
            wiz.setTitle("Trainer wizard");

            if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {

              (new Thread(new Runnable() {
                    @Override
                    public void run() {

                        org.neuroph.training.Process trainingProcess = new org.neuroph.training.Process();
                        trainingProcess.addListener( TrainingProcessManager.getDefault() );
                        
                        //---------------------------------------------------------------------         
                        // Generates different settings for neural network and training process as Stack<Properties>
                        // it would be great to load all these settings from XML file
                        // Creates process var: processPropertiesStack
                        // izgenerise sva podesavanja i stavi na stek. i onda se proces izvrsava za jedno po jedno podesavanje sa steka
                        TrainingPropertiesGeneratorTask trainingPropertiesGeneratorTask = new TrainingPropertiesGeneratorTask("trainingPropertiesGenerator", "trainingPropertiesStack");
                        //set parameters for training process 
                        trainingPropertiesGeneratorTask.setLearningRates((double[]) wiz.getProperty("learningRates"));
                        trainingPropertiesGeneratorTask.setHiddenNeurons((int[]) wiz.getProperty("hiddenNeurons"));
                        trainingPropertiesGeneratorTask.setTrainingSetPercents((int[]) wiz.getProperty("trainingSetPercents"));
                        trainingPropertiesGeneratorTask.setDataSet((DataSet) wiz.getProperty("dataSet"));
                        trainingProcess.addTask(trainingPropertiesGeneratorTask);

                        trainingProcess.addTask(new SetTrainingPropertiesTask("setProperties", "trainingPropertiesStack", "trainingProperties"));

                        DataSetLoaderTask dataLoaderTask = new DataSetLoaderTask("dataSetLoader", "dataSetProperties", "dataSet");
                        trainingProcess.addTask(dataLoaderTask);

                        // some list of preprocessing tasks - this shoul dbe optional along with settings
                        NormalizationTask normalizationTask = new NormalizationTask("normalizationTask", "dataSet", new MaxNormalizer());
                        trainingProcess.addTask(normalizationTask);

                        trainingProcess.addTask(new MultiLayerPerceptronFactoryTask("neuralNetFactory", "neuralNetworkProperties", "neuralNetwork"));

                        // this step should be optional. Should we save sampled sets?
                        trainingProcess.addTask(new SamplingTask("trainingSetSampling"));

                        TrainingTask trainingTask = new TrainingTask("training", "neuralNetwork", "trainingSet");
                        trainingProcess.addTask(trainingTask);

                        // save trained network
                        SaveNeuralNetworkTask saveTask = new SaveNeuralNetworkTask("saveNetwork", "neuralNetwork");
                        trainingProcess.addTask(saveTask);

                        TestTask testTask = new TestTask("test", "neuralNetwork", "testSet"); // add test set here
                        trainingProcess.addTask(testTask);
                        
                        // we should alos do cross validation with several test sets here...

                        trainingProcess.addTask(new LoopTask("setProperties", (Integer) wiz.getProperty("numIteration"))); // repeat while  there are settngs

                        trainingProcess.addTask(new ReportTask("report"));
                        trainingProcess.execute();

                    }
                })).start();
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, "Please select project with data set", "Project required", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /**
     * Saves all trained networks to file
     *
     */
//    private void saveTraindNetworks() {
//        NeurophProject np = CurrentProject.getInstance().getCurrentProject();
//        NeurophProjectFilesFactory fileFactory = NeurophProjectFilesFactory.getDefault();
//        int i = 1;
//        for (NeuralNetwork nnet : TrainerManager.getInstance().getTrainedNetworks()) {
//            nnet.setLabel("trainedNetwork_" + i);
//            fileFactory.createNeuralNetworkFile(nnet);
//            i++;
//        }
//
//    }
}
