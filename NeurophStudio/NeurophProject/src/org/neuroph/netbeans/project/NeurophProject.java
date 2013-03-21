package org.neuroph.netbeans.project;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.CopyOperationImplementation;
import org.netbeans.spi.project.DeleteOperationImplementation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.support.DefaultProjectOperations;
import org.openide.filesystems.FileObject;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Represents implementation of one IDE project in memory.
 * See http://platform.netbeans.org/tutorials/nbm-projecttype.html
 * http://bits.netbeans.org/dev/javadoc/org-netbeans-modules-projectapi/org/netbeans/api/project/Project.html
 * @author Ivana
 */
public class NeurophProject implements Project {

    /**
     * Root project dir
     */
    private final FileObject projectRootDir;
    private final ProjectState state;
    private Lookup lookup;
    private ChangeSupport changeSupport;
    
    public static final String NEURAL_NETWORKS_DIR = "Neural Networks";
    public static final String TRAINING_SETS_DIR = "Training Sets";
    public static final String TEST_SETS_DIR = "Test Sets";

    public NeurophProject(FileObject projectDir, ProjectState state) {
        this.projectRootDir = projectDir;
        this.state = state;
        createProjectFolders();
        CurrentProject.getInstance();
        this.changeSupport = new ChangeSupport(this);
    }

//    public boolean isNewProject() {
//        return isNewProject;
//    }
    /**
     * Gets an associated directory where the project metadata and all project data live.
     * @return a project directory
     */
    @Override
    public FileObject getProjectDirectory() {
        return projectRootDir;
    }

    public final void createProjectFolders() {        
        FileObject neuralNetworksFolder = null;
        FileObject trainingSetsFolder = null;
        FileObject testSetsFolder = null;

        if (projectRootDir.getFileObject(NEURAL_NETWORKS_DIR) == null) {
            try {
                neuralNetworksFolder = projectRootDir.createFolder(NEURAL_NETWORKS_DIR);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            neuralNetworksFolder = projectRootDir.getFileObject(NEURAL_NETWORKS_DIR);
        }


        if (projectRootDir.getFileObject(TRAINING_SETS_DIR) == null) {
            try {
                trainingSetsFolder = projectRootDir.createFolder(TRAINING_SETS_DIR);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            trainingSetsFolder = projectRootDir.getFileObject(TRAINING_SETS_DIR);
        }


        // add test set s folder to project
        if (projectRootDir.getFileObject(TEST_SETS_DIR) == null) {
            try {
                testSetsFolder = projectRootDir.createFolder(TEST_SETS_DIR);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }

        } else {
            testSetsFolder = projectRootDir.getFileObject(TEST_SETS_DIR);
        }
    }
    

    FileObject getNeurophProjectFolder(boolean create) {
        FileObject result =
                projectRootDir.getFileObject(NeurophProjectFactory.PROJECT_DIR);
        if (result == null && create) {

            try {
                result = projectRootDir.createFolder(NeurophProjectFactory.PROJECT_DIR);
            } catch (IOException ioe) {
                Exceptions.printStackTrace(ioe);
            }
        }
        return result;
    }

    // force refresh to scan for external cahnges:
    // when nn or ts file are created in subfolders
    public void refresh() {
        for (FileObject projectFolder : getProjectDirectory().getChildren()) {
            projectFolder.refresh(); // this one force to re-read  of the files in folder - scan fo rexternal changes
        }
    }

   

    //The project type's capabilities are registered in the project's lookup:
    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            lookup = Lookups.fixed(new Object[]{
                        state, //allow outside code to mark the project as needing saving
                        new ActionProviderImpl(), //Provides standard actions like Build and Clean
                        new DemoDeleteOperation(),
                        new DemoCopyOperation(this),
                        new Info(), //Project information implementation
                        new NeurophProjectLogicalView(this), //Logical view of project implementation
                    });
        }
        return lookup;
    }
      
        // notify project that file has been added/created and fire change event to notify all listeners
        public void addNotify() {
            refresh();
      //      changeSupport.fireChange(); // this should be fired if someone listens for added files
        }    

    private final class ActionProviderImpl implements ActionProvider {

        private String[] supported = new String[]{
            ActionProvider.COMMAND_DELETE,
            ActionProvider.COMMAND_COPY,};

        @Override
        public String[] getSupportedActions() {
            return supported;
        }

        @Override
        public void invokeAction(String string, Lookup lookup) throws IllegalArgumentException {
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_DELETE)) {
                DefaultProjectOperations.performDefaultDeleteOperation(NeurophProject.this);
            }
            if (string.equalsIgnoreCase(ActionProvider.COMMAND_COPY)) {
                DefaultProjectOperations.performDefaultCopyOperation(NeurophProject.this);
            }
        }

        @Override
        public boolean isActionEnabled(String command, Lookup lookup) throws IllegalArgumentException {
            if ((command.equals(ActionProvider.COMMAND_DELETE))) {
                return true;
            } else if ((command.equals(ActionProvider.COMMAND_COPY))) {
                return true;
            } else {
                throw new IllegalArgumentException(command);
            }
        }       
    }

    private final class DemoDeleteOperation implements DeleteOperationImplementation {

        @Override
        public void notifyDeleting() throws IOException {
        }

        @Override
        public void notifyDeleted() throws IOException {
        }

        @Override
        public List<FileObject> getMetadataFiles() {
            List<FileObject> dataFiles = new ArrayList<>();
            return dataFiles;
        }

        @Override
        public List<FileObject> getDataFiles() {
            List<FileObject> dataFiles = new ArrayList<>();
            return dataFiles;
        }
    }

    private final class DemoCopyOperation implements CopyOperationImplementation {

        private final NeurophProject project;
        private final FileObject projectDir;

        public DemoCopyOperation(NeurophProject project) {
            this.project = project;
            this.projectDir = project.getProjectDirectory();
        }

        @Override
        public List<FileObject> getMetadataFiles() {
            return Collections.EMPTY_LIST;
        }

        @Override
        public List<FileObject> getDataFiles() {
            return Collections.EMPTY_LIST;
        }

        @Override
        public void notifyCopying() throws IOException {
        }

        @Override
        public void notifyCopied(Project arg0, File arg1, String arg2) throws IOException {
        }
    }

    private final class Info implements ProjectInformation {

        @Override
        public Icon getIcon() {
            return new ImageIcon(ImageUtilities.loadImage(
                    "org/neuroph/netbeans/wizards/project/templates/neurophProjectIcon.gif"));
        }

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener pcl) {
            //do nothing, won't change
        }

        @Override
        public Project getProject() {
            return NeurophProject.this;
        }
    }
}
