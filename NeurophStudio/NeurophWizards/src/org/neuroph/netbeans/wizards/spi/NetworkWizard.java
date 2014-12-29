
package org.neuroph.netbeans.wizards.spi;

import java.awt.Component;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;

/**
 *
 * @author borisf
 */
public interface NetworkWizard {
    
    Component getComponent();
    
    NeuralNetwork<? extends LearningRule> createNeuralNetwork(WizardDescriptor settings);
    
     void storePanelData(Object settings);
    
    void validatePanelData() throws WizardValidationException ;
    
}
