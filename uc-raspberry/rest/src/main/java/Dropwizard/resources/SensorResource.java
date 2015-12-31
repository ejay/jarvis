package Dropwizard.resources;

import com.google.common.base.Optional;
import storage.Storage;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("/sensor-data")
public class SensorResource {
    private final static Logger logger = Logger.getLogger(SensorResource.class.getName());

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void receiveSensorData(@FormParam("timestamp") Optional<String> timestamp,
                           @FormParam("sensorId") Optional<String> sensorId,
                           @FormParam("sensorType") Optional<String> sensorType,
                           @FormParam("value") Optional<String> value) {
        logger.info(String.format("Received sensor data: timestamp = %s, sensorId = %s, sensorType = %s, value = %s",
                timestamp, sensorId, sensorType, value));

        Storage storage = new Storage("localhost");
        storage.store("light", value.get());
        //Test with: curl -i -d "timestamp=time3&sensorId=sensorid3&sensorType=sensortype3&value=value3" http://localhost:8080/sensor-data
    }
}
