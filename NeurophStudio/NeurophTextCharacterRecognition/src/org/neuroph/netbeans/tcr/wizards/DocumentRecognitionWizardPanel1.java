/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.tcr.wizards;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import org.neuroph.netbeans.project.NeurophProject;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;

public class DocumentRecognitionWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private DocumentRecognitionVisualPanel1 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public DocumentRecognitionVisualPanel1 getComponent() {
        if (component == null) {
            component = new DocumentRecognitionVisualPanel1();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        System.out.println("");
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        System.out.println("");
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        System.out.println("");
            
        

// use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        
        NeurophProject neurophProject = (NeurophProject)wiz.getProperty("project");
       
        FileObject fileObject = neurophProject.getProjectDirectory();
        
        String projectPath = fileObject.getPath();
        String imagesFolderPath = projectPath + File.separator + "Images";
        String documentsFolderPath = projectPath + File.separator + "Documents";
        
        new File(documentsFolderPath).mkdirs();
        new File(imagesFolderPath).mkdirs();
        
        List<DocumentWrapper> documents = component.getDocuments();
        for (DocumentWrapper document : documents) {
            
            BufferedImage image = document.getImage();
            String imageName = document.getImageName();
            String format = imageName.substring(imageName.indexOf(".")+1);
            try {
                ImageIO.write(image, format, new File(imagesFolderPath+File.separator+imageName));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Problem occured during saving image: "+imageName, "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            // text files
            
            String textPath = document.getTextPath();
            String textName = document.getTextName();
            
            
            try {
                String text = new String(Files.readAllBytes(Paths.get(textPath, "")));
                Files.write(Paths.get(documentsFolderPath+File.separator+textName), text.getBytes());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Problem occured during saving text file: "+textName, "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            
            
            
            
        }
        
        
        
        
        
        
        // use wiz.putProperty to remember current panel state
    }

}
