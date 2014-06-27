package org.neuroph.netbeans.wizards;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.dev.noprop.NoPropNet;
import org.neuroph.netbeans.project.CurrentProject;
import org.neuroph.netbeans.project.NeurophProject;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.nnet.Adaline;
import org.neuroph.nnet.BAM;
import org.neuroph.nnet.CompetitiveNetwork;
import org.neuroph.nnet.Hopfield;
import org.neuroph.nnet.Instar;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.MaxNet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Outstar;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.RBFNetwork;
import org.neuroph.nnet.SupervisedHebbianNetwork;
import org.neuroph.nnet.UnsupervisedHebbianNetwork;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.BinaryDeltaRule;
import org.neuroph.nnet.learning.DynamicBackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.nnet.learning.PerceptronLearning;
import org.neuroph.nnet.learning.ResilientPropagation;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.random.RangeRandomizer;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.ImageUtilities;
import org.openide.windows.IOProvider;

public final class NewNeuralNetworkWizardIterator implements WizardDescriptor.InstantiatingIterator {

    private int index;
    private WizardDescriptor wizard;
    private WizardDescriptor.Panel[] panels;

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[]{
                        new NewNeuralNetworkWizardPanel1(),
                        new NewNeuralNetworkWizardPanel2()
                    };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                if (c==null) continue;
                if (steps[i] == null) {
                    // Default step name to component name of panel. Mainly
                    // useful for getting the name of the target chooser to
                    // appear in the list of steps.
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                   jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
                }
            }
            getWizard().putProperty("WizardPanel_image", ImageUtilities.loadImage("org/neuroph/netbeans/ide/wizards/neurophLogo.png", true));
        }
        return panels;
    }

    @Override
    public Set instantiate() throws IOException {
        boolean cancelled = getWizard().getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            NeurophProject project = (NeurophProject)getWizard().getProperty("project");
            if (project != null) {
                CurrentProject.getInstance().setCurrentProject(project);
            }
         
            NeurophProjectFilesFactory fileFactory = NeurophProjectFilesFactory.getDefault();
                              
            int type = NeuralNetworkTypeWizard.getInstance().getType();
            String neuralNetworkName = (String) getWizard().getProperty("neural network name");
            switch (type) {
                case NeuralNetworkTypeWizard.EMPTY_NN :
                    NeuralNetwork nnet = new NeuralNetwork();
                     nnet.setLabel(neuralNetworkName);
                     fileFactory.createNeuralNetworkFile(nnet);
                    break;
                case NeuralNetworkTypeWizard.ADALINE:
                    int inputsNumber = Integer.parseInt((String) getWizard().getProperty("inputsNumber"));
                    Adaline nnet0 = NeuralNetworkFactory.createAdaline(inputsNumber);
                    nnet0.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet0);
                    AdalineVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.PERCEPTRON:
                    int inputNeurons = Integer.parseInt((String) getWizard().getProperty("inputs number"));
                    int outputNeurons = Integer.parseInt((String) getWizard().getProperty("outputs number"));
                    String learningRule = ((String) getWizard().getProperty("learning rule"));
                    Perceptron nnet1;
                    if (learningRule.equalsIgnoreCase("Perceptron Learning")) {
                        nnet1 = NeuralNetworkFactory.createPerceptron(inputNeurons,
                                outputNeurons, TransferFunctionType.STEP, PerceptronLearning.class);
                    } else {
                        nnet1 = NeuralNetworkFactory.createPerceptron(inputNeurons,
                                outputNeurons, TransferFunctionType.STEP, BinaryDeltaRule.class);
                    }
                    nnet1.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet1);
                    PerceptronVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.MULTI_LAYER_PERCEPTRON:
                    String inputNeuros = (String) getWizard().getProperty("inputs number");
                    String hiddenNeuros = (String) getWizard().getProperty("hidden number");
                    String outputNeuros = (String) getWizard().getProperty("outputs number");
                    String neurosNum = inputNeuros + " " + hiddenNeuros + " " + outputNeuros;

                    String useBiasS = getWizard().getProperty("use bias").toString();
                    String connectI2OS = getWizard().getProperty("connect in2out").toString();
                    boolean useBias = false;
                    boolean connectI2O = false;
                    if (useBiasS.equalsIgnoreCase("true")) {
                        useBias = true;
                    }
                    if (connectI2OS.equalsIgnoreCase("true")) {
                        connectI2O = true;
                    }

                    String transferFunctionS = (String) getWizard().getProperty("transfer function");
                    TransferFunctionType transferFunction = TransferFunctionType.valueOf(transferFunctionS);

                    String learningRuleS = (String) getWizard().getProperty("learning rule");
                    MultiLayerPerceptron nnet2=null;
                    if (learningRuleS.equalsIgnoreCase("backpropagation")) {
                        nnet2 = NeuralNetworkFactory.createMLPerceptron(
                                neurosNum, transferFunction, BackPropagation.class, useBias, connectI2O);
                    }
                    if (learningRuleS.equalsIgnoreCase("backpropagation with momentum")) {
                        nnet2 = NeuralNetworkFactory.createMLPerceptron(
                                neurosNum, transferFunction, MomentumBackpropagation.class, useBias, connectI2O);
                    }
                    if (learningRuleS.equalsIgnoreCase("resilient propagation")) {
                        nnet2 = NeuralNetworkFactory.createMLPerceptron(
                                neurosNum, transferFunction, ResilientPropagation.class, useBias, connectI2O);
                    }                    
                    if (learningRuleS.equalsIgnoreCase("dynamic backpropagation")) {
                        nnet2 = NeuralNetworkFactory.createMLPerceptron(
                                neurosNum, transferFunction, DynamicBackPropagation.class, useBias, connectI2O);
                    }
                    nnet2.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet2);
                    MultiLayerPerceptronVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.HOPFIELD:
                    int neuronNum = Integer.parseInt((String) getWizard().getProperty("neurons number"));
                    Hopfield nnet3 = NeuralNetworkFactory.createHopfield(neuronNum);
                    nnet3.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet3);
                    HopfieldVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.BAM:
                    int bamInputNeurons = Integer.parseInt((String) getWizard().getProperty("input number"));
                    int bamOutputNeurons = Integer.parseInt((String) getWizard().getProperty("output number"));
                    BAM nnet4 = NeuralNetworkFactory.createBam(bamInputNeurons, bamOutputNeurons);
                    nnet4.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet4);
                    BAMVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.KOHONEN:
                    int kohonenInputNeurons = Integer.parseInt((String) getWizard().getProperty("input number"));
                    int kohonenMapNeurons = Integer.parseInt((String) getWizard().getProperty("map number"));
                    Kohonen nnet5 = NeuralNetworkFactory.createKohonen(kohonenInputNeurons, kohonenMapNeurons);
                    nnet5.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet5);
                    KohonenVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.SUPERVISED_HEBBIAN:
                    int supervisedHebbianInputNeurons = Integer.parseInt((String) getWizard().getProperty("input number"));
                    int supervisedHebbianOutputNeurons = Integer.parseInt((String) getWizard().getProperty("output number"));
                    TransferFunctionType supervisedHebbianTransferFunction = TransferFunctionType.valueOf((String) getWizard().getProperty("transfer function"));
                    SupervisedHebbianNetwork nnet6 = NeuralNetworkFactory.createSupervisedHebbian(supervisedHebbianInputNeurons, supervisedHebbianOutputNeurons, supervisedHebbianTransferFunction);
                    nnet6.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet6);
                    SupervisedHebbianVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.UNSUPERVISED_HEBBIAN:
                    int unsupervisedHebbianInputNeurons = Integer.parseInt((String) getWizard().getProperty("input number"));
                    int unsupervisedHebbianOutputNeurons = Integer.parseInt((String) getWizard().getProperty("output number"));
                    TransferFunctionType unsupervisedHebbianTransferFunction = TransferFunctionType.valueOf((String) getWizard().getProperty("transfer function"));
                    UnsupervisedHebbianNetwork nnet7 = NeuralNetworkFactory.createUnsupervisedHebbian(unsupervisedHebbianInputNeurons, unsupervisedHebbianOutputNeurons, unsupervisedHebbianTransferFunction);
                    nnet7.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet7);
                    UnsupervisedHebbianVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.MAXNET:
                    int maxnetNeuronsNum = Integer.parseInt((String) getWizard().getProperty("neurons number"));
                    MaxNet nnet8 = NeuralNetworkFactory.createMaxNet(maxnetNeuronsNum);
                    nnet8.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet8);
                    MaxnetVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.COMPETITIVE_NETWORK:
                    int competitiveNetworkInputNeurons = Integer.parseInt((String) getWizard().getProperty("input number"));
                    int competitiveNetworkOutputNeurons = Integer.parseInt((String) getWizard().getProperty("output number"));
                    CompetitiveNetwork nnet9 = NeuralNetworkFactory.createCompetitiveNetwork(competitiveNetworkInputNeurons, competitiveNetworkOutputNeurons);
                    nnet9.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet9);
                    CompetitiveNetworkVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.RBF:
                    int rbfinputNeurons = Integer.parseInt((String) getWizard().getProperty("input number"));
                    int rbfNeurons = Integer.parseInt((String) getWizard().getProperty("rbf number"));
                    int rbfoutputNeurons = Integer.parseInt((String) getWizard().getProperty("output number"));
                    RBFNetwork nnet10 = NeuralNetworkFactory.createRbfNetwork(rbfinputNeurons, rbfNeurons, rbfoutputNeurons);
                    nnet10.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet10);
                    RBFVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.INSTAR:
                    int instarInputNeurons = Integer.parseInt((String) getWizard().getProperty("input number"));
                    Instar nnet11 = NeuralNetworkFactory.createInstar(instarInputNeurons);
                    nnet11.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet11);
                    InstarVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.OUTSTAR:
                    int outstarOutputNeurons = Integer.parseInt((String) getWizard().getProperty("output number"));
                    Outstar nnet12 = NeuralNetworkFactory.createOutstar(outstarOutputNeurons);
                    nnet12.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(nnet12);
                    OutstarVisualPanel1.getInstance().clearForm();
                    break;
                case NeuralNetworkTypeWizard.NOPROP:                    
                    int noPropInputNeurons = Integer.parseInt((String) getWizard().getProperty("inputNeurons"));
                    int noPropHiddenNeurons = Integer.parseInt((String) getWizard().getProperty("hiddenNeurons"));
                    int noPropOutputNeurons = Integer.parseInt((String) getWizard().getProperty("outputNeurons"));

                    int[] neuronsCount = new int[3];
                    neuronsCount[0] = noPropInputNeurons;
                    neuronsCount[1] = noPropHiddenNeurons;
                    neuronsCount[2] = noPropOutputNeurons;
                    
                    NoPropNet noPropNet = new NoPropNet(TransferFunctionType.TANH , true, neuronsCount);
                    noPropNet.randomizeWeights(new RangeRandomizer(-1, 1));
                    
                    noPropNet.setLabel(neuralNetworkName);
                    fileFactory.createNeuralNetworkFile(noPropNet);
                    
                    NoPropVisualPanel1.getInstance().clearForm();
//                    
                    break;
            }
           
            
       IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Created neural network "+neuralNetworkName);
            
        String createdFilePath = fileFactory.getCreatedFilePath();
        
        FileObject fao = FileUtil.toFileObject(new File(createdFilePath));
        DataObject dao = DataObject.find(fao);
        return dao != null ? Collections.singleton(dao) : Collections.EMPTY_SET;            
            
        } // end if !canceled
        
      return Collections.EMPTY_SET;
    }

    @Override
    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    @Override
    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    @Override
    public WizardDescriptor.Panel current() {
        return getPanels()[index];
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().length;
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().length - 1;
    }

    @Override
    public boolean hasPrevious() {
        return index > 0;
    }

    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    // If something changes dynamically (besides moving between panels), e.g.
    // the number of panels changes in response to user input, then uncomment
    // the following and call when needed: fireChangeEvent();
    /*
    private Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
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
    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        String[] beforeSteps = null;
        Object prop = getWizard().getProperty("WizardPanel_contentData");
        if (prop != null && prop instanceof String[]) {
            beforeSteps = (String[]) prop;
        }

        if (beforeSteps == null) {
            beforeSteps = new String[0];
        }

        String[] res = new String[(beforeSteps.length - 1) + panels.length];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                if (i==2) res[i] = "Set network specific settings"; // dirty hack
                    else res[i] = panels[i - beforeSteps.length + 1].getComponent().getName();
            }
        }
        return res;
    }

    /**
     * @return the wizard
     */
    public WizardDescriptor getWizard() {
        return wizard;
    }
}
