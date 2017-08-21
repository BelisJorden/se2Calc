package domain;

import domain.entity.Penalty;
import domain.entity.SpeedingOffence;


public class Calculator {


   public int calculateSpeedWithoutHistory(Penalty penalty, SpeedingOffence speedingOffence) {

      return ((speedingOffence.getSpeed() - speedingOffence.getMaxSpeed()) * penalty.getSpeedFactor());
   }

   public int calculateEmissionWithoutHistory(Penalty penalty) {
      return penalty.getEmissionFactor();
   }

   public int calculateWithHistory(int priceWithoutHistory, int offenceAmount, Penalty penalty) {
      return priceWithoutHistory + (offenceAmount * penalty.getHistoryFactor());

   }

   /* public void calculate(Penalty penalty, Offence entity, boolean withHistory) {



        //mss wel ergens anders checken en dan gwn deze methode niet aanroepen
        boolean reccuringEmmisionOffence = checkRecurringEmmisionOffence(entity);
        if (!reccuringEmmisionOffence) {
            int priceWithoutHistory = calculateWithoutHistory(penalty, entity);
            int price;
            if (withHistory) {
                price = calculateWithHistory(penalty, entity, priceWithoutHistory);

            } else {
                price = priceWithoutHistory;
            }
        }
    }

    private boolean checkRecurringEmmisionOffence(Offence entity) {
        boolean reccuringEmmsionOffence = false;
        for (Offence emmisionOffence : emmisionOffences.getEmissionOffences()) {
            if (emmisionOffence.getLicenseplate == entity.getLisencePLate && emmisionOffence.getCity == entity.getCity) {
                reccuringEmmsionOffence = true;
            }
        }
        return reccuringEmmsionOffence;
    }

    private int calculateWithoutHistory(Penalty penalty,Offence entity) {
        int price;
        if (entity == speedOffence) {
            price = (entity.getSpeed() * entity.getMaxSpeed() * penalty.getSpeedFactor());
        } else {
            price = penalty.getEmissionFactor();
        }
        return price;
    }

    private int calculateWithHistory(Penalty penalty, Offence entity, int priceWithoutHistory) {
        return priceWithoutHistory + (penaltyService.amountOfPastPenaltys* penalty.getHistoryFactor());
    }



    //verwijder emmisionoffences na ingestelde tijd
    Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        @Override
        public void run() {
            // Your database code here
            emmisionOFfences.removeOfence();
        }
    }10000, 10000);

    // of dit
    Runnable helloRunnable = new Runnable() {
        public void run() {
           emmisionOFfences.removeOfence();
        }
    };

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);*/


//executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);

}
