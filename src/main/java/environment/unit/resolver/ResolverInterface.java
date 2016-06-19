package environment.unit.resolver;

import environment.unit.container.ContainerInterface;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;

public interface ResolverInterface
{
    /**
     * prefix to identify resolved options
     *
     * @return String
     */
    String getPrefix();

    /**
     * property to resolve
     *
     * @return String
     */
    String getProperty();

    /**
     * @return ResolverInterface
     */
    ResolverInterface setContainer(ContainerInterface container);

    /**
     * @return ContainerInterface
     */
    ContainerInterface getContainer();

    /**
     * @param resources LinkedList
     * @return ResolverInterface
     */
    ResolverInterface setResources(LinkedList<File> resources);

    /**
     * @param entry LinkedHashMap
     */
    Object map(Map.Entry entry) throws Exception;

    /**
     * @throws Exception
     */
    public void resolve() throws Exception;

    /**
     * @param f File
     * @return ResolverInterface
     */
    ResolverInterface addResource(File f);
}
