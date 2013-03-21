package org.neuroph.netbeans.files.nnet;

import java.io.IOException;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.netbeans.main.ViewManager;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author Ivana
 */
public class NeuralNetOpenSupport extends OpenSupport implements OpenCookie, CloseCookie,SaveCookie {
    NeuralNetworkDataObject nnDataObject;

    public NeuralNetOpenSupport(NeuralNetworkDataObject.Entry entry) {
        super(entry);
        nnDataObject = (NeuralNetworkDataObject) entry.getDataObject();
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
//        dobj = (NeuralNetworkDataObject) entry.getDataObject();
//        VisualTopComponent tc = new VisualTopComponent();
// //       tc.open();
//        return tc;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void open() {
        // check if it is allready loaded before loading
        // nnDataObject.loadObject();
        NeuralNetwork nnet = nnDataObject.getNeuralNetwork();

        ViewManager.getInstance().openNeuralNetworkWindow(nnet);
        WindowManager.getDefault().findTopComponent("ExplorerTopComponent").open();
        
        //ExplorerTopComponent.findInstance().open();
//        ExplorerTopComponent.findInstance().setFirstActiveNN(nnet, nnDataObject.getName());
//TopComponent propertiesComponent =
//WindowManager.getDefault().findComponent("properties");
//propertiesComponent.open();
    }

    public void save() throws IOException {
        SaveCookieAction saveAction = new SaveCookieAction(this);
        saveAction.save(nnDataObject);
    }
}
