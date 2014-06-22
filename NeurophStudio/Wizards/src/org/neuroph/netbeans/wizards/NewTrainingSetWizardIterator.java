package org.neuroph.netbeans.wizards;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.project.CurrentProject;
import org.neuroph.netbeans.project.NeurophProject;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.windows.IOProvider;

public final class NewTrainingSetWizardIterator implements WizardDescriptor.InstantiatingIterator {

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
                        new NewTrainingSetWizardPanel1()//,
//                        new NewTrainingSetWizardPanel2()
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
        boolean cancelled = wizard.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            NeurophProject project = (NeurophProject)wizard.getProperty("project");
            CurrentProject.getInstance().setCurrentProject(project);            

            int inputsNumber = Integer.parseInt((String) wizard.getProperty("inputsNumber"));
            int outputsNumber = Integer.parseInt((String) wizard.getProperty("outputsNumber"));
            String label =(String) wizard.getProperty("name");
            String type = (String) wizard.getProperty("type");
            String file = (String) wizard.getProperty("file");
            String delimiter = (String) wizard.getProperty("delimiter");
            if (delimiter.equals("Space")) delimiter = " ";
                else if (delimiter.equals("tab")) delimiter = "\t";

            DataSet dataSet = null;
                     
            // import data from file here if selected
            if (file.isEmpty()) {           
                if (outputsNumber != 0)
                    dataSet = new DataSet(inputsNumber, outputsNumber);  
                else
                    dataSet = new DataSet(inputsNumber);                 
            } else {
                dataSet = DataSet.createFromFile(file, inputsNumber, outputsNumber, delimiter, false);
            }
            
            dataSet.setLabel(label);
            
            NeurophProjectFilesFactory fileFactory = NeurophProjectFilesFactory.getDefault();
            fileFactory.createTrainingSetFile(dataSet);
            
            
           IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Created data set "+dataSet.getLabel()+"\r\n");
            
            String createdFilePath = fileFactory.getCreatedFilePath();
        
            FileObject fao = FileUtil.toFileObject(new File(createdFilePath));
            DataObject dao = DataObject.find(fao);
            return dao != null ? Collections.singleton(dao) : Collections.EMPTY_SET;             
            
        }
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
}
