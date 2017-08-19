import domain.Penalty;
import domain.offence.EmissionOffence;
import domain.offence.EmissionOffences;
import domain.offence.Offence;
import domain.offence.SpeedingOffence;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jorden on 19-8-2017.
 */
public class Controller implements InputListener {
    private Logger logger = Logger.getLogger(Controller.class);

    private InputService inputService;
    private PenaltyService penaltyService;
    private Penalty penalty;
    private boolean calculateWithHistory;
    private Calculator calculator;
    private EmissionOffences emissionOffences;

    public void setEmissionOffences(EmissionOffences emissionOffences) {
        this.emissionOffences = emissionOffences;
    }

    public PenaltyService getPenaltyService() {
        return penaltyService;
    }

    public Calculator getCalculator() {
        return calculator;
    }

    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    public void setPenaltyService(PenaltyService penaltyService) {
        this.penaltyService = penaltyService;
    }

    public Penalty getPenalty() {
        return penalty;
    }

    public boolean isCalculateWithHistory() {
        return calculateWithHistory;
    }

    public void setCalculateWithHistory(boolean calculateWithHistory) {
        this.calculateWithHistory = calculateWithHistory;
    }

    public void setPenalty(Penalty penalty) {
        this.penalty = penalty;
    }

    public InputService getInputService() {
        return inputService;
    }

    public void setInputService(InputService inputService) {
        this.inputService = inputService;
    }

    public void start() {
        try {
            inputService.initialize(this);
        } catch (CommunicationException e) {
            logger.fatal("Unable to initialize communication channel", e);
        }
    }

    public void stop() {
        try {
            inputService.shutdown();
        } catch (CommunicationException e) {
            logger.error("Unable to properly shut down communication channel");
        }
    }


    @Override
    public void onReceive(Offence offence) {
        int price = 0;
        boolean ignoreEmissionOffence = false;

        if (offence instanceof EmissionOffence) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            emissionOffences.addEmissionOffence((EmissionOffence) offence,timestamp);
            ignoreEmissionOffence = checkSavedEmissionOffences((EmissionOffence) offence);
        }

        try {
            //Calculate without history
            if (!calculateWithHistory) {
                if (offence instanceof SpeedingOffence) {
                    SpeedingOffence speedingOffence = (SpeedingOffence) offence;
                    price = calculator.calculateSpeedWithoutHistory(penalty, speedingOffence);

                } else if (offence instanceof EmissionOffence && !ignoreEmissionOffence) {
                    price = calculator.calculateEmissionWithoutHistory(penalty);
                }
                //Calculate with history
            } else {
                if (offence instanceof SpeedingOffence) {
                    SpeedingOffence speedingOffence = (SpeedingOffence) offence;
                    price = calculator.calculateSpeedWithoutHistory(penalty, speedingOffence);
                    price = calculator.calculateWithHistory(price,penaltyService.getAmountForLicensceplate(offence.getLicencePlate()),penalty);

                } else if (offence instanceof EmissionOffence && !ignoreEmissionOffence) {
                    price = calculator.calculateEmissionWithoutHistory(penalty);
                    price = calculator.calculateWithHistory(price, penaltyService.getAmountForLicensceplate(offence.getLicencePlate()), penalty);
                }
            }
            System.out.println("Penalty values " + penalty.getEmissionFactor() + penalty.getSpeedFactor() + penalty.getHistoryFactor());
            System.out.println("Calculated price " + price);
        } catch (Exception e) {
            logger.error("Unexpected error during message handling", e);
        }

        if (!ignoreEmissionOffence) {
           // sendToOutputQue();
        }

        clearSavedEmissionOffencesIfExpired(emissionOffences.getTimeToSave());
    }

    private void clearSavedEmissionOffencesIfExpired(int secondsToSave) {
        // clear after amount of time
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        List<EmissionOffence> emissionOffencesToRemove = new ArrayList<>();
        for (Map.Entry<EmissionOffence,Timestamp> pair : emissionOffences.getSavedEmissionOffences().entrySet()) {
            Timestamp savedTimestamp =  pair.getValue();
            long milliseconds = currentTimestamp.getTime() - savedTimestamp.getTime();
            int seconds = (int) milliseconds / 1000;
            if (seconds >= secondsToSave) {
                emissionOffencesToRemove.add(pair.getKey());
                System.out.println("Offence will be cleared after " + secondsToSave + " seconds");

            }
        }
        emissionOffencesToRemove.forEach(o -> emissionOffences.removeOffence(o));

            


    }

    private boolean checkSavedEmissionOffences(EmissionOffence emissionOffence) {
        boolean inSavedList =false;
        // see if offence city and licensceplate is in list
        for (EmissionOffence savedEmissionOffence : emissionOffences.getSavedEmissionOffences().keySet()){

            if (emissionOffence.getLicencePlate().equals(savedEmissionOffence.getLicencePlate()) &&
                    emissionOffence.getCity().equals(savedEmissionOffence.getCity())) {
                inSavedList = true;

            }
        }

        return  inSavedList;
    }

}
