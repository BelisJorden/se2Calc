import be.kdg.se3.services.PenaltyServiceProxy;
import domain.Penalty;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

/**
 * Created by jorden on 14-8-2017.
 */
public class PenaltyService {
    private PenaltyServiceProxy penaltyService = new PenaltyServiceProxy();
    private final String BASEURL = "www.services4se3.com/penalty/";

    public Penalty getPenalty() {

        String url = BASEURL+"settings";
        Penalty penalty = new Penalty();
        String jsonString = null;
        try {
            jsonString = penaltyService.get(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("oops");
            // hier defaultwaarde geven
            penalty = getDefaultValues();
        }


        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                penalty.setEmissionFactor(jsonObject.getInt("emissionFactor"));
                penalty.setSpeedFactor(jsonObject.getInt("speedFactor"));
                penalty.setHistoryFactor(jsonObject.getInt("historyFactor"));
                System.out.println(jsonObject.getInt("speedFactor") + " " + jsonObject.getInt("emissionFactor") + " " + jsonObject.getInt("historyFactor"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return penalty;

    }

    private Penalty getDefaultValues() {
        Penalty penalty = new Penalty();
        penalty.setEmissionFactor(2);
        penalty.setHistoryFactor(3);
        penalty.setSpeedFactor(2);
        return penalty;
    }


    public int getAmountForLicensceplate(String licencePlate) {
        String url = BASEURL+licencePlate;
        String jsonString = null;
        int amount = 0;
        try {
            jsonString = penaltyService.get(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("oops");
        }

        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                amount = jsonObject.getInt("amountOfPastPenalties");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return amount;
    }
}
