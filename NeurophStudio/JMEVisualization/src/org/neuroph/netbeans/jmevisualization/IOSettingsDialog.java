/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization;

import com.jme3.math.ColorRGBA;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.jmevisualization.charts.JMEVisualizationTopComponent;
import org.neuroph.netbeans.jmevisualization.charts.graphs.JMEDatasetScatter3D;

/**
 *
 * @author Milos Randjic
 */
public class IOSettingsDialog extends javax.swing.JDialog {

    private static IOSettingsDialog instance;
    private ArrayList<ColorRGBA> outputColors;//colors for each dataSet output
    private JMEVisualization jmeVisualization;
    private DataSet dataSet;
    private int[] inputs;
    private String[] inputNames;
    private String[] outputNames;

    /**
     * Creates new form InputSettngsDialog
     */
    private IOSettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
    }

    public static IOSettingsDialog getInstance() {
        if (instance == null) {
            instance = new IOSettingsDialog(null, true);
        }
        return instance;

    }

    public void storeInputs(int[] inputs) {
        this.inputs = inputs;
    }

    public int[] getStoredInputs() {
        return inputs;
    }

    public ArrayList<ColorRGBA> getOutputColors() {
        return outputColors;
    }

    public void setOutputColors(ArrayList<ColorRGBA> outputColors) {
        this.outputColors = outputColors;
    }

    public String[] getInputNames() {
        return inputNames;
    }

    public void setInputNames(String[] inputNames) {
        this.inputNames = inputNames;
    }

    public String[] getOutputNames() {
        return outputNames;
    }

    public void setOutputNames(String[] outputNames) {
        this.outputNames = outputNames;
    }

    /*
     Initialization of input names and output names
     */
    public void initializeInformation(DataSet dataSet, JMEVisualization jmeVisualization) {

        this.dataSet = dataSet;
        this.jmeVisualization = jmeVisualization;

        inputNames = new String[dataSet.getInputSize()];
        outputNames = new String[dataSet.getOutputSize()];
        String[] columnNames = dataSet.getColumnNames();
        int columnIndex = 0;

        for (int i = 0; i < inputNames.length; i++) {
            int k = i + 1;
            if (columnNames[columnIndex] == null) {//if columnName does not exist
                inputNames[i] = "Input " + k;//create automatic input name
            } else {
                inputNames[i] = columnNames[columnIndex];//otherwise, fetch columnName drom dataSet
            }
            columnIndex++;
        }

        for (int i = 0; i < outputNames.length; i++) {
            int k = i + 1;
            if (columnNames[columnIndex] == null) {//if columnName does not exist
                outputNames[i] = "Output " + k;//create automatic output name
            } else {
                outputNames[i] = columnNames[columnIndex];//otherwise, fetch columnName drom dataSet
            }
            columnIndex++;

        }

        outputColors = new ArrayList<>(dataSet.getOutputSize());//create new list

        for (int i = 0; i < dataSet.getOutputSize(); i++) {//assign initial colors
            outputColors.add(new ColorRGBA(0, 0, 0, 0));
        }
        /*
         Set comboBoxModel for each axis {X, Y, Z}, and outputs
         */
        comboX.setModel(new DefaultComboBoxModel(inputNames));
        comboY.setModel(new DefaultComboBoxModel(inputNames));
        comboZ.setModel(new DefaultComboBoxModel(inputNames));
        comboOutputs.setModel(new DefaultComboBoxModel(outputNames));

    }
    
    /*
     Calculates dominant output color for each dataSet row
     */
    public ArrayList<ColorRGBA> calculateDominantOutputColors() {

        ArrayList<ColorRGBA> colors = new ArrayList<>();

        for (int i = 0; i < dataSet.size(); i++) {
            double[] outputValues = dataSet.getRowAt(i).getDesiredOutput();//fetch dataSet row outputs
            int index = 0;//index of found dominant color
            double max = Double.MIN_VALUE;
            /*
             Output is considered as dominant in case when it possesses maximum absolute value
             */
            for (int j = 0; j < outputValues.length; j++) {//find dominant output color
                if (Math.abs(outputValues[j]) > max) {
                    max = outputValues[j];
                    index = j;
                }
            }
            colors.add(outputColors.get(index));//fetching dominant output color from the list of base colors
        }
        return colors;
    }

    /*
     Calculates random integer number in specified interval
     */
    private static int randInt(int min, int max) {       
        return new Random().nextInt((max - min) + 1) + min;
    }

    /*
     Draws created dataset
     */
    private void drawDataSet() {
        Thread t = new Thread(new Runnable() {//drawing in a separate thread
            @Override
            public void run() {
                JMEDatasetScatter3D jmeDataSetScatter = new JMEDatasetScatter3D(dataSet, getStoredInputs(), calculateDominantOutputColors(), jmeVisualization);
                jmeDataSetScatter.createGraph();
            }
        });
        t.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        comboX = new javax.swing.JComboBox();
        comboY = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        comboZ = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        colorChooser = new javax.swing.JColorChooser();
        jLabel4 = new javax.swing.JLabel();
        comboOutputs = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.title")); // NOI18N
        setModal(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jPanel2.border.title"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jLabel1.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 204, 51));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jLabel2.text")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 204));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jLabel3.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(comboX, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(18, 18, 18)
                            .addComponent(comboY, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(18, 18, 18)
                            .addComponent(comboZ, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jPanel3.border.title"))); // NOI18N
        jPanel3.setToolTipText(org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jPanel3.toolTipText")); // NOI18N

        colorChooser.setBackground(new java.awt.Color(255, 255, 255));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jLabel4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton2, org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboOutputs, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(colorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboOutputs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(colorChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jButton3, org.openide.util.NbBundle.getMessage(IOSettingsDialog.class, "IOSettingsDialog.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int x = comboX.getSelectedIndex();//index of first input
        int y = comboY.getSelectedIndex();//index of secont input
        int z = comboZ.getSelectedIndex();//index of third input

        if (x == y || x == z || y == z) {
            JOptionPane.showMessageDialog(this, "Please select different inputs.");
            comboX.setSelectedIndex(0);
            comboY.setSelectedIndex(0);
            comboZ.setSelectedIndex(0);
        } else {
            storeInputs(new int[]{x, y, z});//saving chosen inputs indexes in array
            JOptionPane.showMessageDialog(this, "Inputs saved.");

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int index = comboOutputs.getSelectedIndex();//index of selected output
        Color outputColor = colorChooser.getColor();//fetching selected color from colorChooser
        ColorRGBA outputColorRGBA = outputColors.get(index);//fetching colorRGBA object for editing
        outputColorRGBA.set(outputColor.getRed() / 255.0f,
                outputColor.getGreen() / 255.0f,
                outputColor.getBlue() / 255.0f,
                outputColor.getAlpha() / 255.0f);//updating output color

        JOptionPane.showMessageDialog(this, "Color is set for " + outputNames[index] + ".");

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        drawDataSet();//drawing dataSet
        JMEVisualizationTopComponent.findInstance().loadDataSetLegend();//updating dataSetLegend
        dispose();//closing dialog
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IOSettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                IOSettingsDialog dialog = new IOSettingsDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JColorChooser colorChooser;
    private javax.swing.JComboBox comboOutputs;
    private javax.swing.JComboBox comboX;
    private javax.swing.JComboBox comboY;
    private javax.swing.JComboBox comboZ;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables

}
