package context;

import storage.Storage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Input context: light level of the bed phone.
 * Output context: Whether or not the user is asleep.
 */
public class UserHasWokenUpContextService extends ContextService {
    private static final Logger logger = Logger.getLogger(UserHasWokenUpContextService.class.getName());
    private final static String[] inputContextKeys = new String[]{"BedPhoneLight", "CurrentSleepCycleUser"};
    private final static String outputContextKey = "UserHasWokenUp";

    @Override
    public void update() {
        // 1. fetch input contexts from redis
        Storage storage = new Storage("localhost");
        String bedPhoneLight = storage.get(inputContextKeys[0]);
        String currentSleepCycleUser = storage.get(inputContextKeys[1]);

        // 2. evaluate
        String hasWokenUpString;
        //TODO improve this logic.
        if(bedPhoneLight.equals("low")){
            hasWokenUpString = "false";
        }else{
            hasWokenUpString = "true";
        }

        if(currentSleepCycleUser.equals("sleep") || currentSleepCycleUser.equals("rem")){
            hasWokenUpString = "false";
        }else if(currentSleepCycleUser.equals("awake")){
            hasWokenUpString = "true";
        }

        // 3. store output context in redis
        storage.store(outputContextKey, hasWokenUpString);
        logger.info(String.format("Stored high level context UserHasWokenUp = %s from low level atoms %s = {%s, %s}", hasWokenUpString, Arrays.toString(inputContextKeys), bedPhoneLight, currentSleepCycleUser));
    }

    @Override
    public List<String> getInputContextKeys() {
        return Arrays.asList(inputContextKeys);
    }
}
