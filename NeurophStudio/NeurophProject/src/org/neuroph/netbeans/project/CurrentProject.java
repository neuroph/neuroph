package org.neuroph.netbeans.project;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.neuroph.netbeans.project.NeurophProject;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;

/**
 *
 * @author Ivana
 */
public class CurrentProject implements LookupListener {

    private static CurrentProject instance;
    private NeurophProject currentProject;
    private Result<NeurophProject> result;
    private List<NeurophProject> allProjects;

    public static CurrentProject getInstance() {
        if (instance == null) {
            instance = new CurrentProject();
        }
        return instance;
    }

    private CurrentProject() {
        result = Utilities.actionsGlobalContext().lookupResult(NeurophProject.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));
        allProjects=new LinkedList<NeurophProject>();
    }


    public NeurophProject getCurrentProject() {
        return currentProject;
    }

    public void resultChanged(LookupEvent le) {
        Lookup.Result localresult = (Result) le.getSource();
        Collection<Object> coll = localresult.allInstances();
        if (!coll.isEmpty()) {
            for (Object nnn : coll) {
                if (nnn instanceof NeurophProject) {
                    currentProject = (NeurophProject) nnn;
                    if(!allProjects.contains((NeurophProject)nnn))
                    {allProjects.add((NeurophProject) nnn);}
                }

            }

        }
    }
    public List<NeurophProject> getCurrentProjects(){
        return allProjects;
    }

    public void setCurrentProject(NeurophProject np){
        currentProject=np;
    }

}
