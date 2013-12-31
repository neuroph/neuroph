package org.neuroph.neurosky.demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Starts MindWaveDemo - opens NeuroSkyDemoTopComponent window
 * @author zoran
 */
@ActionID(
        category = "Tools",
        id = "org.neuroph.neurosky.demo.MindWaveDemoAction")
@ActionRegistration(
        displayName = "#CTL_MindWaveDemo")
@ActionReference(path = "Menu/Tools", position = 250)
@Messages("CTL_MindWaveDemo=Mind Wave Demo")
public final class MindWaveDemoAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        NeuroSkyDemoTopComponent tc = NeuroSkyDemoTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        
    }
}
