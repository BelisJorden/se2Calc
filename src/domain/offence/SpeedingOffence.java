package domain.offence;

import domain.offence.Offence;

import java.sql.Timestamp;

/**
 * Created by jorden on 2-8-2017.
 */
public class SpeedingOffence extends Offence {
    /*private Timestamp timestamp;
    private String licencePlate;
    private String street;
    private String city;*/
    private int maxSpeed;
    private int speed;

    public SpeedingOffence() {
    }

    public SpeedingOffence(int speed , int maxSpeed, Timestamp timestamp, String licencePlate, String city, String street ) {
      //  super(timestamp, licencePlate, street, city);
        /*this.timestamp =timestamp;
        this.licencePlate=licencePlate;
        this.street=street;
        this.city = city;*/
        this.maxSpeed = maxSpeed;
        this.speed = speed;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return  "SpeedingOffence{" +
                "maxSpeed=" + maxSpeed +
                ", speed=" + speed +
                '}' + super.toString();
    }
}
