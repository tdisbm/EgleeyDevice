package environment.unit.resolver;

import environment.unit.container.ContainerInterface;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;


public abstract class AbstractResolver implements ResolverInterface
{
    private ContainerInterface container;

    private boolean __resolved__ = false;

    private boolean __prefixed__ = false;

    private boolean __mapped__ = false;

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

        this.prefixing();
        this.mapping();
        this.done(this.getElements());

        this.__resolved__ = true;
    }

    /**
     * get current property elements
     *
     * @return LinkedHashMap
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
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

    /**
     * set current property elements
     *
     * @param elements LinkedHashMap
     * @return this
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
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

    private void prefixing() throws Exception
    {
        if (this.__prefixed__) {
            return;
        }

        String prefix = this.getPrefix();
        String postfix = this.getPostfix();

        if (prefix.isEmpty()) {
            throw new Exception("Resolver's prefix must not be empty");
        }

        Map.Entry entry;
        LinkedHashMap elements = this.getElements();
        LinkedHashMap prefixed = new LinkedHashMap();

        for (Object o : elements.entrySet()) {
            entry = (Map.Entry) o;

            prefixed.put(
                prefix + entry.getKey() + postfix,
                entry.getValue()
            );
        }

        this.setElements(prefixed);
        this.__prefixed__ = true;
    }

    private void mapping() throws Exception {
        if (this.__mapped__) {
            return;
        }

        if (!this.__prefixed__) {
            return;
        }

        LinkedHashMap mapped = new LinkedHashMap();
        LinkedHashMap elements = this.getElements();
        Map.Entry entry;

        if (null == elements) {
            return;
        }

        for (Object o : elements.entrySet()) {
            entry = (Map.Entry) o;

            mapped.put(
                entry.getKey(),
                this.resolve(entry)
            );
        }

        this.setElements(mapped);
        this.__mapped__ = true;
    }
}
