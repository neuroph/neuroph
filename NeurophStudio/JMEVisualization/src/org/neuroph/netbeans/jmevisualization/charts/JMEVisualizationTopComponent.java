package org.neuroph.netbeans.jmevisualization.charts;

import com.jme3.math.ColorRGBA;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.netbeans.jmevisualization.ListRenderer;
import org.neuroph.netbeans.jmevisualization.IOSettingsDialog;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;
import org.neuroph.netbeans.jmevisualization.charts.graphs.JMEDatasetHistogram3D;
import org.neuroph.netbeans.jmevisualization.charts.graphs.JMEDatasetScatter3D;
import org.neuroph.netbeans.jmevisualization.charts.graphs.JMEWeightsHistogram3D;
import org.neuroph.netbeans.jmevisualization.concurrent.Consumer;
import org.neuroph.netbeans.jmevisualization.concurrent.Producer;
import org.neuroph.netbeans.jmevisualization.concurrent.ConsumerProducer;
import org.neuroph.netbeans.jmevisualization.concurrent.dataset.DataSetConsumer;
import org.neuroph.netbeans.jmevisualization.concurrent.dataset.DataSetProducer;
import org.neuroph.netbeans.jmevisualization.concurrent.weights.NeuralNetworkWeightsConsumer;
import org.neuroph.netbeans.jmevisualization.concurrent.weights.NeuralNetworkWeightsProducer;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
import org.neuroph.netbeans.visual.TrainingController;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.IOProvider;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.neuroph.netbeans.jmevisualization//JMEVisualization//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "JMEVisualizationTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.neuroph.netbeans.jmevisualization.JMEVisualizationTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_JMEVisualizationAction",
        preferredID = "JMEVisualizationTopComponent")
@Messages({
    "CTL_JMEVisualizationAction=JMEVisualization",
    "CTL_JMEVisualizationTopComponent=JMEVisualization Window",
    "HINT_JMEVisualizationTopComponent=This is a JMEVisualization window"
})
public final class JMEVisualizationTopComponent extends TopComponent implements LearningEventListener {

    private static JMEVisualizationTopComponent instance;
    private static final String PREFERRED_ID = "JMEVisualizationTopComponent";

    private final InstanceContent content;
    private final AbstractLookup aLookup;
    private final DropTargetListener dtListener;
    private final int acceptableActions = DnDConstants.ACTION_COPY;
    private final DropTarget dropTarget;
    //private Thread firstCalculation = null;

    private NeuralNetwork neuralNetwork;
    private DataSet trainingSet;
    private NeuralNetAndDataSet neuralNetAndDataSet;
    private TrainingController trainingController;

    private ConsumerProducer consumerProducer;
    private Producer producer;
    private Consumer consumer;

    private java.awt.Canvas jmeCanvas;
    private JMEVisualization jmeVisualization;

    private int iterationCounter = 1;
    private boolean trainSignal = false;

    private JMEVisualizationTopComponent() {
        initComponents();
        setName(Bundle.CTL_JMEVisualizationTopComponent());
        setToolTipText(Bundle.HINT_JMEVisualizationTopComponent());
        content = new InstanceContent();
        aLookup = new AbstractLookup(content);
        this.dtListener = new DTListener();
        this.dropTarget = new DropTarget(
                this,
                this.acceptableActions,
                this.dtListener,
                true);
    }

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     *
     * @return
     */
    private static synchronized JMEVisualizationTopComponent getDefault() {
        if (instance == null) {
            instance = new JMEVisualizationTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the MultiLayerPerceptronClassificationSampleTopComponent instance.
     * Never call {@link #getDefault} directly!
     *
     * @return
     */
    public static synchronized JMEVisualizationTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(JMEVisualizationTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof JMEVisualizationTopComponent) {
            return (JMEVisualizationTopComponent) win;
        }
        Logger.getLogger(JMEVisualizationTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    public java.awt.Canvas getJmeCanvas() {
        return jmeCanvas;
    }

    public void setJmeCanvas(java.awt.Canvas jmeCanvas) {
        this.jmeCanvas = jmeCanvas;
    }

    public boolean isTrainSignal() {
        return trainSignal;
    }

    public void setTrainSignal(boolean trainSignal) {
        this.trainSignal = trainSignal;
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(new Lookup[]{
            super.getLookup(),
            aLookup
        });
    }

    /**
     * At each 10nth iteration, neural network is used for collecting training
     * data(producing), and sending collected data for drawing (consuming)
     *
     * @param le
     */
    @Override
    public void handleLearningEvent(LearningEvent le) {
        iterationCounter++;
        if (iterationCounter % 10 == 0) {
            consumerProducer.startProducing();
        }
    }

    /**
     * When topComponent opens, initialization process starts
     */
    @Override
    public void componentOpened() {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                /*
                 Create and start JMEVisualization instance
                 */
                jmeVisualization = new JMEVisualization();
                jmeVisualization.setRotated(false);
                jmeVisualization.setWidth(getVisualizationPanel().getWidth() - 15);
                jmeVisualization.setHeight(getVisualizationPanel().getHeight() - 30);
                jmeVisualization.startApplication();

                /*
                 Fetch Canvas from JMEVisualization instance
                 */
                jmeCanvas = jmeVisualization.getJmeCanvasContext().getCanvas();

                getVisualizationPanel().setLayout(new FlowLayout());
                getVisualizationPanel().add(jmeCanvas);
                getVisualizationPanel().revalidate();

            }
        });
    }
    /*
     When topComponent closes, deinitialization starts
     */

    @Override
    public void componentClosed() {
        /*
         Stop JMEVisualization instance and remove jmeCanvas from panel
         */
        jmeVisualization.stop();
        getVisualizationPanel().remove(jmeCanvas);
        getVisualizationPanel().revalidate();
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    public JPanel getVisualizationPanel() {
        return visualizationPanel;
    }

    /**
     * Removes neuralNetwork, dataSet and trainingController from content
     */
    public void removeContent() {
        try {
            content.remove(neuralNetAndDataSet);
            content.remove(trainingController);
            JMEVisualizationTopComponent.this.requestActive();
        } catch (Exception ex) {
        }
    }

    /**
     * Adds neuralNetwork, dataSet and trainingController to content
     */
    public void addContent() {
        content.add(neuralNetAndDataSet);
        content.add(trainingController);
        JMEVisualizationTopComponent.this.requestActive();
    }

    /**
     * @param min
     * @param max
     * @return Random integer between min and max value (inclusive)
     */
    private static int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    /**
     * Collects all the information needed for neural network training
     */
    public void trainingPreprocessing() {
        neuralNetAndDataSet = new NeuralNetAndDataSet(neuralNetwork, trainingSet);
        trainingController = new TrainingController(neuralNetAndDataSet);
        neuralNetwork.getLearningRule().addListener(this);
        trainingController.setLmsParams(0.7, 0.01, 0);
        LMS learningRule = (LMS) this.neuralNetAndDataSet.getNetwork().getLearningRule();
        if (learningRule instanceof MomentumBackpropagation) {
            ((MomentumBackpropagation) learningRule).setMomentum(0.2);
        }
    }

    /**
     * Loads essential dataSet information, including: selected inputs for
     * dataSet visualization, chosen colors for dataSet output visualization.
     */
    public void loadDataSetLegend() {

        IOSettingsDialog io = IOSettingsDialog.getInstance();
        int[] inputs = io.getStoredInputs();

        /*
         Display input names
         */
        labX.setText(io.getInputNames()[inputs[0]]);
        labY.setText(io.getInputNames()[inputs[1]]);
        labZ.setText(io.getInputNames()[inputs[2]]);

        String[] outputs = io.getOutputNames();
        Color[] colors = new Color[io.getOutputColors().size()];

        /*
         Convert colors from ColorRGBA instance to Color instance
         */
        for (int i = 0; i < colors.length; i++) {
            ColorRGBA cl = io.getOutputColors().get(i);
            colors[i] = new Color(cl.r, cl.g, cl.b, cl.a);
        }
        /*
         Create model and renderer for colors to display in list view
         */
        listColors.setModel(new DefaultComboBoxModel(outputs));
        listColors.setFont(new Font("Tahoma", Font.BOLD, 11));

        ListRenderer renderer = new ListRenderer(listColors);
        renderer.setColors(colors);
        renderer.setStrings(outputs);

        listColors.setCellRenderer(renderer);

    }

    /**
     * Initialize producer and consumer. Initial check is required, in order to
     * instantiate proper producer and consumer.
     *
     * @param queueSize - size of buffer for objects needed for drawing
     */
    public void initializeConsumerProducer(int queueSize) {

        trainSignal = false;
        if (radioDataSet.isSelected()) {
            consumer = new DataSetConsumer(jmeVisualization);
            producer = new DataSetProducer(neuralNetAndDataSet);
        }
        if (radioWeights.isSelected()) {
            consumer = new NeuralNetworkWeightsConsumer(jmeVisualization);
            producer = new NeuralNetworkWeightsProducer(neuralNetAndDataSet);
        }

        /*
         Instantiate producerConsumer and start consuming(drawing)
         */
        consumerProducer = new ConsumerProducer(queueSize);
        consumerProducer.setConsumer(consumer);
        consumerProducer.setProducer(producer);
        
        consumerProducer.startConsuming();
        
    }

    class DTListener implements DropTargetListener {

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            dtde.acceptDrag(dtde.getDropAction());
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            dtde.acceptDrag(dtde.getDropAction());
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
            dtde.acceptDrag(dtde.getDropAction());
        }

        /**
         * Drop function used for fetching dropped neural network and dataSets
         *
         * @param e
         */
        @Override
        public void drop(DropTargetDropEvent e) {
            try {

                Transferable t = e.getTransferable();
                DataFlavor dataFlavor = t.getTransferDataFlavors()[1];
                DataObject dataObject = (DataObject) t.getTransferData(dataFlavor);

                DataSet dataSet = dataObject.getLookup().lookup(DataSet.class);//get the object from lookup listener
                NeuralNetwork nnet = dataObject.getLookup().lookup(NeuralNetwork.class);//get the object from lookup listener

                if (dataSet != null) {
                    trainingSet = dataSet;

                    neuralNetworkAndDataSetInformationCheck(neuralNetwork, trainingSet);
                    if (radioDataSet.isSelected()) {
                        IOSettingsDialog dataSetSettings = IOSettingsDialog.getInstance();
                        dataSetSettings.initializeInformation(trainingSet, jmeVisualization);
                        dataSetSettings.setVisible(true);
                    }

                }

                if (nnet != null) {
                    neuralNetwork = nnet;
                    neuralNetworkAndDataSetInformationCheck(neuralNetwork, trainingSet);
                }

                if (neuralNetwork != null && trainingSet != null) {
                    trainSignal = true;
                    removeContent();
                    trainingPreprocessing();
                    addContent();
                    neuralNetworkAndDataSetInformationCheck(neuralNetwork, trainingSet);
                    initializeConsumerProducer(1000);
                }

                e.dropComplete(true);
            } catch (UnsupportedFlavorException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public DataSet generateRandomDataSet(int inputsNumber, int outputsNumber, int rows) {

        DataSet dataSet = new DataSet(inputsNumber, outputsNumber);
        String[] columnNames = new String[inputsNumber + outputsNumber];
        for (int i = 0; i < columnNames.length; i++) {
            int k = i + 1;
            columnNames[i] = "Sample value " + k;

        }
        //dataSet.setColumnNames(columnNames);

        for (int i = 1; i <= rows; i++) {
            double[] inputs = new double[inputsNumber];
            for (int j = 0; j < inputs.length; j++) {
                inputs[j] = randInt(-100, 100) / 100.0;
            }
            double[] outputs = new double[outputsNumber];
            outputs[randInt(0, outputsNumber - 1)] = randInt(1, outputsNumber);
            dataSet.addRow(new DataSetRow(inputs, outputs));
        }
        return dataSet;
    }
    
    /*
     * This method shows the information, eg. current name of dataset and neural network that are used for training
     */
    public void neuralNetworkAndDataSetInformationCheck(NeuralNetwork neuralNetvork, DataSet dataSet) {

        
        if (dataSet != null) {
            if (dataSet.getLabel() != null) {
                labDataset.setText(dataSet.getLabel());
                
            } else {
                labDataset.setText("Not selected");
            }
        } else {
            labDataset.setText("Not selected");
        }
        if (neuralNetvork != null) {
            if (neuralNetvork.getLabel() != null) {
                labNetwork.setText(neuralNetvork.getLabel());
            } else {
                labNetwork.setText("Not selected");           
            }
        } else {
            labNetwork.setText("Not selected");
        }
    }
    
//    int inputs = Integer.parseInt(txtInputsSize.getText());
//        int outputs = Integer.parseInt(txtOutputsSize.getText());
//        int dataSetSize = Integer.parseInt(txtSize.getText());
//        
//        DataSet dataSet = generateRandomDataSet(inputs, outputs, dataSetSize);       
//        dataSet.setLabel("Test dataset");
//        
//        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(dataSet);
//             
//        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Created dataset "+dataSet.getLabel()+"\r\n");

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        visualizationPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        radioDataSet = new javax.swing.JRadioButton();
        radioWeights = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        labX = new javax.swing.JLabel();
        labY = new javax.swing.JLabel();
        labZ = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listColors = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtInputsSize = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtOutputsSize = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtSize = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        labNetwork = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        labDataset = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        visualizationPanel.setBackground(new java.awt.Color(255, 255, 255));
        visualizationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        visualizationPanel.setPreferredSize(new java.awt.Dimension(640, 480));
        visualizationPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                visualizationPanelMouseClicked(evt);
            }
        });
        visualizationPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                visualizationPanelMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout visualizationPanelLayout = new javax.swing.GroupLayout(visualizationPanel);
        visualizationPanel.setLayout(visualizationPanelLayout);
        visualizationPanelLayout.setHorizontalGroup(
            visualizationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 906, Short.MAX_VALUE)
        );
        visualizationPanelLayout.setVerticalGroup(
            visualizationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jPanel3.border.title"))); // NOI18N

        radioDataSet.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioDataSet);
        org.openide.awt.Mnemonics.setLocalizedText(radioDataSet, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.radioDataSet.text")); // NOI18N

        radioWeights.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioWeights);
        org.openide.awt.Mnemonics.setLocalizedText(radioWeights, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.radioWeights.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioDataSet)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(radioWeights)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioDataSet)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radioWeights)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jPanel1.border.title"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(204, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel4.text")); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 204, 51));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel5.text")); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 255));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel6.text")); // NOI18N

        labX.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(labX, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.labX.text")); // NOI18N

        labY.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(labY, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.labY.text")); // NOI18N

        labZ.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(labZ, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.labZ.text")); // NOI18N

        jScrollPane1.setViewportView(listColors);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel7.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labY, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labZ, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
                            .addGap(56, 56, 56)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(labX))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(labY))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(labZ))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jPanel5.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel1.text")); // NOI18N

        txtInputsSize.setText(org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.txtInputsSize.text")); // NOI18N
        txtInputsSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInputsSizeActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel2.text")); // NOI18N

        txtOutputsSize.setText(org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.txtOutputsSize.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel3.text")); // NOI18N

        txtSize.setText(org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.txtSize.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(6, 6, 6)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                    .addComponent(txtSize, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtInputsSize)
                    .addComponent(txtOutputsSize))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtInputsSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtOutputsSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jPanel6.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel8.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labNetwork, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.labNetwork.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.jLabel9.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(labDataset, org.openide.util.NbBundle.getMessage(JMEVisualizationTopComponent.class, "JMEVisualizationTopComponent.labDataset.text")); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labNetwork, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(labDataset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(labNetwork))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(labDataset))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(visualizationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(visualizationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtInputsSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInputsSizeActionPerformed
        
    }//GEN-LAST:event_txtInputsSizeActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int inputs = Integer.parseInt(txtInputsSize.getText());
        int outputs = Integer.parseInt(txtOutputsSize.getText());
        int dataSetSize = Integer.parseInt(txtSize.getText());

        DataSet dataSet = generateRandomDataSet(inputs, outputs, dataSetSize);
        dataSet.setLabel("Test dataset");

        NeurophProjectFilesFactory.getDefault().createTrainingSetFile(dataSet);

        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Created dataset " + dataSet.getLabel() + "\r\n");


    }//GEN-LAST:event_jButton1ActionPerformed

    private void visualizationPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visualizationPanelMouseClicked
        
    }//GEN-LAST:event_visualizationPanelMouseClicked

    private void visualizationPanelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visualizationPanelMouseMoved
       
    }//GEN-LAST:event_visualizationPanelMouseMoved

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labDataset;
    private javax.swing.JLabel labNetwork;
    private javax.swing.JLabel labX;
    private javax.swing.JLabel labY;
    private javax.swing.JLabel labZ;
    private javax.swing.JList listColors;
    private javax.swing.JRadioButton radioDataSet;
    private javax.swing.JRadioButton radioWeights;
    private javax.swing.JTextField txtInputsSize;
    private javax.swing.JTextField txtOutputsSize;
    private javax.swing.JTextField txtSize;
    private javax.swing.JPanel visualizationPanel;
    // End of variables declaration//GEN-END:variables

}
