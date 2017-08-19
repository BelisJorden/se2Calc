package domain.offence;

import domain.offence.Offence;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jorden on 15-8-2017.
 */
public class EmissionOffences {


    private List<Offence> emissionOffences;

    public EmissionOffences() {
        this.emissionOffences = new LinkedList<>();
    }

    public List<Offence> getEmissionOffences() {
        return emissionOffences;
    }

    public void addOffence(Offence offence) {
        emissionOffences.add(offence);
    }

    public void removeOffence() {
        emissionOffences.remove(0);
    }

}
