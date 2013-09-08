package org.neuroph.netbeans.main.easyneurons.errorgraph;

import com.sun.tools.visualvm.charts.ChartFactory;
import com.sun.tools.visualvm.charts.SimpleXYChartDescriptor;
import com.sun.tools.visualvm.charts.SimpleXYChartSupport;
import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.events.LearningStoppedEvent;
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
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
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
    private static final String FILE_URL = "errorarray.txt";
    private volatile BufferedWriter buffWriter; // make this static?
    private static final long SLEEP_TIME = 10; // pause for drawing to make it run smoothly
    private static final int VALUES_LIMIT = 300; // this is time frame width - how many values displayed in one window frame
    private SimpleXYChartSupport chartSupport;
    //   LearningRule learningRule;
    InstanceContent content;
    AbstractLookup aLookup;
//    int iteration = 0;
//    double error = 0;
//    long[] err = new long[1];
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
        descriptor.setChartTitle("<html><font size='+1'><b>Network Error Graph</b></font></html>");
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

        File file = new File(FILE_URL);
        if (file.exists()) {
            file.delete();
        }
        file = new File(FILE_URL);

        try {
            buffWriter = new BufferedWriter(new FileWriter(file)); // learning thread and GUI thread see different instances of this field
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }


        buffer = new LearningInfoBuffer();
        drawingThread = new DrawingThread(buffer, chartSupport, buffWriter);


        // openedChart = false; 
        //     this.learningRule = learningRule;
        learningRule.addListener(this); // trebalo bi otkaciti ovog listenera na kraju!!!


//        drawingThread.setName("Drawing Thread");
        trainingController.train();
//        trainingController.getNeuralNetAndDataSet().getNetwork().getLearningThread().setName("Learning Thread");

//        drawingThread.start(); // ovaj startovati posle learning thread-a

    }

    // verovatno neki wait i dalj eceka
//    @Override
//    public void  handleLearningEvent(LearningEvent le) {
//       
//        // if it is stop eent finalize learning...
//        LMS lr = (LMS) le.getSource();   
//     
//        LearningInfo learningInfo = new LearningInfo(lr.getCurrentIteration(), lr.getTotalNetworkError());
//        buffer.put(learningInfo);
//        
//        if (lr.isStopped()) {
//            buffer.put(new LearningInfo(-1, -100.0));
//            
//            lr.removeListener(this);
////            drawingThread.stop();
////            drawingThread = null;
////            lr.getNeuralNetwork().getLearningThread().stop();
//            // ovaj put blokira learning thread... treba neki notify da ih oslobodi
//            //buffer.releaseThreads();
//        }
//    }
    // note that this method is invoked from learning thread
    // when lerning thread is stoped buffreader becomes null1 why?
    // first time doesnt open, later opens two, it enters stop block 6 times - hows that possible?
    @Override
    public synchronized void handleLearningEvent(LearningEvent le) {

//        if (buffWriter == null) {
//            File file = new File(FILE_URL);
//            try {
//                buffWriter = new BufferedWriter(new FileWriter(file));
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
       
        LMS lr = (LMS) le.getSource();
        if (le instanceof LearningStoppedEvent) {
                finalizeFile(); // from some reason this is executed twice... - fixed! it was due the remaining listeners, now they are removed
                openJFreeChartInAThread();                
                lr.removeListener(this);
        } else {            
            LearningInfo learningInfo = new LearningInfo(lr.getCurrentIteration(), lr.getTotalNetworkError());
            // and put it on chart
            drawRealTimeChart(learningInfo);            
        }
        
//        LMS lr = (LMS) le.getSource();
//        boolean isStoped = lr.isStopped();
//        // IsStopped true drugi put, eventualno treci
//        if (!isStoped) { // if learning rule is running
//            // get the learning info            
//            LearningInfo learningInfo = new LearningInfo(lr.getCurrentIteration(), lr.getTotalNetworkError());
//            // and put it on chart
//            drawRealTimeChart(learningInfo);
//        } else { // else if learning rule has stopped
//            InputOutput io = IOProvider.getDefault().getIO("Neuroph", false);
//            PrintWriter out = io.getOut();
//            out.println("Entered handleLearningEvent after stop");
//
//            // when learning is stopped write the rest of data to file, and open chart for the whole training
//
//            if (!openedChart) {
//                finalizeFile(); // from some reason this is executed twice...
//                openJFreeChartInAThread();
//            }
//        }

 

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
