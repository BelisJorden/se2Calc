import domain.Penalty;
import domain.offence.EmissionOffence;
import domain.offence.EmissionOffences;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jorden on 19-8-2017.
 */
public class Test {
    public static void main(String[] args) {
        final int PENALTY_VALUES_RETRY = 10000; // 10 sec
        final boolean WITH_HISTORY = false;
        final int SECONDS_TO_SAVE_EMISSIONOFFENCES= 5;
        final int TIMES_TO_TRY_FOR_GETTING_LICENSCEPLATEAMOUNT = 3;


        InputService inputservice = new RabbitMQ("localhost", "nogistest");
        PenaltyService penaltyService = new PenaltyService();
        Calculator calculator = new Calculator();
        EmissionOffences emissionOffences = new EmissionOffences(SECONDS_TO_SAVE_EMISSIONOFFENCES);

        Controller controller = new Controller();
        controller.setInputService(inputservice);
        controller.setPenaltyService(penaltyService);
        controller.setCalculateWithHistory(WITH_HISTORY);
        controller.setCalculator(calculator);
        controller.setEmissionOffences(emissionOffences);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                controller.setPenalty(penaltyService.getPenalty());
            }
        }, 0, PENALTY_VALUES_RETRY);

        controller.start();
    }
}
