package org.neuroph.netbeans.files.tset;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import org.netbeans.api.actions.Openable;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.main.easyneurons.DataSetTopComponent;
import org.neuroph.netbeans.visual.VisualEditorTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.WindowManager;

@Messages({
    "LBL_DataSet_LOADER=Files of DataSet"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_DataSet_LOADER",
        mimeType = "text/x-tset",
        extension = {"tset", "TSET"}
)
@DataObject.Registration(
        mimeType = "text/x-tset",
        iconBase = "org/neuroph/netbeans/files/tset/tsetIcon.png",
        displayName = "#LBL_DataSet_LOADER",
        position = 300
)
@ActionReferences({
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200
    ),
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300
    ),
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500
    ),
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600
    ),
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800
    ),
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000
    ),
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200
    ),
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300
    ),
    @ActionReference(
            path = "Loaders/text/x-tset/Actions",
            id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400
    )
})
/**
 * @author Boris Perović
*/
public class DataSetDataObject extends MultiDataObject implements Openable, LookupListener {

    FileObject fileObject;
    DataSet dataSet;
    DataSetTopComponent dataSetTopComponent;
    
    private final InstanceContent content = new InstanceContent();
    private final AbstractLookup aLookup = new AbstractLookup(content);
    private Lookup.Result<DataSetTopComponent.Save> savable;
    private DataSetTopComponent.Save oldSave;
    
    public DataSetDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        fileObject = pf;
//        registerEditor("text/x-tset", false); // Opens the standard source editor, and has problems since the encoding is set to Windows-1252 and not UTF-8
        
        dataSet = readFromFile(pf);
        content.add(dataSet);
        
        this.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("name")) {
                    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                        @Override
                        public void run() {
                            dataSetTopComponent.setName(fileObject.getNameExt());
                        }
                    });
                }
            }
        });
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    protected Node createNodeDelegate() {
        return new DataNode(this, Children.LEAF, new ProxyLookup(getLookup(), aLookup));
    }
    
    @Override
    public void open() {
        content.remove(dataSet);
        dataSet = readFromFile(fileObject);
        content.add(dataSet);
        
        if (dataSetTopComponent == null || !dataSetTopComponent.isOpened()) {
            dataSetTopComponent = new DataSetTopComponent(this);
//            nnetTopComponent.setFileObject(fileObject);
            dataSetTopComponent.open();
        }
        
        savable = dataSetTopComponent.getLookup().lookupResult(DataSetTopComponent.Save.class);
        savable.addLookupListener(this);
        dataSetTopComponent.requestActive();
    }
    
    private DataSet readFromFile(FileObject fileObject) {
        try (ObjectInputStream stream = new ObjectInputStream(fileObject.getInputStream())) {
            DataSet ds = (DataSet) stream.readObject();
            return ds;
        } catch (ClassNotFoundException | IOException ex) {
            System.err.println(ex.getMessage());
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    @Override
    protected void handleDelete() throws IOException {
        super.handleDelete();
        if (dataSetTopComponent != null) dataSetTopComponent.close();
//                        ExplorerTopComponent explorerTopComponent = (ExplorerTopComponent) WindowManager.getDefault().findTopComponent("easyUMLExplorerTopComponent");
//                        explorerTopComponent.getExplorerManager().setRootContext(Node.EMPTY);
//                        explorerTopComponent.getExplorerTree().setRootVisible(false);
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result source = (Lookup.Result) ev.getSource();
        Collection instances = source.allInstances();
        if (!instances.isEmpty()) {
            for (Object instance : instances) {
                if (instance instanceof DataSetTopComponent.Save && oldSave == null) {
                    oldSave = (DataSetTopComponent.Save) instance;
                    content.add(instance);
                }
                break;
            }
        } else {
            if (oldSave != null) {
                content.remove(oldSave);
                oldSave = null;
            }
        }
    }
}
