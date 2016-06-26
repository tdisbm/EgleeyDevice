package environment.resolver.container;

import environment.unit.Extension;
import environment.unit.Container;

import java.util.LinkedList;

public abstract class ContainerResolver
{
    private LinkedList<Extension> extensions;

    public abstract void resolve(Container container);

    final public ContainerResolver setExtensions(LinkedList<Extension> extensions) {
        this.extensions = extensions;

        return this;
    }

    final protected LinkedList<Extension> getExtensions() {
        return extensions;
    }
}
