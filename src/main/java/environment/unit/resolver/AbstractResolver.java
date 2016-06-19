package environment.unit.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import environment.unit.container.ContainerInterface;
import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


public abstract class AbstractResolver implements ResolverInterface
{
    private ObjectMapper mapper;

    private ContainerInterface container;

    private boolean __resolved__;

    private LinkedList<File> resources;

    public AbstractResolver()
    {
        this.resources = new LinkedList<File>();
        this.mapper = new ObjectMapper(new YAMLFactory());
    }

    /**
     * @param container ContainerInterface
     * @return ResolverInterface
     */
    public ResolverInterface setContainer(ContainerInterface container)
    {
        this.container = container;

        return this;
    }

    /**
     * @return ContainerInterface
     */
    public ContainerInterface getContainer()
    {
        return this.container;
    }

    /**
     * @param f File
     * @return ResolverInterface
     */
    public ResolverInterface addResource(File f)
    {
        if (!f.exists()) {
            return this;
        }

        this.resources.add(f);

        return this;
    }

    /**
     * @param resources LinkedList
     * @return this
     */
    public ResolverInterface setResources(LinkedList<File> resources)
    {
        this.resources = resources;

        return this;
    }

    /**
     *
     * @throws Exception
     */
    public void resolve() throws Exception
    {
        if (this.__resolved__) {
            throw new Exception("Resolver '" + this.getClass() + "' was already resolved");
        }

        if (null == this.container) {
            throw new Exception("Container is not provided to resolver");
        }

        for (File f : this.resources) {
            this.container.merge(
                this.mapper.readValue(f, this.container.getClass())
            );
        }

        LinkedHashMap elements = this.getElements();
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
