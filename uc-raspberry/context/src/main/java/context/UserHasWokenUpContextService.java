package context;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Input context: light level of the bed phone.
 * Output context: Whether or not the user is asleep.
 */
public class UserHasWokenUpContextService extends ContextService {
    private String lightLevelBedPhone;

    @Override
    public void update() {
        // TODO re-evaluate the high level context based on the newly available low-level context.
        throw new NotImplementedException();
    }

    @Override
    public List<String> getInputContextKeys() {
        List<String> list = new ArrayList<>();
        list.add(lightLevelBedPhone);

        return list;
    }
}
