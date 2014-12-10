package org.nugs.graph3d;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.IColorMappable;
import org.jzy3d.colors.colormaps.IColorMap;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.nugs.graph2d.api.Attribute;

/**
 *
 * @author Vedrana Gajic
 */
public class MyColorMap implements IColorMap {

    boolean direction = true;
    HashMap colorsMap;
    Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.GRAY, Color.MAGENTA, Color.BLACK};
    private DataSet dataset;
    private List<Attribute> attributes;

    public MyColorMap(DataSet dataset, List<Attribute> attributes) {
        this.dataset = dataset;
        colorsMap = new HashMap();
        this.attributes = attributes;
        fillColors();
    }

    @Override
    public Color getColor(IColorMappable icm, double f, double f1, double f2) {
        Color color;
        color = Color.BLUE;

        Attribute attr1 = attributes.get(0);
        Attribute attr2 = attributes.get(1);
        Attribute attr3 = attributes.get(2);

        for (DataSetRow row : dataset.getRows()) {

            double[] inputs = row.getInput();
            double[] outputs = row.getDesiredOutput();

            if (attr1.isOutput()) {
                if ((float) outputs[attr1.getIndex()] != f) {
                    continue;
                }
            } else if ((float) inputs[attr1.getIndex()] != f) {
                continue;

            }

            if (attr2.isOutput()) {
                if ((float) outputs[attr2.getIndex()] != f1) {
                    continue;
                }
            } else if ((float) inputs[attr2.getIndex()] != f1) {
                continue;

            }

            if (attr3.isOutput()) {
                if ((float) outputs[attr3.getIndex()] != f2) {
                    continue;
                }
            } else if ((float) inputs[attr3.getIndex()] != f2) {
                continue;

            }

            color = (Color) colorsMap.get(Arrays.toString(outputs));

        }

        return color;

    }
// error od jogl 2.0.2
//    @Override
//    public Color getColor(IColorMappable icm, float f) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    public void setDirection(boolean bln) {
        direction = bln;
    }

    @Override
    public boolean getDirection() {
        return direction;
    }

    private void fillColors() {
        int counter = 0;
        for (int i = 0; i < dataset.size(); i++) {
            double[] outputs = dataset.getRowAt(i).getDesiredOutput();
            String out = Arrays.toString(outputs);
            if (!colorsMap.containsKey(out)) {
                if (colors.length > counter) {
                    colorsMap.put(out, colors[counter]);
                    counter++;
                } else {
                    colorsMap.put(out, Color.random());
                }
            }

        }
    }

    @Override
    public Color getColor(IColorMappable icm, double d) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double colorComponentRelative(double d, double d1, double d2, double d3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double colorComponentAbsolute(double d, double d1, double d2, double d3, double d4) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
