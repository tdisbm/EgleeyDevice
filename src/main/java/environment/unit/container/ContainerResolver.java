package environment.unit.container;

import environment.unit.Extension;

import java.util.LinkedList;

public abstract class ContainerResolver
{
    private LinkedList<Extension> extensions;

    public abstract void resolve(Container container);

    final public ContainerResolver setExtensions(LinkedList<Extension> extensions) {
        this.extensions = extensions;

        return this;
    }

    public LinkedList<Extension> getExtensions() {
        return extensions;
    }
}
