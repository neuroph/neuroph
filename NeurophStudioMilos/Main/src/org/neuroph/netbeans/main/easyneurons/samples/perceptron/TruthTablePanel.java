/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.main.easyneurons.samples.perceptron;

/**
 *
 * @author Marko
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Marko
 */
public class TruthTablePanel extends javax.swing.JPanel {


    private PerceptronSampleFrameTopComponent perceptronSampleView;
    private int width;
    private int height;
    private double[] buttonValues = {0,0,0,0};
    JButton output00;
    JButton output01;
    JButton output10;
    JButton output11;

    /** Creates new form TruthTablePanel */
    public TruthTablePanel(PerceptronSampleFrameTopComponent pa, int w, int h) {
        super();
        perceptronSampleView = pa;
        width = w;
        height = h;
        initComponents();
    }

     public double[] buttonValues() {
    return buttonValues;
    }

    @Override
    public void paintComponent(Graphics g) {
    int gridX,gridY;

    gridX = 2 * width / 3;
    gridY = height / 5;

    // Draw the truth table axes
    g.setColor(Color.black);
    g.drawLine(5,gridY,width-5,gridY);
    g.drawLine(gridX,5,gridX,height-5);
  }

//   @Override
//  public void update(Graphics g) {
//    paint(g);
//  }

    public void disableComponents() {
    output00.setEnabled(false);
    output01.setEnabled(false);
    output10.setEnabled(false);
    output11.setEnabled(false);
  }

   public void enableComponents() {
    output00.setEnabled(true);
    output01.setEnabled(true);
    output10.setEnabled(true);
    output11.setEnabled(true);
  }
   private void initComponents() {
    // Initialize the layout manager and constraint object
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    setLayout(gridbag);

    // Add the input/output truth table row by row
    // Add the first row
    JLabel l1 = new JLabel("x1");
    c.anchor = GridBagConstraints.CENTER;
    c.gridwidth = 1;
    c.insets = new Insets(5,5,5,5);
    c.weightx = 1.0;
    c.weighty = 1.0;
    gridbag.setConstraints(l1, c);
    add(l1);

    JLabel l2 = new JLabel("x2");
    gridbag.setConstraints(l2, c);
    add(l2);

    JLabel l3 = new JLabel("y");
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(l3, c);
    add(l3);

    // Add the second row
    JLabel l4 = new JLabel("0");
    c.gridwidth = 1;
    gridbag.setConstraints(l4, c);
    add(l4);

    JLabel l5 = new JLabel("0");
    gridbag.setConstraints(l5, c);
    add(l5);

    output00 = new JButton(" 0 ");
    ActionListener output00Listener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	   updateButtonValues(0,(JButton)e.getSource());

	}
    };
    output00.addActionListener(output00Listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(output00, c);
    add(output00);

    // Add the third row
    JLabel l6 = new JLabel("1");
    c.gridwidth = 1;
    gridbag.setConstraints(l6, c);
    add(l6);

    JLabel l7 = new JLabel("0");
    gridbag.setConstraints(l7, c);
    add(l7);

    output01 = new JButton(" 0 ");
    ActionListener output01Listener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    updateButtonValues(1,(JButton)e.getSource());
	}
    };
    output01.addActionListener(output01Listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(output01, c);
    add(output01);

    // Add the fourth row
    JLabel l8 = new JLabel("0");
    c.gridwidth = 1;
    gridbag.setConstraints(l8, c);
    add(l8);

    JLabel l9 = new JLabel("1");
    gridbag.setConstraints(l9, c);
    add(l9);

    output10 = new JButton(" 0 ");
    ActionListener output10Listener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    updateButtonValues(2,(JButton)e.getSource());
	}
    };
    output10.addActionListener(output10Listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(output10, c);
    add(output10);

    // Add the fifth and final row
    JLabel l10 = new JLabel("1");
    c.gridwidth = 1;
    gridbag.setConstraints(l10, c);
    add(l10);

    JLabel l11 = new JLabel("1");
    gridbag.setConstraints(l11, c);
    add(l11);

    output11 = new JButton(" 0 ");
    ActionListener output11Listener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    updateButtonValues(3,(JButton)e.getSource());
	}
    };
    output11.addActionListener(output11Listener);
    c.gridwidth = GridBagConstraints.REMAINDER;
    gridbag.setConstraints(output11, c);
    add(output11);

    validate();
    setSize(width,height);
  }

    private void updateButtonValues(int nbButton, JButton button) {
	if (button.getText().equals(" 0 ")) {
	    button.setText(" 1 ");
	    buttonValues[nbButton] = 1;
	} else {
	    button.setText(" 0 ");
	    buttonValues[nbButton] = 0;
	}

	// Send message to parent applet that a button has changed.
	perceptronSampleView.buttonHasChanged(nbButton, buttonValues[nbButton]);
    }




}
