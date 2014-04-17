package org.neuroph.netbeans.files.dset;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.neuroph.core.data.DataSet;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;


 @NbBundle.Messages(
    "DATA_SET_FILE=Data Set File"
 )
 @MIMEResolver.Registration(
   displayName="#DATA_SET_FILE",
   resource="DataSetResolver.xml"
 )
public class DataSetDataObject extends MultiDataObject {
     
    DataSet dataSet;
    FileObject dsFileObject;  
    DataSetOpenSuport openSupport;
    DataSetSaveCookie saveCookie; 
    CookieSet cookies;


    public DataSetDataObject(FileObject fileObject, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fileObject, loader);
        dsFileObject = fileObject;
        
     //   dataSet = readFromFile(fileObject);
        
        cookies = getCookieSet();
        openSupport = new DataSetOpenSuport(getPrimaryEntry());        
        cookies.add((Node.Cookie) openSupport);
        cookies.add(this);
        loadData();
    }

    @Override
    protected Node createNodeDelegate() {
        DataNode node = new DataNode(this, Children.LEAF, getLookup());
        return node;
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }
    
    public final void loadData() {
        this.dataSet = readFromFile(dsFileObject);
        cookies.assign(DataSet.class, this.dataSet); // add data set to lookup
        
        saveCookie= new DataSetSaveCookie(this); 
        cookies.add(saveCookie); // this should be added only on change
    }     

    private DataSet readFromFile(FileObject fileObject) {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(fileObject.getInputStream());
            try {
                DataSet o = (DataSet) stream.readObject();
                stream.close();
                return o;
            } catch (ClassNotFoundException ex) {
                Exceptions.printStackTrace(ex);
                stream.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
        }
        return null;
    }
    
    public DataSet getDataSet() {
        return getLookup().lookup(DataSet.class);
    }

    
    
}
