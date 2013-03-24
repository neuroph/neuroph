package org.neuroph.netbeans.project;

import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.queries.VisibilityQuery;
import org.openide.filesystems.FileObject;
import org.openide.loaders.ChangeableDataFilter;
import org.openide.loaders.DataFilter;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 *
 * @author zoran
 */
// to refresh children use  refresh(true) - but this will refresh only folders in this context
// create Neuroph Project folder nodes... (and their children)
public class NeurophProjectChildFactory extends ChildFactory<FileObject> {

    private NeurophProject project;

    public NeurophProjectChildFactory(NeurophProject project) {
        this.project = project;
    }

    @Override
    protected boolean createKeys(List<FileObject> keys) {
        FileObject projectDir = project.getProjectDirectory();

        keys.add(projectDir.getFileObject(NeurophProject.NEURAL_NETWORKS_DIR));
        keys.add(projectDir.getFileObject(NeurophProject.TRAINING_SETS_DIR));
        keys.add(projectDir.getFileObject(NeurophProject.TEST_SETS_DIR));

        return true;
    }

    @Override
    protected Node createNodeForKey(FileObject key) { // keys may be FileObjects - project folders

        // 1. Find the DataFolder for the FileObject
        DataFolder df = DataFolder.findFolder(key); // each folder should listen changes in his children... and call df.createNodeChildren(filter);
        // 2. Create a custom filter
        DataFilter filter = new MyDataFilter();
        // 3. Get DataFolder children (with filter) - this will get actual files
        Children children = df.createNodeChildren(filter); // create these children with NodeFactory - create NeuralNetworkChildFactory i NeuralNetworkChildNode
        // 4. Create a proxy lookup
        Lookup lookup = new ProxyLookup(new Lookup[]{
                    df.getNodeDelegate().getLookup(), project.getLookup()
                });
        // 5. Create a filter node
        FilterNode fn = new FilterNode(df.getNodeDelegate(), children, lookup);

//        fn.addNodeListener(new NodeListener() {
//
//            public void childrenAdded(final NodeMemberEvent nme) {
//                   WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
//                    public void run() {
//                        // code to be invoked when system UI is ready
//
//                        TopComponent projWindow = WindowManager.getDefault().findTopComponent("projectTabLogical_tc");
//                        projWindow.requestActive();
//                        ExplorerManager em = ((ExplorerManager.Provider) projWindow).getExplorerManager();
//                        Node folderNode = (Node) nme.getSource();
//                        em.setExploredContext(folderNode, new Node[]{folderNode});
////                        Node[] addedNode = nme.getDelta();
////
////                        try {
////                            em.setSelectedNodes(addedNode);
////                            NeuralNetworkDataObject nndo = (NeuralNetworkDataObject) addedNode[0].getCookie(NeuralNetworkDataObject.class);
////                            ViewManager.getInstance().openNeuralNetworkWindow(nndo.getNeuralNetwork());
////                        } catch (PropertyVetoException ex) {
////                            Exceptions.printStackTrace(ex);
////                        }
//
//                    }
//                }); // run
//            }
//
//            public void childrenRemoved(NodeMemberEvent nme) {
//                //         throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void childrenReordered(NodeReorderEvent nre) {
//                //          throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void nodeDestroyed(NodeEvent ne) {
//                //         throw new UnsupportedOperationException("Not supported yet.");
//            }
//
//            public void propertyChange(PropertyChangeEvent evt) {
//                //         throw new UnsupportedOperationException("Not supported yet.");
//            }
//        });
// 
        return fn;
    }

    /**
     * Filter DataObjects that must not be shown (like .svn folders)
     */
    static final class MyDataFilter implements ChangeListener,
            ChangeableDataFilter {

        private final ChangeSupport changeSupport =
                new ChangeSupport(this);

        public MyDataFilter() {
            VisibilityQuery.getDefault().addChangeListener(this);
        }

        @Override
        public boolean acceptDataObject(DataObject obj) {
            FileObject fo = obj.getPrimaryFile();
            return VisibilityQuery.getDefault().isVisible(fo);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            changeSupport.fireChange();
        }

        @Override
        public void addChangeListener(ChangeListener listener) {
            changeSupport.addChangeListener(listener);
        }

        @Override
        public void removeChangeListener(ChangeListener listener) {
            changeSupport.removeChangeListener(listener);
        }
    }
}