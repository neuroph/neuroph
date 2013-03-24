/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.wizards.project.templates;


import org.openide.WizardDescriptor;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;



/**
 * Panel just asking for basic info.
 */
public class NeurophProjectTemplateWizardPanelIMR extends NeurophProjectTemplateWizardPanel implements WizardDescriptor.Panel,
        WizardDescriptor.ValidatingPanel, WizardDescriptor.FinishablePanel {

  

    @Override
    public boolean isFinishPanel() {

        return true;
    }

}
