package domain.entity;

import org.apache.log4j.Logger;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EmissionOffences {

    private Logger logger = Logger.getLogger(EmissionOffences.class);
    private Map<EmissionOffence,Timestamp> savedEmissionOffences;
    private int timeToSave;

    public EmissionOffences(int timeToSave) {
        this.savedEmissionOffences = new HashMap<>();
        this.timeToSave = timeToSave;
    }

    public Map<EmissionOffence, Timestamp> getSavedEmissionOffences() {
        return savedEmissionOffences;
    }

    public int getTimeToSave() {
        return timeToSave;
    }

    public void addEmissionOffence(EmissionOffence emissionOffence, Timestamp timestamp) {
        this.savedEmissionOffences.put(emissionOffence, timestamp);
    }

    public void removeOffence(EmissionOffence offence) {
        savedEmissionOffences.remove(offence);
    }


    public void clearSavedEmissionOffencesIfExpired() {
        // clear after amount of time
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        List<EmissionOffence> emissionOffencesToRemove = new ArrayList<>();
        for (Map.Entry<EmissionOffence,Timestamp> pair : savedEmissionOffences.entrySet()) {
            Timestamp savedTimestamp =  pair.getValue();
            long milliseconds = currentTimestamp.getTime() - savedTimestamp.getTime();
            int seconds = (int) milliseconds / 1000;
            if (seconds >= timeToSave) {
                emissionOffencesToRemove.add(pair.getKey());
                logger.info("EmissionOffence will be cleared after being saved for  " + timeToSave + " seconds");

            }
        }
        emissionOffencesToRemove.forEach(o -> savedEmissionOffences.remove(o));




    }

    public boolean checkSavedEmissionOffences(EmissionOffence emissionOffence) {
        boolean inSavedList =false;
        // see if entity city and licensceplate is in list
        for (EmissionOffence savedEmissionOffence : savedEmissionOffences.keySet()){

            if (emissionOffence.getLicencePlate().equals(savedEmissionOffence.getLicencePlate()) &&
                    emissionOffence.getCity().equals(savedEmissionOffence.getCity())) {
                inSavedList = true;

            }
        }

        return  inSavedList;
    }


}
