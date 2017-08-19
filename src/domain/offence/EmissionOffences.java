package domain.offence;

import java.sql.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class EmissionOffences {

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


}
