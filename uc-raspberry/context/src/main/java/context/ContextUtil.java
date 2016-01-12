package context;

import java.util.ArrayList;
import java.util.List;

public class ContextUtil {

    public static List<ContextService> filterOnKey(List<ContextService> contextServices, String key){
        List<ContextService> returnList = new ArrayList<>();

        for(ContextService contextService : contextServices){
            if(contextService.getInputContextKeys().contains(key)){
                returnList.add(contextService);
            }
        }

        return returnList;
    }
}
