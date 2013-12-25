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

package org.neuroph.netbeans.main.easyneurons.samples.mlperceptron;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Marko
 */
public class InputSpacePanel extends javax.swing.JPanel {

    Graphics graphicsBuffer;
    Image imageBuffer;
    double gridPoints [][]; // tacke za testiranje mreze prilikom iscrtavanja

    Vector points; // ulazne tacke koje je user iskliktao na panelu
    DataSet trainingSet;
    int value; // indikTOR koje je dugme misa kliknuto - NACI NEGO DRUGO RESENJE
   
    int helpX = -100; // pomocne linije koje pokazuju koordinate tacke
    int helpY = -100;


    /** Creates new form InputSpacePanel */
    public InputSpacePanel() {
        setSize(570, 570);
        setInitialGridPoints();
        points = new Vector();
        value = 1;
        trainingSet = new DataSet(2, 1);
        initComponents();
    }

    //
    public Rectangle getRect()
    {
        Rectangle r = new Rectangle(this.getWidth(), this.getHeight());
        r.width = r.height;
        return r;
    }

    void setInitialGridPoints()
    {
        gridPoints = new double[50][50];
        for (int i = 0; i<50;i++)
            for(int j =0;j<50;j++)
            {
                gridPoints[i][j]= -100;
            }
    }

    @Override
    protected void paintComponent(Graphics g) {

        if(imageBuffer==null)
        {
            imageBuffer = createImage(this.getWidth(), this.getHeight());
            graphicsBuffer = imageBuffer.getGraphics();
            graphicsBuffer.translate(20, 50);
       }



        graphicsBuffer.setColor(getBackground());
        graphicsBuffer.fillRect(0,0, this.getWidth(), this.getHeight());

        graphicsBuffer.setColor(getForeground());

        // ---------------------

        Rectangle rec = getRect();
        graphicsBuffer.setColor(Color.WHITE);
        graphicsBuffer.fillRect(0, 0, rec.width, rec.height);


        int h = (rec.height - 70)/50;
        int w = (rec.width - 70)/50;

        int v,x,y;

        // ovo crta grid points
        for(int i =0;i<50;i++){
            for(int j = 0;j<50;j++)
            {
                x = i*10;
                y = j*10;
                if(gridPoints[i][j]!=-100)
                {
                    //System.out.println("SYS SPACE JE["+i+"]["+j+"]:"+gridPoints[i][j]);
                    float r = (float) gridPoints[i][j];
                    float b = 1-r;
                    graphicsBuffer.setColor(new Color(r, 0, b));
                    graphicsBuffer.fillRect(x, y, 10, 10);
                    //g.setColor(Color.BLACK);
                    //g.drawRect(x, y, 10, 10);
                    if(gridPoints[i][j]>0.5){ graphicsBuffer.setColor(Color.RED);} //System.out.println(gridPoints[i][j]);}
                       else {graphicsBuffer.setColor(Color.BLUE); }//System.out.println("PLAVO");}
                    graphicsBuffer.fillRect(x+3, y+3, 3, 3);
                }
            }
        }
    //    System.out.println("****************************************************************************************************************");
        graphicsBuffer.setColor(Color.WHITE);
        graphicsBuffer.fillRect(-20, -50, 570, 50);
        graphicsBuffer.fillRect(-20, 0, 20, 520);
        graphicsBuffer.fillRect(500, 0, 50, 500);
        graphicsBuffer.fillRect(0, 500, 550, 20);

        graphicsBuffer.setColor(Color.BLACK);
        graphicsBuffer.drawLine(0, 510, 0, -50);       //x,y linije
        graphicsBuffer.drawLine(-10, 500, 550, 500);

        graphicsBuffer.drawLine(0, -50, -3, -43);   //strlice
        graphicsBuffer.drawLine(0, -50, 3, -43);
        graphicsBuffer.drawLine(550, 500, 543, 497);
        graphicsBuffer.drawLine(550, 500, 543, 503);

        graphicsBuffer.setColor(Color.lightGray);
        graphicsBuffer.drawLine(0, 0, 550, 0);         // pomocne linije
        graphicsBuffer.drawLine(500, 500, 500, -50);

        graphicsBuffer.setColor(Color.BLACK);
        graphicsBuffer.drawString("x1", 525, 515);     // oznake x1 i x2
        graphicsBuffer.drawString("x2", -15, -25);

        graphicsBuffer.drawLine(-5, 0, 5, 0);
        graphicsBuffer.drawLine(500, 505, 500, 495);

        graphicsBuffer.drawString("1", -15, 5);
        graphicsBuffer.drawString("1", 497, 518);
        graphicsBuffer.drawString("0", -10, 513);

        // ovo crta ulazne tacke
        Enumeration e = points.elements();
        while (e.hasMoreElements())
        {
		int[] point  = (int[]) e.nextElement();

		drawPoint(point[0],point[1]-20,point[2]-50,graphicsBuffer); //System.out.println("x: "+point[1]+" y: "+point[2]);
       }

        // pomocne linije koje pokazuju na kojoj se kordinati nalazi kurzor
        if(helpX!=-100 && 20<=helpX && helpX<=520 && helpY!=-100 && 50<=helpY && helpX<=550)
        {
            drawHelpLine(helpX, helpY, graphicsBuffer);
        }


        g.drawImage(imageBuffer, 0, 0, this);

}

//    @Override
//    public void update(Graphics g) {
//        if(imageBuffer==null)
//        {
//            imageBuffer = createImage(this.getWidth(), this.getHeight());
//        }
//
//        graphicsBuffer = imageBuffer.getGraphics();
//        graphicsBuffer.translate(20, 50);
//
//        graphicsBuffer.setColor(getBackground());
//        graphicsBuffer.fillRect(0,0, this.getWidth(), this.getHeight());
//
//        graphicsBuffer.setColor(getForeground());
//        paintComponent(graphicsBuffer);
//
//
//        g.drawImage(imageBuffer, -20, -50, this);
//
//
//    }

    double getRealX(int x)               // pomocne linije X koordinata
    {
        double xx = (double)(x-20)/500;   
        if(xx>1) xx=1;
        if(xx<0) xx=0;
        return xx;
    }

    double getRealY(int y)              // pomocne linije Y koordinata
    {
        double yy = (double)(550-y)/500;
        if(yy>1) yy=1;
        if(yy<0) yy=0;
        return yy;
    }

    public void drawPoint(int v,int x, int y, Graphics g)         //  crta ulazne tacke, koje je korisnika zadao kliktanjem
    {
        Rectangle r = getRect();

        if(v==1) g.setColor(Color.RED);
        else g.setColor(Color.BLUE);

          g.fillArc(x-3, y-3, 7, 7, 0, 360);

          g.setColor(new Color(0.5f, 0.5f, 0.5f));
          g.drawArc(x-3, y-3, 7, 7, 0, 360);
    }

     public void clearPoints()                    // brise sve tacke
     {
        points.removeAllElements();
        trainingSet.clear();
        setInitialGridPoints();
        repaint();
     }

     public void setValue(int v)
     {
       value = v;
     }

    public void setGridPoints(int x1, int x2, double v)         // postavlja vrednosti za iscrtavanje mreze,
    {
        int x = x1;
        int y = x2;
            if(v != -100)
            {
                if (1.0 < v) {
				v = 1.0;
			}
			//else if ((-1.0 < v) && (1.0 > v)) {
			//	v = (v + 1) / 2;
			//}
		if (0 > v) {
				v = 0.0;
			}
            }
       gridPoints[x][y]= v;
       if(x == 49 && y== 49)             // kada se sve tacke mreze odrede poziva se crtanje
       repaint();
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

        if(20<=X && X<=520 && 50<=Y && Y<=550){
        helpX = X; 
        helpY = Y;
        }
        else
        {
            helpX = -100;
            helpY = -100;
        }
        repaint();
     //   update(getGraphics());
    }//GEN-LAST:event_formMouseMoved

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        int button_value;

     if(value == 1){
        if(evt.getButton() == 3) button_value = 0;
        else button_value = 1;


        int X = evt.getX();  //System.out.println("X je:" +X);
        int Y = evt.getY();  //System.out.println("Y je:" +Y);

        int[] point = new int[3];
        point[0]=button_value;
        point[1]=X;
        point[2]=Y;

        points.add(point);
        trainingSet.addRow(new DataSetRow(new double[]{getRealX(X), getRealY(Y)}, new double[]{button_value}));

        Graphics g = getGraphics();
        drawPoint(button_value,X,Y, g);
     }
    }//GEN-LAST:event_formMousePressed



    public DataSet getTrain()            // vraca se traning set
    {
        return trainingSet;
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void drawHelpLine(int X,int Y, Graphics g)   //crtaju se pomocne linije sa koordinatama na kojima se mis nalazi
    {
        double xx = (double) X;
        double yy = (double) Y;

        double xVal=(xx-20)/500;// System.out.println(xVal);
        double yVal=1-(yy-50)/500;// System.out.println(yVal);

        g.setColor(Color.lightGray);

        g.drawLine(X-20, Y-50, 0, Y-50);
        g.drawLine(X-20, Y-50, X-20, 500);

        g.setColor(Color.RED);
        g.drawString("("+xVal+" , "+yVal+")", X-10, Y-60);
        
    }

   //DnD dodaci
// DragNDrop - start
    public void drawPointsFromTrainingSet(DataSet inputTrainingSet)
    {
        points.clear();
        for (Iterator<DataSetRow> it = inputTrainingSet.iterator(); it.hasNext();) {
            DataSetRow outsideElement = it.next();
            double outsideX = outsideElement.getInput()[0];
            double outsideY = outsideElement.getInput()[1];

            double outsideOutput = outsideElement.getDesiredOutput()[0];
            int outsideValue = 0;
            if(outsideOutput > 0.5) outsideValue = 1;

            int outsideInputValueX = (int)((outsideX*500)+20);
            int outsideInputValueY = (int)(550-(outsideY*500));

            int[] point = new int[3];
            point[0]=outsideValue;
            point[1]=outsideInputValueX;
            point[2]=outsideInputValueY;

            points.add(point);


            Graphics g = getGraphics();
            drawPoint(outsideValue, outsideInputValueX, outsideInputValueY, g);
        }
    }
// DragNDrop - end

}
