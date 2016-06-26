package environment.unit.container;

import environment.unit.Extension;
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

    private LinkedList<Extension> extensions = new LinkedList<>();

    private LinkedList<ContainerResolver> resolvers = new LinkedList<>();

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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    final public LinkedHashMap get() {
        return !this.__compiled__ ? null : this.definitions;
    }

    final public Container set(String resource, Object definition) {
        if (resource != null && !resource.isEmpty()) {
            this.definitions.put(resource, definition);
        }

        return this;
    }

    public final boolean has(String resource) {
        return !(this.definitions.get(resource) == null);
    }

    final public Container extend(Extension extension) {
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

        for (ContainerResolver resolver: this.resolvers) {
            resolver.resolve(this);
        }

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

    public boolean hasExtension(String definition) {
        String prefix;
        String postfix;

        String definitionPrefix = definition.substring(0, 1);
        String definitionPostfix = definition.substring(definition.length() - 1, definition.length());

        for (Extension e : this.extensions) {
            prefix = e.getPrefix();
            postfix = e.getPostfix();

            if (prefix.equals(definitionPrefix)) {
                if (postfix == null || postfix.isEmpty()) {
                    postfix = "";
                    definitionPostfix = "";
                }

                if (postfix.equals(definitionPostfix) && prefix.equals(definitionPrefix)) {
                    return true;
                }
            }
        }

        return false;
    }

    final public Container addResolver(ContainerResolver resolver){
        resolver.setExtensions(this.extensions);
        this.resolvers.add(resolver);

        return this;
    }

    final public boolean isCompiled() {
        return this.__compiled__;
    }

    final public boolean isLinearized() {
        return this.__linearized__;
    }
}
