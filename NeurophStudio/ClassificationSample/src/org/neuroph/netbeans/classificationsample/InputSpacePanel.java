/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */

package org.neuroph.netbeans.classificationsample;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
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
 */
public class InputSpacePanel extends javax.swing.JPanel {

    NeuralNetwork neuralNetwork;
    
    Graphics graphicsBuffer;
    Image imageBuffer;
    double gridPoints [][]; // tacke za testiranje mreze prilikom iscrtavanja

    Vector points; // ulazne tacke koje je user iskliktao na panelu
    DataSet trainingSet;
    int value; // indikTOR koje je dugme misa kliknuto - NACI NEGO DRUGO RESENJE
   
    int helpX = -1000; // pomocne linije koje pokazuju koordinate tacke
    int helpY = -1000;
    private boolean visualize = true;
    private boolean positive = false;
    private int visualizationOption;
    private boolean drawingLocked = false;
    
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
    
    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }
    
    public boolean isVisualize() {
        return visualize;
    }

    public void setVisualize(boolean visualize) {
        this.visualize = visualize;
    }
    
    
    
    public static Color[] neuronColor;
    public static Color[] neuronColorInverted;

    public void setNeuronColors(NeuralNetwork neuralNetwork, boolean secondLayer) {
        int numberOfColors;
        if (secondLayer) {
            numberOfColors = neuralNetwork.getLayerAt(1).getNeurons().length - 1;
        } else {
            numberOfColors = neuralNetwork.getLayerAt(neuralNetwork.getLayersCount() - 2).getNeurons().length - 1;
        }

        InputSpacePanel.neuronColor = new Color[numberOfColors];
        InputSpacePanel.neuronColorInverted = new Color[numberOfColors];

        Random random = new Random();
        for (int i = 0; i < numberOfColors; i++) {
            float r = random.nextFloat();
            float g = random.nextFloat();
            float b = random.nextFloat();

            neuronColor[i] = new Color(r, g, b);
            neuronColorInverted[i] = new Color(1 - r, 1 - g, 1 - b);
        }
    }

    /** Creates new form InputSpacePanel */
    public InputSpacePanel() {
        setSize(570, 570);
        setInitialGridPoints();
        points = new Vector();
        value = 1;
        trainingSet = new DataSet(2, 1);
        initComponents();
    }
    
    private boolean pointDrawed = false;

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
   

    public Rectangle getRect()
    {
        Rectangle r = new Rectangle(this.getWidth(), this.getHeight());
        r.width = r.height;
        return r;
    }

    void setInitialGridPoints()
    {
        int size;
        if(isPositive()){
            size = 50;
        }else{
            size = 57;
        }
        gridPoints = new double[size][size];
        for (int i = 0; i<size;i++)
            for(int j =0;j<size;j++)
            {
                gridPoints[i][j]= -100;
            }
    }
    
    public void setNeuralNetwork(NeuralNetwork neuralNetwork){
        this.neuralNetwork = neuralNetwork;       
    }
      
    public double calculateY(double scalingCoefficient, int x, double w0, double w1, double w2) {
        return scalingCoefficient - scalingCoefficient * (-w1 * x / scalingCoefficient - w0) / w2;
    }
    
    public Double[] calculateCoefficients(Integer[] coords){
        //0-x1,  1-y1,  2-x2,  3-y2
        double k = (coords[3]-coords[1])/(coords[2]-coords[0]);
        double n = -coords[0]*k + coords[1];
        return new Double[]{k,n};
    }
    
    public Integer[] calculateLineCoordinates(double w0, double w1, double w2) {
        int x1, x2, y1, y2;

        if(isPositive()){
            x1 = 0;
            y1 = (int) calculateY(500, x1, w0, w1, w2);
            
            x2 = 500;                      
            y2 = (int) calculateY(500, x2, w0, w1, w2);
        }else{
            x1 = -570/2;
            y1 = (int) calculateY(570/2, x1, w0, w1, w2)-570/2;
            
            x2 = 570/2;                      
            y2 = (int) calculateY(570/2, x2, w0, w1, w2)-570/2;
        }
            
        

        return new Integer[]{x1, y1, x2, y2};
    }
    
    public void visualizeLines2D() {

        graphicsBuffer.setColor(Color.black);
        if (neuralNetwork != null) {
            Neuron[] neurons;
            int numberOfNeurons;
            neurons = neuralNetwork.getLayerAt(1).getNeurons();//second layer neurons
            numberOfNeurons = neurons.length - 1;
            for (int i = 0; i < numberOfNeurons; i++) {//iterates through layer neurons
                Neuron neuron = neurons[i];
                Connection[] conn;
                conn = neuron.getInputConnections();//for each neuron, fetch input connections
                /*
                 * for each connection, fetch weight value
                 */
                double w1 = conn[0].getWeight().getValue();
                double w2 = conn[1].getWeight().getValue();
                double w3 = conn[2].getWeight().getValue();

                Integer[] coordinates = calculateLineCoordinates(w3, w1, w2);
                graphicsBuffer.drawLine(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);

            }
        }

    }

    /*
     * This method enables training process visualization,
     * where every neuron is represented as a linear function,
     * that separates 2D space with 2 colors.
     */
    public void visualizeColoredAreas2D() {

        
        if (neuralNetwork != null) {
            Neuron[] neurons;
            int numberOfNeurons;
            neurons = neuralNetwork.getLayerAt(1).getNeurons();//second layer neurons
            numberOfNeurons = neurons.length - 1;           
            int x, y, size, startIndex, offset, scalingCoefficient;        
            if(isPositive()){
                size = 500;
                startIndex = 0;
                offset = 0;
                scalingCoefficient = size;
            }else{
                size = 570;
                startIndex = -size/2; 
                offset = 570/2;
                scalingCoefficient = 570/2;
            }
            for (int m = startIndex; m < size; m++) {//500x500 matrix is created 
                for (int n = startIndex; n < size; n++) {
                    x = m;//x coordinate 10
                    y = n;//y coordinate
                    double rgb = 0;//rgb color, needed for color fusion
                    double irgb = 0;//irgb color, needed for color fusion
                    int rgbCounter = 0;//number of rgb counted neurons
                    int irgbCounter = 0;//number of irgb counted neurons
                    for (int i = 0; i < numberOfNeurons; i++) {//iterates through layer neurons
                        Neuron neuron = neurons[i];
                        Connection[] conn;
                        conn = neuron.getInputConnections();//for each neuron, fetch input connections
                        /*
                         * for each connection, fetch weight value
                         */
                        double w1 = conn[0].getWeight().getValue();
                        double w2 = conn[1].getWeight().getValue();
                        double w3 = conn[2].getWeight().getValue();  

                        if (calculateY(scalingCoefficient, x, w3, w1, w2) > y + offset) {
                            rgb += InputSpacePanel.neuronColor[i].getRGB();
                            rgbCounter++;
                        } else {
                            irgb += InputSpacePanel.neuronColorInverted[i].getRGB();
                            irgbCounter++;                        
                        }            
                    }
                    int rgbColor = (int) Math.round((Math.round(rgb / rgbCounter) + Math.round(irgb / irgbCounter)) / 2);
                    graphicsBuffer.setColor(new Color(rgbColor));
                    if (isPositive()) {
                        graphicsBuffer.fillRect(x, y, 2, 2);  //10    
                    } else {
                        graphicsBuffer.fillRect(x , y, 2, 2);  
                    }
 
                }
            }
        }
    }
    
     public void visualizeNetworkAnswer2D() {
        int x, y;      
        int size;
        if(isPositive()){
            size = 50;
        }else{
            size = 57;
        }
        //draws grid 
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                x = i * 10;
                y = j * 10;
                if (gridPoints[i][j] != -100) {
                    float r = (float) gridPoints[i][j];
                    float b = 1 - r;
                    graphicsBuffer.setColor(new Color(r, 0, b));
                    if(isPositive()){
                        graphicsBuffer.fillRect(x, y, 10, 10);
                    }else{                       
                        graphicsBuffer.fillRect(x-570/2, y-570/2, 10, 10);
                    }
                   
                    if (gridPoints[i][j] > 0.5) {
                        graphicsBuffer.setColor(Color.RED);
                    } else {
                        graphicsBuffer.setColor(Color.BLUE);
                    }
                    if(isPositive()){
                        graphicsBuffer.fillRect(x + 3, y + 3, 3, 3);
                    } else {
                        graphicsBuffer.fillRect(x - 570 / 2 + 3, y - 570 / 2 + 3, 3, 3);//x-570/2 + 3, y-570/2 + 3, 3, 3
                    }

                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        if(imageBuffer==null)
        {
            imageBuffer = createImage(this.getWidth(), this.getHeight());
            graphicsBuffer = imageBuffer.getGraphics();
           
            if(positive){
                graphicsBuffer.translate(20, 50);
            }else{
                graphicsBuffer.translate(570/2, 570/2);
            } 
        }

        Rectangle rec = getRect();
        graphicsBuffer.setColor(Color.WHITE);
        if(positive){
            graphicsBuffer.fillRect(0, 0, rec.width, rec.height);
        }else{
            graphicsBuffer.fillRect(-570/2, -570/2, 570, 570);
        }

        if(visualize)
        switch (getVisualizationOption()) {
            case 1:
                visualizeNetworkAnswer2D();
                repaint();
                break;

            case 2:
                visualizeColoredAreas2D();
                repaint();
                break;

            case 3:               
                visualizeLines2D();
                repaint();
                break;
        }
           
        if(positive){
            
            graphicsBuffer.setColor(Color.WHITE);
            graphicsBuffer.fillRect(-20, -50, 570, 50);
            graphicsBuffer.fillRect(-20, 0, 20, 520);
            graphicsBuffer.fillRect(500, 0, 50, 500);
            graphicsBuffer.fillRect(0, 500, 550, 20);
            
            graphicsBuffer.setColor(Color.BLACK);
            
            //draws x and y lines
            graphicsBuffer.drawLine(0, 510, 0, -50);       
            graphicsBuffer.drawLine(-10, 500, 550, 500);
            
            //draws arrows
//            graphicsBuffer.drawLine(0, -50, -3, -43);
//            graphicsBuffer.drawLine(0, -50, 3, -43);
//            graphicsBuffer.drawLine(550, 500, 543, 497);
//            graphicsBuffer.drawLine(550, 500, 543, 503);
            
            graphicsBuffer.setColor(Color.lightGray);
            
            graphicsBuffer.drawLine(0, 0, 550, 0);
            graphicsBuffer.drawLine(500, 500, 500, -50);
            
            graphicsBuffer.setColor(Color.BLACK);
            
//            graphicsBuffer.drawString("x1", 525, 515);
//            graphicsBuffer.drawString("x2", -15, -25);
            graphicsBuffer.drawLine(-5, 0, 5, 0);
            graphicsBuffer.drawLine(500, 505, 500, 495);
            
            graphicsBuffer.drawString("1", -15, 5);
            graphicsBuffer.drawString("1", 497, 518);
            graphicsBuffer.drawString("0", -10, 513);
            
            //draws input points
            Enumeration e = points.elements();
            while (e.hasMoreElements()) {
                int[] point = (int[]) e.nextElement();
                drawPoint(point[0],point[1]-20,point[2]-50,graphicsBuffer);             
            }

            //draws help line
            if (helpX!=-100 && 20<=helpX && helpX<=520 && helpY!=-100 && 50<=helpY && helpX<=550)
            {
                drawHelpLine(helpX, helpY, graphicsBuffer);
            }

            g.drawImage(imageBuffer, 0, 0, this);

        }
        else{
            graphicsBuffer.setColor(Color.BLACK);

            graphicsBuffer.drawLine(-570 / 2, 0, 570 / 2, 0);
            graphicsBuffer.drawLine(0, -570 / 2, 0, 570 / 2);

            graphicsBuffer.drawString("1", 5, -570/2 + 10);
            graphicsBuffer.drawString("-1", 5, 570/2 - 5);
            
            
            graphicsBuffer.drawString("1", 570/2 - 10, 15);
            graphicsBuffer.drawString("-1", -570/2 + 5, 15);
            
            graphicsBuffer.drawString("0", -10, 15);
            //draws input points
            Enumeration e = points.elements();
            while (e.hasMoreElements()) {              
                int[] point = (int[]) e.nextElement();
                drawPoint(point[0], point[1] - 570 / 2, point[2] - 570 / 2, graphicsBuffer);
            }

            //draws help line
            if (helpX != -1000 && -570 / 2 <= helpX && helpX <= 570 && helpY != -1000 && -570 / 2 <= helpY && helpY <= 570)
            {
                drawHelpLine(helpX, helpY, graphicsBuffer);
            }

            g.drawImage(imageBuffer, 0, 0, this);

        }
    }

    /*
     * Transforms panel's value to Decartes x value
     */
    double transformFromPanelToDecartX(int x) {
        double xx;

        if (positive) {
            xx = (double) (x - 20) / 500;
            if (xx > 1) {
                xx = 1;
            }
            if (xx < 0) {
                xx = 0;
            }

        } else {
            double X = (double) x;

            xx = X / (570 / 2) - 1;

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
     * Transforms panel's value to Decartes y value
     */
    double transformFromPanelToDecartY(int y) {
        double yy;
        if (positive) {
            yy = (double) (550 - y) / 500;
            if (yy > 1) {
                yy = 1;
            }
            if (yy < 0) {
                yy = 0;
            }
        } else {
            double Y = (double) y;
            yy = 1 - Y / (570 / 2);
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
     * Transforms Decart's x value to panel's value
     */
    public int transformFromDecartToPanelX(double x) {

        if (positive) {
            return (int) ((x * 500) + 20);
        } else {
            return (int) ((1 + x) * 570 / 2);
        }
    }
    
    /*
     * Transforms Decart's y value to panel's value
     */
    public int transformFromDecartToPanelY(double y) {
        if (positive) {
            return (int) (550 - (y * 500));
        } else {
            return (int) ((1 - y) * 570 / 2);
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

    /*
     * Erases all points
     */
    public void clearPoints()
    {       
        visualize=false;
        setAllPointsRemoved(true);
        points.removeAllElements();
        trainingSet.clear();
        setInitialGridPoints();
        repaint();
    }
    
    public void prepareDrawing(){
        setInitialGridPoints();
        repaint();
    }

     public void setValue(int v)
     {
       value = v;
     }

     /*
      * Sets values for network drawing
      */
    public void setGridPoints(int x1, int x2, double v) {
        int x = x1;
        int y = x2;
        int size;
        if(isPositive()){
            size = 50;
        }else{
            size = 57;
        }
        if (v != -100) {
            if (1.0 < v) {
                v = 1.0;
            }
            if (0 > v) {
                v = 0.0;
            }
        }
        gridPoints[x][y] = v;
        if (x == size-1 && y == size-1) {
            repaint();
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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

        if (positive) {
            if (20 <= X && X <= 520 && 50 <= Y && Y <= 550) {
                helpX = X;
                helpY = Y;
            } else {
                helpX = -1000;//-100
                helpY = -1000;//-100
            }
        } else {
            if (-570 / 2 <= X && X <= 570 && -570 / 2 <= Y && Y <= 570) {
                helpX = X;
                helpY = Y;
            } else {
                helpX = -1000;//-100
                helpY = -1000;//-100
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



    public DataSet getTrain()
    {
        return trainingSet;
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /*
     * Draws current mouse coordinates on the panel 
     */
    public void drawHelpLine(int X, int Y, Graphics g){
        double xVal = transformFromPanelToDecartX(X);
        double yVal = transformFromPanelToDecartY(Y);
        
        g.setColor(Color.lightGray);
        
        if (positive) {
            g.drawLine(X - 20, Y - 50, 0, Y - 50);
            g.drawLine(X - 20, Y - 50, X - 20, 500);
            g.setColor(Color.RED);
            g.drawString("(" + xVal + " , " + yVal + ")", X - 10, Y - 60);

        } else {
            g.drawLine(X - 570 / 2, Y - 570 / 2, 0, Y - 570 / 2);
            g.drawLine(X - 570 / 2, Y - 570 / 2, X - 570 / 2, 500);
            g.setColor(Color.RED);
            g.drawString("(" + xVal + " , " + yVal + ")", X - 570 / 2 - 10, Y - 570 / 2 - 10);
        }

    }

    /*
     * Dran-and-Drop add-on
     */
    public void drawPointsFromTrainingSet(DataSet inputTrainingSet)
    {
        points.clear();
        repaint();
        for (Iterator<DataSetRow> it = inputTrainingSet.iterator(); it.hasNext();) {
            DataSetRow outsideElement = it.next();
            double outsideX = outsideElement.getInput()[0];
            double outsideY = outsideElement.getInput()[1];

            double outsideOutput = outsideElement.getDesiredOutput()[0];
            int outsideValue = 0;
            if(outsideOutput > 0.5) outsideValue = 1;

            int outsideInputValueX = transformFromDecartToPanelX(outsideX);
            int outsideInputValueY = transformFromDecartToPanelY(outsideY);
            
            int[] point = new int[3];
            point[0]=outsideValue;
            point[1]=outsideInputValueX;
            point[2]=outsideInputValueY;

            points.add(point);

            Graphics g = getGraphics();
            drawPoint(outsideValue, outsideInputValueX, outsideInputValueY, g);
            
        }
    }

   
   

    

   

}
