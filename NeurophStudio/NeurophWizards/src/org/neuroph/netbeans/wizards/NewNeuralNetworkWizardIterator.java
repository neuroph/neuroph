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
import org.neuroph.netbeans.project.CurrentProject;
import org.neuroph.netbeans.project.NeurophProject;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.netbeans.wizards.spi.NetworkWizard;
import org.neuroph.netbeans.wizards.spi.NetworkWizardFactory;
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
     * Initialize panels representing individual wizard's steps and sets various
     * properties for them influencing wizard appearance.
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
                if (c == null) {
                    continue;
                }
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
            NeurophProject project = (NeurophProject) getWizard().getProperty("project");
            if (project != null) {
                CurrentProject.getInstance().setCurrentProject(project);
            }

            NeurophProjectFilesFactory fileFactory = NeurophProjectFilesFactory.getDefault();

            NeuralNetworkTypeWizard type = (NeuralNetworkTypeWizard) getWizard().getProperty("type");
            String neuralNetworkName = (String) getWizard().getProperty("neural network name");
            NetworkWizard wizard = NetworkWizardFactory.createNetworkWizard(type);
            NeuralNetwork neuralNetwork = wizard.createNeuralNetwork(getWizard());
            fileFactory.createNeuralNetworkFile(neuralNetwork);
//            switch (type) {
//                case EMPTY_NN:
//                    NeuralNetwork nnet = new NeuralNetwork();
//                    nnet.setLabel(neuralNetworkName);
//                    fileFactory.createNeuralNetworkFile(nnet);
//                    break;
//         
//              
//            }

            IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Created neural network " + neuralNetworkName);
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
                if (i == 2) {
                    res[i] = "Set network specific settings"; // dirty hack
                } else {
                    res[i] = panels[i - beforeSteps.length + 1].getComponent().getName();
                }
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
