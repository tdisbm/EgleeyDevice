package environment.unit.container;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;


public abstract class AbstractContainer implements ContainerInterface
{
    private LinkedHashMap elements = new LinkedHashMap();

    private boolean compiled = false;

    public Object get(String resource)
    {
        if (!this.compiled) {
            return null;
        }

        return this.elements.get(resource);
    }

    /**
     * @param container LinkedHashMap
     */
    public void merge(ContainerInterface container) throws
        IntrospectionException,
        InvocationTargetException,
        IllegalAccessException
    {
        LinkedHashMap newValue;
        LinkedHashMap oldValue;
        Map.Entry current;

        for (Field i : container.getClass().getDeclaredFields()) {
            i.setAccessible(true);
            newValue = (LinkedHashMap) i.get(container);
            oldValue = (LinkedHashMap) i.get(this);

            if (newValue == null) {
                continue;
            }

            if (oldValue == null) {
                i.set(this, new LinkedHashMap());
                oldValue = (LinkedHashMap) i.get(this);
            }

            for (Object o : newValue.entrySet()) {
                current = (Map.Entry) o;

                oldValue.put(
                    current.getKey(),
                    current.getValue()
                );
            }
        }
    }

    public void compile() throws IllegalAccessException
    {
        if (this.compiled) {
            return;
        }

        LinkedHashMap entities;
        Map.Entry current;

        for (Field i : this.getClass().getDeclaredFields()) {
            i.setAccessible(true);
            entities = (LinkedHashMap) i.get(this);


            for (Object o : entities.entrySet()) {
                current = (Map.Entry) o;

                this.elements.put(
                    current.getKey(),
                    current.getValue()
                );
            }
        }

        this.compiled = true;
    }

    public boolean isCompiled()
    {
        return this.compiled;
    }
}
