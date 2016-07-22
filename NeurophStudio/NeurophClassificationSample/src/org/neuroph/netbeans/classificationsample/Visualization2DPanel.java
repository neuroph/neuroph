package org.neuroph.netbeans.classificationsample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.neuroph.core.*;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 * Visualization Panel to draw data set and neural network response during
 * training
 *
 * @author Marko
 * @author Milos Randjic
 * @author Zoran Sevarac
 */
public class Visualization2DPanel extends javax.swing.JPanel implements ComponentListener {

    private NeuralNetwork neuralNetwork;
    private DataSet dataSet;

    private Graphics graphicsBuffer;
    private Image imageBuffer;

    private ArrayList<Point> dataSetPoints;//input points from training set
    private double gridPoints[][];//points used to visualiza network output(answer) during training (defines the grid) 

    private int value; //indicator for mouse click button registration

    private int helpX = -1000;//coordinates that show current mouse position on panel
    private int helpY = -1000;

    private boolean visualizationStarted = true;
    private boolean positiveInputsOnly = false;
    private int visualizationOption;

    private boolean drawingPointsDisabled = false; // TODO: change to drawingPointsEnabled
    private boolean pointDrawn = false; // true if points are manually drawn

    // ukinucemo padding nem potrebe, nek boji sve
    private static int padding = 0; // bio je 10 

    public static Color[] neuronColor;
    public static Color[] neuronColorInverted;

    // size of the visualization panel, same height
    /**
     * Velicina panela za crtanje - uzmi visinu i isto toiku sirinu
     */
    public int panelSize = 800;// = 800;
    private int[] selectedInputs; // which inputs from dataset should be visualized

    /**
     * Creates new form Visualization2DPanel
     */
    public Visualization2DPanel() {
        initGridPoints();
        dataSetPoints = new ArrayList();    // zasto kreiram ovde prazan??? ne bi trebalo to je budzenje
        value = 1; // point value on mouse click

        initComponents();
        addComponentListener(this);
    }

    public void setSelectedInputs(int[] selectedInputs) {
        this.selectedInputs = selectedInputs;
    }

    public void setDrawingPointsDisabled(boolean drawingPointsDisabled) {
        this.drawingPointsDisabled = drawingPointsDisabled;
    }

    //returns the visualizationOpetion(int)
    public int getVisualizationOption() {
        return visualizationOption;
    }

    //sets the visualizationOption
    public void setVisualizationOption(int visualizationOption) {
        this.visualizationOption = visualizationOption;
    }

    //returns boolean value that shows if the inputs are only positive
    public boolean positiveInputsOnly() {
        return positiveInputsOnly;
    }

    //sets the if the inputs are only positive or not
    public void setPositiveInputsOnly(boolean positiveInputsOnly) {
        this.positiveInputsOnly = positiveInputsOnly;
    }

    public void setVisualizationStarted(boolean visualizationStarted) {
        this.visualizationStarted = visualizationStarted;
    }

    //returns boolean value that shows if the point is drawed
    public boolean isPointDrawed() {
        return pointDrawn;
    }

    public void setPointDrawn(boolean pointDrawn) {
        this.pointDrawn = pointDrawn;
    }
    private boolean allPointsRemoved = false;

    //returns boolean value that shows if all the points are removed
    public boolean isAllPointsRemoved() {
        return allPointsRemoved;
    }

    //sets the allPointsRemoved atribute
    public void setAllPointsRemoved(boolean allPointsRemoved) {
        this.allPointsRemoved = allPointsRemoved;
    }

    //sets the neuralNetwork passed in the parameter
    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    //sets the value of the indicator for mouse click button registration
    public void setValue(int v) {
        value = v;
    }

    public void setNeuronColors(NeuralNetwork neuralNetwork) {
        int numberOfColors;
        numberOfColors = neuralNetwork.getLayerAt(1).getNeurons().size() - 1; //gets number of neurons in second layer
        neuronColor = new Color[numberOfColors]; //initializes neuronColor - a queue type Color with the size of numberOfColors
        neuronColorInverted = new Color[numberOfColors];//same as the one before
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
     * Creates and initializes all grid points to default value -1000
     * gridPoints are used as main points for visualization on 10 pixel distance
     */
    private void initGridPoints() {
        // width and height of points grid - depends on the size of visualization area - in practice one ponit on 10 pixels
        int gridSize = panelSize / 10; // 80     gridCellSize =   panelSize / 10
        // create the array
        gridPoints = new double[gridSize][gridSize];
        // init all points to -1000
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridPoints[i][j] = -1000;
            }
        }
    }

    /*
     * Sets the value for the specified grid point
     * @param x grid point x index
     * @param y grid point y index
     * @param val value (network output) for the grid point at [x][y]
     */
    public void setGridPoint(int x, int y, double val) {

        if (val != -1000) { // -1000 is initial value for all grid points
            if (val > 1.0) {    // everything above 1 is 1 
                val = 1.0;
            }
            if (val < 0) {      // everything below zero is zero
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
        dataSetPoints.clear();//erases all points from Vector
        if (dataSet != null) {
            dataSet.clear();//erases training set rows
        }
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
        return dataSet;
    }

    /*
     * Calculates scaled y value, needed for drawing lines on panel
     * @param x 
     */
    private double calculateY(double scalingCoefficient, int x, double w0, double w1, double w2) {
        return scalingCoefficient - scalingCoefficient * (-w1 * x / scalingCoefficient - w0) / w2;
    }

    /*
     * Calculates coordinates for drawing a single line on panel
     * w0, w1, w2 weights from the hidden neurons
     * returns coords for two point for drawing line
     * 
     */
    private Point[] calculateWeightsLine(double w0, double w1, double w2) {
        int x1, x2, y1, y2;

        if (positiveInputsOnly()) {
            x1 = 0;
            y1 = (int) calculateY(panelSize, x1, w0, w1, w2);
            x2 = panelSize;
            y2 = (int) calculateY(panelSize, x2, w0, w1, w2);
        } else {
            x1 = -panelSize / 2;
            y1 = (int) calculateY(panelSize / 2, x1, w0, w1, w2) - panelSize / 2;

            x2 = panelSize / 2;
            y2 = (int) calculateY(panelSize / 2, x2, w0, w1, w2) - panelSize / 2;
        }

        return new Point[]{new Point(x1, y1), new Point(x2, y2)};
    }

    private List<Neuron> hiddenNeurons;
    private List<Weight> weights;

    public void prepareTrainingBuffers() {
        hiddenNeurons = neuralNetwork.getLayerAt(1).getNeurons();
        weights = new ArrayList<>();
     
        for (int i = 0; i < hiddenNeurons.size() - 1; i++) {//iterates through neurons in hidden layer
            List<Connection> conn = hiddenNeurons.get(i).getInputConnections(); //for each neuron, fetch input connections 
            Weight w1 = conn.get(selectedInputs[0]).getWeight(); // weight value from first selected input neuron
            Weight w2 = conn.get(selectedInputs[1]).getWeight(); // weight value from second selected input neuron
            Weight w3 = conn.get(neuralNetwork.getLayerAt(0).getNeuronsCount() - 1).getWeight(); // weight value from bias            

            weights.add(w1);
            weights.add(w2);
            weights.add(w3);
        }
    }

    /**
     * Draws lines that divide input space Draws line using weights from hidden
     * neurons (including bias)
     */
    private void visualizeLines2D() {
        graphicsBuffer.setColor(Color.black); //sets line color       

        for (int i = 0; i < weights.size(); i += 3) {
            double w1 = weights.get(i).value; // weight value from first selected input neuron
            double w2 = weights.get(i + 1).value; // weight value from second selected input neuron
            double w3 = weights.get(i + 2).value; // weight value from bias

            Point[] linePoints = calculateWeightsLine(w3, w1, w2); //calculating coordinates for drawing the line
            graphicsBuffer.drawLine(linePoints[0].x, linePoints[0].y, linePoints[1].x, linePoints[1].y);//drawing the line
        }
    }

    /*
     * This method enables training process visualization,
     * where every neuron is represented as a linear function,
     * that separates 2D space with 2 colors.
     *
     * TODO: izbaciti toArray  i buferovati strukture neuronske mreze
     */
    private void visualizeColoredAreas2D() {
        int size = panelSize, startIndex, offset, scalingCoefficient;
        if (positiveInputsOnly()) {
            startIndex = 0;
            offset = 0;
            scalingCoefficient = size;
        } else {
            startIndex = -size / 2;
            offset = panelSize / 2;
            scalingCoefficient = panelSize / 2;
        }

        for (int x = startIndex; x < size; x+=5) { // ako je +=2 mnogo je brze ali i dalje izreckano
            for (int y = startIndex; y < size; y+=5) {
                double rgb = 0;//rgb color, needed for color fusion
                double irgb = 0;//irgb color, needed for color fusion
                int rgbCounter = 0;//number of rgb counted neurons
                int irgbCounter = 0;//number of irgb counted neurons

                for (int i = 0; i < weights.size(); i += 3) {
                    double w1 = weights.get(i).value; // weight value from first selected input neuron
                    double w2 = weights.get(i + 1).value; // weight value from second selected input neuron
                    double w3 = weights.get(i + 2).value; // weight value from bias

                    // kreiraj boju za odgovarajuci region    
                    if (calculateY(scalingCoefficient, x, w3, w1, w2) > y + offset) {//membership function which separates 2D space with two colors
                        rgb += neuronColor[i/3].getRGB();
                        rgbCounter++;
                    } else {
                        irgb += neuronColorInverted[i/3].getRGB();
                        irgbCounter++;
                    }
                }

                int rgbColor = (int) Math.round((Math.round(rgb / rgbCounter) + Math.round(irgb / irgbCounter)) / 2);
                graphicsBuffer.setColor(new Color(rgbColor));//sets new fusionated color
                graphicsBuffer.fillRect(x, y, 5, 5);
               
            }
        }
    }

    /*
     * This method enables visualization of neural network output
     * It is using gridPoints which is a matrix of grid points with values of the neural network outputs for each grid point
     *    
     * Povrsine 10x10 su osnovne celije koje se koriste za bojenje pozadine
     *
     * grid points se crtaju na rastojanju 10x10
     */
    private void visualizeNetworkAnswer2D() {
        int x, y;
        int size = 80; //bilo je 57  grid size 570x570 - on 10       
        //  int size = panelSize / 10;

        //draws grid points, and colors background
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                x = i * 10;
                y = j * 10;
                if (gridPoints[i][j] != -1000) {//for particular (i,j) pair, there is one output, gridPoints[i][j]

                    // create color depending on output/pint value
                    float r = (float) gridPoints[i][j];//assigning color for particular output value
                    float b = 1 - r; //making a complement color                 
                    graphicsBuffer.setColor(new Color(r, 0, b));//creating new color that combines previous two

                    // 1. popunjava pozadinu odgovarajucom bojom - ovo je glavno popunjavanje
                    if (positiveInputsOnly()) {
                        graphicsBuffer.fillRect(x, y, 10, 10);
                    } else {
                        graphicsBuffer.fillRect(x - panelSize / 2, y - panelSize / 2, 10, 10); // 10, 10
                    }

                    // 2. decide when to draw red or blue grid point depending on the 0.5 threshold   
                    if (gridPoints[i][j] > 0.5) {// this is a treshold function, that separates one output from another
                        graphicsBuffer.setColor(Color.RED);
                    } else {
                        graphicsBuffer.setColor(Color.BLUE);
                    }

                    // draw small rect as a grid point
                    if (positiveInputsOnly()) {
                        graphicsBuffer.fillRect(x + 3, y + 3, 3, 3);
                    } else {
                        graphicsBuffer.fillRect(x - panelSize / 2 + 3, y - panelSize / 2 + 3, 3, 3); // 3, 3, 3, 3
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
            g.drawLine(X, Y, X, panelSize);
            g.setColor(Color.BLACK);
            g.drawString("(" + xVal + " , " + yVal + ")", X - 10, Y - 10);
        } else {
            g.drawLine(X - panelSize / 2, Y - panelSize / 2, 0, Y - panelSize / 2);
            g.drawLine(X - panelSize / 2, Y - panelSize / 2, X - panelSize / 2, 500);
            g.setColor(Color.BLACK);
            g.drawString("(" + xVal + " , " + yVal + ")", X - panelSize / 2 - 10, Y - panelSize / 2 - 10);
        }
    }

    /**
     * Draws data set points on visualization panel abd fills points collection
     * (buffered for redraws) maybe I can draw the onto frawingbuffer?
     *
     * @param dataSet data set to draw
     * @param selectedInputs inputs indexes selected to be visualized
     */
    public void drawPointsFromDataSet(DataSet dataSet, int[] selectedInputs) {
        this.dataSet = dataSet;
        this.selectedInputs = selectedInputs;
        dataSetPoints.clear();//initially, all points are erased
        initGridPoints();
        repaint();//repainting the component - sta ce mi ova komponenta ovde? da bi obrisao sve?

        // Graphics g = getGraphics();
        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double decartX = dataSetRow.getInput()[selectedInputs[0]];//first selected input value
            double decartY = dataSetRow.getInput()[selectedInputs[1]];//second selected input value

            double output = dataSetRow.getDesiredOutput()[0]; //output value - TODO: what if there is more then one output? user should beable to choose
            int createdOutput = 0;
            if (output > 0.5) {
                createdOutput = 1;
            }

            int panelX = decartToPanelX(decartX);//transforming Descartes' value to panel value
            int panelY = decartToPanelY(decartY);

            Point point = new Point(panelX, panelY, createdOutput);
            dataSetPoints.add(point);
            drawPoint(point, graphicsBuffer);//drawing point with specified arguments - schedule drawing in another thread
        }
    }

    private void reGeneratePoints() {
        if (dataSet == null) {
            return; // do nothing if there is no dataset
        }
        dataSetPoints.clear();
        for (DataSetRow dataSetRow : dataSet.getRows()) {
            double decartX = dataSetRow.getInput()[selectedInputs[0]];//first selected input value
            double decartY = dataSetRow.getInput()[selectedInputs[1]];//second selected input value

            double output = dataSetRow.getDesiredOutput()[0]; //output value - TODO: what if there is mor then one outputs?
            int createdOutput = 0;
            if (output > 0.5) {
                createdOutput = 1;
            }

            int panelX = decartToPanelX(decartX);//transforming Descartes' value to panel value
            int panelY = decartToPanelY(decartY);

            Point point = new Point(panelX, panelY, createdOutput);

            dataSetPoints.add(point);
        }

        repaint();
    }

    private void initImageBuffer() {
        if (panelSize > 0) {
            imageBuffer = createImage(panelSize, panelSize);
            graphicsBuffer = imageBuffer.getGraphics();
            if (!positiveInputsOnly) {
                graphicsBuffer.translate(panelSize / 2, panelSize / 2);//translates panel coordinates to -570/2, in order to enable both positive and negative inputs
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // panelSize = getHeight()-2*padding; // velicina prostora za crtanje je kvadrat dimenzija visina ovog panela - 2*padding      
        panelSize = 800;

        // initialize drawing buffer if needed - ovo treba reinicijalizovati prilikom resize-a
        if (imageBuffer == null) {
            initImageBuffer(); // init offscreen drawing bufferfor for the first time
        }

        // clear drawing buffer (draw white background)
        graphicsBuffer.setColor(Color.WHITE);//drawing white rectangles for visualization
        if (positiveInputsOnly) {
            graphicsBuffer.fillRect(0, 0, panelSize, panelSize);//fills the specified rectangle
        } else {
            graphicsBuffer.fillRect(-panelSize / 2, -panelSize / 2, panelSize, panelSize);
        }

        // redraw whats needed if visualization is running
        if (visualizationStarted) { //if visualization is started, by clicking the train button on the taskbar, switching function decides which visualization is going to be invoked
            switch (getVisualizationOption()) {
                case 1:
                    visualizeNetworkAnswer2D();
                    break;
                case 2:
                    visualizeColoredAreas2D();  // why repaint here again ?????
                    break;
                case 3:
                    visualizeLines2D();  // why repaint here again ?????
                    break;
            }
        }
        
        // draw axes
        if (positiveInputsOnly) {
            //draws coordinate axis lines and labels
            graphicsBuffer.setColor(Color.BLACK);
            graphicsBuffer.drawLine(0, panelSize - 1, panelSize - 1, panelSize - 1);
            graphicsBuffer.drawLine(0, 0, 0, panelSize - 1);
            graphicsBuffer.drawString("1", 5, 10);
            graphicsBuffer.drawString("1", panelSize - 10, panelSize - 5);
            graphicsBuffer.drawString("0", 5, panelSize - 5);

            // draw points - can this be done in parellel?
            for (Point point : dataSetPoints) {
                drawPoint(point, graphicsBuffer);
            }
            //dataSetPoints.parallelStream().forEach(p - > drawPoinr());

            //draws help line
            if (helpX != -1000 && 0 <= helpX && helpX <= panelSize && helpY != -1000 && 0 <= helpY && helpX <= panelSize) {
                drawHelpLine(helpX, helpY, graphicsBuffer);
            }
            g.drawImage(imageBuffer, 0, 0, this);

        } else {
            //draws coordinate axis lines and labels
            graphicsBuffer.setColor(Color.BLACK);

            graphicsBuffer.drawLine(-panelSize / 2 + padding, 0, panelSize / 2 - padding, 0); // x-axis
            graphicsBuffer.drawLine(0, -panelSize / 2 + padding, 0, panelSize / 2 - padding); // y-axis

            graphicsBuffer.drawString("1", 15, -panelSize / 2 + 25 + padding); // y = 1
            graphicsBuffer.drawString("-1", 15, panelSize / 2 - 25 - padding); // y = -1
            graphicsBuffer.drawString("1", panelSize / 2 - 25 - padding, 15); // x = 1
            graphicsBuffer.drawString("-1", -panelSize / 2 + padding + 25, 15); // x = -1
            graphicsBuffer.drawString("0", -15, 15); // 0, 0

            // arrows
            graphicsBuffer.fillPolygon(new int[]{0, 7, -7, 0}, // top
                    new int[]{-panelSize / 2 + padding, -panelSize / 2 + padding + 15, -panelSize / 2 + padding + 15, -panelSize / 2 + padding}, 4);
            graphicsBuffer.fillPolygon(new int[]{0, 7, -7, 0}, // bottom
                    new int[]{panelSize / 2 - padding, panelSize / 2 - padding - 15, panelSize / 2 - padding - 15, panelSize / 2 - padding}, 4);
            graphicsBuffer.fillPolygon(new int[]{-panelSize / 2 + padding, -panelSize / 2 + padding + 15, -panelSize / 2 + padding + 15, -panelSize / 2 + padding},
                    new int[]{0, -7, 7, 0}, 4); // left
            graphicsBuffer.fillPolygon(new int[]{panelSize / 2 - padding, panelSize / 2 - padding - 15, panelSize / 2 - padding - 15, panelSize / 2 - padding},
                    new int[]{0, -7, 7, 0}, 4);  // right          

         
            for (Point point : dataSetPoints) {
                Point tempPoint = new Point();
                tempPoint.x = point.x - (panelSize / 2 - padding);
                tempPoint.y = point.y - (panelSize / 2 - padding);
                tempPoint.output = point.output;

                drawPoint(tempPoint, graphicsBuffer);
            }

            //draws help line
            if (helpX != -1000 && -panelSize / 2 <= helpX && helpX <= panelSize && helpY != -1000 && -panelSize / 2 <= helpY && helpY <= panelSize) {
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
        if (positiveInputsOnly) {//transforms for positive inputs only
            double X = (double) x;
            xx = (double) X / (double) (panelSize - padding);
            if (xx > 1) {
                xx = 1;
            }
            if (xx < 0) {
                xx = 0;
            }
        } else {
            double X = (double) x;
            xx = X / (panelSize / 2 - padding) - 1;
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
    double transformFromPanelToDecartY(int panelY) {
        double yy;
        if (positiveInputsOnly) {//transforms for positive inputs only
            double Y = (double) panelY;
            yy = 1 - Y / (double) (panelSize - padding);
            if (yy > 1) {
                yy = 1;
            }
            if (yy < 0) {
                yy = 0;
            }
        } else {
            double Y = (double) panelY;
            yy = 1 - Y / (panelSize / 2 - padding);
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
    
     * @param x x value/coord in decart system
     * @return Returns x pixel coord in this panel
     */
    public int decartToPanelX(double x) {
        if (positiveInputsOnly) {
            double valueX = x * (double) (panelSize - padding);
            return (int) valueX;
        } else {
            return (int) ((1 + x) * ((panelSize - 2 * padding) / 2)) + padding;
            //   return (int) ( x/((panelSize / 2 - padding)/1000));
        }
    }

    /*
     * Transforms Decartes' y value to panel's value
     */
    public int decartToPanelY(double y) {
        if (positiveInputsOnly) {
            double valueY = (1.0 - y) * (double) (panelSize - padding);
            return (int) valueY;
        } else {
            return (int) ((1 - y) * (panelSize / 2 - padding));
        }
    }

    /*
     * Draws input points, added by clicking on the panel
     */
    public void drawPoint(Point point, Graphics g) { // TODO: graphics treba izbaciti i uvek crtati u buffer
        if (point.output == 1) { // what if we have more classes ? TODO: fix this case:create an array of colours to be used along with legend
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }

        g.fillArc(point.x - 3, point.y - 3, 7, 7, 0, 360);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(java.awt.Color.white);
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
            .addGap(0, 777, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 501, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved

        int X = evt.getX();
        int Y = evt.getY();
        if (positiveInputsOnly) {
            if (0 <= X && X <= (panelSize - padding) && 0 <= Y && Y <= (panelSize - padding)) {
                helpX = X;
                helpY = Y;
            } else {
                helpX = -1000;
                helpY = -1000;
            }
        } else if ((-panelSize / 2 - padding) <= X && X <= (panelSize - padding) && (-panelSize / 2 - padding) <= Y && Y <= (panelSize - padding)) {
            helpX = X;
            helpY = Y;
        } else {
            helpX = -1000;
            helpY = -1000;
        }
        repaint();
    }//GEN-LAST:event_formMouseMoved

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if (!drawingPointsDisabled) {
            setPointDrawn(true);
            int button_value;
            if (value == 1) {
                if (evt.getButton() == 3) {
                    button_value = 0;
                } else {
                    button_value = 1;
                }
                int X = evt.getX();
                int Y = evt.getY();

                Point point = new Point(X, Y, button_value);

                dataSetPoints.add(point);
                if (dataSet == null) {
                    dataSet = new DataSet(2, 1);// if its first drawn point - todo: this must be changed!!!
                }
                dataSet.addRow(new DataSetRow(new double[]{transformFromPanelToDecartX(X), transformFromPanelToDecartY(Y)}, new double[]{button_value}));
                Graphics g = getGraphics();
                drawPoint(point, g); // samo ovdecrtam u glavni g jer treba da bude interaktivno
            }
        }
    }//GEN-LAST:event_formMousePressed

    @Override
    public void componentResized(ComponentEvent e) {
        reGeneratePoints();
        repaint();
        initImageBuffer(); // resize the drawing buffer too
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    private class Point {

        public int x, y, output;

        public Point() {
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(int x, int y, int output) {
            this.x = x;
            this.y = y;
            this.output = output;
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
