package org.neuroph.netbeans.classificationsample;

import javax.swing.DefaultComboBoxModel;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.datasetgen.DataSetGenerator;
import org.neuroph.datasetgen.shapes.CircleGenerator;
import org.neuroph.datasetgen.shapes.DiamondGenerator;
import org.neuroph.datasetgen.shapes.ElipseGenerator;
import org.neuroph.datasetgen.shapes.MoonGenerator;
import org.neuroph.datasetgen.shapes.RandomPolynomialGenerator;
import org.neuroph.datasetgen.shapes.RingGenerator;
import org.neuroph.datasetgen.shapes.SquareGenerator;
import org.neuroph.datasetgen.shapes.XORGenerator;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.NeuralNetworkFactory;
import org.neuroph.util.TransferFunctionType;
import org.openide.windows.IOProvider;

/**
 *
 * @author Marko / Milos Randjic
 */
public class MultiLayerPerceptronClassificationSamplePanel extends javax.swing.JPanel {

   // private int tansferFunctionCode = 1; // not used?    
    private int neuralNetworkCounter = 0;//number of created networks
    private String neuronsCount;
    private NeuralNetwork neuralNetwork;
    private TransferFunctionType transferFunctionType;
    private DataSetGenerator[] shapeGenerators;
    
    // ovo treba da slusa u lookupu a ne d abude hardkodirano...
    public MultiLayerPerceptronVisualizationTopComponent mlpVisualizationTc;
    
    public static int VISUALIZATION_OPTION;
    public static boolean SHOW_POINTS;

    public MultiLayerPerceptronClassificationSamplePanel() {
        initComponents();
        initializeNeuralNetworkComponents();
        initShapes(0, 0);
    }    

    public MultiLayerPerceptronVisualizationTopComponent getMlpSampleTc() {
        return mlpVisualizationTc;
    }

    // trebalo bi da setuje ovaj tc iz lookup-a
    public void setMlpSampleTc(MultiLayerPerceptronVisualizationTopComponent mlpSampleTc) {
        this.mlpVisualizationTc = mlpSampleTc;
    }
               
    // why do I have to see bs from here?
//    public MultiLayerPerceptronClassificationSamplePanel(MultiLayerPerceptronVisualizationTopComponent mlptc) {
//        initComponents();
//        initializeNeuralNetworkComponents();
//        initShapes(0, 0);
//        this.mlpSampleTc = mlptc;
//    }

    private void initializeNeuralNetworkComponents() {
        comboTransferFunction.addItem("Sigmoid");
        comboTransferFunction.addItem("Tanh");
        transferFunctionType = TransferFunctionType.SIGMOID;
    }

    private void initShapes(int shapeIndex, int numberOfPoints) {
        shapeGenerators = new DataSetGenerator[8];
        shapeGenerators[0] = new ElipseGenerator(numberOfPoints, 0, 0, 1, 0.5);
        shapeGenerators[1] = new CircleGenerator(numberOfPoints, 0, 0, 0.5);
        shapeGenerators[2] = new RingGenerator(numberOfPoints, 0, 0, 0.125, 0.5);
        shapeGenerators[3] = new MoonGenerator(numberOfPoints, 0, 0, 0.5);
        shapeGenerators[4] = new SquareGenerator(numberOfPoints, 1.25);
        shapeGenerators[5] = new DiamondGenerator(numberOfPoints, 1);
        shapeGenerators[6] = new XORGenerator(numberOfPoints);
        shapeGenerators[7] = new RandomPolynomialGenerator(numberOfPoints);
        comboShapes.setModel(new DefaultComboBoxModel(shapeGenerators));
        comboShapes.setSelectedIndex(shapeIndex);
    }

    public void setNeuralNetworkInformation(String info) {
        labNeuralNetwork.setText(info);
    }

    public void setDataSetInformation(String info) {
        labDataSet.setText(info);
    }

    public void setCheckPoints(boolean flag) {
        checkPositiveInputs.setSelected(flag);
    }

    public DataSetGenerator getSelectedShapeGenerator() {
        initShapes(comboShapes.getSelectedIndex(), slideNumberOfPoints.getValue());
        return shapeGenerators[comboShapes.getSelectedIndex()];
    }

    public double getLearningRate() {
        return 0.7;
    }

    public double getMaxError() {
        return 0.01;
    }

    public double getMomentum() {
        return 0.2;
    }

    public int getMaxIteration() {
        return 0;
    }


    public void setTransferFunctionCode() {
        String selectedTf = (String) comboTransferFunction.getSelectedItem();
        if (selectedTf.equals("Tanh")) {
           transferFunctionType = TransferFunctionType.TANH;
        }
        if (selectedTf.equals("Sigmoid")) {
            transferFunctionType = TransferFunctionType.SIGMOID;
        }
    }

    public void getNeuronsCount() {
        String structure = comboNetworkStructure.getSelectedItem().toString().replace(':', ' ');
        String hiddenNeurons = structure.substring(1, structure.length() - 1);
   
        neuronsCount = "2 " + hiddenNeurons + " 1";
    }

    public NeuralNetwork createNeuralNetwork() {
        getNeuronsCount();
               
        neuralNetwork = NeuralNetworkFactory.createMLPerceptron(
                    neuronsCount, transferFunctionType, MomentumBackpropagation.class, true, false);
        neuralNetworkCounter++;
        neuralNetwork.setLabel("MlpSampleNet" + neuralNetworkCounter);
        return neuralNetwork;
    }

    public void createNeuralNetworkFile(NeuralNetwork neuralNetwork) {
        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(neuralNetwork);
    }

    public void clear() {
        mlpVisualizationTc.clear();
    }

    public void checkVisualizationOption() {
        if (radioAnswer.isSelected()) {
            mlpVisualizationTc.getVisualizationPanel().setVisualizationOption(1);
        } else if (radioAreas.isSelected()) {
            mlpVisualizationTc.getVisualizationPanel().setVisualizationOption(2);
        } else if (radioLines.isSelected()) {
            mlpVisualizationTc.getVisualizationPanel().setVisualizationOption(3);
        }
    }

    public void setVisualizationOptions() {
        checkVisualizationOption();
        if (checkPoints.isSelected()) {
            SHOW_POINTS = true;
            mlpVisualizationTc.getVisualizationPanel().resetAndRepaintGridPoints();
        } else {
            SHOW_POINTS = false;
            clear();
        }
    }

    public void checkForPositiveInputs() {
        if (mlpVisualizationTc.getVisualizationPanel().positiveInputsOnly()) {
            checkPositiveInputs.setSelected(true);
            comboShapes.setEnabled(true);
            slideNumberOfPoints.setEnabled(true);
        } else {
            checkPositiveInputs.setSelected(false);
            comboShapes.setEnabled(false);
            slideNumberOfPoints.setEnabled(false);
        }
    }

    public void visualizationOptionCheck() {
        if (radioAnswer.isSelected()) {
            mlpVisualizationTc.getVisualizationPanel().setVisualizationOption(1);
        } else if (radioAreas.isSelected()) {
            mlpVisualizationTc.getVisualizationPanel().setVisualizationOption(2);
        } else if (radioLines.isSelected()) {
            mlpVisualizationTc.getVisualizationPanel().setVisualizationOption(3);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        buttonGroup2 = new javax.swing.ButtonGroup();
        dataSetPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        comboShapes = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        slideNumberOfPoints = new javax.swing.JSlider();
        createDataSetButton = new javax.swing.JButton();
        labNumberOfPoints = new javax.swing.JLabel();
        neuralNetworkPanel = new javax.swing.JPanel();
        jlTransferFunction = new javax.swing.JLabel();
        comboTransferFunction = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        comboNetworkStructure = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        informationPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        labNeuralNetwork = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labDataSet = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        visualizationPanel = new javax.swing.JPanel();
        radioAnswer = new javax.swing.JRadioButton();
        radioAreas = new javax.swing.JRadioButton();
        radioLines = new javax.swing.JRadioButton();
        checkPoints = new javax.swing.JCheckBox();
        checkPositiveInputs = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(415, 454));
        setLayout(new java.awt.GridLayout(4, 1, 0, 3));

        dataSetPanel.setBackground(new java.awt.Color(255, 255, 255));
        dataSetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Step 1: Create Data Set", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11))); // NOI18N
        dataSetPanel.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Shape:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 8, 9, 0);
        dataSetPanel.add(jLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 1, 3, 0);
        dataSetPanel.add(comboShapes, gridBagConstraints);

        jLabel1.setText("Number of points:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 5);
        dataSetPanel.add(jLabel1, gridBagConstraints);

        slideNumberOfPoints.setBackground(new java.awt.Color(255, 255, 255));
        slideNumberOfPoints.setMajorTickSpacing(400);
        slideNumberOfPoints.setMaximum(2000);
        slideNumberOfPoints.setPaintLabels(true);
        slideNumberOfPoints.setPaintTicks(true);
        slideNumberOfPoints.setToolTipText("Number of points in data set");
        slideNumberOfPoints.setValue(1000);
        slideNumberOfPoints.setPreferredSize(new java.awt.Dimension(300, 45));
        slideNumberOfPoints.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                slideNumberOfPointsStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(21, 0, 9, 2);
        dataSetPanel.add(slideNumberOfPoints, gridBagConstraints);

        createDataSetButton.setText("Create Data Set");
        createDataSetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDataSetButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 18);
        dataSetPanel.add(createDataSetButton, gridBagConstraints);

        labNumberOfPoints.setText("1000");
        labNumberOfPoints.setMaximumSize(new java.awt.Dimension(40, 14));
        labNumberOfPoints.setMinimumSize(new java.awt.Dimension(40, 14));
        labNumberOfPoints.setPreferredSize(new java.awt.Dimension(40, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        dataSetPanel.add(labNumberOfPoints, gridBagConstraints);

        add(dataSetPanel);

        neuralNetworkPanel.setBackground(new java.awt.Color(255, 255, 255));
        neuralNetworkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Step 2: Create Neural Network", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11))); // NOI18N
        neuralNetworkPanel.setLayout(new java.awt.GridBagLayout());

        jlTransferFunction.setText("Transfer function:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        neuralNetworkPanel.add(jlTransferFunction, gridBagConstraints);

        comboTransferFunction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTransferFunctionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        neuralNetworkPanel.add(comboTransferFunction, gridBagConstraints);

        jLabel4.setText("Network structure:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 5, 0);
        neuralNetworkPanel.add(jLabel4, gridBagConstraints);

        comboNetworkStructure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2:2:1", "2:3:1", "2:4:1", "2:5:1", "2:6:1", "2:7:1", "2:8:1", "2:9:1", "2:10:1", "2:11:1", "2:13:1", "2:14:1", "2:15:1", "2:20:1", "2:25:1", "2:50:1", "2:100:1", "2:2:2:1", "2:3:3:1", "2:4:4:1", "2:5:5:1", "2:10:10:1", "2:3:4:1", "2:4:3:1", "2:3:5:1", "2:5:3:1", "2:4:5:1", "2:5:4:1", "2:3:10:1", "2:10:3:1", "2:4:10:1", "2:10:4:1", "2:5:10:1", "2:2:2:2:1", "2:3:3:3:1", "2:3:4:3:1", "2:4:3:4:1", "2:4:4:4:1", "2:3:4:5:1", "2:5:4:3:1", "2:4:5:4:1", "2:5:4:5:1", "2:5:5:5:1" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 45);
        neuralNetworkPanel.add(comboNetworkStructure, gridBagConstraints);

        jButton1.setText("Create Neural Network");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 18);
        neuralNetworkPanel.add(jButton1, gridBagConstraints);

        add(neuralNetworkPanel);

        informationPanel.setBackground(new java.awt.Color(255, 255, 255));
        informationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Step 3: Train Network", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11))); // NOI18N
        informationPanel.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Neural Network:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 1, 1);
        informationPanel.add(jLabel3, gridBagConstraints);

        labNeuralNetwork.setText("Not selected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 5, 5);
        informationPanel.add(labNeuralNetwork, gridBagConstraints);

        jLabel5.setText("Data Set:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 5, 0);
        informationPanel.add(jLabel5, gridBagConstraints);

        labDataSet.setText("Not selected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 5, 5);
        informationPanel.add(labDataSet, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Ubuntu", 0, 12)); // NOI18N
        jLabel6.setForeground(java.awt.Color.lightGray);
        jLabel6.setText("<html>Drag n' drop data set and neural network<br/> to visualization window and click train button<br/> in toolbar to start training</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 6, 0);
        informationPanel.add(jLabel6, gridBagConstraints);

        add(informationPanel);

        jPanel5.setLayout(new java.awt.GridLayout(1, 3));

        visualizationPanel.setBackground(new java.awt.Color(255, 255, 255));
        visualizationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Visualization Settings:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 11))); // NOI18N
        visualizationPanel.setLayout(new java.awt.GridBagLayout());

        radioAnswer.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioAnswer);
        radioAnswer.setSelected(true);
        radioAnswer.setText("Network Answer");
        radioAnswer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioAnswerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 18, 5, 5);
        visualizationPanel.add(radioAnswer, gridBagConstraints);

        radioAreas.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioAreas);
        radioAreas.setText("Areas");
        radioAreas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioAreasActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 18, 5, 0);
        visualizationPanel.add(radioAreas, gridBagConstraints);

        radioLines.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioLines);
        radioLines.setText("Lines");
        radioLines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioLinesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 18, 17, 0);
        visualizationPanel.add(radioLines, gridBagConstraints);

        checkPoints.setBackground(new java.awt.Color(255, 255, 255));
        checkPoints.setSelected(true);
        checkPoints.setText("Show points");
        checkPoints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPointsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 17, 5, 5);
        visualizationPanel.add(checkPoints, gridBagConstraints);

        checkPositiveInputs.setBackground(new java.awt.Color(255, 255, 255));
        checkPositiveInputs.setText("Range [0,1]");
        checkPositiveInputs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPositiveInputsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 18, 0, 5);
        visualizationPanel.add(checkPositiveInputs, gridBagConstraints);

        jPanel5.add(visualizationPanel);

        add(jPanel5);
    }// </editor-fold>//GEN-END:initComponents

    private void slideNumberOfPointsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_slideNumberOfPointsStateChanged
        labNumberOfPoints.setText(String.valueOf(slideNumberOfPoints.getValue()));
    }//GEN-LAST:event_slideNumberOfPointsStateChanged

    private void createDataSetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSetButtonActionPerformed
        // ako su tacke nacrtane rucno
        if (mlpVisualizationTc.getVisualizationPanel().isPointDrawed()) {
            mlpVisualizationTc.customDataSetCheck();
            mlpVisualizationTc.sampleTrainingSetFileCheck();
            mlpVisualizationTc.setSelectedInputs(new int[]{0, 1});
            labDataSet.setText(mlpVisualizationTc.getTrainingSet().getLabel());
        } else { // ako se generisu pomocu generatora data seta
            DataSet dataSet = getSelectedShapeGenerator().generate();
            NeurophProjectFilesFactory.getDefault().createTrainingSetFile(dataSet);
            mlpVisualizationTc.setTrainingSet(dataSet);
            mlpVisualizationTc.updateNeuralNetAndDataSetInfo(null, dataSet);           
            mlpVisualizationTc.setSelectedInputs(new int[]{0, 1});
            mlpVisualizationTc.coordinateSystemDomainCheck();
            mlpVisualizationTc.getVisualizationPanel().drawPointsFromDataSet(dataSet, mlpVisualizationTc.getSelectedInputs());
            mlpVisualizationTc.repaint();
        }
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Sample data set created.");
// dont reset selected values in controls window        
//        initShapes(0, 0);
//        slideNumberOfPoints.setValue(1000);
        
        // open dataset node here  - how - best way by makeing programatical selection
//        TopComponent projectsLogicalTC = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
        // get expolorer or beantreeview from tc lookup?
        
        
    }//GEN-LAST:event_createDataSetButtonActionPerformed

    private void checkPositiveInputsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPositiveInputsActionPerformed
        labDataSet.setText("Not selected");
        labNeuralNetwork.setText("Not selected");
        if (mlpVisualizationTc.getNeuralNetwork() != null) {
            mlpVisualizationTc.stop();
        }
        clear();
        mlpVisualizationTc.getVisualizationPanel().setDrawingPointsDisabled(false);
        mlpVisualizationTc.setPointDrawed(false);
        mlpVisualizationTc.removeNetworkAndDataSetFromContent();

        mlpVisualizationTc.getVisualizationPanel().repaint();
    }//GEN-LAST:event_checkPositiveInputsActionPerformed

    private void radioAnswerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioAnswerActionPerformed
        visualizationOptionCheck();
    }//GEN-LAST:event_radioAnswerActionPerformed

    private void radioAreasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioAreasActionPerformed
        visualizationOptionCheck();
    }//GEN-LAST:event_radioAreasActionPerformed

    private void radioLinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioLinesActionPerformed
        visualizationOptionCheck();
    }//GEN-LAST:event_radioLinesActionPerformed

    private void checkPointsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPointsActionPerformed
    }//GEN-LAST:event_checkPointsActionPerformed

    private void comboTransferFunctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTransferFunctionActionPerformed
        setTransferFunctionCode();
    }//GEN-LAST:event_comboTransferFunctionActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        neuralNetwork = createNeuralNetwork();
        createNeuralNetworkFile(neuralNetwork);
        IOProvider.getDefault().getIO("Neuroph", false).getOut().println("Neural network created.");
        comboNetworkStructure.setSelectedIndex(0);
        comboTransferFunction.setSelectedIndex(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox checkPoints;
    private javax.swing.JCheckBox checkPositiveInputs;
    private javax.swing.JComboBox comboNetworkStructure;
    private javax.swing.JComboBox comboShapes;
    private javax.swing.JComboBox comboTransferFunction;
    private javax.swing.JButton createDataSetButton;
    private javax.swing.JPanel dataSetPanel;
    private javax.swing.JPanel informationPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JLabel jlTransferFunction;
    private javax.swing.JLabel labDataSet;
    private javax.swing.JLabel labNeuralNetwork;
    private javax.swing.JLabel labNumberOfPoints;
    private javax.swing.JPanel neuralNetworkPanel;
    private javax.swing.JRadioButton radioAnswer;
    private javax.swing.JRadioButton radioAreas;
    private javax.swing.JRadioButton radioLines;
    private javax.swing.JSlider slideNumberOfPoints;
    private javax.swing.JPanel visualizationPanel;
    // End of variables declaration//GEN-END:variables
}
