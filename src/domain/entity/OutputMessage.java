package domain.entity;


import domain.entity.Offence;


public class OutputMessage {
    private Offence offence;
    private int offenceAmount;

    public OutputMessage(Offence offence, int offenceAmount) {
        this.offence = offence;
        this.offenceAmount = offenceAmount;
    }

    public Offence getOffence() {
        return offence;
    }

    public int getOffenceAmount() {
        return offenceAmount;
    }
}
