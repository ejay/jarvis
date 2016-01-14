package context;

import storage.Storage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.List;

/**
 * Input context: light level of the bed phone.
 * Output context: Whether or not the user is asleep.
 */
public class UserHasWokenUpContextService extends ContextService {
    private final static String[] inputContextKeys = new String[]{"BedPhoneLight"};
    private final static String outputContextKey = "UserHasWokenUp";

    @Override
    public void update() {
        // 1. fetch input contexts from redis
        Storage storage = new Storage("localhost");
        String bedPhoneLight = storage.get(inputContextKeys[0]);

        // 2. evaluate
        String hasWokenUpString;
        //TODO improve this logic.
        if(bedPhoneLight.equals("low")){
            hasWokenUpString = "false";
        }else{
            hasWokenUpString = "true";
        }


        // 3. store output context in redis
        storage.store(outputContextKey, hasWokenUpString);
    }

    @Override
    public List<String> getInputContextKeys() {
        return Arrays.asList(inputContextKeys);
    }
}
