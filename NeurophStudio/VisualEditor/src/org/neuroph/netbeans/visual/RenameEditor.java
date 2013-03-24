/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.neuroph.netbeans.visual;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;

/**
 *
 * @author Damir
 */
public class RenameEditor implements TextFieldInplaceEditor {

        @Override
        public boolean isEnabled(Widget widget) {
            return true;
        }

        @Override
        public String getText(Widget widget) {
            return ((IconNodeWidget) widget).getLabelWidget().getLabel();
        }

        @Override
        public void setText(Widget widget, String text) {
            ((IconNodeWidget) widget).setLabel(text);
        }
    }
