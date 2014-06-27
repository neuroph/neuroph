package org.neuroph.netbeans.wizards;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class NewTrainingSetWizardPanel1 implements WizardDescriptor.ValidatingPanel {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public Component getComponent() {
        if (component == null) {
            component = new NewTrainingSetVisualPanel1();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }

    public final void addChangeListener(ChangeListener l) {
    }

    public final void removeChangeListener(ChangeListener l) {
    }
    /*
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
    public final void addChangeListener(ChangeListener l) {
    synchronized (listeners) {
    listeners.add(l);
    }
    }
    public final void removeChangeListener(ChangeListener l) {
    synchronized (listeners) {
    listeners.remove(l);
    }
    }
    protected final void fireChangeEvent() {
    Iterator<ChangeListener> it;
    synchronized (listeners) {
    it = new HashSet<ChangeListener>(listeners).iterator();
    }
    ChangeEvent ev = new ChangeEvent(this);
    while (it.hasNext()) {
    it.next().stateChanged(ev);
    }
    }
     */

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered
    // by the user.
    @Override
    public void readSettings(Object settings) {

    }

    @Override
    public void storeSettings(Object settings) {
        NewTrainingSetValuesWizard.getInstance().setName(((NewTrainingSetVisualPanel1)getComponent()).getLabelTextField().getText().trim());
        ((WizardDescriptor) settings).putProperty("name", ((NewTrainingSetVisualPanel1)getComponent()).getLabelTextField().getText().trim());
        try{
            NewTrainingSetValuesWizard.getInstance().setInputNumber(Integer.parseInt(((NewTrainingSetVisualPanel1)getComponent()).getInputsTextField().getText().trim()));
        }catch(NumberFormatException e){
            //this is used to prevent from cerating an exception because Wizard call storeSetting() method before validate()
            //value will be stored in when validate method cheks out
        }
        ((WizardDescriptor) settings).putProperty("inputsNumber", ((NewTrainingSetVisualPanel1)getComponent()).getInputsTextField().getText().trim());
        try{
            NewTrainingSetValuesWizard.getInstance().setOutputNumber(Integer.parseInt(((NewTrainingSetVisualPanel1)getComponent()).getOutputsTextField().getText().trim()));
        }catch(NumberFormatException e){
            //this is used to prevent from cerating an exception because Wizard call storeSetting() method before validate()
            //value will be stored in when validate method cheks out
        }
        ((WizardDescriptor) settings).putProperty("outputsNumber", ((NewTrainingSetVisualPanel1)getComponent()).getOutputsTextField().getText().trim());
        NewTrainingSetValuesWizard.getInstance().setType(((NewTrainingSetVisualPanel1)getComponent()).getTypeComboBox().getSelectedItem().toString().trim());
        ((WizardDescriptor) settings).putProperty("type", ((NewTrainingSetVisualPanel1)getComponent()).getTypeComboBox().getSelectedItem().toString().trim());
        
        ((WizardDescriptor) settings).putProperty("file", ((NewTrainingSetVisualPanel1)getComponent()).getFileField().getText().toString().trim());
        ((WizardDescriptor) settings).putProperty("delimiter", ((NewTrainingSetVisualPanel1)getComponent()).getDelimiterComboBox().getSelectedItem().toString().trim());
        
    }

    @Override
    public void validate() throws WizardValidationException {
        String name = ((NewTrainingSetVisualPanel1) getComponent()).getLabelTextField().getText().trim();
        if (name.equals("") || name.equals(null)) {
            throw new WizardValidationException(null, "Invalid input! Training set name must not be null value!", null);
        }
        try {
            int inputNumber = Integer.parseInt(((NewTrainingSetVisualPanel1) getComponent()).getInputsTextField().getText().trim());
        } catch (NumberFormatException e) {
            throw new WizardValidationException(null, "Invalid input! Number of inputs must be integer value!", null);
        }
        try {
            int outputNumber = Integer.parseInt(((NewTrainingSetVisualPanel1) getComponent()).getOutputsTextField().getText().trim());
        } catch (NumberFormatException e) {
            throw new WizardValidationException(null, "Invalid input! Number of outputs must be integer value!", null);
        }
    }
}

