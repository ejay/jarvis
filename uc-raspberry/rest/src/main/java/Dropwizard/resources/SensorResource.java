package Dropwizard.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("/sensor-data")
@Produces(MediaType.APPLICATION_JSON)
public class SensorResource {
    private final static Logger logger = Logger.getLogger(SensorResource.class.getName());

    @GET
    @Timed
    public void receiveSensorData(@QueryParam("timestamp") Optional<String> timestamp,
                           @QueryParam("sensorId") Optional<String> sensorId,
                           @QueryParam("sensorType") Optional<String> sensorType,
                           @QueryParam("value") Optional<String> value) {
        logger.info(String.format("Received sensor data: timestamp = %s, sensorId = %s, sensorType = %s, value = %s",
                timestamp.get(), sensorId.get(), sensorType.get(), value.get()));
        // localhost:8080/sensor-data?timestamp=time3&sensorId=sensorid3&sensorType=sensortype3&value=value3
    }
}
