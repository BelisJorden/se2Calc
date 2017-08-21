import domain.entity.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;

public class Controller implements InputListener {
    private Logger logger = Logger.getLogger(Controller.class);

    private InputService inputService;
    private PenaltyService penaltyService;

    private boolean calculateWithHistory;
    private Calculator calculator;
    private EmissionOffences emissionOffences;
    private OutputService outputService;


    public void setOutputService(OutputService outputService) {
        this.outputService = outputService;
    }

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



    public boolean isCalculateWithHistory() {
        return calculateWithHistory;
    }

    public void setCalculateWithHistory(boolean calculateWithHistory) {
        this.calculateWithHistory = calculateWithHistory;
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
        boolean licencePlateError = false;
        emissionOffences.clearSavedEmissionOffencesIfExpired();
        if (offence instanceof EmissionOffence) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ignoreEmissionOffence = emissionOffences.checkSavedEmissionOffences((EmissionOffence) offence);
            emissionOffences.addEmissionOffence((EmissionOffence) offence,timestamp);

        }

        try {
            //Calculate without history
            if (!calculateWithHistory) {
                if (offence instanceof SpeedingOffence) {
                    SpeedingOffence speedingOffence = (SpeedingOffence) offence;
                    price = calculator.calculateSpeedWithoutHistory(penaltyService.getPenalty(), speedingOffence);

                } else if (offence instanceof EmissionOffence && !ignoreEmissionOffence) {
                    price = calculator.calculateEmissionWithoutHistory(penaltyService.getPenalty());
                }
                //Calculate with history
            } else {
                 int amountForLicenseplate = penaltyService.getAmountForLicensceplate(offence.getLicencePlate());

                if (amountForLicenseplate == 0) {
                    licencePlateError = true;

                }
                if (offence instanceof SpeedingOffence && !licencePlateError) {
                    SpeedingOffence speedingOffence = (SpeedingOffence) offence;
                    price = calculator.calculateSpeedWithoutHistory(penaltyService.getPenalty(), speedingOffence);
                    price = calculator.calculateWithHistory(price,amountForLicenseplate,penaltyService.getPenalty());

                } else if (offence instanceof EmissionOffence && !ignoreEmissionOffence && !licencePlateError) {
                    price = calculator.calculateEmissionWithoutHistory(penaltyService.getPenalty());
                    price = calculator.calculateWithHistory(price, amountForLicenseplate, penaltyService.getPenalty());
                }
            }
            System.out.println("Penalty values " + penaltyService.getPenalty().getEmissionFactor() + penaltyService.getPenalty().getSpeedFactor() + penaltyService.getPenalty().getHistoryFactor());
            System.out.println("Calculated price " + price);
        } catch (Exception e) {
            logger.error("Unexpected error during message handling", e);
        }

        if (ignoreEmissionOffence) {
            logger.info("This offence will be ignored because it already occured in this zone some time ago");
        }

        if (!ignoreEmissionOffence && !licencePlateError) {
            OutputMessage outputMessage = new OutputMessage(offence, price);
           outputService.publish(outputMessage);
            System.out.println("Set the price " + price + " on the outputque");
        }

    }

   /* private void clearSavedEmissionOffencesIfExpired(int secondsToSave) {
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
        // see if entity city and licensceplate is in list
        for (EmissionOffence savedEmissionOffence : emissionOffences.getSavedEmissionOffences().keySet()){

            if (emissionOffence.getLicencePlate().equals(savedEmissionOffence.getLicencePlate()) &&
                    emissionOffence.getCity().equals(savedEmissionOffence.getCity())) {
                inSavedList = true;

            }
        }

        return  inSavedList;
    }*/

}
