package environment;

import environment.resolver.dependency_injection.DependencyResolver;
import environment.unit.Container;
import environment.unit.Extension;
import environment.extension.ParameterExtension;
import environment.extension.scene.SceneExtension;
import environment.extension.SensorExtension;
import environment.extension.TaskExtension;
import environment.resolver.container.ContainerResolver;

import java.io.File;
import java.util.LinkedList;

class AppRegister {
    private LinkedList<File> resources;

    private LinkedList<Extension> extensions;

    private LinkedList<ContainerResolver> resolvers;

    private Container container;

    AppRegister() {
        this.container = new AppContainer();
        this.resources = new LinkedList<>();
        this.extensions = new LinkedList<>();
        this.resolvers = new LinkedList<>();

        this.appDefault();
    }

    private void appDefault() {
        this

        .registerResource(new File("config/scenes.yml"))
        .registerResource(new File("config/sensors.yml"))
        .registerResource(new File("config/tasks.yml"))
        .registerResource(new File("config/parameters.yml"))

        .registerResolver(new DependencyResolver())

        .registerExtension(new SceneExtension())
        .registerExtension(new ParameterExtension())
        .registerExtension(new TaskExtension())
        .registerExtension(new SensorExtension());
    }

    final AppRegister registerResource(File resource) {
        if (resource.exists()) {
            this.resources.add(resource);
        }

        return this;
    }

    final AppRegister registerResolver(ContainerResolver resolver) {
        this.resolvers.add(resolver);
        this.container.addResolver(resolver);

        return this;
    }

    final AppRegister registerContainer(Container container) {
        this.container = container;

        return this;
    }

    final AppRegister registerExtension(Extension extension) {
        extension.setContainer(this.container);
        this.container.extend(extension);
        this.extensions.add(extension);

        return this;
    }

    final LinkedList<Extension> getExtensions() {
        return this.extensions;
    }

    final LinkedList<ContainerResolver> getResolvers() {
        return this.resolvers;
    }

    final LinkedList<File> getResources() {
        return this.resources;
    }

    final Container getContainer() {
        return this.container;
    }
}
