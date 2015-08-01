/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.tcr.wizard;

import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class OCRTrainingWizardPanel1 implements WizardDescriptor.ValidatingPanel<WizardDescriptor> {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private OCRTrainingVisualPanel1 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public OCRTrainingVisualPanel1 getComponent() {
        if (component == null) {
            component = new OCRTrainingVisualPanel1();
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
        return true;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        // use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        wiz.putProperty("image", getComponent().getImage());
        wiz.putProperty("text", getComponent().getText());
        wiz.putProperty("scanStr", getComponent().getScanStr());
        wiz.putProperty("fontStr", getComponent().getFontStr());
        System.out.println("STORE");
    }

    public void validate() throws WizardValidationException {
        if (getComponent().getImage() == null && getComponent().getText() == null && getComponent().getImageFolderPath() == null) 
            throw new WizardValidationException(null, "Load the image and text. Specify folder for storing images", null);
        if (getComponent().getImage() == null) 
            throw new WizardValidationException(null, "Load the image.", null);
        if (getComponent().getText() == null) {
            throw new WizardValidationException(null, "Insert the text for trainig.", null);
        }
        if (getComponent().getImageFolderPath() == null) {
            throw new WizardValidationException(null, "Specify path to the folder for storing images.", null);
        }
        
        
        
    }

    
    
}
