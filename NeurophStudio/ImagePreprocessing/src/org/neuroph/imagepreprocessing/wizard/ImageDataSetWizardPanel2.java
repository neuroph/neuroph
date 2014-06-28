/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imagepreprocessing.wizard;

import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class ImageDataSetWizardPanel2 implements WizardDescriptor.Panel<WizardDescriptor>,WizardDescriptor.ValidatingPanel<WizardDescriptor> {

    boolean isValid = true;
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private ImageDataSetVisualPanel2 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public ImageDataSetVisualPanel2 getComponent() {
        if (component == null) {
            component = new ImageDataSetVisualPanel2();
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
        return isValid;
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
        wiz.putProperty("rotation", createRotationArray());
        wiz.putProperty("scaling", createScalingArray());
        wiz.putProperty("transition", createTransitionArray());
        wiz.putProperty("translationColor", component.getTranslationBackgroundPanel().getBackground());
        wiz.putProperty("rotationColor", component.getRotationBackgroundPanel1().getBackground());
      
    }

    private int[] createRotationArray() {

        if (component.getjCheckBoxRotation().isSelected()) {
            int from = (Integer) component.getjSpinnerRotationFrom().getValue();
            int to = (Integer) component.getjSpinnerRotationTo().getValue();
            int step = (Integer) component.getjSpinnerRotationStep().getValue();
            int numIter = ((to - from) / step) + 1;
            int[] array = new int[numIter];
            for (int i = 0; i < numIter; i++) {
                array[i] = from + step * i;
            }
            return array;
        }
        return new int[0];
    }

    private double[] createScalingArray() {
        
        if (component.getjCheckBoxScaling().isSelected()) {
            double from = (Double) component.getjSpinnerScalingFrom().getValue();
            double to = (Double) component.getjSpinnerScalingTo().getValue();
            double step = (Double) component.getjSpinnerScalingStep().getValue();
            int numIter = (int) (((to - from) / step) + 1);
            double[] array = new double[numIter];
            for (int i = 0; i < numIter; i++) {
                array[i] = from + step * i;
            }
            return array;
        }
        return new double[0];
    }

    private int[] createTransitionArray() {
        if (component.getjCheckBoxTranslation().isSelected()) {
            int from = (Integer) component.getjSpinnerTranslationFrom().getValue();
            int to = (Integer) component.getjSpinnerTranslationTo().getValue();
            int step = (Integer) component.getjSpinnerTranslationStep().getValue();
            int numIter = ((to - from) / step) + 1;
            int[] array = new int[numIter];
            for (int i = 0; i < numIter; i++) {
                array[i] = from + step * i;
            }
            return array;
        }
        return new int[0];
    }

    @Override
    public void validate() throws WizardValidationException {
        String message = "";
        if (component.getjCheckBoxScaling().isSelected() && (Double)component.getjSpinnerScalingStep().getValue() == 0) {
           message += "Scaling step can't be 0!\n"; 
        }
        if (component.getjCheckBoxRotation().isSelected() && (Integer)component.getjSpinnerRotationStep().getValue() == 0) {
            message += "Rotation step can't be 0!\n"; 
        }
        if (component.getjCheckBoxTranslation().isSelected() && (Integer)component.getjSpinnerTranslationStep().getValue() == 0) {
            message += "Translation step can't be 0!"; 
        }
        
        if (!message.equals("")) {
            throw new WizardValidationException(null, message, null);
        }
         
    }

}
