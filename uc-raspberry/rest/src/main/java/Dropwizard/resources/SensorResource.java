package Dropwizard.resources;

import com.google.common.base.Optional;
import context.ContextService;
import context.ContextUtil;
import storage.Storage;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

@Path("/sensor-data")
public class SensorResource {
    private final static Logger logger = Logger.getLogger(SensorResource.class.getName());
    private List<ContextService> contextServices;

    public SensorResource(List<ContextService> contextServices) {
        this.contextServices = contextServices;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    //Test with: curl -i -d "key=testkey3&value=testvalue3" http://localhost:8080/sensor-data
    public void receiveKeyValuePair(@FormParam("key") Optional<String> key, @FormParam("value") Optional<String> value){
        Storage storage = new Storage("localhost");
        String keyString = key.get();
        String valueString = value.get();

        if(keyString == null){
            logger.warning("Attempted to insert empty key, storing failed.");
            return;
        }else if(valueString == null){
            logger.warning("Attempted to insert empty value, storing failed.");
            return;
        }

        logger.info(String.format("Received key = %s, value = %s", keyString, valueString));
        storage.store(keyString, valueString);

        List<ContextService> servicesToUpdate = ContextUtil.filterOnKey(contextServices, keyString);

        servicesToUpdate.forEach(ContextService::update);
    }
}
