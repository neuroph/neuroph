package org.neuroph.bci.mindwave.sampling;

import java.util.ArrayList;
import java.util.List;
import org.neuroph.bci.mindwave.MindWaveSample;

/**
 *
 * @author Zoran Sevarac
 */
public class MentalImageSample {
   String name;
   List<MindWaveSample> samples;

    public MentalImageSample(String name) {
        this.name = name;
        this.samples = new ArrayList<>();
    }

    public MentalImageSample(String name, List<MindWaveSample> samples) {
        this.name = name;
        this.samples = samples;
    }
    
       
    public void addMindWaveSample(MindWaveSample sample) {
        this.samples.add(sample);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MindWaveSample> getSamples() {
        return samples;
    }

    public void setSamples(List<MindWaveSample> samples) {
        this.samples = samples;
    }
    // prva tri preskoci, a poslednji za binarnu klasifikaciju; ovo izmedju FFT i normalizacija
    // timeStamp,attention,meditation,delta,theta,lowAlpha,highAlpha,lowBeta,highBeta,lowGamma,highGamma, name
    public String getAsCsv() {
        StringBuilder sb= new StringBuilder();
        
        for(MindWaveSample sample : samples) {
            sb.append(sample.getAsCsv()).append(", "+name).append(System.lineSeparator());
        }                
        return sb.toString();
    }
   
}
