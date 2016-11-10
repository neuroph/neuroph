package org.neuroph.netbeans.visual;

import java.io.IOException;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.actions.AbstractSavable;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.explorer.ExplorerNeuralNetworkNode;
import org.neuroph.netbeans.explorer.ExplorerTopComponent;
import org.neuroph.netbeans.visual.palette.PaletteSupport;
import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.filesystems.FileObject;
import org.openide.loaders.SaveAsCapable;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.neuroph.netbeans.visual//VisualEditor//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "VisualEditorTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.neuroph.netbeans.visual.VisualEditorTopComponent")
//@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_VisualEditorAction",
        preferredID = "VisualEditorTopComponent"
)
@Messages({
    "CTL_VisualEditorAction=VisualEditor",
    "CTL_VisualEditorTopComponent=VisualEditor Window",
    "HINT_VisualEditorTopComponent=This is a VisualEditor window"
})
public final class VisualEditorTopComponent extends TopComponent implements ExplorerManager.Provider {

    private FileObject fileObject;
    private NeuralNetworkScene neuralNetworkScene;
    private final InstanceContent content = new InstanceContent();
    private final AbstractLookup aLookup = new AbstractLookup(content);   
    private final ExplorerManager explorerManager = new ExplorerManager();
    private NeuralNetAndDataSet neuralNetAndDataSet;

    // should never be called, except from Window -> VisualEditor
    public VisualEditorTopComponent() {        
        this(new NeuralNetwork<>(), "Unsaved Neural Network");
    }

    public VisualEditorTopComponent(NeuralNetwork<?> nnet, String name) {
        initComponents();
        setName(name);
        setToolTipText(Bundle.HINT_VisualEditorTopComponent());
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));

        neuralNetworkScene = new NeuralNetworkScene(nnet, this);
        visualEditorScrollPane.setViewportView(neuralNetworkScene.createView());

        neuralNetworkScene.visualizeNetwork();

        explorerManager.setRootContext(new ExplorerNeuralNetworkNode(nnet));

            //palette = PaletteSupport.createPalette();
        // http://bits.netbeans.org/dev/javadoc/org-netbeans-spi-palette/architecture-summary.html
        // TODO save added by default. Add it and remove it based on modification of NeuralNetwork.
        content.add(new Save(this, content));
        ProxyLookup jointLookup = new ProxyLookup(
                new Lookup[]{
                    Lookups.singleton(neuralNetworkScene),
                    Lookups.singleton(PaletteSupport.getPalette()),
                    Lookups.singleton(new VisualEditorNavigatorLookupHint()),
                    Lookups.singleton(new SaveAs(this)),
                    aLookup,
                    neuralNetworkScene.getLookup(),
                    ExplorerUtils.createLookup(explorerManager, getActionMap())
                });
        associateLookup(jointLookup);
    }

    @Override
    public void componentOpened() {
        WindowManager.getDefault().findTopComponent("ExplorerTopComponent").open();
        WindowManager.getDefault().findTopComponent("properties").open();
    }

    @Override
    protected void componentActivated() {
        super.componentActivated();

        DataSet dataSet = neuralNetworkScene.getDataSet();
        if (dataSet != null) {
            neuralNetAndDataSet = new NeuralNetAndDataSet(neuralNetworkScene.getNeuralNetwork(), dataSet);
            content.add(neuralNetAndDataSet);
            content.add(new TrainingController(neuralNetAndDataSet)); // TODO remove this on deactivation?
        }
        
        TopComponent tc = WindowManager.getDefault().findTopComponent("ExplorerTopComponent"); // TODO: explorer should listento Lookup of this TC ...
        if (tc != null) {
            ExplorerTopComponent explorerTC = (ExplorerTopComponent) tc; 
            explorerTC.initializeOrSelectNNetRoot(neuralNetworkScene.getNeuralNetwork());
            explorerTC.setCurrentObjectScene(neuralNetworkScene);
        }
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated();

        if (neuralNetAndDataSet != null) {
            content.remove(neuralNetAndDataSet);
        }

        if (!isActivatedLinkedTC()) {
            neuralNetworkScene.deselectAll();
        }
    }

    @Override
    public boolean canClose() {
        Save saveObj = getLookup().lookup(Save.class);
        if (saveObj != null) {
            Confirmation msg = new NotifyDescriptor.Confirmation(
                    "Do you want to save \"" + this.getName() + "\"?",
                    NotifyDescriptor.YES_NO_OPTION,
                    NotifyDescriptor.QUESTION_MESSAGE);
            Object result = DialogDisplayer.getDefault().notify(msg);
            if (NotifyDescriptor.YES_OPTION.equals(result)) {
                saveTopComponent();

                // disable "Save" option
                content.remove(saveObj);
                saveObj.unregisterPublic();
            } else if (NotifyDescriptor.NO_OPTION.equals(result)) {
                // disable "Save" option
                content.remove(saveObj);
                saveObj.unregisterPublic();
            } else {
                // do not close, as the dialog has been closed
                return false;
            }
        }
        return super.canClose();
    }

    @Override
    protected void componentClosed() {
        TopComponent exTC = WindowManager.getDefault().findTopComponent("ExplorerTopComponent");
        if (exTC != null) {
            ExplorerTopComponent explorerTC = (ExplorerTopComponent) exTC;
            if (explorerTC.isValidNNetInRoot(neuralNetworkScene.getNeuralNetwork())) {
                explorerTC.emptyTree();
            }
        }

        super.componentClosed();
    }

    public boolean isActivatedLinkedTC() {
        TopComponent activatedTC = WindowManager.getDefault().getRegistry().getActivated();
        TopComponent propertiesTC = WindowManager.getDefault().findTopComponent("properties");
        TopComponent explorerTC = WindowManager.getDefault().findTopComponent("ExplorerTopComponent");
        TopComponent navigatorTC = WindowManager.getDefault().findTopComponent("navigatorTC");
        if (activatedTC == propertiesTC || activatedTC == explorerTC || activatedTC == navigatorTC || activatedTC == this) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        visualEditorScrollPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(visualEditorScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane visualEditorScrollPane;
    // End of variables declaration//GEN-END:variables

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    /**
     * Obtain the GraphViewTopComponent instance. Never call {@link #getDefault}
     * directly!
     */
//    public static synchronized GraphViewTopComponent findInstance() {
//        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
//        if (win == null) {
//            Logger.getLogger(GraphViewTopComponent.class.getName()).warning(
//                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
//            return getDefault();
//        }
//        if (win instanceof GraphViewTopComponent) {
//            return (GraphViewTopComponent) win;
//        }
//        Logger.getLogger(GraphViewTopComponent.class.getName()).warning(
//                "There seem to be multiple components with the '" + PREFERRED_ID
//                + "' ID. That is a potential source of errors and unexpected behavior.");
//        return getDefault();
//    }
    public void refresh() {
        neuralNetworkScene.refresh();
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
    }

    private void saveTopComponentToPath(String path) {
        neuralNetworkScene.getNeuralNetwork().save(path);
    }

    private void saveTopComponent() {
        String path = fileObject.getPath();
        saveTopComponentToPath(path);
    }

    /**
     * Enables SaveAs functionality.
     */
    private class SaveAs implements SaveAsCapable {

        VisualEditorTopComponent nnetTopComponent;

        public SaveAs(VisualEditorTopComponent nnetTopComponent) {
            this.nnetTopComponent = nnetTopComponent;
        }

        @Override
        public void saveAs(FileObject folder, String name) throws IOException {
            String path = folder.getPath() + "/" + name;
            nnetTopComponent.saveTopComponentToPath(path);
        }
    }

    /**
     * Enables Save functionality.
     */
    public class Save extends AbstractSavable {

        private final VisualEditorTopComponent neuralNetworkTopComponent;
        private final InstanceContent ic;
/**
         * Constructor of save object. Registers Save object to SavableRegistry
         * to enable SaveAll functionality.
         * @param topComponent
         * @param instanceContent 
         */
        public Save(VisualEditorTopComponent topComponent, InstanceContent instanceContent) {
            this.neuralNetworkTopComponent = topComponent;
            this.ic = instanceContent;
            register();
        }

        @Override
        protected String findDisplayName() {
            return "Neural network " + neuralNetworkTopComponent.getName(); // get display name somehow
        }

        /**
         * Exposes unregister() method so that this Save object can be removed
         * from SavableRegistry if the file has been saved in some other way.
         * A user can save the file on closing of the UMLTopComponent, if it has
         * not been saved.
         */
        public void unregisterPublic() {
            unregister();
        }

        /**
         * Invoked when the user saves the diagram.
         */
        @Override
        protected void handleSave() {
            neuralNetworkTopComponent.saveTopComponent();

            //TODO reenable this on notifyModified
//            ic.remove(this);
            // unregisters here automatically, since handleSave() is called from AbstractSavable.save()
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Save) {
                return ((Save) other).neuralNetworkTopComponent.equals(neuralNetworkTopComponent);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return neuralNetworkTopComponent.hashCode();
        }
    }
}
