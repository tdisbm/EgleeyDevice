package environment.unit.resolver;

import environment.unit.container.ContainerInterface;

import java.io.File;
import java.util.LinkedHashMap;
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
     * postfix to identify resolved options
     *
     * @return String
     */
    String getPostfix();

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
     * @param entry LinkedHashMap
     */
    Object resolve(Map.Entry entry) throws Exception;

    /**
     * callback after resolve apply
     */
    void done(LinkedHashMap instances);

    /**
     * @throws Exception
     */
    void resolve() throws Exception;
}
