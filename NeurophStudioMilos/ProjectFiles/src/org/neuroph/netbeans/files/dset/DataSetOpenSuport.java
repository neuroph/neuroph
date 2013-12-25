package org.neuroph.netbeans.files.dset;

import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.main.easyneurons.DataSetTopComponent;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.WindowManager;

/**
 * This class provides open support for data set files.
 * @author Zoran Sevarac
 */
public class DataSetOpenSuport extends OpenSupport implements OpenCookie, CloseCookie {

    DataSetDataObject dataSetDataObject;
    DataSetTopComponent dataSetTopComponent;

    public DataSetOpenSuport(DataSetDataObject.Entry entry) {
        super(entry);
        dataSetDataObject = (DataSetDataObject) entry.getDataObject();
    }

    @Override
    public void open() {
        if  (dataSetTopComponent !=null) {
            if (dataSetTopComponent.isOpened()) {
                dataSetTopComponent.requestActive();
            } else {            
                dataSetTopComponent.open();
                dataSetTopComponent.requestActive();
             }
        } else {
               //dataSetDataObject.loadData(); // done in constructor
               DataSet ds = dataSetDataObject.getLookup().lookup(DataSet.class);
               dataSetTopComponent = new DataSetTopComponent(dataSetDataObject); // otherwise create new window to open network in
               dataSetTopComponent.open(); 
               dataSetTopComponent.requestActive();
        }    
        
        WindowManager.getDefault().findTopComponent("ExplorerTopComponent").open();
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
