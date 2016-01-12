package context;

import java.util.List;

public abstract class ContextService {

    public abstract void update();
    public abstract List<String> getInputContextKeys();
}
