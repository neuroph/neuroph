package org.neuroph.netbeans.wizards;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.main.easyneurons.DataSetTableModel;

public final class NewTrainingSetVisualPanel2 extends JPanel {

    private DataSet trainingSet;
    private DataSetTableModel tableModel;
    private String trainingSetType;
    private int inputs, outputs;
    private String trainingSetLabel;

    /** Creates new form NewTrainingSetVisualPanel2 */
    public NewTrainingSetVisualPanel2() {
//   http://forums.netbeans.org/topic29936.html&highlight=
//        WizardDescriptor wizard = this.get
//            int inputs = Integer.parseInt((String) wizard.getProperty("input number"));
//            int outputs = Integer.parseInt((String) wizard.getProperty("output number"));

        // nigde nije setovan i to je verovatno problemInteractiveRenderer


//         int inputs = NewTrainingSetValuesWizard.getInstance().getInputNumber();
//         int outputs = NewTrainingSetValuesWizard.getInstance().getOutputNumber();

//        tableModel = new DataSetTableModel(2, 1); // ovde bi trebalo reci kolik okolona ima i to pokupiti iz prethodnog panela
//        tableModel.addTableModelListener(new NewTrainingSetVisualPanel2.InteractiveTableModelListener());

        initComponents();

//        if (!tableModel.hasEmptyRow()) {
//            tableModel.addEmptyRow();
//        }
//        trainingSetTable.setSurrendersFocusOnKeystroke(true);
//        trainingSetTable.setModel(tableModel);


//        TableColumn hidden = trainingSetTable.getColumnModel().getColumn(tableModel.HIDDEN_INDEX);
//        hidden.setMinWidth(20);
//        hidden.setPreferredWidth(20);
//        hidden.setMaxWidth(20);
//        hidden.setCellRenderer(new InteractiveRenderer(tableModel.HIDDEN_INDEX));
//
//        this.trainingSetTable.getTableHeader().setReorderingAllowed(false);

    }

    @Override
    public String getName() {
        return "Edit Training Set table";
    }

    public DataSetTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTrainingSetTable() {
        return trainingSetTable;
    }

    

    public void setTrainingSet(DataSet trainingSet) {
        this.trainingSet = trainingSet;

        this.tableModel = new DataSetTableModel(this.trainingSet);
        this.trainingSetTable.setModel(this.tableModel);
        trainingSetTable.setSurrendersFocusOnKeystroke(true);

        TableColumn hidden = trainingSetTable.getColumnModel().getColumn(tableModel.HIDDEN_INDEX);
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);
        hidden.setCellRenderer(new InteractiveRenderer(tableModel.HIDDEN_INDEX));

        this.tableModel.fireTableDataChanged();

    }

    public void setTrainingSetEditFrameVariables(String name, String type, int inputs, int outputs) {
        DataSet trainingSet = new DataSet(inputs, outputs);
        trainingSet.setLabel(name);
        this.trainingSetType = type;
        this.trainingSet = trainingSet;
        this.inputs = inputs;
        this.outputs = outputs;
        this.tableModel = new DataSetTableModel(inputs, outputs);

        tableModel.addTableModelListener(new InteractiveTableModelListener());
        trainingSetTable.setModel(tableModel);
//        initComponents();

        if (!tableModel.hasEmptyRow()) {
            tableModel.addEmptyRow();
        }
        trainingSetTable.setSurrendersFocusOnKeystroke(true);
        trainingSetTable.setModel(tableModel);

        TableColumn hidden = trainingSetTable.getColumnModel().getColumn(tableModel.HIDDEN_INDEX);
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);
        hidden.setCellRenderer(new InteractiveRenderer(tableModel.HIDDEN_INDEX));
        this.tableModel.fireTableDataChanged();


        this.trainingSetLabel = trainingSet.getLabel();
        this.trainingSetLabelTextField.setText(this.trainingSetLabel);
        this.trainingSetTable.getTableHeader().setReorderingAllowed(false);

    }

    public void highlightLastRow(int row) {
        int lastrow = tableModel.getRowCount();
        if (row == lastrow - 1) {
            trainingSetTable.setRowSelectionInterval(lastrow - 1, lastrow - 1);
        } else {
            trainingSetTable.setRowSelectionInterval(row + 1, row + 1);
        }

        trainingSetTable.setColumnSelectionInterval(0, 0);
    }

    class InteractiveRenderer extends DefaultTableCellRenderer {

        protected int interactiveColumn;

        public InteractiveRenderer(int interactiveColumn) {
            this.interactiveColumn = interactiveColumn;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row,
                int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == interactiveColumn && hasFocus) {
//                if ((TrainingSetEditFrame.this.tableModel.getRowCount() - 1) == row
//                        && !TrainingSetEditFrame.this.tableModel.hasEmptyRow()) {
//                    TrainingSetEditFrame.this.tableModel.addEmptyRow();
//                }
                if ((NewTrainingSetVisualPanel2.this.tableModel.getRowCount() - 1) == row
                        && !NewTrainingSetVisualPanel2.this.tableModel.hasEmptyRow()) {
                    tableModel.addEmptyRow();
                }


                highlightLastRow(row);
            }

            return c;
        }
    }

    public class InteractiveTableModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent evt) {
            if (evt.getType() == TableModelEvent.UPDATE) {
                int column = evt.getColumn();
                int row = evt.getFirstRow();
                // System.out.println("row: " + row + " column: " + column);
                trainingSetTable.setColumnSelectionInterval(column + 1, column + 1);
                trainingSetTable.setRowSelectionInterval(row, row);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tablePopupMenu = new javax.swing.JPopupMenu();
        addRowMenuItem = new javax.swing.JMenuItem();
        delRowMenuItem = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        trainingSetLabelTextField = new javax.swing.JTextField();
        tableScrollPane = new javax.swing.JScrollPane();
        trainingSetTable = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        addRowButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(addRowMenuItem, org.openide.util.NbBundle.getMessage(NewTrainingSetVisualPanel2.class, "NewTrainingSetVisualPanel2.addRowMenuItem.text")); // NOI18N
        addRowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowMenuItemActionPerformed(evt);
            }
        });
        tablePopupMenu.add(addRowMenuItem);

        org.openide.awt.Mnemonics.setLocalizedText(delRowMenuItem, org.openide.util.NbBundle.getMessage(NewTrainingSetVisualPanel2.class, "NewTrainingSetVisualPanel2.delRowMenuItem.text")); // NOI18N
        delRowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delRowMenuItemActionPerformed(evt);
            }
        });
        tablePopupMenu.add(delRowMenuItem);

        setMinimumSize(new java.awt.Dimension(657, 383));
        setPreferredSize(new java.awt.Dimension(657, 383));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(NewTrainingSetVisualPanel2.class, "NewTrainingSetVisualPanel2.jLabel1.text")); // NOI18N

        trainingSetLabelTextField.setEditable(false);
        trainingSetLabelTextField.setText(org.openide.util.NbBundle.getMessage(NewTrainingSetVisualPanel2.class, "NewTrainingSetVisualPanel2.trainingSetLabelTextField.text")); // NOI18N

        trainingSetTable.setComponentPopupMenu(tablePopupMenu);
        tableScrollPane.setViewportView(trainingSetTable);

        org.openide.awt.Mnemonics.setLocalizedText(addRowButton, org.openide.util.NbBundle.getMessage(NewTrainingSetVisualPanel2.class, "NewTrainingSetVisualPanel2.addRowButton.text")); // NOI18N
        addRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRowButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(loadButton, org.openide.util.NbBundle.getMessage(NewTrainingSetVisualPanel2.class, "NewTrainingSetVisualPanel2.loadButton.text")); // NOI18N
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(helpButton, org.openide.util.NbBundle.getMessage(NewTrainingSetVisualPanel2.class, "NewTrainingSetVisualPanel2.helpButton.text")); // NOI18N
        helpButton.setEnabled(false);

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addRowButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loadButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(helpButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addRowButton)
                    .addComponent(loadButton)
                    .addComponent(helpButton)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(trainingSetLabelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(79, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 627, Short.MAX_VALUE)
                    .addGap(15, 15, 15)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(trainingSetLabelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 313, Short.MAX_VALUE)
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(48, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowButtonActionPerformed
        ((DataSetTableModel) trainingSetTable.getModel()).addEmptyRow();
        DataSetTableModel tm=((DataSetTableModel) trainingSetTable.getModel());
        for(int i=0;i<tm.getRowCount();i++){
            for(int j=0;j<tm.getColumnCount();j++)
            System.out.println("value at: "+j+" is:"+((DataSetTableModel) trainingSetTable.getModel()).getValueAt(i, j));
        }
}//GEN-LAST:event_addRowButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        TrainingDataFileDialog dialog = new TrainingDataFileDialog(inputs, outputs, this, true);
        dialog.setVisible(true);
}//GEN-LAST:event_loadButtonActionPerformed

    private void delRowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delRowMenuItemActionPerformed
        ((DataSetTableModel) trainingSetTable.getModel())
				.removeRow(trainingSetTable.getSelectedRow());
    }//GEN-LAST:event_delRowMenuItemActionPerformed

    private void addRowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRowMenuItemActionPerformed
        ((DataSetTableModel) trainingSetTable.getModel()).addEmptyRow();
    }//GEN-LAST:event_addRowMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addRowButton;
    private javax.swing.JMenuItem addRowMenuItem;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JMenuItem delRowMenuItem;
    private javax.swing.JButton helpButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton loadButton;
    private javax.swing.JPopupMenu tablePopupMenu;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTextField trainingSetLabelTextField;
    private javax.swing.JTable trainingSetTable;
    // End of variables declaration//GEN-END:variables
}

