package Dropwizard;

import Dropwizard.health.SensorHealthCheck;
import Dropwizard.resources.SensorResource;
import context.ContextService;
import context.UserHasWokenUpContextService;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

import java.util.ArrayList;
import java.util.List;

public class SensorApplication extends Application<SensorConfig> {
    List<ContextService> contextServices;

    public static void main(String[] args) throws Exception {
        ContextService contextService = new UserHasWokenUpContextService();

        List<ContextService> contextServices = new ArrayList<>();
        contextServices.add(contextService);

        new SensorApplication(contextServices).run(args);
    }

    public SensorApplication(List<ContextService> contextServices) {
        this.contextServices = contextServices;
    }

    @Override
    public void run(SensorConfig sensorConfig, Environment environment) throws Exception {
        environment.jersey().register(new SensorResource(contextServices));
        environment.healthChecks().register("Sensor health check", new SensorHealthCheck());
    }
}
