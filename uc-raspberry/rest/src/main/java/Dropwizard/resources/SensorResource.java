package Dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

import javax.ws.rs.*;
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
//                timestamp.get(), sensorId.get(), sensorType.get(), value.get()));
        // http://localhost:8080/sensor-data?timestamp=time3&sensorId=sensorid3&sensorType=sensortype3&value=value3
        // http://localhost:8080/sensor-data/timestamp/time3/sensorId/sensorid3/sensorType/sensortype3/value/value3
        // curl -i -d "timestamp=time3&sensorId=sensorid3&sensorType=sensortype3&value=value3" http://localhost:8080/sensor-data
    }
}
