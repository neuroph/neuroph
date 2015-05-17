/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.lpr.wiz;

import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class LicencePlateRecognitionWizardPanel2 implements WizardDescriptor.Panel {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private LicencePlateRecognitionVisualPanel2 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public LicencePlateRecognitionVisualPanel2 getComponent() {
        if (component == null) {
            component = new LicencePlateRecognitionVisualPanel2();
        }
        return component;
    }

    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    public void readSettings(Object data) {
    }

    public void storeSettings(Object data) {
        String imageDir = component.getJunkImageDirPath();
        ((WizardDescriptor) data).putProperty("junkDir", imageDir);

    }

    public boolean isValid() {
        return true;
    }

    public void addChangeListener(ChangeListener cl) {
    }

    public void removeChangeListener(ChangeListener cl) {
    }
}
