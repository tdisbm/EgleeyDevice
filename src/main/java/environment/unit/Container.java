package environment.unit;

import environment.resolver.container.ContainerResolver;
import environment.unit.cache.ContainerCache;
import org.jetbrains.annotations.Nullable;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


public abstract class Container
{
    private LinkedHashMap<String, Object> definitions = new LinkedHashMap<>();

    private LinkedList<Extension> extensions = new LinkedList<>();

    private LinkedList<ContainerResolver> resolvers = new LinkedList<>();

    private boolean __compiled__ = false;

    private boolean __linearized__ = false;


    final public Container extend(Extension extension) {
        extension.setContainer(this);

        this.extensions.add(extension);

        return this;
    }

    final public Container set(String resource, Object definition) {
        if (this.__compiled__) {
            System.out.println("Container is already compiled, ca't set any definition");
        }

        if (resource != null && !resource.isEmpty()) {
            this.definitions.put(resource, definition);
        }

        return this;
    }

    final public Container addResolver(ContainerResolver resolver){
        resolver.setExtensions(this.extensions);
        this.resolvers.add(resolver);

        return this;
    }


    final public boolean has(String resource) {
        return !(this.definitions.get(resource) == null);
    }

    final public boolean hasExtension(String definition) {
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

    final public boolean isCompiled() {
        return this.__compiled__;
    }

    final public boolean isLinearized() {
        return this.__linearized__;
    }

    @Nullable
    final public Object get(String resource) {
        if (!this.__linearized__) {
            return null;
        }

        return this.definitions.get(resource);
    }

    final public LinkedHashMap get() {
        return !this.__compiled__ ? null : this.definitions;
    }


    final public void merge(Container container) throws
        IntrospectionException,
        InvocationTargetException,
        IllegalAccessException
    {
        LinkedHashMap newValue;
        LinkedHashMap oldValue;

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

            oldValue.putAll(newValue);
        }
    }

    final public void compile() throws IllegalAccessException {
        if (this.__compiled__) {
            return;
        }

        this.linearize();

        try {
            for (ContainerResolver resolver: this.resolvers) {
                resolver.resolve(this);
            }

            this.__compiled__ = true;

            for (Extension extension : this.extensions) {
                extension.mapping();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
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
                    (String) current.getKey(),
                    current.getValue()
                );
            }
        }

        this.__linearized__ = true;
    }
}
