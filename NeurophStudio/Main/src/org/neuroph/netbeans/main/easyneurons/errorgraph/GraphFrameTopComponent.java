package org.neuroph.netbeans.main.easyneurons.errorgraph;

import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.main.LearningInfo;
import org.neuroph.netbeans.visual.TrainingController;
import org.neuroph.nnet.learning.LMS;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.core.events.LearningEventType;
import org.openide.windows.TopComponent;

/**
 * Top component which displays Real time Total MSE graph
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
    public static final String FILE_BUFFER =   System.getProperty("java.io.tmpdir") +  "/errorarray.txt";
    private volatile BufferedWriter buffWriter; // make this static?
    private static final long SLEEP_TIME = 10; // pause for drawing to make it run smoothly
    private static final int VALUES_LIMIT = 300; // this is time frame width - how many values displayed in one window frame
    private SimpleXYChartSupport chartSupport;

    private InstanceContent content;
    private AbstractLookup aLookup;

    LearningInfoBuffer buffer;
    DrawingThread drawingThread;

    public GraphFrameTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(GraphFrameTopComponent.class, "CTL_GraphFrameTopComponent"));
        setToolTipText(NbBundle.getMessage(GraphFrameTopComponent.class, "HINT_GraphFrameTopComponent"));
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        content = new InstanceContent();
        aLookup = new AbstractLookup(content);
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(new Lookup[]{
            super.getLookup(),
            aLookup
        });
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
        descriptor.setChartTitle("<html><font size='+1'><b>Total Network Error Graph</b></font></html>");
        descriptor.setXAxisDescription("<html>Iteration</html>");
        descriptor.setYAxisDescription("<html>Total Network Error</html>");
        chartSupport = ChartFactory.createSimpleXYChart(descriptor);
        add(chartSupport.getChart(), BorderLayout.CENTER);

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

    public void observe(LearningRule learningRule) {

        File fileBuffer = new File(FILE_BUFFER);
        if (fileBuffer.exists()) {
            fileBuffer.delete();
        }
        fileBuffer = new File(FILE_BUFFER);

        try {
            buffWriter = new BufferedWriter(new FileWriter(fileBuffer)); // learning thread and GUI thread see different instances of this field?
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        buffer = new LearningInfoBuffer();
        drawingThread = new DrawingThread(buffer, chartSupport, buffWriter);

        learningRule.addListener(this); // trebalo bi otkaciti ovog listenera na kraju!!
        trainingController.train();
    }

    // note that this method is invoked from learning thread
    // when lerning thread is stoped buffreader becomes null1 why?
    // first time doesnt open, later opens two, it enters stop block 6 times - hows that possible?
    @Override
    public synchronized void handleLearningEvent(LearningEvent le) {
     
        LMS lr = (LMS) le.getSource();
        if (le.getEventType() ==  LearningEventType.LEARNING_STOPPED) {
                finalizeFile(); // from some reason this is executed twice... - fixed! it was due the remaining listeners, now they are removed
                lr.removeListener(this);
                openJFreeChartInAThread();                                                
        } else {            
            LearningInfo learningInfo = new LearningInfo(lr.getCurrentIteration(), lr.getTotalNetworkError());
            // and put it on chart
            drawRealTimeChart(learningInfo);            
        }
        
    }
    
    private void drawRealTimeChart( LearningInfo learningInfo ) {
        
           int iteration = learningInfo.getIteration();
            double error = learningInfo.getError();
            // draw them on chart
            long[] err = new long[1];
            err[0] = (long) (error * 1000); // ovo je bitna stvar jer graf prikazuje long vrednosti
            chartSupport.addValues(System.currentTimeMillis(), err);
            chartSupport.updateDetails(new String[]{String.valueOf(iteration), learningInfo.getError().toString()});
            // and write it to file
            
            try {
                writeLearningDataToFile(iteration, error);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }   
            
            try {
                 Thread.sleep(SLEEP_TIME); // for smooth drawing
             } catch (InterruptedException ex) {
                 Exceptions.printStackTrace(ex);
             }            
    }

    private void finalizeFile() {
    //    if (buffWriter != null) {
            try {
                buffWriter.close();
                buffWriter = null;
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        //}
    }

    private void writeLearningDataToFile(int iteration, double error) throws IOException {
        String errorNumber = String.valueOf(error);
        String iterationNumber = String.valueOf(iteration);
        buffWriter.write(iterationNumber + "," + errorNumber);
        buffWriter.newLine();
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
                    close();                    
                    openJFreeChart();                    
                }
            });

        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    TrainingController trainingController;

    public void setTrainingController(TrainingController trainingController) {
        content.add(trainingController);
        this.trainingController = trainingController;
    }
}
