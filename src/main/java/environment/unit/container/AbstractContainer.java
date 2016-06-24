package environment.unit.container;

import environment.unit.dependency_injection.DependencyBuilder;
import environment.unit.resolver.ResolverInterface;
import environment.unit.tree_builder.TreeBuilder;
import environment.unit.tree_builder.TreeRunner;
import environment.unit.tree_builder.nodes.*;
import org.jetbrains.annotations.Nullable;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


public abstract class AbstractContainer implements ContainerInterface
{
    private LinkedHashMap elements = new LinkedHashMap();

    private LinkedList<ResolverInterface> resolvers = new LinkedList<ResolverInterface>();

    private DependencyBuilder dependencyBuilder = new DependencyBuilder();

    private TreeRunner runner;

    private boolean __compiled = false;

    @Nullable
    final public Object get(String resource)
    {
        if (!this.__compiled) {
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

    public final boolean has(String resource)
    {
        return (Boolean) this.elements.get(resource);
    }

    final public ContainerInterface addResolver(ResolverInterface resolver)
    {
        resolver.setContainer(this);

        this.resolvers.add(resolver);

        return this;
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
        if (this.__compiled) {
            return;
        }

        AbstractNode mainRoot = new ArrayNode("main_root");
        TreeBuilder builder;
        Field treeBuilder;

        for (ResolverInterface resolver : this.resolvers) {
            try {
                resolver.prefixing();
                treeBuilder = resolver.getClass().getSuperclass().getDeclaredField("treeBuilder");
                treeBuilder.setAccessible(true);

                builder = (TreeBuilder) treeBuilder.get(resolver);
                mainRoot.addChild(builder.getRoot().setParent(mainRoot));
            } catch (Exception ignored) {}
        }

        this.runner = new TreeRunner(
            new TreeBuilder(mainRoot)
        );

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

        this.buildDependencyTree();
//
//        this.__compiled = true;
//
//        System.out.println("Container '" + this.getClass().getName() + "' is compiled");
//
//        this.dependencyBuilder.compile(this);
    }

    final public boolean isCompiled()
    {
        return this.__compiled;
    }

    private void buildDependencyTree()
    {
        if (this.runner == null) {
            return;
        }

        TreeBuilder dependencyTree = new TreeBuilder(new MapNode("dependencies"));
        ArrayList<AbstractNode> depNodes = this.runner.findByClass(
            DependencyNode.class,
            null
        );
        ArrayList<AbstractNode> classNodes = this.runner.findByClass(
            InstanceNode.class,
            null
        );

        if (classNodes.size() == 0 || depNodes.size() == 0) {
            return;
        }

        for (AbstractNode node : depNodes) {
            node.link(this.elements);
        }
    }
}
