package org.neuroph.netbeans.files.dset;

import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 *
 * @author zoran
 */
public class DataSetNode extends DataNode {

    public DataSetNode(DataObject obj, Children ch, Lookup lookup) {
        super(obj, ch, lookup);
        this.setDisplayName(obj.getName());
        this.setShortDescription("Data Set File");        
    }
    
    
    
}
