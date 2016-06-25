package environment.unit;

import environment.component.dependency_injection.DependencyBuilder;
import org.jetbrains.annotations.Nullable;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


public abstract class Container
{
    private LinkedHashMap definitions = new LinkedHashMap();

    private LinkedList<Extension> extensions = new LinkedList<Extension>();

    private DependencyBuilder dependencyBuilder = new DependencyBuilder();

    private boolean __compiled__ = false;

    private boolean __linearized__ = false;

    @Nullable
    final public Object get(String resource) {
        if (!this.__compiled__) {
            return null;
        }

        Object result = this.definitions.get(resource);

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

    final public LinkedHashMap get() {
        return !this.__compiled__ ? null : this.definitions;
    }

    public final boolean has(String resource)
    {
        return (Boolean) this.definitions.get(resource);
    }

    final public Container extend(Extension extension)
    {
        extension.setContainer(this);

        this.extensions.add(extension);

        return this;
    }

    final public void merge(Container container) throws
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

    final public void compile() throws IllegalAccessException {
        if (this.__compiled__) {
            return;
        }

        this.linearize();

        this.dependencyBuilder
            .setExtensions(this.extensions)
            .compile(this);

        this.__compiled__ = true;
    }

    private void linearize() throws IllegalAccessException {
        if (this.__linearized__) {
            return;
        }

        for (Extension extension : this.extensions) {
            try {
                extension.prefixing();
            } catch (Exception ignored) {}
        }

        LinkedHashMap entities;
        Map.Entry current;

        for (Field i : this.getClass().getDeclaredFields()) {
            i.setAccessible(true);
            entities = (LinkedHashMap) i.get(this);


            for (Object o : entities.entrySet()) {
                current = (Map.Entry) o;

                this.definitions.put(
                    current.getKey(),
                    current.getValue()
                );
            }
        }

        this.__linearized__ = true;
    }

    final public boolean isCompiled() {
        return this.__compiled__;
    }

    final public boolean isLinearized() {
        return this.__linearized__;
    }
}
