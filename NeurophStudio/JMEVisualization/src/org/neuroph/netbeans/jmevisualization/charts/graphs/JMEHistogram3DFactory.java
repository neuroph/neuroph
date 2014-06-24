/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization.charts.graphs;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.beans.Beans;
import org.neuroph.netbeans.jmevisualization.JMEVisualization;
import org.nugs.graph3d.api.Histogram3DFactory;
import org.nugs.graph3d.api.Histogram3DProperties;
import org.nugs.graph3d.api.Point3D;

/**
 *
 * @author Milos Randjic
 */
public class JMEHistogram3DFactory implements Histogram3DFactory<Void, Point3D.Float>{
    
    private JMEVisualization jmeVisualization;    

    public JMEHistogram3DFactory(JMEVisualization jmeVisualization) {
        this.jmeVisualization = jmeVisualization;        
    }
    
    // pozovi ovo iz neke akcije da iscrtaj dataset
    @Override
    public Void createHistogram3D(Point3D.Float[] points, Histogram3DProperties prop) {
        
        Beans.setDesignTime(false);       
        Vector3f[] data = new Vector3f[points.length];     
        for (int i = 0; i < points.length; i++) {           
            data[i] = new Vector3f(points[i].getX(), points[i].getY(), points[i].getZ());              
        }
                   
        //wrong approach
        Box box = new Box(10,10,10);
        for (int i = 0; i < points.length; i++) {
            
            Geometry boxGeometry = new Geometry("box " + i, box);
            Material m = new Material(jmeVisualization.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
            m.setColor("Color",  ColorRGBA.Red);
            boxGeometry.setMaterial(m);
            boxGeometry.move(data[i]);         
            jmeVisualization.addGeometry(boxGeometry);
        }        
                         
        return null;
    }

    @Override
    public Void createHistogram3D(Point3D.Float[] points) {
        return createHistogram3D(points, new Histogram3DProperties());
    }

    
}
