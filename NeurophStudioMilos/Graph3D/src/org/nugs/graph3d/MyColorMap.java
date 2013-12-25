package org.nugs.graph3d;

import org.nugs.graph2d.api.Attribute;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.IColorMappable;
import org.jzy3d.colors.colormaps.IColorMap;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

/**
 *
 * @author Vedrana Gajic
 */
public class MyColorMap implements IColorMap {

    boolean direction = true;
    HashMap colorsMap;
    Color[] colors = {Color.RED, Color.BLUE, new Color(java.awt.Color.ORANGE), Color.GREEN, Color.YELLOW, Color.CYAN, Color.GRAY, Color.MAGENTA, Color.BLACK, new Color(java.awt.Color.PINK)};
    private DataSet dataset;
    private List<Attribute> attributes;

    public MyColorMap(DataSet dataset, List<Attribute> attributes) {
        this.dataset = dataset;
        colorsMap = new HashMap();
        this.attributes = attributes;
        fillColors();
    }

    @Override
    public Color getColor(IColorMappable icm, float f, float f1, float f2) {
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

    @Override
    public Color getColor(IColorMappable icm, float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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
}
