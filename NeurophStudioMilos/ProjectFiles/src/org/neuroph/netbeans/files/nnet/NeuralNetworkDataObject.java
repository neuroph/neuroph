package org.neuroph.netbeans.files.nnet;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.neuroph.core.NeuralNetwork;
import org.openide.cookies.SaveCookie;
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
    "NEURAL_NETWORK_FILE=Neural Network File"
 )
 @MIMEResolver.Registration(
   displayName="#NEURAL_NETWORK_FILE",
   resource="NeuralNetworkResolver.xml"
 )
public class NeuralNetworkDataObject extends MultiDataObject {
    
    NeuralNetwork neuralNetwork; // this is our data
    FileObject nnFileObject;    // maybe we dont need it here since we can put it into lookup...
    NeuralNetOpenSupport openSupport;
    SaveCookie saveCookie;
    CookieSet cookies;

    public NeuralNetworkDataObject(FileObject fileObject, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(fileObject, loader);
        nnFileObject = fileObject;

        cookies = getCookieSet();
        openSupport = new NeuralNetOpenSupport(getPrimaryEntry());        
        cookies.add((Node.Cookie) openSupport);      
        cookies.add(this);
        loadData();              
    }

    // creates node for projects window
    @Override
    protected Node createNodeDelegate() {
        DataNode node = new DataNode(this, Children.LEAF, getLookup());
        return node;
    }

//  use this method if we want to load it from NeuralNteOpenSupport not from constructor
    public void loadData() {
        this.neuralNetwork = readFromFile(nnFileObject);
        cookies.assign(NeuralNetwork.class, this.neuralNetwork); // add neural network to lookup
        
        saveCookie= new NeuralNetSaveCookie(this); 
        cookies.add(saveCookie); // this should be added only on change
    } 
    
    private NeuralNetwork readFromFile(FileObject fileObject) {
        ObjectInputStream stream = null;
        try {
            stream = new ObjectInputStream(fileObject.getInputStream());
            try {
                NeuralNetwork nn = (NeuralNetwork) stream.readObject();
                stream.close();

                return nn;
            } catch (ClassNotFoundException ex) {
                Exceptions.printStackTrace(ex);
                stream.close();
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }
    
    public NeuralNetwork getNeuralNetwork() {
        return getLookup().lookup(NeuralNetwork.class);
    }
    
    
    
    
}
