package org.neuroph.bci.mindwave;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * zasto se javlja signal sa svim nulama? timestamp
 *
 * @author zoran
 */
public class MindWaveSample implements Serializable {
// {"eSense":{"attention":17,"meditation":29},"eegPower":{"delta":499299,"theta":69339,"lowAlpha":8930,"highAlpha":17974,"lowBeta":3034,"highBeta":7352,"lowGamma":5247,"highGamma":2960},"poorSignalLevel":0}    
    // eSense

    private int attention, meditation;
    // eegPower
    private int delta, theta, lowAlpha, highAlpha, lowBeta, highBeta, lowGamma, highGamma;
    // signal control flag
    private boolean poorSignalLevel;
    private Date timeStamp;
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

    public MindWaveSample(String clientData) throws JSONException {
        if (clientData == null) throw new RuntimeException("Null value from MindWave client!");
        
        JSONObject json = new JSONObject(clientData);

        if (!json.isNull("eegPower")) { // null i nule se javljaju zbog ovog uslova

            if (!json.isNull("poorSignalLevel")) {
                poorSignalLevel = false; // shouldnt we assume 1 here ?
            }

            if (!json.isNull("eSense")) {
                attention = json.getJSONObject("eSense").getInt("attention");
                meditation = json.getJSONObject("eSense").getInt("meditation");
            }

            JSONObject eegPower = json.getJSONObject("eegPower");
            delta = eegPower.getInt("delta");
            theta = eegPower.getInt("theta");
            lowAlpha = eegPower.getInt("lowAlpha");
            highAlpha = eegPower.getInt("highAlpha");
            lowBeta = eegPower.getInt("lowBeta");
            highBeta = eegPower.getInt("highBeta");
            lowGamma = eegPower.getInt("lowGamma");
            highGamma = eegPower.getInt("highGamma");

            timeStamp = new Date();
        } // else throw exception
    }

    public int getAttention() {
        return attention;
    }

    public int getMeditation() {
        return meditation;
    }

    public int getDelta() {
        return delta;
    }

    public int getTheta() {
        return theta;
    }

    public int getLowAlpha() {
        return lowAlpha;
    }

    public int getHighAlpha() {
        return highAlpha;
    }

    public int getLowBeta() {
        return lowBeta;
    }

    public int getHighBeta() {
        return highBeta;
    }

    public int getLowGamma() {
        return lowGamma;
    }

    public int getHighGamma() {
        return highGamma;
    }

    public boolean isPoorSignalLevel() {
        return poorSignalLevel;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String toString() {
        return "MindWaveSample{timeStamp=" + timeStamp + ", attention=" + attention + ", meditation=" + meditation + ", delta=" + delta + ", theta=" + theta + ", lowAlpha=" + lowAlpha + ", highAlpha=" + highAlpha + ", lowBeta=" + lowBeta + ", highBeta=" + highBeta + ", lowGamma=" + lowGamma + ", highGamma=" + highGamma + ", poorSignalLevel=" + poorSignalLevel +"}";
    }
    
    public long[] getValues() {
        long[] values = new long[10];
    
        values[0] = attention;// * 1000;
        values[1] = meditation;// * 1000;
        values[2] = delta; // /10;
        values[3] = theta; // /2;
        values[4] = lowAlpha;
        values[5] = highAlpha;
        values[6] = lowBeta;
        values[7] = highBeta;
        values[8] = lowGamma; 
        values[9] = highGamma;
        
        return values;
    }    
    
    public String getAsCsv() {
        return timeStamp +", "+attention+"," + meditation+","+delta+","+theta+","+lowAlpha+","+highAlpha+","+lowBeta+","+highBeta+","+lowGamma+","+highGamma;
    }
    


}
