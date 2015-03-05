package org.neuroph.netbeans.visual;

import org.netbeans.spi.navigator.NavigatorLookupHint;

/**
 *
 * @author Boris Perović <borisvperovic@gmail.com>
 */
public class VisualEditorNavigatorLookupHint implements NavigatorLookupHint {

    @Override
    public String getContentType() {
        return "text/x-nnet";
    }
}
