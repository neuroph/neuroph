/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.neuroph.netbeans.visual.widgets;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.neuroph.core.Neuron;
import org.neuroph.core.learning.LearningRule;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author zoran
 */
public class LearningRuleWidget extends IconNodeWidget implements Lookup.Provider {
    LearningRule learningRule;
    private final Lookup lookup;
    
    private static final Border DEFAULT_BORDER = BorderFactory.createRoundedBorder(5, 5, Color.white, Color.BLACK);
    private static final Border HOVER_BORDER = BorderFactory.createRoundedBorder(5, 5, new Color(240, 240, 240), Color.GRAY);
    private static final Border SELECTED_BORDER = BorderFactory.createRoundedBorder(5, 5, new Color(240, 240, 250), Color.black);
        
    
    
    public LearningRuleWidget(NeuralNetworkScene scene, LearningRule learningRule) {
        super(scene);
        this.learningRule = learningRule;
        lookup = Lookups.fixed(learningRule, this);
        
        setBorder(DEFAULT_BORDER);
//        setMinimumSize(new Dimension(100, 40));
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(scene.createObjectHoverAction()); 
        setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.LEFT_TOP, 15));        
    }
    
    @Override
    public Lookup getLookup() {
        return lookup;
    }    
    
    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        super.notifyStateChanged(previousState, state);

        if (state.isSelected()) {
            setBorder(SELECTED_BORDER);
        } else {
            if (state.isHovered()) {
                setBorder(HOVER_BORDER);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                setBorder(DEFAULT_BORDER);
            }
        }
    }    
    
    
}
