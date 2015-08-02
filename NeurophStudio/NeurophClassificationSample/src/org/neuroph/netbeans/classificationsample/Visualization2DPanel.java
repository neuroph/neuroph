package org.neuroph.netbeans.classificationsample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import org.neuroph.core.*;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Marko 
 * @author Milos Randjic
 */
public class Visualization2DPanel extends javax.swing.JPanel {

    private int resolution;
    private NeuralNetwork neuralNetwork;
    private Graphics graphicsBuffer;
    private Image imageBuffer;
    private double gridPoints[][];//points used to visualiza network output(answer) during training (defines the grid) 
    private ArrayList points;//input points from training set
    private DataSet trainingSet;
    private int value; //indicator for mouse click button registration
 
    private int helpX = -1000;//coordinates that show current mouse position on panel
    private int helpY = -1000;
        
    
    private boolean visualizationStarted = true;
    private boolean positiveInputsOnly = false;
    private int visualizationOption;
    private boolean drawingLocked = false;
    private boolean pointDrawed = false;
    
    public static Color[] neuronColor;
    public static Color[] neuronColorInverted;

    // size of the visualization panel, same height
    // it should also be changed in MultiLayerPerceptronClassificationSampleTopComponent.initializePanel
    public int panelWidth = 800;
    
    /**
     * Creates new form Visualization2DPanel
     */
    public Visualization2DPanel() {

        resolution = panelWidth; // was panelWidth
        setSize(resolution, resolution);
        initGridPoints();
        points = new ArrayList();
        value = 1;
        trainingSet = new DataSet(2, 1);
        initComponents();
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public boolean isDrawingLocked() {
        return drawingLocked;
    }

    public void setDrawingLocked(boolean drawingLocked) {
        this.drawingLocked = drawingLocked;
    }

    public int getVisualizationOption() {
        return visualizationOption;
    }

    public void setVisualizationOption(int visualizationOption) {
        this.visualizationOption = visualizationOption;
    }

    public boolean positiveInputsOnly() {
        return positiveInputsOnly;
    }

    public void setPositiveInputsOnly(boolean positiveInputsOnly) {
        this.positiveInputsOnly = positiveInputsOnly;
    }

    public boolean isVisualizationStarted() {
        return visualizationStarted;
    }

    public void setVisualizationStarted(boolean visualizationStarted) {
        this.visualizationStarted = visualizationStarted;
    }

    public boolean isPointDrawed() {
        return pointDrawed;
    }

    public void setPointDrawed(boolean pointDrawed) {
        this.pointDrawed = pointDrawed;
    }
    private boolean allPointsRemoved = false;

    public boolean isAllPointsRemoved() {
        return allPointsRemoved;
    }

    public void setAllPointsRemoved(boolean allPointsRemoved) {
        this.allPointsRemoved = allPointsRemoved;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public void setValue(int v) {
        value = v;
    }

    public void setNeuronColors(NeuralNetwork neuralNetwork) {
        int numberOfColors;
        numberOfColors = neuralNetwork.getLayerAt(1).getNeurons().length - 1;//gets number of neurons at second layer
        Visualization2DPanel.neuronColor = new Color[numberOfColors];
        Visualization2DPanel.neuronColorInverted = new Color[numberOfColors];
        Random random = new Random();
        for (int i = 0; i < numberOfColors; i++) {//for each neuron, assign two colors
            float r = random.nextFloat();
            float g = random.nextFloat();
            float b = random.nextFloat();
            neuronColor[i] = new Color(r, g, b);
            neuronColorInverted[i] = new Color(1 - r, 1 - g, 1 - b);
        }
    }
    
    /*
     * Creates an initializes all grid points to default value -1000
     */
    private void initGridPoints() {
        // width and height of points grid - depends on the size of visualization area - in practice one ponit on 10 pixels
        int size = 80; // 57       
        // create the array
        gridPoints = new double[size][size];    
        // init all points to -1000
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gridPoints[i][j] = -1000;
            }
        }
    }
    
    
    
    /*
     * Sets the value for the specified grid point
     */
    public void setGridPoints(int x, int y, double val) {
       
        if (val != -1000) { // -1000 is initial value for all grid points
            if (val > 1.0) {
                val = 1.0;
            }
            if (val < 0) {
                val = 0.0;
            }
        }
        
        gridPoints[x][y] = val;        
    }

    /*
     * Erases all points
     */
    public void clearPoints() {
        visualizationStarted = false;//visualization signal is false
        setAllPointsRemoved(true);//indicates that all points are removed
        points.clear();//erases all points from Vector
        trainingSet.clear();//erases training set rows
        initGridPoints();
        repaint();
    }

    /*
     * Initializes grid points and repaints the panel
     */
    public void resetAndRepaintGridPoints() {
        initGridPoints();
        repaint();
    }

    public DataSet getTrainingSet() {
        return trainingSet;
    }

    /*
     * Calculates scaled y value, needed for drawing lines on panel
     */
    private double calculateY(double scalingCoefficient, int x, double w0, double w1, double w2) {
        return scalingCoefficient - scalingCoefficient * (-w1 * x / scalingCoefficient - w0) / w2;
    }

    /*
     * Calculates line coefficients, k and n
     */
//    public Double[] calculateCoefficients(Integer[] coords) {
//        //0-x1,  1-y1,  2-x2,  3-y2
//        double k = (coords[3] - coords[1]) / (coords[2] - coords[0]);
//        double n = -coords[0] * k + coords[1];
//        return new Double[]{k, n};
//    }

    /*
     * Calculates coordinates for drawing a single line on panel
     */
    private int[] calculateLineCoordinates(double w0, double w1, double w2) {
        int x1, x2, y1, y2;
        if (positiveInputsOnly()) {
            x1 = 0;
            y1 = (int) calculateY(panelWidth, x1, w0, w1, w2);
            x2 = panelWidth;
            y2 = (int) calculateY(panelWidth, x2, w0, w1, w2);
        } else {
            x1 = -panelWidth / 2;
            y1 = (int) calculateY(panelWidth / 2, x1, w0, w1, w2) - panelWidth / 2;

            x2 = panelWidth / 2;
            y2 = (int) calculateY(panelWidth / 2, x2, w0, w1, w2) - panelWidth / 2;
        }
        return new int[]{x1, y1, x2, y2};
    }

    
    /**
     * Draws lines dividing input space
     */
    private void visualizeLines2D() {
        graphicsBuffer.setColor(Color.black);//sets line color       
        if (neuralNetwork != null) {//if neural network exists
            Neuron[] neurons = neuralNetwork.getLayerAt(1).getNeurons();//second layer neurons 
            int[] selectedInputs = InputSettngsDialog.getInstance().getStoredInputs();//contains indexes of 2 selected inputs
            for (int i = 0; i < neurons.length - 1; i++) {//iterates through second layer neurons
                Connection[] conn = neurons[i].getInputConnections();//for each neuron, fetch input connections 
                /*
                 * for each connection, fetch weight value
                 */
                double w1 = conn[selectedInputs[0]].getWeight().getValue();//weight value from first selected input neuron
                double w2 = conn[selectedInputs[1]].getWeight().getValue();//weight value from second selected input neuron
                double w3 = conn[neuralNetwork.getLayerAt(0).getNeuronsCount() - 1].getWeight().getValue();//weight value from bias neuron
                int[] coordinates = calculateLineCoordinates(w3, w1, w2);//calculating coordinates for drawing the line
                graphicsBuffer.drawLine(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);//drawing the line
            }
        }
    }

    /*
     * This method enables training process visualization,
     * where every neuron is represented as a linear function,
     * that separates 2D space with 2 colors.
     */
    private void visualizeColoredAreas2D() {
        if (neuralNetwork != null) {
            Neuron[] neurons = neuralNetwork.getLayerAt(1).getNeurons();//second layer neurons
            int x, y, size = panelWidth, startIndex, offset, scalingCoefficient;
            if (positiveInputsOnly()) {             
                startIndex = 0;
                offset = 0;
                scalingCoefficient = size;
            } else {               
                startIndex = -size / 2;
                offset = panelWidth / 2;
                scalingCoefficient = panelWidth / 2;
            }
            int[] selectedInputs = InputSettngsDialog.getInstance().getStoredInputs();//contains indexes of 2 selected inputs
            for (int m = startIndex; m < size; m++) { 
                for (int n = startIndex; n < size; n++) {
                    x = m;//x coordinate
                    y = n;//y coordinate
                    double rgb = 0;//rgb color, needed for color fusion
                    double irgb = 0;//irgb color, needed for color fusion
                    int rgbCounter = 0;//number of rgb counted neurons
                    int irgbCounter = 0;//number of irgb counted neurons
                    for (int i = 0; i < neurons.length - 1; i++) {//iterates through layer neurons
                        Connection[] conn = neurons[i].getInputConnections();//for each neuron, fetch input connections
                        /*
                         * for each connection, fetch weight value
                         */
                        double w1 = conn[selectedInputs[0]].getWeight().getValue();//weight value from first selected input neuron
                        double w2 = conn[selectedInputs[1]].getWeight().getValue();//weight value from second selected input neuron
                        double w3 = conn[neuralNetwork.getLayerAt(0).getNeuronsCount() - 1].getWeight().getValue();//weight value from bias neuron

                        if (calculateY(scalingCoefficient, x, w3, w1, w2) > y + offset) {//membership function which separates 2D space with two colors
                            rgb += Visualization2DPanel.neuronColor[i].getRGB();
                            rgbCounter++;
                        } else {
                            irgb += Visualization2DPanel.neuronColorInverted[i].getRGB();
                            irgbCounter++;
                        }
                    }
                    int rgbColor = (int) Math.round((Math.round(rgb / rgbCounter) + Math.round(irgb / irgbCounter)) / 2);
                    graphicsBuffer.setColor(new Color(rgbColor));//sets new fusionated color
                    graphicsBuffer.fillRect(x, y, 2, 2);
                }
            }
        }
    }

    /*
     * This method enables visualization of neural network output
     */
    private void visualizeNetworkAnswer2D() {
        int x, y;
        int size = 80; //bilo je 57  grid size 570x570 - on 10       
        
        //draws grid 
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                x = i * 10;
                y = j * 10;
                if (gridPoints[i][j] != -1000) {//for particular (i,j) pair, there is one output, gridPoints[i][j]
                    float r = (float) gridPoints[i][j];//assigning color for particular output value
                    float b = 1 - r;//making a complement color
                 
                    graphicsBuffer.setColor(new Color(r, 0, b));//creating new color that combines previous two
                 
                    //1. sta je ovo
                    if (positiveInputsOnly()) {
                        graphicsBuffer.fillRect(x, y, 10, 10);
                    } else {
                        graphicsBuffer.fillRect(x - panelWidth / 2, y - panelWidth / 2, 10, 10); // 10, 10
                    }
                    
                    if (gridPoints[i][j] > 0.5) {//this is a treshold function, that separates one output from another
                        graphicsBuffer.setColor(Color.RED);
                    } else {
                        graphicsBuffer.setColor(Color.BLUE);
                    }
                                        
                    // 2sta j eovo
                    if (positiveInputsOnly()) {
                        graphicsBuffer.fillRect(x + 3, y + 3, 3, 3);
                    } else {
                        graphicsBuffer.fillRect(x - panelWidth / 2 + 3, y - panelWidth / 2 + 3, 3, 3); // 3, 3, 3, 3
                    }
                }
            }
        }
    }

    /*
     * Draws current mouse coordinates on the panel 
     */
    private void drawHelpLine(int X, int Y, Graphics g) {
        DecimalFormat df = new DecimalFormat("#.####");//formats coordinates to maximum 4 decimal places
        double xVal = Double.parseDouble(df.format(transformFromPanelToDecartX(X)));
        double yVal = Double.parseDouble(df.format(transformFromPanelToDecartY(Y)));
        g.setColor(Color.lightGray);
        if (positiveInputsOnly) {
            g.drawLine(X, Y, 0, Y);
            g.drawLine(X, Y, X, panelWidth);
            g.setColor(Color.BLACK);
            g.drawString("(" + xVal + " , " + yVal + ")", X - 10, Y - 10);
        } else {
            g.drawLine(X - panelWidth / 2, Y - panelWidth / 2, 0, Y - panelWidth / 2);
            g.drawLine(X - panelWidth / 2, Y - panelWidth / 2, X - panelWidth / 2, 500);
            g.setColor(Color.BLACK);
            g.drawString("(" + xVal + " , " + yVal + ")", X - panelWidth / 2 - 10, Y - panelWidth / 2 - 10);
        }
    }

    public void drawPointsFromTrainingSet(DataSet inputTrainingSet, int[] inputs) {
        points.clear();//initially, all points are erased
        repaint();//repainting the component
        for (Iterator<DataSetRow> it = inputTrainingSet.iterator(); it.hasNext();) {
            DataSetRow dataSetRow = it.next();
            double decartX = dataSetRow.getInput()[inputs[0]];//first selected input value
            double decartY = dataSetRow.getInput()[inputs[1]];//second selected input value
            double output = dataSetRow.getDesiredOutput()[0];//output value
            int createdOutput = 0;
            if (output > 0.5) {
                createdOutput = 1;
            }
            int panelX = transformFromDecartToPanelX(decartX);//transforming Descartes' value to panel value
            int panelY = transformFromDecartToPanelY(decartY);
            int[] point = new int[3];
            point[0] = createdOutput;
            point[1] = panelX;
            point[2] = panelY;
            points.add(point);
            Graphics g = getGraphics();
            drawPoint(createdOutput, panelX, panelY, g);//drawing point with specified arguments
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
      
        // initialize drawing buffer if needed
        if (imageBuffer == null) {
            imageBuffer = createImage(panelWidth, panelWidth);
            graphicsBuffer = imageBuffer.getGraphics();
            if (!positiveInputsOnly) {
                graphicsBuffer.translate(panelWidth / 2, panelWidth / 2);//translates panel coordinates to -570/2, in order to enable both positive and negative inputs
            }
        }
        
        // clear drawing buffer
        graphicsBuffer.setColor(Color.WHITE);//drawing white rectangles for visualization
        if (positiveInputsOnly) {
            graphicsBuffer.fillRect(0, 0, panelWidth, panelWidth);
        } else {
            graphicsBuffer.fillRect(-panelWidth / 2, -panelWidth / 2, panelWidth, panelWidth);
        }
        
        // redraw whats needed
        if (visualizationStarted) { //if visualization is started, by clicking the train button on the taskbar, switching function decides which visualization is going to be invoked
            switch (getVisualizationOption()) {
                case 1:
                    visualizeNetworkAnswer2D();
                  //  repaint(); // why repaint here again ?????
                    break;
                case 2:
                    visualizeColoredAreas2D();  // why repaint here again ?????
                    //repaint();
                    break;
                case 3:
                    visualizeLines2D();  // why repaint here again ?????
                    //repaint();
                    break;
            }
        }
        if (positiveInputsOnly) {
            //draws coordinate axis lines and labels
            graphicsBuffer.setColor(Color.BLACK);
            graphicsBuffer.drawLine(0, panelWidth-1, panelWidth-1, panelWidth-1);
            graphicsBuffer.drawLine(0, 0, 0, panelWidth-1);
            graphicsBuffer.drawString("1", 5, 10);
            graphicsBuffer.drawString("1", panelWidth-10, panelWidth-5);
            graphicsBuffer.drawString("0", 5, panelWidth-5);
            
            //draws input points
            Iterator e = points.iterator();
            while (e.hasNext()) {
                int[] point = (int[]) e.next();
                drawPoint(point[0], point[1], point[2], graphicsBuffer);
            }
            
            //draws help line
            if (helpX != -1000 && 0 <= helpX && helpX <= panelWidth && helpY != -1000 && 0 <= helpY && helpX <= panelWidth) {
                drawHelpLine(helpX, helpY, graphicsBuffer);
            }
            g.drawImage(imageBuffer, 0, 0, this);

        } else {
            //draws coordinate axis lines and labels
            graphicsBuffer.setColor(Color.BLACK);
            graphicsBuffer.drawLine(-panelWidth / 2, 0, panelWidth / 2, 0);
            graphicsBuffer.drawLine(0, -panelWidth / 2, 0, panelWidth / 2);
            graphicsBuffer.drawString("1", 5, -panelWidth / 2 + 10);
            graphicsBuffer.drawString("-1", 5, panelWidth / 2 - 5);
            graphicsBuffer.drawString("1", panelWidth / 2 - 10, 15);
            graphicsBuffer.drawString("-1", -panelWidth / 2 + 5, 15);
            graphicsBuffer.drawString("0", -10, 15);
            
            //draws input points
            Iterator e = points.iterator();
            while (e.hasNext()) {
                int[] point = (int[]) e.next();
                drawPoint(point[0], point[1] - panelWidth / 2, point[2] - panelWidth / 2, graphicsBuffer);
            }
            
            //draws help line
            if (helpX != -1000 && -panelWidth / 2 <= helpX && helpX <= panelWidth && helpY != -1000 && -panelWidth / 2 <= helpY && helpY <= panelWidth) {
                drawHelpLine(helpX, helpY, graphicsBuffer);
            }
            g.drawImage(imageBuffer, 0, 0, this);
        }
    }

    /*
     * Transforms panel's value to Decartes' x value
     */
    double transformFromPanelToDecartX(int x) {
        double xx;
        if (positiveInputsOnly) {
            double X = (double) x;
            xx = (double) X / (double)panelWidth;
            if (xx > 1) {
                xx = 1;
            }
            if (xx < 0) {
                xx = 0;
            }
        } else {
            double X = (double) x;
            xx = X / (panelWidth / 2) - 1;
            if (xx > 1) {
                xx = 1;
            }
            if (xx < -1) {
                xx = -1;
            }
        }
        return xx;
    }

    /*
     * Transforms panel's value to Decartes' y value
     */
    double transformFromPanelToDecartY(int y) {
        double yy;
        if (positiveInputsOnly) {
            double Y = (double) y;
            yy = 1 - Y / (double)panelWidth;
            if (yy > 1) {
                yy = 1;
            }
            if (yy < 0) {
                yy = 0;
            }
        } else {
            double Y = (double) y;
            yy = 1 - Y / (panelWidth / 2);
            if (yy > 1) {
                yy = 1;
            }
            if (yy < -1) {
                yy = -1;
            }
        }
        return yy;
    }

    /*
     * Transforms Decartes' x value to panel's value
     */
    public int transformFromDecartToPanelX(double x) {
        if (positiveInputsOnly) {
            double valueX = x * (double)panelWidth;
            return (int) valueX;
        } else {
            return (int) ((1 + x) * panelWidth / 2);
        }
    }

    /*
     * Transforms Decartes' y value to panel's value
     */
    public int transformFromDecartToPanelY(double y) {
        if (positiveInputsOnly) {
            double valueY = (1.0 - y) * (double)panelWidth;
            return (int) valueY;
        } else {
            return (int) ((1 - y) * panelWidth / 2);
        }
    }

    /*
     * Draws input points, added by clicking on the panel
     */
    public void drawPoint(int v, int x, int y, Graphics g) {
        if (v == 1) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }
        g.fillArc(x - 3, y - 3, 7, 7, 0, 360);
        g.setColor(new Color(0.5f, 0.5f, 0.5f));
        g.drawArc(x - 3, y - 3, 7, 7, 0, 360);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName("Form"); // NOI18N
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved

        int X = evt.getX();
        int Y = evt.getY();
        if (positiveInputsOnly) {
            if (0 <= X && X <= panelWidth && 0 <= Y && Y <= panelWidth) {
                helpX = X;
                helpY = Y;
            } else {
                helpX = -1000;
                helpY = -1000;
            }
        } else {
            if (-panelWidth / 2 <= X && X <= panelWidth && -panelWidth / 2 <= Y && Y <= panelWidth) {
                helpX = X;
                helpY = Y;
            } else {
                helpX = -1000;
                helpY = -1000;
            }
        }
        repaint();
    }//GEN-LAST:event_formMouseMoved

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if (!isDrawingLocked()) {
            setPointDrawed(true);
            int button_value;
            if (value == 1) {
                if (evt.getButton() == 3) {
                    button_value = 0;
                } else {
                    button_value = 1;
                }
                int X = evt.getX();
                int Y = evt.getY();
                int[] point = new int[3];
                point[0] = button_value;
                point[1] = X;
                point[2] = Y;
                points.add(point);
                trainingSet.addRow(new DataSetRow(new double[]{transformFromPanelToDecartX(X), transformFromPanelToDecartY(Y)}, new double[]{button_value}));
                Graphics g = getGraphics();
                drawPoint(button_value, X, Y, g);
            }
        }
    }//GEN-LAST:event_formMousePressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
