/*
// * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.neuroph.netbeans.jmevisualization.charts.JMEVisualizationTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Window",
        id = "org.neuroph.netbeans.jmevisualization.JMEStarter")
@ActionRegistration(
        displayName = "#CTL_JMEStarter")
@ActionReference(path = "Menu/Window", position = 3333)
@Messages("CTL_JMEStarter=JMEStarter")
public final class JMEStarter implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JMEVisualizationTopComponent tc = JMEVisualizationTopComponent.findInstance();
        tc.open();
    }
}
