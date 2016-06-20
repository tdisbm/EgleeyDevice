package environment.unit.resolver;

import environment.unit.container.ContainerInterface;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;


public abstract class AbstractResolver implements ResolverInterface
{
    private ContainerInterface container;

    private boolean __resolved__;

    /**
     * @param container ContainerInterface
     * @return ResolverInterface
     */
    final public ResolverInterface setContainer(ContainerInterface container)
    {
        this.container = container;

        return this;
    }

    /**
     * @return ContainerInterface
     */
    final public ContainerInterface getContainer()
    {
        return this.container;
    }

    /**
     *
     * @throws Exception
     */
    final public void resolve() throws Exception
    {
        if (this.__resolved__) {
            throw new Exception("Resolver '" + this.getClass() + "' was already resolved");
        }

        if (null == this.container) {
            throw new Exception("Container is not provided to resolver");
        }

        LinkedHashMap elements = this.getElements();

        if (null == elements) {
            return;
        }

        LinkedHashMap mappedEntries = new LinkedHashMap();
        Map.Entry entry;

        for (Object o : elements.entrySet()) {
            entry = (Map.Entry) o;

            mappedEntries.put(
                this.getPrefix() + entry.getKey(),
                this.map(entry)
            );
        }

        this.setElements(mappedEntries);
        this.done(this.getElements());

        this.__resolved__ = true;
    }

    private LinkedHashMap getElements() throws
        IllegalAccessException,
        NoSuchFieldException
    {
        Field field = this.container
            .getClass()
            .getDeclaredField(
                this.getProperty()
            );

        field.setAccessible(true);

        return (LinkedHashMap) field.get(this.container);
    }

    private ResolverInterface setElements(LinkedHashMap elements) throws
        IllegalAccessException,
        NoSuchFieldException
    {
        Field field = this.container
            .getClass()
            .getDeclaredField(
                this.getProperty()
            );

        field.setAccessible(true);
        field.set(this.container, elements);

        return this;
    }
}
