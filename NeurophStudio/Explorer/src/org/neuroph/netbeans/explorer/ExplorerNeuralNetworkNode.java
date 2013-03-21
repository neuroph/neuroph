package org.neuroph.netbeans.explorer;

import java.awt.Image;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.LearningRule;
import org.neuroph.netbeans.properties.InputNeuronsEditor;
import org.neuroph.netbeans.properties.LayerEditor;
import org.neuroph.netbeans.properties.LearningRuleEditor;
import org.neuroph.netbeans.properties.OutputNeuronsEditor;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;

/**
 * Node for neural network in Explorer window
 * @author Ivana Jovicic
 * @author Zoran Sevarac
 */
public class ExplorerNeuralNetworkNode extends AbstractNode  {
    NeuralNetwork neuralNet;

    public ExplorerNeuralNetworkNode(NeuralNetwork neuralNet ) {
        // this should be done like super (Children.create(new MyChildren(), true), Lookups.singleton(obj));
// http://platform.netbeans.org/tutorials/nbm-nodesapi2.html#propertysheet
     //   super( new NeuralNetworkChildren(neuralNet), Lookups.singleton(neuralNet));
      super( new ExplorerNeuralNetworkChildren(neuralNet));
           //super( new NeuralNetworkChildren(neuralNet), Lookups.fixed(neuralNet)); // having neuralNet makes issues for Explorer window
      this.setDisplayName(neuralNet.getLabel());
      this.neuralNet = neuralNet;
    }


    @Override
    public Image getIcon(int type) {
        return ImageUtilities.loadImage("org/neuroph/netbeans/files/nnet/neuralNetIcon.png");
    }

    @Override
    public Image getOpenedIcon(int i) {
        return getIcon(i);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = Sheet.createPropertiesSet();

        try {
            Property type = new PropertySupport.Reflection(this.neuralNet, Class.class, "getClass", null);
            PropertySupport.Reflection learningRule = new PropertySupport.Reflection(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection layers = new PropertySupport.Reflection(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection input = new PropertySupport.Reflection(this.neuralNet, LearningRule.class, "getLearningRule", null);
            PropertySupport.Reflection output = new PropertySupport.Reflection(this.neuralNet, LearningRule.class, "getLearningRule", null);

            type.setShortDescription("Neural Network Type");
            learningRule.setPropertyEditorClass(LearningRuleEditor.class);
            input.setPropertyEditorClass(InputNeuronsEditor.class);
            output.setPropertyEditorClass(OutputNeuronsEditor.class);
            layers.setPropertyEditorClass(LayerEditor.class);

            type.setName("Type");
            learningRule.setName("Learning Rule");
            layers.setName("Layers");
            input.setName("Input Neurons");
            output.setName("Output Neurons");


            set.put(type);
            set.put(learningRule);
            set.put(layers);
            set.put(input);
            set.put(output);

        } catch (NoSuchMethodException ex) {
            ErrorManager.getDefault();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        sheet.put(set);
        return sheet;
    }
    
    // No actions in this context at the moment. However it can be added see the commented code below
//    @Override
//    public Action[] getActions(boolean popup) {
//        return new Action[]{new MyAction()};
//    }

// Lookup listener is used by the the action code below
//    public NeuralNetworkDataObject result =
//            Utilities.actionsGlobalContext().lookup(NeuralNetworkDataObject.class);
//
//    public void resultChanged(LookupEvent le) {
//
//        Lookup.Result localresult = (Result) le.getSource();
//        Collection<NeuralNetworkDataObject> coll = localresult.allInstances();
//
//        for (NeuralNetworkDataObject object : coll) {
//            result = object;
//        }
//
//
//    }

// These actions are not needed in Navigator view, but the rest of code is kept as an example how
//  to add context actions to nodes here


//    private class MyAction extends AbstractAction implements Presenter.Popup {
//
//        public MyAction() {
//            putValue(NAME, "Serialise");
//        }
//
//        public void actionPerformed(ActionEvent e) {
//            try {
//                NeuralNetwork in = getLookup().lookup(NeuralNetwork.class);
//
//                if (result == null) {
//                    JOptionPane.showMessageDialog(null, "nullll");
//                }
//
//                System.out.println(result.getFolder().toString());
//                String p = result.getFolder().toString();
//                int fp = p.indexOf("[");
//                String fin = p.substring(++fp, p.length() - 1);
//                System.out.println(fin);
//                LocalFileSystem lfs = new LocalFileSystem();
//                lfs.setRootDirectory(new File(fin));
//                FileSystem mfs = FileUtil.createMemoryFileSystem();
//                //   JarFileSystem jfs=new JarFileSystem();
//                MultiFileSystem mufs = new MultiFileSystem(new FileSystem[]{lfs, mfs});
//                FileObject root = mufs.getRoot();
//                FileObject test = root.getFileObject(result.getName() + ".nnet");
//                if (test == null) {
//                    System.out.println("help");
//                }
//                ObjectOutputStream stream = new ObjectOutputStream(test.getOutputStream());
//                try {
//                    stream.writeObject(in);
//                } finally {
//                    stream.close();
//                }
//                //  Writer w = IOProvider.getDefault().getIO("File content", false).getOut();
//                //  w.write(content);
//                //  System.out.println(content);
//            } catch (PropertyVetoException ex) {
//                Exceptions.printStackTrace(ex);
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//            //  Writer w = IOProvider.getDefault().getIO("File content", false).getOut();
//            //  w.write(content);
//            //  System.out.println(content);
//
//
//
//
//        }
//
//        public JMenuItem getPopupPresenter() {
//            JMenu result = new JMenu("Submenu");
//            result.add(new JMenuItem(this));
//            result.add(new JMenuItem(new Deserialise()));
//            return result;
//        }
//    }
//
//    private class Deserialise extends AbstractAction implements Presenter.Popup {
//
//        public Deserialise() {
//            putValue(NAME, "Deserialise");
//        }
//
//        public void actionPerformed(ActionEvent e) {
//            try {
//                NeuralNetwork in = getLookup().lookup(NeuralNetwork.class);
//                //  in.getClass().
//
//                System.out.println(result.getFolder().toString());
//                String p = result.getFolder().toString();
//                int fp = p.indexOf("[");
//                String fin = p.substring(++fp, p.length() - 1);
//                System.out.println(fin);
//                LocalFileSystem lfs = new LocalFileSystem();
//                lfs.setRootDirectory(new File(fin));
//                FileSystem mfs = FileUtil.createMemoryFileSystem();
//                //   JarFileSystem jfs=new JarFileSystem();
//                MultiFileSystem mufs = new MultiFileSystem(new FileSystem[]{lfs, mfs});
//                FileObject root = mufs.getRoot();
//                FileObject test = root.getFileObject(result.getName() + ".nnet");
//                if (test == null) {
//                    System.out.println("help");
//                }
//                ObjectInputStream stream = new ObjectInputStream(test.getInputStream());
//                try {
//                    NeuralNetwork o = (NeuralNetwork) stream.readObject();
//
//                    stream.close();
//                    System.out.println("Odstampaj ime:" + o.getInputNeurons().size());
//                } catch (ClassNotFoundException ex) {
//                    Exceptions.printStackTrace(ex);
//                    stream.close();
//                }
//            } catch (PropertyVetoException ex) {
//                Exceptions.printStackTrace(ex);
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
//
//        public JMenuItem getPopupPresenter() {
//            JMenu result = new JMenu("Submenu");
//            result.add(new JMenuItem(this));
//            return result;
//        }
//    }
}
