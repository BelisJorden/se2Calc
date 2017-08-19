package domain;

/**
 * Created by jorden on 14-8-2017.
 */
public class Penalty {

    private int speedFactor;
    private int emissionFactor;
    private int historyFactor;

    public Penalty() {
    }


    public int getSpeedFactor() {
        return speedFactor;
    }

    public int getEmissionFactor() {
        return emissionFactor;
    }

    public int getHistoryFactor() {
        return historyFactor;
    }

    public void setSpeedFactor(int speedFactor) {
        this.speedFactor = speedFactor;
    }

    public void setEmissionFactor(int emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public void setHistoryFactor(int historyFactor) {
        this.historyFactor = historyFactor;
    }
}
