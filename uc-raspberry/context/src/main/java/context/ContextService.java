package context;

import java.util.List;
import java.util.logging.Logger;

public abstract class ContextService {
    private static final Logger logger = Logger.getLogger(ContextService.class.getName());
    public abstract void update();
    public abstract List<String> getInputContextKeys();

    public static String discretize(String keyString, String valueString) {
        String discretizedValue = "";

        switch (keyString){
            case "BedPhoneLight_raw":
            case "RoomLight_raw":
                Double value = Double.parseDouble(valueString);

                if(value < 50){
                    discretizedValue = "low";
                }else if(value >= 50 && value < 500){
                    discretizedValue = "medium";
                }else if(value >= 500){
                    discretizedValue = "high";
                }
                logger.info("discretized key: " + keyString+":"+discretizedValue);
                break;
            case "CurrentSleepCycleUser_raw":
                Double sleepState = Double.parseDouble(valueString);

                if(sleepState == 0){
                    discretizedValue = "sleep";
                }else if(sleepState == 1){
                    discretizedValue = "rem";
                }else if(sleepState == 2){
                    discretizedValue = "awake";
                }
                logger.info("discretized key: " + keyString+":"+discretizedValue);

                break;
            default:
                logger.warning("Attempted to discretize unkown key: " + keyString);
        }

        return discretizedValue;
    }
}
