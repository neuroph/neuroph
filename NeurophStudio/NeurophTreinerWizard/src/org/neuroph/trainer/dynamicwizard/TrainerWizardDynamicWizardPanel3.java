/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.trainer.dynamicwizard;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.neuroph.trainer.dynamicwizard.panels.AdvancePanel;
import org.neuroph.trainer.dynamicwizard.panels.SimplePanel;
import org.neuroph.trainer.dynamicwizard.panels.manager.PanelManager;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;

public class TrainerWizardDynamicWizardPanel3 implements WizardDescriptor.Panel<WizardDescriptor>, WizardDescriptor.ValidatingPanel<WizardDescriptor> {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;
    private boolean isValid = true;

    private Component returnComponent() {
        int type = PanelManager.getInstance().getType();
        if (type == PanelManager.SIMPLE_WIZARD) {
            return SimplePanel.getPanel();
        } else {
            return AdvancePanel.getObject();
        }
    }

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public Component getComponent() {
        return returnComponent();
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return isValid;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        // use wiz.getProperty to retrieve previous panel state
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {

        try {
            if (PanelManager.getInstance().getType() == PanelManager.SIMPLE_WIZARD) {

                String lr = SimplePanel.getPanel()
                        .getTextFieldLearningRates().getText().trim();
                String hn = SimplePanel.getPanel()
                        .getTextFieldHiddenNeurons().getText().trim();
                String tsp = SimplePanel.getPanel()
                        .getTextFieldPercents().getText().trim();

                String[] splitLr = lr.split(",");
                String[] splitHn = hn.split(",");
                String[] splitTsp = tsp.split(",");

                //inicalize arrays
                double[] learningRates = new double[splitLr.length];
                int[] hiddenNeurons = new int[splitHn.length];
                int[] trainingSetPercents = new int[splitTsp.length];

                //fill arrays
                for (int i = 0; i < splitLr.length; i++) {
                    learningRates[i] = Double.parseDouble(splitLr[i].trim());
                }
                for (int i = 0; i < splitHn.length; i++) {
                    hiddenNeurons[i] = Integer.parseInt(splitHn[i].trim());
                }
                for (int i = 0; i < splitTsp.length; i++) {
                    trainingSetPercents[i] = Integer.parseInt(splitTsp[i].trim());
                }
                int crossValRepeatCount = (Integer) SimplePanel.getPanel().getjSpinnerCV().getValue();

                wiz.putProperty("learningRates", learningRates);
                wiz.putProperty("hiddenNeurons", hiddenNeurons);
                wiz.putProperty("trainingSetPercents", trainingSetPercents);
                wiz.putProperty("crossValRepeatCount", crossValRepeatCount);
                wiz.putProperty("numIteration", learningRates.length * hiddenNeurons.length * trainingSetPercents.length * crossValRepeatCount - 1);
            } else {
                //Advanced wizard

                double[] learningRates = createLearningRates();
                int[] hiddenNeurons = createHiddenNeurons();
                int[] trainingSetPercents = createTrainingSetPercents();

                int crossValRepeatCount = (Integer) AdvancePanel.getObject().getjSpinnerCV().getValue();

                wiz.putProperty("learningRates", learningRates);
                wiz.putProperty("hiddenNeurons", hiddenNeurons);
                wiz.putProperty("trainingSetPercents", trainingSetPercents);
                wiz.putProperty("crossValRepeatCount", crossValRepeatCount);
                wiz.putProperty("numIteration", learningRates.length * hiddenNeurons.length * trainingSetPercents.length * crossValRepeatCount - 1);
            }
        } catch (NumberFormatException numberFormatException) {
        }

    }

    /**
     * Creates array for learning rates from given min,max and step
     *
     * @return
     */
    private double[] createLearningRates() {

        double min = (Double) AdvancePanel.getObject().getjSpinnerLrMin().getValue();
        double max = (Double) AdvancePanel.getObject().getjSpinnerLrMax().getValue();
        double step = (Double) AdvancePanel.getObject().getjSpinnerLrStep().getValue();

        int numEl = (int)Math.round( (max - min) / step) + 1;
        double[] learningRates = new double[numEl];
        int j = 0;
        for (double i = min; i <= max; i += step) {
            learningRates[j] = i;
            j++;
        }

        return learningRates;
    }

    /**
     * Creates array for hidden neurons rates from given min,max and step
     *
     * @return
     */
    private int[] createHiddenNeurons() {

        int min = (Integer) AdvancePanel.getObject().getjSpinnerHnMin().getValue();
        int max = (Integer) AdvancePanel.getObject().getjSpinnerHnMax().getValue();
        int step = (Integer) AdvancePanel.getObject().getjSpinnerHnStep().getValue();

        int numEl = (int) (((max - min) / step) + 1);
        int[] hn = new int[numEl];
        int j = 0;
        for (int i = min; i <= max; i += step) {
            hn[j] = i;
            j++;
        }

        return hn;
    }

    /**
     * Creates array for training set percents from given min,max and step
     *
     * @return
     */
    private int[] createTrainingSetPercents() {

        int min = (Integer) AdvancePanel.getObject().getjSpinnerTsMin().getValue();
        int max = (Integer) AdvancePanel.getObject().getjSpinnerTsMax().getValue();
        int step = (Integer) AdvancePanel.getObject().getjSpinnerTSStep().getValue();

        int numEl = (int) (((max - min) / step) + 1);
        int[] ts = new int[numEl];
        int j = 0;
        for (int i = min; i <= max; i += step) {
            ts[j] = i;
            j++;
        }

        return ts;
    }

    @Override
    public void validate() throws WizardValidationException {

        if (PanelManager.getInstance().getType() == PanelManager.SIMPLE_WIZARD) {

            try {
                String lr = SimplePanel.getPanel()
                        .getTextFieldLearningRates().getText().trim();
                String hn = SimplePanel.getPanel()
                        .getTextFieldHiddenNeurons().getText().trim();
                String tsp = SimplePanel.getPanel()
                        .getTextFieldPercents().getText().trim();
                String[] splitLr = lr.split(",");
                String[] splitHn = hn.split(",");
                String[] splitTsp = tsp.split(",");
                //inicalize arrays
                double[] learningRates = new double[splitLr.length];
                int[] hiddenNeurons = new int[splitHn.length];
                int[] trainingSetPercents = new int[splitTsp.length];
                //fill arrays
                for (int i = 0; i < splitLr.length; i++) {
                    learningRates[i] = Double.parseDouble(splitLr[i]);
                }
                for (int i = 0; i < splitHn.length; i++) {
                    hiddenNeurons[i] = Integer.parseInt(splitHn[i]);
                }
                for (int i = 0; i < splitTsp.length; i++) {
                    trainingSetPercents[i] = Integer.parseInt(splitTsp[i]);
                }
                int crossValRepeatCount = (Integer) SimplePanel.getPanel().getjSpinnerCV().getValue();


            } catch (Exception e) {
                throw new WizardValidationException(null, e.getMessage(), null);
            }

        }
    }
}
