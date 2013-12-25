package org.neuroph.netbeans.files.nnet;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.main.easyneurons.NeuralNetworkTopComponent;
import org.neuroph.netbeans.visual.VisualEditorTopComponent;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * This class provides open support for neural network files.
 * @author Ivana Jovicic
 * @author Zoran Sevarac
 */
public class NeuralNetOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {
    
    NeuralNetworkDataObject nnetDataObject;
    TopComponent nnetTopComponent;

    public NeuralNetOpenSupport(NeuralNetworkDataObject.Entry entry) {
        super(entry);
        nnetDataObject = (NeuralNetworkDataObject) entry.getDataObject();
    }

    // this should use http://bits.netbeans.org/6.7/javadoc/org-openide-windows/org/openide/windows/CloneableOpenSupport.html
    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void open() {
           
        if  (nnetTopComponent !=null) {
            if (nnetTopComponent.isOpened()) {
                nnetTopComponent.requestActive();
            } else {            
                nnetTopComponent.open();
                nnetTopComponent.requestActive();
             }
        } else {
               // nnetDataObject.loadData(); // done in constructor
               NeuralNetwork nnet = nnetDataObject.getLookup().lookup(NeuralNetwork.class);
               // here we should call createCloneableTopComponent...
//               nnetTopComponent = new NeuralNetworkTopComponent(nnetDataObject); // otherwise create new window to open network in
               nnetTopComponent = new VisualEditorTopComponent(nnetDataObject);
               nnetTopComponent.open(); 
               nnetTopComponent.requestActive();                            
        }    
        
        WindowManager.getDefault().findTopComponent("ExplorerTopComponent").open();        
    }
           
}
