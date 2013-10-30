package org.neuroph.netbeans.wizards;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class NewNeuralNetworkWizardPanel2 implements WizardDescriptor.ValidatingPanel {
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;

    private Component returnComponent() {
        int type = NeuralNetworkTypeWizard.getInstance().getType();
        switch (type) {
            case NeuralNetworkTypeWizard.ADALINE:
                return AdalineVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.PERCEPTRON:
                return PerceptronVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.MULTI_LAYER_PERCEPTRON:
                return MultiLayerPerceptronVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.HOPFIELD:
                return HopfieldVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.BAM:
                return BAMVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.KOHONEN:
                return KohonenVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.SUPERVISED_HEBBIAN:
                return SupervisedHebbianVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.UNSUPERVISED_HEBBIAN:
                return UnsupervisedHebbianVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.MAXNET:
                return MaxnetVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.COMPETITIVE_NETWORK:
                return CompetitiveNetworkVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.RBF:
                return RBFVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.INSTAR:
                return InstarVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.OUTSTAR:
                return OutstarVisualPanel1.getInstance();
            case NeuralNetworkTypeWizard.NOPROP:
                return NoPropVisualPanel1.getInstance();                
            default:
                //return null;
                return new JPanel();

        }
    }
    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.

    public Component getComponent() {
//        Component componentToSet = returnComponent();
//        if (component == null || !component.equals(componentToSet)) {
//            if (component != null) {
//                System.out.println(component.equals(componentToSet));
//            }
//            component = returnComponent();
//        }
            component = returnComponent();
            return component;
    }

    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }

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
    public void readSettings(Object settings) {
    }
    
    public void storeSettings(Object settings) {
        int type = NeuralNetworkTypeWizard.getInstance().getType();
        switch (type) {
            case NeuralNetworkTypeWizard.ADALINE:
                ((WizardDescriptor) settings).putProperty("inputsNumber", ((AdalineVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                break;
            case NeuralNetworkTypeWizard.PERCEPTRON:
                ((WizardDescriptor) settings).putProperty("inputs number", ((PerceptronVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("outputs number", ((PerceptronVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("learning rule", ((PerceptronVisualPanel1) getComponent()).getLearningRuleComboBox().getSelectedItem().toString().trim());
                break;
            case NeuralNetworkTypeWizard.MULTI_LAYER_PERCEPTRON:
                ((WizardDescriptor) settings).putProperty("inputs number", ((MultiLayerPerceptronVisualPanel1) getComponent()).getInputNeurosField().getText().trim());
                ((WizardDescriptor) settings).putProperty("hidden number", ((MultiLayerPerceptronVisualPanel1) getComponent()).getHiddenNeurosField().getText().trim());
                ((WizardDescriptor) settings).putProperty("outputs number", ((MultiLayerPerceptronVisualPanel1) getComponent()).getOutputNeurosField().getText().trim());
                ((WizardDescriptor) settings).putProperty("transfer function", ((MultiLayerPerceptronVisualPanel1) getComponent()).getTransferFuntcionComboBox().getSelectedItem().toString().toUpperCase());
                ((WizardDescriptor) settings).putProperty("learning rule", ((MultiLayerPerceptronVisualPanel1) getComponent()).getLearningRuleComboBox().getSelectedItem());
                ((WizardDescriptor) settings).putProperty("use bias", ((MultiLayerPerceptronVisualPanel1) getComponent()).getUseBiasNeurosCheckBox().isSelected());
                ((WizardDescriptor) settings).putProperty("connect in2out", ((MultiLayerPerceptronVisualPanel1) getComponent()).getConnectInputToOutputCheckBox().isSelected());
                break;
            case NeuralNetworkTypeWizard.HOPFIELD:
                ((WizardDescriptor) settings).putProperty("neurons number", ((HopfieldVisualPanel1) getComponent()).getNeuronsNumField().getText().trim());
                break;
            case NeuralNetworkTypeWizard.BAM:
                ((WizardDescriptor) settings).putProperty("input number", ((BAMVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
                ((WizardDescriptor) settings).putProperty("output number", ((BAMVisualPanel1) getComponent()).getOutputNeuronsField().getText().trim());
                break;
            case NeuralNetworkTypeWizard.KOHONEN:
                ((WizardDescriptor) settings).putProperty("input number", ((KohonenVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
                ((WizardDescriptor) settings).putProperty("map number", ((KohonenVisualPanel1) getComponent()).getMapNeuronsField().getText().trim());
                break;
            case NeuralNetworkTypeWizard.SUPERVISED_HEBBIAN:
                ((WizardDescriptor) settings).putProperty("input number", ((SupervisedHebbianVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("output number", ((SupervisedHebbianVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("transfer function", ((SupervisedHebbianVisualPanel1) getComponent()).getTransferFunctionComboBox().getSelectedItem().toString().toUpperCase());
                break;
            case NeuralNetworkTypeWizard.UNSUPERVISED_HEBBIAN:
                ((WizardDescriptor) settings).putProperty("input number", ((UnsupervisedHebbianVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("output number", ((UnsupervisedHebbianVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("transfer function", ((UnsupervisedHebbianVisualPanel1) getComponent()).getTransferFunctionComboBox().getSelectedItem().toString().toUpperCase());
                break;
            case NeuralNetworkTypeWizard.MAXNET:
                ((WizardDescriptor) settings).putProperty("neurons number", ((MaxnetVisualPanel1) getComponent()).getNeuronsNumField().getText().trim());
                break;
            case NeuralNetworkTypeWizard.COMPETITIVE_NETWORK:
                ((WizardDescriptor) settings).putProperty("input number", ((CompetitiveNetworkVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("output number", ((CompetitiveNetworkVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                break;
            case NeuralNetworkTypeWizard.RBF:
                ((WizardDescriptor) settings).putProperty("input number", ((RBFVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("rbf number", ((RBFVisualPanel1) getComponent()).getRbfNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("output number", ((RBFVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                break;
            case NeuralNetworkTypeWizard.INSTAR:
                System.out.println("polje je:"+((InstarVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                ((WizardDescriptor) settings).putProperty("input number", ((InstarVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                break;
            case NeuralNetworkTypeWizard.OUTSTAR:
                ((WizardDescriptor) settings).putProperty("output number", ((OutstarVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                break;                
            case NeuralNetworkTypeWizard.NOPROP:
                ((WizardDescriptor) settings).putProperty("inputNeurons", ((NoPropVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
                ((WizardDescriptor) settings).putProperty("outputNeurons", ((NoPropVisualPanel1) getComponent()).getOutputNeuronsField().getText().trim());
                ((WizardDescriptor) settings).putProperty("hiddenNeurons", ((NoPropVisualPanel1) getComponent()).getHiddenNeuronsField().getText().trim());                
                break;                
        }
    }

    public void validate() throws WizardValidationException {
        int type = NeuralNetworkTypeWizard.getInstance().getType();
        switch (type) {
            case NeuralNetworkTypeWizard.ADALINE:
                try{
                    int adalineInputNumber = Integer.parseInt(((AdalineVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Input number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.PERCEPTRON:
                try{
                    int perceptronInputsNumber = Integer.parseInt(((PerceptronVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                try{
                    int perceptronOutputsNumber = Integer.parseInt(((PerceptronVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Outputs number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.MULTI_LAYER_PERCEPTRON:
                try{
                    int multiLayerPerceptronInputsNumber = Integer.parseInt(((MultiLayerPerceptronVisualPanel1) getComponent()).getInputNeurosField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                String[] hiddenNeurons = ((MultiLayerPerceptronVisualPanel1) getComponent()).getHiddenNeurosField().getText().trim().split(" ");
                for (int i = 0; i < hiddenNeurons.length; i++) {
                    try {
                        int multiLayerPerceptronHiddenNumber = Integer.parseInt(hiddenNeurons[i]);
                    } catch (NumberFormatException ex) {
                        throw new WizardValidationException(null, "Hidden numbers must integer value seperated with space!", null);
                    }
                }
                try{
                    int mutliLayerPerceptronOutputsNumber = Integer.parseInt(((MultiLayerPerceptronVisualPanel1) getComponent()).getOutputNeurosField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Outputs number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.HOPFIELD:
                try{
                    int hopfieldNeuronsNumber = Integer.parseInt(((HopfieldVisualPanel1) getComponent()).getNeuronsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Neurons number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.BAM:
                try{
                    int bamInputsNumber = Integer.parseInt(((BAMVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                try{
                    int bamOutputsNumber = Integer.parseInt(((BAMVisualPanel1) getComponent()).getOutputNeuronsField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Outputs number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.KOHONEN:
                try{
                    int kohonenInputsNumber = Integer.parseInt(((KohonenVisualPanel1) getComponent()).getInputNeuronsField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                try{
                    int kohonenMapNumber = Integer.parseInt(((KohonenVisualPanel1) getComponent()).getMapNeuronsField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Map number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.SUPERVISED_HEBBIAN:
                try{
                    int supervisedHebbianInputsNumber = Integer.parseInt(((SupervisedHebbianVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                try{
                    int supervisedHebbianOutputsNumber = Integer.parseInt(((SupervisedHebbianVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Outputs number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.UNSUPERVISED_HEBBIAN:
                try{
                    int unsupervisedHebbianInputsNumber = Integer.parseInt(((UnsupervisedHebbianVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                try{
                    int unsupervisedHebbianOutputsNumber = Integer.parseInt(((UnsupervisedHebbianVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Outputs number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.MAXNET:
                try{
                    int maxnetNeuronNumber = Integer.parseInt(((MaxnetVisualPanel1) getComponent()).getNeuronsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Neurons number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.COMPETITIVE_NETWORK:
                try{
                    int competitiveNetworkInputsNumber = Integer.parseInt(((CompetitiveNetworkVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                try{
                    int competitiveNetworkOutputsNumber = Integer.parseInt(((CompetitiveNetworkVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Outputs number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.RBF:
                try{
                    int rbfInputsNumber = Integer.parseInt(((RBFVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                try{
                    int rbfNumber = Integer.parseInt(((RBFVisualPanel1) getComponent()).getRbfNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "RBF number must integer value!", null);
                }
                try{
                    int rbfOutputsNumber = Integer.parseInt(((RBFVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Outputs number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.INSTAR:
                try{
                    int instarInputsNumber = Integer.parseInt(((InstarVisualPanel1) getComponent()).getInputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Inputs number must integer value!", null);
                }
                break;
            case NeuralNetworkTypeWizard.OUTSTAR:
                try{
                    int instarOutputsNumber = Integer.parseInt(((OutstarVisualPanel1) getComponent()).getOutputsNumField().getText().trim());
                }catch(NumberFormatException ex){
                     throw new WizardValidationException(null, "Outputs number must integer value!", null);
                }
                break;
        }

    }
}

