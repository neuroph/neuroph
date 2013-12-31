package org.neuroph.bci.mindwave;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Tools",
        id = "org.neuroph.bci.mindwave.MindWaveAction")
@ActionRegistration(
        displayName = "#CTL_BCIMindWaveAction")
@ActionReference(path = "Menu/Tools", position = 200)
@Messages("CTL_BCIMindWaveAction=Mind Wave")
public final class MindWaveAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        MindWaveTopComponent tc = MindWaveTopComponent.findInstance();
        tc.open();
        tc.requestActive();
    }
}
