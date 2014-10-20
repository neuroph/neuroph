/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.jmevisualization;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import org.nugs.graph3d.api.Range;

/**
 *
 * @author Milos Randjic
 */
public class CoordinateSystem extends Mesh {
    
    private Range xRange;
    private Range yRange;
    private Range zRange;
    
    public Range getxRange() {
        return xRange;
    }

    public void setxRange(Range xRange) {
        this.xRange = xRange;
    }

    public Range getyRange() {
        return yRange;
    }

    public void setyRange(Range yRange) {
        this.yRange = yRange;
    }

    public Range getzRange() {
        return zRange;
    }

    public void setzRange(Range zRange) {
        this.zRange = zRange;
    }

    float range;

    public CoordinateSystem(float range) {
        this.xRange = new Range(-range*100, range*100);
        this.yRange = new Range(-range*100, range*100);
        this.zRange = new Range(-range*100, range*100);
        this.range = range*100;
        setMode(Mode.Lines);
        
    }

    public CoordinateSystem(Range xRange, Range yRange, Range zRange) {
        this.xRange = xRange;
        this.yRange = yRange;
        this.zRange = zRange;
    }
    
    

    public Geometry generatePlanes(int step) {

        int planeSize = (int) (24 * range);
        Vector3f[] planeVector = new Vector3f[planeSize];

        int index = 0;

        float x = (float) xRange.getMin();//-range;
        float y = (float) yRange.getMin();//-range;
        float z = (float) zRange.getMin();//-range;
        

        while (index < 4 * range && z <= zRange.getMax()) {

            planeVector[index] = new Vector3f((float) xRange.getMin(), 0, z);//-range, 0, z
            planeVector[++index] = new Vector3f((float) xRange.getMax(), 0, z);//range, 0, z
            index++;
            z += step;
        }

        while (index < 8 * range && x <= xRange.getMax()) {

            planeVector[index] = new Vector3f(x, 0, (float) zRange.getMin());//x, 0, -range
            planeVector[++index] = new Vector3f(x, 0, (float) zRange.getMax());//x, 0, range
            index++;
            x += step;
        }

        x = (float) xRange.getMin();
        z = (float) zRange.getMin();


        while (index < 12 * range && y <= yRange.getMax()) {

            planeVector[index] = new Vector3f((float) xRange.getMin(), y, 0);//-range, y, 0
            planeVector[++index] = new Vector3f((float) xRange.getMax(), y, 0);//range, y, 0
            index++;
            y += step;
        }

        while (index < 16 * range && x <= xRange.getMax()) {

            planeVector[index] = new Vector3f(x, (float) yRange.getMin(), 0);//x, -range, 0
            planeVector[++index] = new Vector3f(x, (float) yRange.getMax(), 0);//x, range, 0
            index++;
            x += step;
        }

        y = -range;

        while (index < 20 * range && y <= yRange.getMax()) {

            planeVector[index] = new Vector3f(0, y, (float) zRange.getMin());//0, y, -range
            planeVector[++index] = new Vector3f(0, y, (float) zRange.getMax());//0, y, range
            index++;
            y += step;
        }

        while (index < 24 * range && z <= zRange.getMax()) {

            planeVector[index] = new Vector3f(0, (float) yRange.getMin(), z);//0, -range, z
            planeVector[++index] = new Vector3f(0, (float) yRange.getMax(), z);//0, range, z
            index++;
            z += step;
        }
        
        
        setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(planeVector));

        short[] connections = new short[planeSize];

        for (int i = 0; i < connections.length; i++) {
            connections[i] = (short) i;

        }
        
        setBuffer(VertexBuffer.Type.Index, 2, BufferUtils.createShortBuffer(connections));
        updateBound();

        return new Geometry("CoordinateSystem", this);
    }

    
}
