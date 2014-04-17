package org.neuroph.netbeans.imr.wiz;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.netbeans.imr.utils.ImagesLoader;
import org.neuroph.netbeans.imr.utils.UtilWizardPanel1;
import org.neuroph.netbeans.imr.utils.UtilWizardPanel2;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.ocr.OcrHelper;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.VectorParser;
import org.openide.WizardDescriptor;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class IMRWizardIterator implements WizardDescriptor.InstantiatingIterator {

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
                //new IMRWizardPanel1(),
                new ImageRecognitionWizardPanel1(),
                new ImageRecognitionWizardPanel2(),
                new UtilWizardPanel1(),
                new UtilWizardPanel2()
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

    public Set instantiate() throws IOException {
        String imageDir = (String) wizard.getProperty("imageDir");
        String junkDir = (String) wizard.getProperty("junkDir");
        ColorMode colorMode = (ColorMode) wizard.getProperty("colorMode");
        Dimension samplingResollution = (Dimension) wizard.getProperty("resolution");
        String trainigSetName = (String) wizard.getProperty("trainigSetName");
        List<String> imageLabels = createTrainingSet(imageDir, junkDir, samplingResollution, colorMode, trainigSetName);
        String neuralNetworkName = (String) wizard.getProperty("networkName");
        String neurons = (String) wizard.getProperty("neurons");
        String transferFunction = (String) wizard.getProperty("transferFunction");
        createNeuralNetwork(neuralNetworkName, transferFunction, samplingResollution, neurons, imageLabels, colorMode);
        TopComponent tc = WindowManager.getDefault().findTopComponent("ImgRecTestTopComponent");
        tc.open();
        return Collections.EMPTY_SET;
    }

    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    public WizardDescriptor.Panel current() {
        return getPanels()[index];
    }

    public String name() {
        return index + 1 + ". from " + getPanels().length;
    }

    public boolean hasNext() {
        return index < getPanels().length - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    public void addChangeListener(ChangeListener l) {
    }

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

    private List<String> createTrainingSet(String imageDir, String junkDir, Dimension samplingResolution, ColorMode colorMode, String trainigSetName) {
        HashMap<String, FractionRgbData> rgbDataMap = new HashMap<String, FractionRgbData>();
        List imageLabels = new ArrayList<String>();

        try {
            File labeledImagesDir = new File(imageDir);
            rgbDataMap.putAll(ImagesLoader.getFractionRgbDataForDirectory(labeledImagesDir, samplingResolution));

            for (String imgName : rgbDataMap.keySet()) {
                StringTokenizer st = new StringTokenizer(imgName, "._");
                String imageLabel = st.nextToken();
                if (!imageLabels.contains(imageLabel)) {
                    imageLabels.add(imageLabel);
                }
            }
            Collections.sort(imageLabels);
        } catch (IOException ioe) {
            System.err.println("Unable to load images from labeled images dir: '" + imageDir + "'");
            System.err.println(ioe.toString());
        }

        if ((junkDir != null) && (!junkDir.equals(""))) {
            try {
                File junkImagesDir = new File(junkDir);
                rgbDataMap.putAll(ImagesLoader.getFractionRgbDataForDirectory(junkImagesDir, samplingResolution));
            } catch (IOException ioe) {
                System.err.println("Unable to load images from junk images dir: '" + junkDir + "'");
                System.err.println(ioe.toString());
            }
        }
        DataSet activeTrainingSet = null;
        if (colorMode == ColorMode.FULL_COLOR) {
            activeTrainingSet = ImageRecognitionHelper.createTrainingSet(imageLabels, rgbDataMap);
        } else {
            activeTrainingSet = ImageRecognitionHelper.createBlackAndWhiteTrainingSet(imageLabels, rgbDataMap);
        }

        activeTrainingSet.setLabel(trainigSetName);
        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(activeTrainingSet);
        
        return imageLabels;
    }

    public void createNeuralNetwork(String neuralNetworkName, String transferFunction, Dimension resolution, String hiddenLayersStr, List<String> imageLabels, ColorMode colorMode) {
        ArrayList<Integer> hiddenLayersNeuronsCount = VectorParser.parseInteger(hiddenLayersStr.trim());
        NeuralNetwork activeNeuralNetwork = ImageRecognitionHelper.createNewNeuralNetwork(neuralNetworkName, resolution, colorMode, imageLabels, hiddenLayersNeuronsCount, TransferFunctionType.valueOf(transferFunction.toUpperCase()));
        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(activeNeuralNetwork);
    }
}
