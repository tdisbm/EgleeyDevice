package environment.unit.container;


import environment.unit.resolver.ResolverInterface;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

public interface ContainerInterface
{
    /**
     * @param resource String
     * @return String
     */
    Object get(String resource);

    /**
     * @param resource String
     * @return boolean
     */
    boolean has(String resource);

    /**
     * compile elements of the container
     */
    void compile() throws IllegalAccessException;

    /**
     * @return boolean
     */
    boolean isCompiled();

    /**
     * @param container ContainerInterface
     */
    void merge(ContainerInterface container) throws IntrospectionException, InvocationTargetException, IllegalAccessException;

    /**
     * add resolver to container
     *
     * @param resolver ResolverInterface
     * @return ContainerInterface;
     */
    ContainerInterface addResolver(ResolverInterface resolver);
}
