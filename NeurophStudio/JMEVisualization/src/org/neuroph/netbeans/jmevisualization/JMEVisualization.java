/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.jmevisualization.charts.JMEVisualizationTopComponent;

/**
 * JME based Visualization Component
 * 
 * @author Milos Randjic
 * @author Zoran Sevarac
 */
public class JMEVisualization extends SimpleApplication {

    private int width;
    private int height;
    private static JPanel visualizationPanel;
    
    
    
    
    @Override
    public void simpleInitApp() {   
        
        flyCam.setDragToRotate(true);
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(50);
        flyCam.setZoomSpeed(10);     
        
        CoordinateSystem coordinateSys = new CoordinateSystem(1);
        Geometry coordinateSystem = coordinateSys.generatePlanes(10);
        coordinateSystem.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
        rootNode.attachChild(coordinateSystem); // rootNode from  SimpleApplication
        
    }
    
    private JmeCanvasContext jmeCanvasContext;

 
 
    public JmeCanvasContext getJmeCanvasContext() {
        return jmeCanvasContext;
    }

    public void setJmeCanvasContext(JmeCanvasContext jmeCanvasContext) {
        this.jmeCanvasContext = jmeCanvasContext;
    }
    
//    private static JMEVisualization instance;
//    
//    public static JMEVisualization getInstance() {
//        if (instance==null)
//            instance = new JMEVisualization();
//        return instance;
//    }
           
    // ovde se na top component stavlja jme canvas koji crta grafike
    public void startApplication() {
        AppSettings settings = new AppSettings(true);
        settings.setWidth(getWidth());
        settings.setHeight(getHeight());

        setSettings(settings);
        createCanvas();

        jmeCanvasContext = (JmeCanvasContext) getContext();
        jmeCanvasContext.setSystemListener(this);
        jmeCanvasContext.getCanvas().setPreferredSize(new Dimension(getWidth(), getHeight()));

        this.startCanvas();
    }
    
    public void addGeometry(Geometry geometry) {
        rootNode.attachChild(geometry);
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
   
    public static JPanel getVisualizationPanel() {
        return visualizationPanel;
    }

    public static void setVisualizationPanel(JPanel visualizationPanel) {
        JMEVisualization.visualizationPanel = visualizationPanel;
    }

}
