package Dropwizard;

import Dropwizard.health.SensorHealthCheck;
import Dropwizard.resources.SensorResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class SensorApplication extends Application<SensorConfig> {
    public static void main(String[] args) throws Exception {
        new SensorApplication().run(args);
    }

    @Override
    public void run(SensorConfig sensorConfig, Environment environment) throws Exception {
        environment.jersey().register(new SensorResource());
        environment.healthChecks().register("Sensor health check", new SensorHealthCheck());
    }
}
