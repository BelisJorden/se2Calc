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
    private Penalty penalty;

    public Penalty getPenalty() {
        return penalty;
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
        try {
            if (offence instanceof SpeedingOffence) {

             //   System.out.println(((SpeedingOffence) offence).getMaxSpeed());
            //    System.out.println("Unmarshalled Speed: " + offence.toString());
            } else {
           //     System.out.println(((EmissionOffence) offence).getEuronorm());
           //     System.out.println("Unmarshalled Emission" + offence.toString());

            }
            System.out.println("Penalty values " + penalty.getEmissionFactor() + penalty.getSpeedFactor() + penalty.getHistoryFactor());



        } catch (Exception e) {
            logger.error("Unexpected error during message handling", e);
        }
    }

}
