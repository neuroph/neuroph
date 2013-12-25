package org.neuroph.netbeans.project;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * Create in-memory projects from disk directories.
 * See http://platform.netbeans.org/tutorials/nbm-projecttype.html
 * http://bits.netbeans.org/dev/javadoc/org-netbeans-modules-projectapi/org/netbeans/spi/project/ProjectFactory.html
 * @author Ivana Jovicic
 * @author Zoran Sevara
 */
@org.openide.util.lookup.ServiceProvider(service=ProjectFactory.class)
public class NeurophProjectFactory implements ProjectFactory {

    /**
     * Directory used to identify Neuroph projects - project type id folder.
     * If some dir contains subdir named neurophproject then its considered to be Neuroph project
     */
    public static final String PROJECT_DIR = "neurophproject";

    /**
     * Test whether a given directory refers to a project
     * recognized by this factory without trying to create it.
     * @param projectDirectory a directory which might refer to a project 
     * @return true if this factory recognize project, false otherwise
     */
    @Override
    public boolean isProject(FileObject projectDirectory) {
        return projectDirectory.getFileObject(PROJECT_DIR) != null;
    }

    /**
     * Create a project from directory on disk. If this factory does not in fact
     * recognize the directory, it should just return null.
     * @param projectDirectory - project directory on disk
     * @param state - a callback permitting the project to indicate when it is modified
     * @return a Neuroph project implementation, or null if this factory does not recognize it
     * @throws IOException - if there is a problem loading
     */
    @Override
    public Project loadProject(FileObject projectDirectory , ProjectState state) throws IOException {
        return isProject(projectDirectory) ? new NeurophProject(projectDirectory , state) : null;
    }

    /**
     * Save a project to disk
     * @param project a project created with this factory's loadProject method
     * @throws IOException - if there is a problem saving
     * @throws ClassCastException - if this factory did not create this project
     */
    @Override
    public void saveProject(final Project project) throws IOException, ClassCastException {
        FileObject projectRoot = project.getProjectDirectory();
        if (projectRoot.getFileObject(PROJECT_DIR) == null) {
            throw new IOException("Project dir " + projectRoot.getPath() +
                    " deleted," +
                    " cannot save project");
        }
        //Force creation of the neuroph project type id dir if it was deleted:
        ((NeurophProject) project).getNeurophProjectFolder(true);
    }

}
