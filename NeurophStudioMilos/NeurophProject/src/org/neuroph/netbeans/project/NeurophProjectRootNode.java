package org.neuroph.netbeans.project;

import java.awt.Image;
import javax.swing.Action;
import org.netbeans.spi.project.ui.support.CommonProjectActions;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.nodes.Sheet;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

public class NeurophProjectRootNode extends FilterNode {
  private static Image smallImage =
         ImageUtilities.loadImage("org/neuroph/netbeans/wizards/project/templates/neurophProjectIcon.gif"); // NOI18N

 final NeurophProject project;

  public NeurophProjectRootNode(Node originalNode, NeurophProject project) {
    super(originalNode,
          Children.create(new NeurophProjectChildFactory(project), true), // new FilterNode.Children(node)
         //The projects system wants the project in the Node's lookup.
         //NewAction and friends want the original Node's lookup.
         //Make a merge of both
          new ProxyLookup(new Lookup[]{Lookups.singleton(project),
                                       originalNode.getLookup() })
       );
    
    this.project = project;
//    this.project.addChangeListener(this);
  }
  
  @Override
  public String getDisplayName() {
    return project.getProjectDirectory().getName();
  }
  
  // A bonus: this snippet will merge NB folder icon and
  // your 8x8 icon
  @Override
  public Image getIcon(int type) {
    DataFolder root = DataFolder.findFolder(
    FileUtil.getConfigRoot());
    Image original = root.getNodeDelegate().getIcon(type);
    return ImageUtilities.mergeImages(original, smallImage, 0, 0);
  }
  @Override
  public Image getOpenedIcon(int type) {
    DataFolder root = DataFolder.findFolder(
    FileUtil.getConfigRoot());
    Image original = root.getNodeDelegate().getIcon(type);
    return ImageUtilities.mergeImages(original, smallImage, 0, 0);
  }

    @Override
    public Action[] getActions(boolean arg0) {
        Action[] nodeActions = new Action[6];
        nodeActions[0] = CommonProjectActions.newFileAction();
        nodeActions[1] = CommonProjectActions.copyProjectAction();
        nodeActions[2] = CommonProjectActions.moveProjectAction();
        nodeActions[3] = CommonProjectActions.renameProjectAction();
        nodeActions[4] = CommonProjectActions.deleteProjectAction();
        nodeActions[5] = CommonProjectActions.closeProjectAction();             
        
        //nodeActions[4] = CommonProjectActions.customizeProjectAction();

        return nodeActions;
    }
    
    
       
    

//    public void stateChanged(ChangeEvent e) {
//        // this should refresh children!
//    //    JOptionPane.showMessageDialog(null, "Project root node : project has changed");
//
//    }

           
    
}