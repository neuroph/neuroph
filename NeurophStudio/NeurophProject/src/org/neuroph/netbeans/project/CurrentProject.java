package org.neuroph.netbeans.project;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
        
    public final static String DEFAULT_NEURAL_NETWORK_NAME = "NewNeuralNetwork";    

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
        allProjects=new LinkedList<>();
    }


    public NeurophProject getCurrentProject() {
        return currentProject;
    }

    @Override
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

    public String getNewNeuralNetworkName() {   
        if (currentProject==null) return "";
        
        String newNetworkName;
        int networkCount=0;
        boolean foundName=false;
        do {
            networkCount++;
            newNetworkName = DEFAULT_NEURAL_NETWORK_NAME + networkCount;    
            String filePath = currentProject.getProjectDirectory().getPath() +"/" + NeurophProject.NEURAL_NETWORKS_DIR +"/"+ newNetworkName+".nnet";
            File file = new File(filePath);
            if (!file.exists()) foundName = true;
            
        } while(!foundName);
        
        newNetworkName = DEFAULT_NEURAL_NETWORK_NAME + networkCount;
        
        return newNetworkName;
    }

}