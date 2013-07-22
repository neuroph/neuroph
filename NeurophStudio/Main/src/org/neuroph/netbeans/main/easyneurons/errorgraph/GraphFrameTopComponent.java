package org.neuroph.netbeans.main.easyneurons.errorgraph;

import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.main.LearningInfo;
import org.neuroph.nnet.learning.LMS;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays Total MSE graph
 */
@ConvertAsProperties(dtd = "-//org.neuroph.netbeans.main.easyneurons.errorgraph//GraphFrame//EN",
autostore = false)
public final class GraphFrameTopComponent extends TopComponent implements LearningEventListener {

    private static GraphFrameTopComponent instance;
    /**
     * path to the icon used by the component and its open action
     */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "GraphFrameTopComponent";
    private final int BUFFER_VALUE = 5;
    private static final String FILE_URL = "errorarray.txt";
    private int iterationCounter;
    private BufferedWriter buffWriter;
    private static final long SLEEP_TIME = 10;
    private static final int VALUES_LIMIT = 300; // this is time frame width - how many values displayed in one window frame
    private SimpleXYChartSupport chartSupport;
    LearningRule learningRule;
    ConcurrentLinkedQueue<LearningInfo> dataQueueBuffer;
//    int iteration = 0;
//    double error = 0;
    long[] err = new long[1];

    public GraphFrameTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(GraphFrameTopComponent.class, "CTL_GraphFrameTopComponent"));
        setToolTipText(NbBundle.getMessage(GraphFrameTopComponent.class, "HINT_GraphFrameTopComponent"));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     */
    public static synchronized GraphFrameTopComponent getDefault() {
        if (instance == null) {
            instance = new GraphFrameTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the GraphFrameTopComponent instance. Never call
     * {@link #getDefault} directly!
     */
    public static synchronized GraphFrameTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(GraphFrameTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof GraphFrameTopComponent) {
            return (GraphFrameTopComponent) win;
        }
        Logger.getLogger(GraphFrameTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }
    // bilo bi dobro da mogu da se setuju spolja

    @Override
    public void componentOpened() {
// decimal scale factor treba podesavati na osnovu Max Error parametra algoritma za ucenje!
        SimpleXYChartDescriptor descriptor =
                SimpleXYChartDescriptor.decimal(0, 1000, 50, 0.001d, true, VALUES_LIMIT);
        // long minValue, long maxValue, long initialYMargin, double chartFactor, boolean hideableItems, int valuesBuffer
        // chart factor - podeoci na y osi? , najmanji korak koji grafik moze da prikaze
        // fora je sto ova metoda prihvata min max values kao long , a greska se dobija kao double koji se mnozi sa 1000 u handleLearningEvent
        // min error 0.001, mnozimo je sa 1000 i to je 1, tj. minValue = 1 a max value za 1 treba da bude 1000
       
        

        descriptor.addLineItems("Total MSE ");
        descriptor.setDetailsItems(new String[]{"Iteration", "Total Network Error"});
        descriptor.setChartTitle("<html><font size='+1'><b>Network Error Graph</b></font></html>");
        descriptor.setXAxisDescription("<html>Iteration</html>");
        descriptor.setYAxisDescription("<html>Total Network Error</html>");
        chartSupport = ChartFactory.createSimpleXYChart(descriptor);
        add(chartSupport.getChart(), BorderLayout.CENTER);

        try {
            buffWriter = new BufferedWriter(new FileWriter(FILE_URL));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void componentClosed() {
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    //GuiWorker guiWorker;
    public void observe(LearningRule learningRule) {

            this.learningRule = learningRule;
            learningRule.addListener(this);
            
        try {
            if (buffWriter==null)
                    buffWriter = new BufferedWriter(new FileWriter(FILE_URL));        
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void handleLearningEvent(LearningEvent le) {
        try {
            LMS lr = (LMS) le.getSource();
            // IsStopped true drugi put, eventualno treci
            if (!lr.isStopped()) {
                LearningInfo learningInfo = new LearningInfo(lr.getCurrentIteration(), lr.getTotalNetworkError());
                int iteration = lr.getCurrentIteration();
                double error = learningInfo.getError();
                err[0] = (long) (error * 1000); // ovo je bitna stvar je rgrf prikazuje long vrednosti
                chartSupport.addValues(System.currentTimeMillis(), err);
                chartSupport.updateDetails(new String[]{String.valueOf(iteration), learningInfo.getError().toString()});
                storeLearningData(iteration, error);
            } else {
                finishStoring();
                openJFreeChartInAThread();
            }

            try {
                Thread.sleep(SLEEP_TIME); // dont synchronize with this - better wait & notify
            } catch (InterruptedException ex) {
                Exceptions.printStackTrace(ex);
            }

        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void finishStoring() throws IOException {
        if (buffWriter==null) {
            buffWriter.flush();
            buffWriter.close();
            buffWriter = null;
        }
        iterationCounter = 0;
    }

    public synchronized void storeLearningData(int iteration, double error) throws IOException {
      
        writeLearningDataToFile(iteration, error);
        if (isBufferFull()) {
            buffWriter.flush();
            iterationCounter = 0;
        }
    }

    private boolean isBufferFull() {
        return iterationCounter == BUFFER_VALUE;
    }

    private void writeLearningDataToFile(int iteration, double error) throws IOException {
        if (buffWriter == null) {
            buffWriter = new BufferedWriter(new FileWriter(FILE_URL));
        }
        String errorNumber = String.valueOf(error);
        String iterationNumber = String.valueOf(iteration);
        buffWriter.write(iterationNumber);
        buffWriter.write(",");
        buffWriter.write(errorNumber);
        buffWriter.newLine();
        iterationCounter++;
    }

    private void openJFreeChart() {
        JFreeChartTopComponent graphFrame = new JFreeChartTopComponent();
        graphFrame.open();
        graphFrame.requestActive();
    }

    private void openJFreeChartInAThread() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    openJFreeChart();
                }
            });

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

}
