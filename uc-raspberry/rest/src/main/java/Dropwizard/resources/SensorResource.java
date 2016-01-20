package Dropwizard.resources;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.base.Optional;
import context.ContextService;
import context.ContextUtil;
import storage.Storage;

import javax.ws.rs.*;
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

        if(keyString.contains("_raw")){
            String discretizedValue = ContextService.discretize(keyString, valueString);
            keyString = keyString.substring(0,keyString.length() - 4);

            storage.store(keyString, discretizedValue);
        }

        List<ContextService> servicesToUpdate = ContextUtil.filterOnKey(contextServices, keyString);

        servicesToUpdate.forEach(ContextService::update);
    }

    @POST
//    @Path("json")
    @Consumes(MediaType.APPLICATION_JSON)
    public void receiveJSON(JSONPObject jsonObject){
        Storage storage = new Storage("localhost");

        if(jsonObject == null){
            logger.warning("Attempted to insert empty key, storing failed.");
            return;
        }

        logger.info(String.format(jsonObject.toString()));

    }


}
