package environment;

import environment.unit.Container;
import environment.unit.Extension;
import environment.extension.ParameterExtension;
import environment.extension.SceneExtension;
import environment.extension.SensorExtension;
import environment.extension.TaskExtension;

import java.io.File;
import java.util.LinkedList;

public class AppRegister
{
    private LinkedList<File> resources;

    private LinkedList<Extension> extensions;

    private Container container;

    public AppRegister()
    {
        this.container = new AppContainer();
        this.resources = new LinkedList<File>();
        this.extensions = new LinkedList<Extension>();

        this.appDefault();
    }

    private void appDefault()
    {
        this

        .registerResource(new File("config/scenes.yml"))
        .registerResource(new File("config/sensors.yml"))
        .registerResource(new File("config/tasks.yml"))
        .registerResource(new File("config/parameters.yml"))

        .extend(new SceneExtension())
        .extend(new ParameterExtension())
        .extend(new TaskExtension())
        .extend(new SensorExtension());
    }

    final public AppRegister registerResource(File resource)
    {
        if (resource.exists()) {
            this.resources.add(resource);
        }

        return this;
    }

    final public AppRegister extend(Extension extension) {
        extension.setContainer(this.container);
        this.container.extend(extension);
        this.extensions.add(extension);

        return this;
    }

    public LinkedList<Extension> getExtensions()
    {
        return this.extensions;
    }

    public LinkedList<File> getResources()
    {
        return this.resources;
    }

    public Container getContainer()
    {
        return this.container;
    }
}
