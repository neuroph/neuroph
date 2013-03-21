package org.neuroph.netbeans.files.nnet;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.neuroph.core.NeuralNetwork;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
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

    NeuralNetOpenSupport openAction;
    NeuralNetwork neuralNetwork;
    FileObject nnFileObject;
    CookieSet cookies;

    public NeuralNetworkDataObject(FileObject fileObject, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        // U konstruktoru se prosleđeni fajl deserilaizuje nakon čega se dolazi do klase NeuralNetwork.
        // deserijalizaciju ne treba raditi ovde nego u OpenSupport u metodi open - to ima vise smisla
        // ovako bi prilikom ucitavanja projekta odmah nepotrebno ucitavao sve neuronske mreze cim  se kreira DataObject

        super(fileObject, loader);
        nnFileObject = fileObject;

//        nnetwork = new NeuralNetwork();
        neuralNetwork = readFile(fileObject);

        cookies = getCookieSet();
        cookies.assign(NeuralNetwork.class, neuralNetwork); // put it in lookup
        openAction = new NeuralNetOpenSupport(getPrimaryEntry());

        cookies.add((Node.Cookie) openAction);
        cookies.add(this);
    }

    // creates node for projects window
    @Override
    protected Node createNodeDelegate() {
        // can use DataNode here as well
        DataNode node = new DataNode(this, Children.LEAF, getLookup());
       // DataNode node = new DataNode(this, Children.LEAF, cookies.getLookup());
        //  node.setShortDescription("Name is " + getLookup().lookup(NeuralNetwork.class).toString());
        node.setDisplayName(nnFileObject.getName());

        return node;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

//  use this method if we want to load it from NeuralNteOpenSupport not from constructor
//    public void loadObject() {
//        this.nnetwork = readFile(nnFileObject);
//         cookies.assign(NeuralNetwork.class, nnetwork);
//    }
    private NeuralNetwork readFile(FileObject fileObject) {
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
}
