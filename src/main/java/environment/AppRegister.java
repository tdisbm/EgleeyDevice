package environment;

import environment.unit.container.ContainerInterface;
import environment.unit.resolver.ResolverInterface;
import environment.worker.resolver.SceneResolver;
import environment.worker.resolver.SensorResolver;
import environment.worker.resolver.TaskResolver;

import java.io.File;
import java.util.LinkedList;

public class AppRegister
{
    private LinkedList<File> resources;

    private LinkedList<ResolverInterface> resolvers;

    private ContainerInterface container;

    public AppRegister()
    {
        this.container = new AppContainer();
        this.resources = new LinkedList<File>();
        this.resolvers = new LinkedList<ResolverInterface>();

        this.appDefault();
    }

    private void appDefault()
    {
        this

        .registerResource(new File("config/scenes.yml"))
        .registerResource(new File("config/sensors.yml"))
        .registerResource(new File("config/tasks.yml"))
        .registerResource(new File("config/parameters.yml"))

        .registerResolver(new SceneResolver())
        .registerResolver(new TaskResolver())
        .registerResolver(new SensorResolver());
    }

    public AppRegister registerResource(File resource)
    {
        if (resource.exists()) {
            this.resources.add(resource);
        }

        return this;
    }

    public AppRegister registerResolver(ResolverInterface resolver)
    {
        resolver
            .setContainer(this.container);

        this.resolvers.add(resolver);

        return this;
    }

    public LinkedList<ResolverInterface> getResolvers()
    {
        return this.resolvers;
    }

    public LinkedList<File> getResources()
    {
        return this.resources;
    }

    public ContainerInterface getContainer()
    {
        return this.container;
    }
}
