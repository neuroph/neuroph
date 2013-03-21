package org.neuroph.netbeans.files.dset;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.neuroph.core.learning.DataSet;
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
    "DATA_SET_FILE=Neural Network File"
 )
 @MIMEResolver.Registration(
   displayName="#DATA_SET_FILE",
   resource="DataSetResolver.xml"
 )
public class DataSetDataObject extends MultiDataObject {
    DataSet dataSet;
    FileObject dsFileObject;
    CookieSet cookies;


    public DataSetDataObject(FileObject fileObject, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fileObject, loader);
        dsFileObject = fileObject;
        
        dataSet = readFile(fileObject);

        cookies = getCookieSet();
        cookies.assign(DataSet.class, dataSet);
        cookies.add((Node.Cookie) new DataSetOpenSuport(getPrimaryEntry()));
        cookies.add(this);
    }

    @Override
    protected Node createNodeDelegate() {
        DataNode node = new DataNode(this, Children.LEAF, getLookup());
        node.setShortDescription("Name is " + dataSet.getLabel());
        node.setDisplayName(dsFileObject.getName());
        node.setShortDescription("Traning set file");
        return node;
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    public final DataSet readFile(FileObject fileObject) {
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

    public DataSet getTrainingSet() {
        return dataSet;
    }
}
