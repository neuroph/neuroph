/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imagepreprocessing.wizard;

import java.io.File;
import javax.swing.event.ChangeListener;
import org.neuroph.imgrec.ColorMode;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;

public class ImageDataSetWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor>,WizardDescriptor.ValidatingPanel<WizardDescriptor> {
    
    
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private ImageDataSetVisualPanel1 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public ImageDataSetVisualPanel1 getComponent() {
        if (component == null) {
            try {
                component = new ImageDataSetVisualPanel1();
            } catch (Exception ex) {
                component = null;
            }
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
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
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
        String imageDir = component.getImageDirPath();
        ((WizardDescriptor) wiz).putProperty("imageDir", imageDir);
        wiz.putProperty("samplingWidth", Integer.parseInt(component.getSamplingWidthTextField().getText()));
        wiz.putProperty("samplingHeight", Integer.parseInt(component.getSamplingHeightTextField().getText()));
        
    }

    @Override
    public void validate() throws WizardValidationException {
        try {
            Integer.parseInt(component.getSamplingWidthTextField().getText());
            Integer.parseInt(component.getSamplingHeightTextField().getText());
        } catch (Exception e) {
            throw new WizardValidationException(null, "Sampling resolution must be a number", null);
        }
    }

}
