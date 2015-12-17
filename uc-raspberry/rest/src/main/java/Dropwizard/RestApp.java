package Dropwizard;

import Dropwizard.health.TemplateHealthCheck;
import Dropwizard.resources.RestResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by jorrit on 17-12-15.
 */
public class RestApp extends Application<RestConfig> {
    public static void main(String[] args) throws Exception {
        new RestApp().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<RestConfig> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(RestConfig configuration,Environment environment) {
        final RestResource resource = new RestResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
        );
        final TemplateHealthCheck healthCheck =
                new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        environment.jersey().register(resource);
    }
}
