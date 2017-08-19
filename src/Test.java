import domain.Penalty;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jorden on 19-8-2017.
 */
public class Test {
    public static void main(String[] args) {
        PenaltyService penaltyService = new PenaltyService();
        final int PENALTY_VALUES_RETRY = 10000; // 10 sec

        InputService inputservice = new RabbitMQ("localhost", "nogistest");
        Controller controller = new Controller();
        controller.setInputService(inputservice);

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
