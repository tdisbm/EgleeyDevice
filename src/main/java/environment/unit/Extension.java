package environment.unit;

import environment.component.tree_builder.TreeBuilder;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;


public abstract class Extension
{
    private Container container;

    private TreeBuilder treeBuilder = new TreeBuilder();

    private boolean __prefixed__ = false;

    private boolean __mapped__ = false;

    public Extension() {
        try {
            this.treeBuilder = this.buildPrototype(this.treeBuilder);

            if (null == this.treeBuilder) {
                throw new Exception("Resolver '" + this.getClass() + "' is not configured");
            }

            if (this.treeBuilder.getRootName().equals("")) {
                throw new Exception("Resolver '" + this.getClass() + "' has empty root");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param container ContainerInterface
     * @return Extension
     */
    final public Extension setContainer(Container container) {
        this.container = container;

        return this;
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
                this.treeBuilder.getRootName()
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
    private Extension setElements(LinkedHashMap elements) throws
        IllegalAccessException,
        NoSuchFieldException
    {
        Field field = this.container
            .getClass()
            .getDeclaredField(
                this.treeBuilder.getRootName()
            );

        field.setAccessible(true);
        field.set(this.container, elements);

        return this;
    }

    final void prefixing() throws Exception {
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

    final public void mapping() throws Exception {
        if (this.__mapped__) {
            return;
        }

        if (!this.__prefixed__) {
            return;
        }

        if (null == this.container) {
            throw new Exception("Container is not provided to resolver");
        }

        LinkedHashMap elements = this.getElements();
        Map.Entry entry;
        Object definition;

        if (null == elements) {
            return;
        }

        for (Object o : elements.entrySet()) {
            entry = (Map.Entry) o;
            definition = this.container.get((String) entry.getKey());

            this.map(definition, entry);
        }

        this.__mapped__ = true;
    }

    public abstract void map(Object definition, Map.Entry prototype) throws Exception;

    public abstract String getPrefix();

    public abstract String getPostfix();

    public abstract TreeBuilder buildPrototype(TreeBuilder builder) throws Exception;
}
