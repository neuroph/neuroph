package org.neuroph.netbeans.imr.wiz;

import java.awt.Component;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeListener;
import org.neuroph.imgrec.ColorMode;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;

/**
 *
 * @author Djordje
 */
public class ImageRecognitionWizardPanel1  implements WizardDescriptor.Panel  {

    private ImageRecognitionVisualPanel1 component;
    private boolean isValid = true;

    public Component getComponent() {
        if (component == null) {
            try {
                component = new ImageRecognitionVisualPanel1();
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
        String imageDir = component.getImageDirPath();
        ((WizardDescriptor) data).putProperty("imageDir", imageDir);
        ColorMode colorMode = getColorMode();
        ((WizardDescriptor) data).putProperty("colorMode", colorMode);
    }

    public boolean isValid() {
        return isValid;
    }

    public void addChangeListener(ChangeListener cl) {
    }

    public void removeChangeListener(ChangeListener cl) {
    }

    private ColorMode getColorMode() {
        ColorMode colorMode;         
        
        ButtonGroup group  = component.getColorModeButtonGroup();
        for (Enumeration e=group.getElements(); e.hasMoreElements(); ) {
            JRadioButton b = (JRadioButton)e.nextElement();
            if (b.getModel() == group.getSelection()) {
               String selectedColorMode = b.getText() ;
                if (selectedColorMode != null && selectedColorMode.equalsIgnoreCase("Color")) {
                    return ColorMode.FULL_COLOR;
                } else if (selectedColorMode != null && selectedColorMode.equalsIgnoreCase("Black and white")) {
                    return ColorMode.BLACK_AND_WHITE;
                } else {
                    return null;
                }               
            }
    }
        
//        String selectedColorMode = component.getColorModeButtonGroup().getSelection().getActionCommand();
//        if (selectedColorMode != null && selectedColorMode.equalsIgnoreCase("Color")) {
//            colorMode = ColorMode.FULL_COLOR;
//        } else if (selectedColorMode != null && selectedColorMode.equalsIgnoreCase("Black and white")) {
//            colorMode = ColorMode.BLACK_AND_WHITE;
//        } else {
//            return null;
//        }
        return null;

    }

/*    public void validate() throws WizardValidationException {
        File file = new File((CurrentProject.getInstance().getCurrentProject().getProjectDirectory()).getPath() + "/Images/ImagesDir");
        File[] files = file.listFiles();
        
        
        if (!file.exists() || file == null || files.length==0) {
            isValid = false;
            throw new WizardValidationException(null, "You must add some images in order to train neuro network!", null);
        }
        
        if(file.exists() && file != null && files.length!=0) {
            isValid = true;
        }
    } */
}
