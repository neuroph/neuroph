package org.neuroph.util;

//import org.encog.engine.EncogEngine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This singleton holds global settings for the whole framework
 * @author Jeff Heaton
 */
public class Neuroph {
	
	private static Neuroph instance;

        /**
         * Flag to determine if flat network support from Encog is turned on
         */
	private boolean flattenNetworks = false;
	
	public static Neuroph getInstance() {
		if( instance==null )
			instance = new Neuroph();
		return instance;
	}
        
        public static String getVersion() {
            return "2.8";        }
		
	/**
         * Get setting for flatten network (from Encog engine)
	 * @return the flattenNetworks
	 */
	public boolean shouldFlattenNetworks() {
		return flattenNetworks;
	}

	/**
         * Turn on/off flat networ support from Encog
	 * @param flattenNetworks the flattenNetworks to set
	 */
	public void setFlattenNetworks(boolean flattenNetworks) {
		this.flattenNetworks = flattenNetworks;
	}

        /**
         * Shuts down the Encog engine
         */
	public void shutdown() {
		//EncogEngine.getInstance().shutdown();
	}
        
        public ArrayList<String> getInputFunctions() {
           try {
                ArrayList classes =  Neuroph.getClassNamesFromPackage("org.neuroph.core.input");
                classes.remove("InputFunction");
                return classes;
            } catch (IOException ex) {
                Logger.getLogger(Neuroph.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

        public ArrayList<String> getTransferFunctions() {
           try {
                ArrayList classes =  Neuroph.getClassNamesFromPackage("org.neuroph.core.transfer");
                classes.remove("TransferFunction");
                return classes;
            } catch (IOException ex) {
                Logger.getLogger(Neuroph.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        public ArrayList<String> getNeurons() {
           try {
                ArrayList classes =  Neuroph.getClassNamesFromPackage("org.neuroph.nnet.comp.neuron");
                classes.add(0, "Neuron");
                return classes;
            } catch (IOException ex) {
                Logger.getLogger(Neuroph.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }        
        
        public ArrayList<String> getLayers() {
           try {
                ArrayList classes =  Neuroph.getClassNamesFromPackage("org.neuroph.nnet.comp.layer");
                classes.add(0, "Layer");
                return classes;
            } catch (IOException ex) {
                Logger.getLogger(Neuroph.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }  
        
        public ArrayList<String> getLearningRules() {
           try {
                ArrayList classes =  Neuroph.getClassNamesFromPackage("org.neuroph.nnet.learning");
                return classes;
            } catch (IOException ex) {
                Logger.getLogger(Neuroph.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }        

    private static ArrayList<String> getClassNamesFromPackage(String packageName) throws IOException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL packageURL;
        ArrayList<String> names = new ArrayList<String>();;

        packageName = packageName.replace(".", "/");
        packageURL = classLoader.getResource(packageName);

        if(packageURL.getProtocol().equals("jar")){
            String jarFileName;
            JarFile jf ;
            Enumeration<JarEntry> jarEntries;
            String entryName;

            // build jar file name, then loop through zipped entries
            jarFileName = URLDecoder.decode(packageURL.getFile(), "UTF-8");
            jarFileName = jarFileName.substring(5,jarFileName.indexOf("!"));
            //System.out.println(">"+jarFileName);
            jf = new JarFile(jarFileName);
            jarEntries = jf.entries();
            while(jarEntries.hasMoreElements()){
                entryName = jarEntries.nextElement().getName();
                if(entryName.startsWith(packageName) && entryName.length()>packageName.length()+5){
                    entryName = entryName.substring(packageName.length(),entryName.lastIndexOf('.'));
                    names.add(entryName.substring(1));
                }
            }

        // loop through files in classpath
        }else{
            File folder = new File(packageURL.getFile());
            File[] contenuti = folder.listFiles();
            String entryName;
            for(File actual: contenuti){
                entryName = actual.getName();
                entryName = entryName.substring(0, entryName.lastIndexOf('.'));
                names.add(entryName);
            }
        }
        return names;
    }        
        
}
