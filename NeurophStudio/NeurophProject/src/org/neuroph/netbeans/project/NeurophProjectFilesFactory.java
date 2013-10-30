package org.neuroph.netbeans.project;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javax.swing.JOptionPane;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.openide.util.Exceptions;

/**
 * create singleton out of this
 * this class should be a factory for project files instead CreateXXXFileService
 * @author zoran
 */
public class NeurophProjectFilesFactory {
    NeurophProject currentProject;
    NeuralNetwork neuralNet;
    String networkName; // we should use/have some network ID instead name, since name may not be unigue  
    String createdFilePath; // we nned it to select that node in project tree upon network creation

    private static NeurophProjectFilesFactory instance;
    
    private NeurophProjectFilesFactory() {
    }
    
    public static NeurophProjectFilesFactory getDefault() {
        if (instance == null) {
            instance = new NeurophProjectFilesFactory();
        }
        return instance;
    }    
    
    
    
    public void createNeuralNetworkFile(NeuralNetwork nnet) {
        try {
            currentProject = CurrentProject.getInstance().getCurrentProject();                     
            String path = currentProject.getProjectDirectory().getPath();
            path += "/" + NeurophProject.NEURAL_NETWORKS_DIR + "/";
            this.neuralNet = nnet;
            networkName = this.neuralNet.getLabel();
            String fullFilePath = path + networkName + ".nnet";
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fullFilePath));
            try {
                createdFilePath = null;
                stream.writeObject(this.neuralNet);   
                createdFilePath = fullFilePath;
                currentProject.addNotify(); // notify project that new file has been created                         
            } finally {
                stream.close();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            JOptionPane.showMessageDialog(null, "Please select project");
        }
    }

    public void createTrainingSetFile(DataSet trainingSet) {
        try {
            currentProject = CurrentProject.getInstance().getCurrentProject();             
            String path = currentProject.getProjectDirectory().getPath();
            
            path += "/" + NeurophProject.TRAINING_SETS_DIR + "/";
            String fullFilePath = path + trainingSet.getLabel() + ".tset";
            
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fullFilePath));
            try {
                createdFilePath = null;
                stream.writeObject(trainingSet);        
                createdFilePath = fullFilePath;
                currentProject.addNotify();             
            } finally {
                stream.close();
            }

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            JOptionPane.showMessageDialog(null, "Please select project");
        }
    }
   
    public void createTestSetFile(DataSet testSet) {
        try {
            currentProject = CurrentProject.getInstance().getCurrentProject();             
            String path = currentProject.getProjectDirectory().getPath();
            
            path += "/" + NeurophProject.TEST_SETS_DIR + "/";
            String fullFilePath = path + testSet.getLabel() + ".tset";
            
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fullFilePath));
            try {
                createdFilePath = null;
                stream.writeObject(testSet);        
                createdFilePath = fullFilePath;
                currentProject.addNotify();             
            } finally {
                stream.close();
            }

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            JOptionPane.showMessageDialog(null, "Please select project");
        }
    }    
    
    public String getCreatedFilePath() {
        return createdFilePath;
    }    


}
