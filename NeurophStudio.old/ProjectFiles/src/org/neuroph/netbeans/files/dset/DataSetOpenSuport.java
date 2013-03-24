package org.neuroph.netbeans.files.dset;

import org.neuroph.core.learning.DataSet;
import org.neuroph.netbeans.main.ViewManager;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author user
 */
public class DataSetOpenSuport extends OpenSupport implements OpenCookie, CloseCookie {
    DataSetDataObject tsDataObject;

    public DataSetOpenSuport(DataSetDataObject.Entry entry) {
        super(entry);
        tsDataObject = (DataSetDataObject) entry.getDataObject();
    }

    @Override
    public void open() {
        DataSet tset = tsDataObject.getTrainingSet();
        ViewManager.getInstance().openTrainingSetWindow(tset);
        WindowManager.getDefault().findTopComponent("ExplorerTopComponent").open();
        
       // ExplorerTopComponent.findInstance().open(); // use lookup instead this call: put lookup in ExplorerTopComponent and when it sees NeuralNetwork open its subnodes
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
