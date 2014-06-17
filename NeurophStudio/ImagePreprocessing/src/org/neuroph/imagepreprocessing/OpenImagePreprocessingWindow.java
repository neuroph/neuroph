/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.imagepreprocessing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(
        category = "Window",
        id = "org.neuroph.imagepreprocessing.OpenImagePreprocessingWindow"
)
@ActionRegistration(
        displayName = "#CTL_OpenImagePreprocessingWindow"
)
@ActionReference(path = "Menu/Tools", position = 1800)
@Messages("CTL_OpenImagePreprocessingWindow=Image Preprocessing")
public final class OpenImagePreprocessingWindow implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent component = WindowManager.getDefault()
                .findTopComponent("PreprocessingWindowTopComponent");
        component.open();
        component.requestActive();
    }
}
