import adapters.input.RabbitMQInput;
import adapters.output.RabbitMQOutput;
import domain.Calculator;
import domain.Controller;
import domain.PenaltyService;
import domain.entity.EmissionOffences;
import domain.service.InputService;
import domain.service.OutputService;

import java.util.Timer;
import java.util.TimerTask;

public class Test {
    public static void main(String[] args) {
        final int PENALTY_VALUES_RETRY_SECONDS = 10; // 10 sec
        final boolean WITH_HISTORY = true;
        final int SECONDS_TO_SAVE_EMISSIONOFFENCES= 30;

        final int TIMES_TO_TRY_FOR_GETTING_LICENSCEPLATEAMOUNT = 3;
        final int SECONDS_TO_WAIT_BEFORE_RETRYING_LICENSCEPLATE = 2;
        final int DEFAULT_EMISSIONFACTOR = 55;
        final int DEFAULT_SPEEDFACTOR = 5;
        final int DEFAULT_HISTORYFACTOR =10;


        InputService inputservice = new RabbitMQInput("localhost", "nogistest");
        PenaltyService penaltyService = new PenaltyService(DEFAULT_EMISSIONFACTOR,DEFAULT_SPEEDFACTOR,DEFAULT_HISTORYFACTOR
                ,TIMES_TO_TRY_FOR_GETTING_LICENSCEPLATEAMOUNT,SECONDS_TO_WAIT_BEFORE_RETRYING_LICENSCEPLATE);
        Calculator calculator = new Calculator();
        EmissionOffences emissionOffences = new EmissionOffences(SECONDS_TO_SAVE_EMISSIONOFFENCES);
        OutputService outputService = new RabbitMQOutput("output", "localhost");

        Controller controller = new Controller();
        controller.setInputService(inputservice);
        controller.setPenaltyService(penaltyService);
        controller.setCalculateWithHistory(WITH_HISTORY);
        controller.setCalculator(calculator);
        controller.setEmissionOffences(emissionOffences);
        controller.setOutputService(outputService);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
               penaltyService.getPenaltyValues();
            }
        }, 0, PENALTY_VALUES_RETRY_SECONDS *1000);

        controller.start();
    }
}
