package org.neuroph.netbeans.files.dset;

import java.io.IOException;
import org.neuroph.core.data.DataSet;
import org.openide.cookies.SaveCookie;

/**
 *
 * @author Marjan Hrzic
 * @author Zoran Sevarac
 */
public class DataSetSaveCookie implements SaveCookie {
    
    private DataSetDataObject dataSetDataObject;    
    
    public DataSetSaveCookie(DataSetDataObject dsDataObject) {
        this.dataSetDataObject = dsDataObject;
    }
    
    public void save() throws IOException {
        DataSet dataSet = dataSetDataObject.getLookup().lookup(DataSet.class);
        String filePath = dataSetDataObject.getPrimaryFile().getPath();        
        dataSet.save(filePath);
    }
}
