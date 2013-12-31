package org.neuroph.bci.mindwave.wizard;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.templates.TemplateRegistration;
import org.neuroph.bci.mindwave.BciHelper;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.imr.utils.UtilWizardPanel2;
import org.neuroph.netbeans.project.CurrentProject;
import org.neuroph.netbeans.project.NeurophProject;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.VectorParser;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

// TODO define position attribute
/**
 *
 * @author zoran
 */
@TemplateRegistration(folder = "Neuroph", displayName = "#BCIWizardIterator_displayName", description = "bCI.html", iconBase="org/neuroph/bci/mindwave/wizard/bci_icon.png")
@Messages("BCIWizardIterator_displayName=Brain Wave Recognition")
public final class BCIWizardIterator implements WizardDescriptor.InstantiatingIterator<WizardDescriptor> {

    private int index;
    private WizardDescriptor wizard;
    private List<WizardDescriptor.Panel<WizardDescriptor>> panels;

    private List<WizardDescriptor.Panel<WizardDescriptor>> getPanels() {
        if (panels == null) {
            panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
            panels.add(new BCIWizardPanel1());
            panels.add(new BCIWizardPanel2());
            panels.add( new UtilWizardPanel2());
            
            
            
            String[] steps = createSteps();
            for (int i = 0; i < panels.size(); i++) {
                Component c = panels.get(i).getComponent();
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
        }
        return panels;
    }

    @Override
    public Set<?> instantiate() throws IOException {
        boolean cancelled = wizard.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            NeurophProject project = (NeurophProject)wizard.getProperty("project");
            if (project != null) {
                CurrentProject.getInstance().setCurrentProject(project);
            }
         
            NeurophProjectFilesFactory fileFactory = NeurophProjectFilesFactory.getDefault();
        
        
        Integer sampleLength = (Integer) wizard.getProperty("sampleLength");
        Integer samplingRate = (Integer) wizard.getProperty("samplingRate");
        Integer numberOfChannels = (Integer) wizard.getProperty("numberOfChannels");
        
        int numberOfInputs = sampleLength * samplingRate * numberOfChannels;
        
        String outputClasses = wizard.getProperty("outputClasses").toString().trim();
        String classes[] = outputClasses.split(System.lineSeparator());
        int numberOfOutputs = classes.length;
        
        String neuralNetworkName = (String) wizard.getProperty("networkName");
        String hiddenNeurons = (String) wizard.getProperty("neurons");
        String transferFunction = (String) wizard.getProperty("transferFunction");
        
        createNeuralNetwork(neuralNetworkName, transferFunction, numberOfInputs, hiddenNeurons, numberOfOutputs, classes);
                
        String createdFilePath = NeurophProjectFilesFactory.getDefault().getCreatedFilePath();
        
        FileObject fao = FileUtil.toFileObject(new File(createdFilePath));
        DataObject dao = DataObject.find(fao);
        return dao != null ? Collections.singleton(dao) : Collections.EMPTY_SET;               
        }
              return Collections.EMPTY_SET;
    }

    public void createNeuralNetwork(String neuralNetworkName, String transferFunction, Integer numberOfInputs, String hiddenLayersStr, Integer numberOfOutputs,  String[] classes) {
        ArrayList<Integer> hiddenLayersNeuronsCount = VectorParser.parseInteger(hiddenLayersStr.trim());
        NeuralNetwork neuralNetwork = BciHelper.createNewNeuralNetwork(neuralNetworkName, TransferFunctionType.valueOf(transferFunction.toUpperCase()), numberOfInputs, hiddenLayersNeuronsCount, numberOfOutputs, classes);
        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(neuralNetwork);
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
    public WizardDescriptor.Panel<WizardDescriptor> current() {
        return getPanels().get(index);
    }

    @Override
    public String name() {
        return index + 1 + ". from " + getPanels().size();
    }

    @Override
    public boolean hasNext() {
        return index < getPanels().size() - 1;
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
    // the number of panels changes in response to user input, then use
    // ChangeSupport to implement add/removeChangeListener and call fireChange
    // when needed

    // You could safely ignore this method. Is is here to keep steps which were
    // there before this wizard was instantiated. It should be better handled
    // by NetBeans Wizard API itself rather than needed to be implemented by a
    // client code.
    private String[] createSteps() {
        String[] beforeSteps = (String[]) wizard.getProperty("WizardPanel_contentData");
        assert beforeSteps != null : "This wizard may only be used embedded in the template wizard";
        String[] res = new String[(beforeSteps.length - 1) + panels.size()];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels.get(i - beforeSteps.length + 1).getComponent().getName();
            }
        }
        return res;
    }
}
