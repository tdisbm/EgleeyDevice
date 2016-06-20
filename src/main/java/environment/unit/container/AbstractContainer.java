package environment.unit.container;

import org.jetbrains.annotations.Nullable;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;


public abstract class AbstractContainer implements ContainerInterface
{
    private LinkedHashMap elements = new LinkedHashMap();

    private boolean compiled = false;

    @Nullable
    final public Object get(String resource)
    {
        if (!this.compiled) {
            return null;
        }

        Object result = this.elements.get(resource);

        try {
            if (null == result) {
                Field field = this.getClass().getDeclaredField(resource);
                field.setAccessible(true);
                result = field.get(this);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    final public void merge(ContainerInterface container) throws
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

    final public void compile() throws IllegalAccessException
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

        System.out.println("Container '" + this.getClass().getName() + "' is compiled");

        this.compiled = true;
    }

    final public boolean isCompiled()
    {
        return this.compiled;
    }
}
