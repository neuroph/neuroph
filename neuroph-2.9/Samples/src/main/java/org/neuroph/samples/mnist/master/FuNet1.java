package org.neuroph.samples.mnist.master;

import org.neuroph.contrib.eval.classification.ClassificationResult;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.samples.convolution.mnist.MNISTDataSet;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import org.neuroph.contrib.eval.classification.Utils;

/**
 * Simple application which demonstrated the usage of CNN for digit recognition
 */
public class FuNet1 extends JFrame implements Runnable {

    private BufferedImage canvas;
    private NeuralNetwork network;


    DataSet testSet;
    private JLabel label;

    public FuNet1() {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        try {
            network = network.load(new FileInputStream("/home/mithquissir/Desktop/cnn/5-50-100/30.nnet"));
            testSet = MNISTDataSet.createFromFile(MNISTDataSet.TEST_LABEL_NAME, MNISTDataSet.TEST_IMAGE_NAME, 10000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
    }

    //this was moved from the overriden paintComponent()
    // instead it update the canvas BufferedImage and calls repaint()
    public void updateCanvas() {
        Graphics2D g2 = canvas.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setPaint(getColor());

        if (tool == 1) {

            g2.fillOval(currentX - ((int) value / 2), currentY - ((int) value / 2), (int) value, (int) value);


        } else if (tool == 2) {
            g2.setStroke(new BasicStroke((float) value, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(oldX, oldY, currentX, currentY);
            g2.setStroke(new BasicStroke(1.0f));
        }
        repaint();


    }

    // used in both the updateCanvas and 'clear' method.
    private Color getColor() {
        Color c = null;
        if (color == 1)
            c = Color.black;
        else if (color == 2)
            c = new Color(240, 240, 240);
        else if (color == 3)
            c = Color.white;
        else if (color == 4)
            c = Color.red;
        else if (color == 5)
            c = Color.green;
        else if (color == 6)
            c = Color.blue;

        return c;
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        canvas = new BufferedImage(320, 320, BufferedImage.TYPE_BYTE_GRAY);

        buttonGroup1 = new ButtonGroup();
        buttonGroup2 = new ButtonGroup();
        jPanel4 = new JPanel();
        jSlider2 = new JSlider();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel(new GridBagLayout());
        JLabel canvasLabel = new JLabel(new ImageIcon(canvas));
        jPanel2.add(canvasLabel, null);

        jPanel3 = new JPanel();
        jRadioButton3 = new JRadioButton();
        jRadioButton4 = new JRadioButton();
        jRadioButton5 = new JRadioButton();
        jRadioButton6 = new JRadioButton();
        jRadioButton7 = new JRadioButton();
        jRadioButton8 = new JRadioButton();
        jButton1 = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("FuNet1 --- powered by Neuroph");

        jPanel4.setBorder(BorderFactory.createTitledBorder("Line thickness"));


        jSlider2.setMajorTickSpacing(10);
        jSlider2.setMaximum(51);
        jSlider2.setMinimum(1);
        jSlider2.setMinorTickSpacing(5);
        jSlider2.setPaintTicks(true);
        jSlider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });

//        jLabel1.setText("Stroke Size (Radius)");

        GroupLayout jPanel4Layout = new GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(jSlider2, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        label = new JLabel("");
        Font labelFont = label.getFont();

        label.setFont(new Font(labelFont.getName(), Font.PLAIN, 30));


        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jSlider2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGroup(jPanel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED))
                        ));

        jPanel2.setBackground(new Color(0, 0, 0));
        jPanel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        // add the listeners to the label that contains the canvas buffered image
        canvasLabel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                jPanel2MousePressed(evt);
            }

            public void mouseReleased(MouseEvent evt) {
                jPanel2MouseReleased(evt);
            }
        });
        canvasLabel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });


        jButton1.setText("Clear");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jButton1, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                                                        .addComponent(label, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(label, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)

                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                                        .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        Graphics g = canvas.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        repaint();
        pack();
    }// </editor-fold>

    // clear the canvas using the currently selected color.
    private void jButton1ActionPerformed(ActionEvent evt) {
//        System.out.println("You cleared the canvas.");
        Graphics g = canvas.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        repaint();
    }


    int currentX, currentY, oldX, oldY;

    private void jPanel2MouseDragged(MouseEvent evt) {
        currentX = evt.getX();
        currentY = evt.getY();
        updateCanvas();
        if (tool == 1) {
            oldX = currentX;
            oldY = currentY;

        }

    }

    private void jPanel2MousePressed(MouseEvent evt) {

        oldX = evt.getX();
        oldY = evt.getY();
        if (tool == 2) {
            currentX = oldX;
            currentY = oldY;
        }


    }

    int tool = 1;

    //Slider Properties//
    double value = 40;

    private void jSlider2StateChanged(ChangeEvent evt) {
        value = jSlider2.getValue();

    }

    //COLOR CODE//
    int color = 1;


    //mouse released//
    private void jPanel2MouseReleased(MouseEvent evt) {

        currentX = evt.getX();
        currentY = evt.getY();


        final double SCALE = 0.1;
        BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D grph = (Graphics2D) bi.getGraphics();
        grph.scale(SCALE, SCALE);

        grph.drawImage(canvas, 0, 0, null);
        grph.dispose();

        newPix = new double[32 * 32];
        pixels = bi.getRGB(0, 0, 32, 32, pixels, 0, 32);

        for (int i = 0; i < pixels.length; i++) {
            newPix[i] = 255 - (pixels[i] & 0xff);
            newPix[i] /= 255;
        }


        long start = System.currentTimeMillis();
        network.setInput(newPix);
        network.calculate();
        System.out.println("Execution time: " + (System.currentTimeMillis() - start) + " milliseconds");

        try {
            ImageIO.write(bi, "png", new File("number.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        double[] networkOutput = network.getOutput();
        int maxNeuronIdx = Utils.maxIdx(networkOutput);

        ClassificationResult max = new ClassificationResult(maxNeuronIdx, networkOutput[maxNeuronIdx]);


        System.out.println("New calculation:");
        System.out.println("Class: " + max.getClassIdx());
        System.out.println("Probability: " + max.getNeuronOutput());

        label.setText(String.valueOf(max.getClassIdx()));


    }

    //set ui visible//
    public static void main(String args[]) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                FuNet1 net = new FuNet1();

                scheduler.scheduleAtFixedRate(net, 0, 300, TimeUnit.MILLISECONDS);

                net.setVisible(true);
            }

        });
    }

    // Variables declaration - do not modify
    private ButtonGroup buttonGroup1;
    private ButtonGroup buttonGroup2;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    public JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    //    private JRadioButton jRadioButton10;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton4;
    private JRadioButton jRadioButton5;
    private JRadioButton jRadioButton6;
    private JRadioButton jRadioButton7;
    private JRadioButton jRadioButton8;
    //    private JRadioButton jRadioButton9;
    public JSlider jSlider2;

    int[] pixels;
    double[] newPix;


    @Override
    public void run() {

    }
// End of variables declaration
}