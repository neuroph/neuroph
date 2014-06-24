package org.neuroph.netbeans.rightclick;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.files.dset.DataSetDataObject;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "File",
id = "IDEActions.CreateTrainSetAndTestSet")
@ActionRegistration(displayName = "#CTL_CreateTrainSetAndTestSet")
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-tset/Actions", position = 550)
})
@Messages("CTL_CreateTrainSetAndTestSet=Create training set and test set")
public final class CreateTrainSetAndTestSet implements ActionListener {

    private final DataSetDataObject context;

    public CreateTrainSetAndTestSet(DataSetDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        panels.add(new WizardCreateTraSetAndTestSetWizardPanel1());
        String[] steps = new String[panels.size()];
        for (int i = 0; i < panels.size(); i++) {
            Component c = panels.get(i).getComponent();
            // Default step name to component name of panel.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle("Split Training Set");
        if (DialogDisplayer.getDefault().notify(wiz) == WizardDescriptor.FINISH_OPTION) {
            // do something
            try {
                String numTest = (String) wiz.getProperty("numTest");
                String numTraining = (String) wiz.getProperty("numTraining");
                DataSet[] tsetArray = context.getDataSet().createTrainingAndTestSubsets(Integer.parseInt(numTraining), Integer.parseInt(numTest));
                tsetArray[0].setLabel(context.getDataSet().getLabel() + "(" + numTraining + "%)");
                tsetArray[1].setLabel(context.getDataSet().getLabel() + "(" + numTest + "%)");
                NeurophProjectFilesFactory.getDefault().createTrainingSetFile(tsetArray[0]);
                NeurophProjectFilesFactory.getDefault().createTestSetFile(tsetArray[1]);
            } catch (Exception e) {
                e.getMessage();
            }

        }

    }
}
