/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.control.UpdateControl;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Dimension;
import java.util.concurrent.Callable;

/**
 * JME based Visualization Component
 *
 * @author Milos Randjic
 * @author Zoran Sevarac
 */
public class JMEVisualization extends SimpleApplication {

    private JmeCanvasContext jmeCanvasContext;
    private int width;
    private int height; 
    private boolean rotated;

    @Override
    public void simpleInitApp() {

        rootNode.addControl(new UpdateControl());
        flyCam.setDragToRotate(true);
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(200);
        flyCam.setZoomSpeed(10);   

    }

    public JmeCanvasContext getJmeCanvasContext() {
        return jmeCanvasContext;
    }

    public void setJmeCanvasContext(JmeCanvasContext jmeCanvasContext) {
        this.jmeCanvasContext = jmeCanvasContext;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int aWidth) {
        width = aWidth;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int aHeight) {
        height = aHeight;
    }
    
    public boolean isRotated() {
        return rotated;
    }

    public void setRotated(boolean rotated) {
        this.rotated = rotated;
    }

    /*
     Starts the application
     Initial paramaters are set and canvas is shown on screen
     */
    public void startApplication() {
  
        AppSettings appSettings = new AppSettings(true);
        appSettings.setWidth(getWidth());
        appSettings.setHeight(getHeight());

        setSettings(appSettings);
        createCanvas();

        jmeCanvasContext = (JmeCanvasContext) getContext();
        jmeCanvasContext.setSystemListener(this);
        jmeCanvasContext.getCanvas().setPreferredSize(new Dimension(getWidth(), getHeight()));

        this.startCanvas();

    }

    /*
     Attach a child from another thread. Callable interface is required for this action
     Callable needs to be enqueued in order to attach properly
     */
    public void attachChild(final Geometry geometry) {
        rootNode.getControl(UpdateControl.class).enqueue(new Callable<Geometry>() {

            @Override
            public Geometry call() throws Exception {

                rootNode.attachChild(geometry);
                //getJmeCanvasContext().getCanvas().requestFocus();
                return null;

            }
        });
    }

    /*
     Datach all children from another thread. Callable interface is required for this action
     Callable needs to be enqueued in order to detach properly
     */
    public void detachAllChildren() {
        rootNode.getControl(UpdateControl.class).enqueue(new Callable<Geometry>() {

            @Override
            public Geometry call() throws Exception {

                rootNode.detachAllChildren();
                //getJmeCanvasContext().getCanvas().requestFocus();
                return null;

            }
        });
    }

    /*
     Attach a coordinate system with specified range and gridDensity, from another thread
     Callable interface is required for this action
     Callable needs to be enqueued in order to attach properly
     */
    public void attachCoordinateSystem(final int range, final int gridDensity) {
        rootNode.getControl(UpdateControl.class).enqueue(new Callable<Geometry>() {

            @Override
            public Geometry call() throws Exception {

                CoordinateSystem coordinateSys = new CoordinateSystem(range);
                Geometry coordinateSystem = coordinateSys.generatePlanes(gridDensity);
                coordinateSystem.setMaterial(new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md"));

                /*
                 Create coordinate axis arrows, for better orientation
                 */
                Arrow xArrow = new Arrow(new Vector3f(Vector3f.UNIT_X.x * 220, Vector3f.UNIT_X.y * 220, Vector3f.UNIT_X.z * 220));
                Arrow yArrow = new Arrow(new Vector3f(Vector3f.UNIT_Y.x * 220, Vector3f.UNIT_Y.y * 220, Vector3f.UNIT_Y.z * 220));
                Arrow zArrow = new Arrow(new Vector3f(Vector3f.UNIT_Z.x * 220, Vector3f.UNIT_Z.y * 220, Vector3f.UNIT_Z.z * 220));

                /*
                 Set line width
                 */
                xArrow.setLineWidth(4f);
                yArrow.setLineWidth(4f);
                zArrow.setLineWidth(4f);

                /*
                 Create geometries from those arrows
                 */
                Geometry xArrowG = new Geometry("xArrow", xArrow);
                Geometry yArrowG = new Geometry("yArrow", yArrow);
                Geometry zArrowG = new Geometry("zArrow", zArrow);
                
                /*
                Move geometries to appropriate location
                */
                xArrowG.move(-110, 0, 0);
                yArrowG.move(0,-110,0);
                zArrowG.move(0, 0, -110);

                /*
                 Create material and add appropriate color for each axis arrow
                 */
                Material xMaterial = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                xMaterial.setColor("Color", ColorRGBA.Red);

                Material yMaterial = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                yMaterial.setColor("Color", ColorRGBA.Green);

                Material zMaterial = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                zMaterial.setColor("Color", ColorRGBA.Blue);

                /*
                 Set material for each arrow geometry
                 */
                xArrowG.setMaterial(xMaterial);
                yArrowG.setMaterial(yMaterial);
                zArrowG.setMaterial(zMaterial);

                /*
                 Attach arrrow geometry objects
                 */
                rootNode.attachChild(xArrowG);
                rootNode.attachChild(yArrowG);
                rootNode.attachChild(zArrowG);
                rootNode.attachChild(coordinateSystem);

                //getJmeCanvasContext().getCanvas().requestFocus();

                return null;
            }
        });
    }

    /*
     Attach a histogram grid with specified number of maxBars and number of rows, from another thread
     Callable interface is required for this action
     Callable needs to be enqueued in order to attach properly
     */
    public void attachHistoramGrid(final int maxBarsLength, final int numberOfBarRows) {
        rootNode.getControl(UpdateControl.class).enqueue(new Callable<Geometry>() {

            @Override
            public Geometry call() throws Exception {

                /*
                 Create and rotate plane geometry objects
                 */
                Geometry xPlane = (Geometry) new Geometry("xPlane", new Grid(maxBarsLength * (numberOfBarRows + 1) + 2, (numberOfBarRows) * 5 - 3, 5)).rotate(-1.57f, 0, 0);
                Geometry yPlane = (Geometry) new Geometry("yPlane", new Grid(maxBarsLength * (numberOfBarRows + 1) + 2, 40, 5)).rotate(-1.57f, -1.57f, 0);
                Geometry zPlane = (Geometry) new Geometry("zPlane", new Grid(40, (numberOfBarRows) * 5 - 3, 5)).rotate(0, 0, 0);

                /*
                 Create and set material for each plane
                 */
                Material m = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                xPlane.setMaterial(m);
                yPlane.setMaterial(m);
                zPlane.setMaterial(m);

                /*
                 Move y and z plane, in order to cover negative parts of coordinate system
                 */
                yPlane.move(0, 0, -100);
                zPlane.move(0, 0, -100);

                /*
                 Attach plane geometry objects
                 */
                rootNode.attachChild(xPlane);
                rootNode.attachChild(yPlane);
                rootNode.attachChild(zPlane);

                //getJmeCanvasContext().getCanvas().requestFocus();

                return null;
            }
        });

    }

}
