package org.neuroph.netbeans.lpr;

import java.awt.Component;
import java.io.IOException;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;

/**
 *
 * @author Djordje
 */
public class LicencePlateRecognitionWizardPanel2 implements WizardDescriptor.Panel {

    private LicencePlateRecognitionVisualPanel2 component;

    public Component getComponent() {
        if (component == null) {
            try {
                component = new LicencePlateRecognitionVisualPanel2();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
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
