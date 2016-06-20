package environment.unit.container;


import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

public interface ContainerInterface
{
    /**
     * @param resource String
     * @return String
     */
    Object get(String resource);

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
}
