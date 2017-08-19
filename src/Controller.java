import domain.Penalty;
import domain.offence.EmissionOffence;
import domain.offence.Offence;
import domain.offence.SpeedingOffence;
import org.apache.log4j.Logger;

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
        try {
            //Calculate without history
            if (!calculateWithHistory) {
                if (offence instanceof SpeedingOffence) {
                    SpeedingOffence speedingOffence = (SpeedingOffence) offence;
                    price = calculator.calculateSpeedWithoutHistory(penalty, speedingOffence);

                } else if (offence instanceof EmissionOffence) {
                    price = calculator.calculateEmissionWithoutHistory(penalty);
                }
                //Calculate with history
            } else {
                if (offence instanceof SpeedingOffence) {
                    SpeedingOffence speedingOffence = (SpeedingOffence) offence;
                    price = calculator.calculateSpeedWithoutHistory(penalty, speedingOffence);
                    price = calculator.calculateWithHistory(price,penaltyService.getAmountForLicensceplate(offence.getLicencePlate()),penalty);

                } else if (offence instanceof EmissionOffence) {
                    price = calculator.calculateEmissionWithoutHistory(penalty);
                    price = calculator.calculateWithHistory(price, penaltyService.getAmountForLicensceplate(offence.getLicencePlate()), penalty);
                }
            }
            System.out.println("Penalty values " + penalty.getEmissionFactor() + penalty.getSpeedFactor() + penalty.getHistoryFactor());

        } catch (Exception e) {
            logger.error("Unexpected error during message handling", e);
        }
    }

}
