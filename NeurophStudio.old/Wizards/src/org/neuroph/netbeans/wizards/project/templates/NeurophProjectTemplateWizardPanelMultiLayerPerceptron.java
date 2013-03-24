package org.neuroph.netbeans.wizards.project.templates;


import org.openide.WizardDescriptor;




/**
 * Panel just asking for basic info.
 */
public class NeurophProjectTemplateWizardPanelMultiLayerPerceptron extends NeurophProjectTemplateWizardPanel implements WizardDescriptor.Panel,
        WizardDescriptor.ValidatingPanel, WizardDescriptor.FinishablePanel {

  

    @Override
    public boolean isFinishPanel() {
        return true;
    }

}
