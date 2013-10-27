package org.neuroph.netbeans.project;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.nodes.Node;


/**
 * Ability for a Project to supply a logical view of itself.
 * >>> LogicalViewProviderImpl: provider of logical view, meaning the top node in the projects tab.
 * See http://platform.netbeans.org/tutorials/nbm-projecttype.html
 * see file:///D:/NetBeansPlatform/ImportantTutorials/lookup%20%C2%AB%20Extreme%20Java-Nodes%20i%20Project.htm
 * @author Ivana
 */
class NeurophProjectLogicalView implements LogicalViewProvider {
    private final NeurophProject project;

    public NeurophProjectLogicalView(NeurophProject project) {
        this.project = project;
    }

    /**
     * Returns a node displaying the contents of the project in an intuitive way
     * @return
     */
   @Override
    public org.openide.nodes.Node createLogicalView() {
        // get project directory
        FileObject projectDir = project.getProjectDirectory(); 
        //Get the DataObject that represents it
        DataFolder projectDataObject = DataFolder.findFolder(projectDir);
        //Get its default node-we'll wrap our node around it to change the display name, icon, etc
        Node originalProjectNode = projectDataObject.getNodeDelegate();
        originalProjectNode.setShortDescription("Neuroph project in "+projectDir.getPath());
        // wrap project data node with NeurophProjectRootNode
        return new NeurophProjectRootNode(originalProjectNode, this.project);
    }

  
    @Override
    public Node findPath(Node root, Object target) {
        Project prj = root.getLookup().lookup(Project.class);
        if (prj == null) {
            return null;
        }

        if (target instanceof FileObject) {
            FileObject fo = (FileObject) target;
            Project owner = FileOwnerQuery.getOwner(fo);
            if (!prj.equals(owner)) {
                return null; // Don't waste time if project does not own the fo
            }

            for (Node folderNode : root.getChildren().getNodes(true)) {
                for(Node fileNode : folderNode.getChildren().getNodes() ) {
                    FileObject file = fileNode.getLookup().lookup(FileObject.class) ;
                    if (file.equals((FileObject) target)) {
                        return fileNode;
                    }
                }
            }
        }
        
        return null;
    }

}