package org.neuroph.netbeans.rightclick;

import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class WizardCreateTraSetAndTestSetWizardPanel1 implements WizardDescriptor.ValidatingPanel {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private WizardCreateTraSetAndTestSetVisualPanel1 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public WizardCreateTraSetAndTestSetVisualPanel1 getComponent() {
        if (component == null) {
            component = new WizardCreateTraSetAndTestSetVisualPanel1();
        }
        return component;
    }

   
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

  
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
//        System.out.println("    Usaou u isvalid");
//         try {
//           
//            int training = Integer.parseInt(((WizardCreateTraSetAndTestSetVisualPanel1)getComponent()).getjTextFieldTraining().getText());
//            ((WizardCreateTraSetAndTestSetVisualPanel1)getComponent()).getjLabel6().setText("");
//           ((WizardCreateTraSetAndTestSetVisualPanel1)getComponent()). getjLabel6().setForeground(Color.red);
//            if(training<0||training>100){
//                ((WizardCreateTraSetAndTestSetVisualPanel1)getComponent()).getjLabel6().setText("Sum must be 100%");
//                return false;
//            }
//            String valueTestSet = String.valueOf(100 - training);
//            ((WizardCreateTraSetAndTestSetVisualPanel1)getComponent()).getjTextFieldTest().setText(valueTestSet);
//            return true;
//        } catch (Exception e) {
//            return false;            
//        }
        
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

   
    public void addChangeListener(ChangeListener l) {       
    }

    @Override
    public void removeChangeListener(ChangeListener l) {        
    }

   
    public void readSettings(Object wiz) {
        // use wiz.getProperty to retrieve previous panel state
    }

   
    public void storeSettings(Object wiz) {
        // use wiz.putProperty to remember current panel state
         String testPer = ((WizardCreateTraSetAndTestSetVisualPanel1)getComponent()).getjTextFieldTest().getText();
         String treinPer = ((WizardCreateTraSetAndTestSetVisualPanel1)getComponent()).getjTextFieldTraining().getText();
        ((WizardDescriptor)wiz).putProperty("numTest", testPer);
        ((WizardDescriptor)wiz).putProperty("numTraining", treinPer);
    }
    public void validate() throws WizardValidationException {
        String name = ((WizardCreateTraSetAndTestSetVisualPanel1) getComponent()).getjTextFieldTraining().getText().trim();
        if (name.equals("") || name.equals(null)) {
            throw new WizardValidationException(null, "Invalid input! Training set name must not be null value!", null);
        }
        try {
            int inputNumber = Integer.parseInt(((WizardCreateTraSetAndTestSetVisualPanel1) getComponent()).getjTextFieldTest().getText().trim());
             int inputNumber2 = Integer.parseInt(((WizardCreateTraSetAndTestSetVisualPanel1) getComponent()).getjTextFieldTraining().getText().trim());
        } catch (NumberFormatException e) {
            throw new WizardValidationException(null, "Invalid input! Number of inputs must be integer value!", null);
        }
        try {
            int inputNumber = Integer.parseInt(((WizardCreateTraSetAndTestSetVisualPanel1) getComponent()).getjTextFieldTest().getText().trim());
             int inputNumber2 = Integer.parseInt(((WizardCreateTraSetAndTestSetVisualPanel1) getComponent()).getjTextFieldTraining().getText().trim());
             if(100-inputNumber!=inputNumber2)throw new WizardValidationException(null, "Sum must be 100", null);
        } catch (NumberFormatException e) {
            throw new WizardValidationException(null, "Invalid input! Number of outputs must be integer value!", null);
        }
    }

  
}
