import be.kdg.se3.services.PenaltyServiceProxy;

import domain.entity.Penalty;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PenaltyService {
    private PenaltyServiceProxy penaltyServiceProxy = new PenaltyServiceProxy();
    private static final String BASEURL = "www.services4se3.com/penalty/";
    private Logger logger = Logger.getLogger(PenaltyService.class);
    private Penalty penalty;

    private int defaultEmissionFactor;
    private int defaultSpeedfactor;
    private int defaultHistoryfactor;
    private int retryAmount;
    private int retryAfterSeconds;

    public PenaltyService(int defaultEmissionFactor, int defaultSpeedfactor, int defaultHistoryfactor,int retryAmount,int retryAfterSeconds) {
        this.defaultEmissionFactor = defaultEmissionFactor;
        this.defaultSpeedfactor = defaultSpeedfactor;
        this.defaultHistoryfactor = defaultHistoryfactor;
        this.retryAmount = retryAmount;
        this.retryAfterSeconds = retryAfterSeconds;
        this.penalty = new Penalty();
    }

    public Penalty getPenalty() {
        return penalty;
    }


    public void  getPenaltyValues() {

        String url = BASEURL+"settings";
        String jsonString = null;
        try {
            jsonString = penaltyServiceProxy.get(url);
        } catch (IOException e) {

            logger.error("Error during communication with PenaltyServiceProxy",e);

            if (penalty.getEmissionFactor()==0 && penalty.getHistoryFactor()==0 && penalty.getSpeedFactor()==0) {
                setDefaultValues();
                logger.info("Now Using penalty defaultvalues");
            } else {
                logger.info("Using cached penalty values");
            }

        }

        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                penalty.setEmissionFactor(jsonObject.getInt("emissionFactor"));
                penalty.setSpeedFactor(jsonObject.getInt("speedFactor"));
                penalty.setHistoryFactor(jsonObject.getInt("historyFactor"));
            } catch (JSONException e) {
                logger.error("Error during json conversion in PenaltyService",e);
            }
        }


    }

    private void setDefaultValues() {
        penalty.setEmissionFactor(defaultEmissionFactor);
        penalty.setHistoryFactor(defaultHistoryfactor);
        penalty.setSpeedFactor(defaultSpeedfactor);
    }


    public int getAmountForLicensceplate(String licencePlate) {
        String url = BASEURL+licencePlate;
        String jsonString = null;
        int amount = 0;
        String error = null;
        String description = null;
        for (int i = 0; i < retryAmount+1; i++) {
            try {
                jsonString = penaltyServiceProxy.get(url);
                i=retryAmount;
            } catch (IOException e) {
                logger.error("Error during communication with PenaltyServiceProxy",e);
                if (i + 1 == retryAmount) {
                    logger.info("After retrying for " + retryAmount + " times " +
                            "the error still couldn't be resolved, no calculation will be done for this offence ");
                }
                try {
                    TimeUnit.SECONDS.sleep(retryAfterSeconds);
                } catch (InterruptedException ie) {
                    logger.error("Error while waiting to retry call for licenceplate",ie);
                }
            }
        }

        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject.has("amountOfPastPenalties")) {
                    amount = jsonObject.getInt("amountOfPastPenalties");
                }
                if (jsonObject.has("error")) {
                    error = jsonObject.getString("error");
                }
                if (jsonObject.has("description")) {
                    description = jsonObject.getString("description");
                }
            } catch (JSONException e) {
                logger.error("Error during json conversion in PenaltyService",e);
            }

        }
        if (error != null && description != null) {
            logger.info("Unknown or invalid licenseplate " + description);
        }

        return amount;
    }
}
