package org.neuroph.netbeans.stock.wizard;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.netbeans.stock.StockPredictionHelper;
import org.neuroph.netbeans.stock.StockPredictionManager;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.VectorParser;
import org.openide.WizardDescriptor;

public final class SPredictionWizardIterator implements WizardDescriptor.InstantiatingIterator {

    private int index;

    private WizardDescriptor wizard;
    private WizardDescriptor.Panel[] panels;

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[] {
                new SPredictionWizardPanel1(),
                new SPredictionWizardPanel2(),
                new SPredictionWizardPanel3()
            };
            String[] steps = createSteps();
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                if (steps[i] == null) {
                    // Default step name to component name of panel. Mainly
                    // useful for getting the name of the target chooser to
                    // appear in the list of steps.
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    @Override
    public Set instantiate() throws IOException {

        String trainingLabel = (String)wizard.getProperty("trainingSetLabel");
        String filePath = (String)wizard.getProperty("file");
   
        String delimiter = (String)wizard.getProperty("delimiter");
        double normScale = Double.parseDouble( (String)wizard.getProperty("normScale") );
                
        StockPredictionManager.getInstance().loadStockData(filePath, delimiter); // data file is loaded an parsed here

        String netLabel = (String)wizard.getProperty("netLabel");
        String transferFunction = (String) wizard.getProperty("tFunction");
     
        int inputNeurons = Integer.parseInt((String) wizard.getProperty("input"));
        String hiddenNeurons = (String) wizard.getProperty("hidden");
        int outputNeurons = Integer.parseInt((String) wizard.getProperty("output"));

        int memory = Integer.parseInt((String) wizard.getProperty("memory"));
        int frequency = Integer.parseInt((String) wizard.getProperty("frequency"));        
        int stepsAhead = Integer.parseInt((String) wizard.getProperty("stepsAhead"));     
        outputNeurons = stepsAhead; // output neurons must be equal to stepsAhead
        
        DataSet trainingSet = createTrainingSet(trainingLabel, normScale, memory, frequency, stepsAhead);
        createNeuralNetwork(netLabel, transferFunction, trainingSet, inputNeurons, hiddenNeurons, outputNeurons);
        
        // TODO: save normalization scale somewhere somehow, so its available when project is opened again
        StockPredictionManager.getInstance().setNormalizationScale(normScale);

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
    public void addChangeListener(ChangeListener l) {}
    @Override
    public void removeChangeListener(ChangeListener l) {}

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
        Object prop = wizard.getProperty("WizardPanel_contentData");
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
                res[i] = panels[i - beforeSteps.length + 1].getComponent().getName();
            }
        }
        return res;
    }

    private DataSet createTrainingSet(String trainingLabel, double normScale, int memory, int frequency, int stepsAhead) {
        DataSet trainingSet = StockPredictionHelper.createTrainingSet(trainingLabel, normScale, memory, frequency, stepsAhead);
        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(trainingSet);                   
        return trainingSet;
    }

    private NeuralNetwork createNeuralNetwork(String netLabel, String transferFunction, DataSet trainingSet, int inputNeurons, String hiddenLayersStr, int outputNeurons) {
        ArrayList<Integer> hiddenLayersNeuronsCount = VectorParser.parseInteger(hiddenLayersStr.trim());
        NeuralNetwork neuralNet = StockPredictionHelper.createNewNeuralNetwork(netLabel, trainingSet, TransferFunctionType.valueOf(transferFunction.toUpperCase()), inputNeurons, hiddenLayersNeuronsCount, outputNeurons);
        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(neuralNet);   
        return neuralNet;
    }

}