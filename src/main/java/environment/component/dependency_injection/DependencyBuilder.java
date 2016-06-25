package environment.component.dependency_injection;

import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.TreeRunner;
import environment.component.tree_builder.nodes.*;
import environment.unit.Container;
import environment.unit.Extension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DependencyBuilder
{
    private LinkedList<Extension> extensions = new LinkedList<Extension>();

    private TreeRunner runner;

    public DependencyBuilder setExtensions(LinkedList<Extension> extensions)
    {
        this.extensions = extensions;

        Node mainRoot = new ArrayNode("main_root");
        Field treeBuilder;

        for (Extension extension : this.extensions) {
            try {
                treeBuilder = extension.getClass().getSuperclass().getDeclaredField("treeBuilder");
                treeBuilder.setAccessible(true);

                TreeBuilder builder = (TreeBuilder) treeBuilder.get(extension);
                mainRoot.addChild(builder.getRoot().setParent(mainRoot));
            } catch (Exception ignored) {}
        }

        this.runner = new TreeRunner(
            new TreeBuilder(mainRoot)
        );

        return this;
    }

    public void compile(Container container)
    {
        if (!container.isLinearized()) {
            return;
        }

        this.buildDependencyTree(container);
    }

    private void buildDependencyTree(Container container)
    {
        if (this.runner == null) {
            return;
        }

        TreeBuilder dependencyTree = new TreeBuilder(new MapNode("dependency_linker"));

        ArrayList<Node> dependencyNodes = this.runner.findByClass(
            DependencyNode.class,
            null
        );
        ArrayList<Node> instanceNodes = this.runner.findByClass(
            InstanceNode.class,
            null
        );

        if (instanceNodes.size() == 0 || dependencyNodes.size() == 0) {
            return;
        }

        try {
            dependencyTree.next(new MapNode("dependencies"));

            for (Node node : dependencyNodes) {
                linkReferences(node, container);
                dependencyTree.addChild(node);
            }

            dependencyTree.end();
        } catch (Exception ignored) {}

        try {
            dependencyTree.next(new MapNode("instances"));

            for (Node node : instanceNodes) {
                linkReferences(node, container);
                dependencyTree.addChild(node);
            }

            dependencyTree.end();
        } catch (Exception ignored) {}

        this.solveDependencyTree(
            new TreeRunner(dependencyTree),
            container
        );
    }

    private void linkReferences(Node node, Container container) {
        LinkedHashMap<?, ?> definitions = null;
        LinkedHashMap result = new LinkedHashMap();
        Object current;

        try {
            Field field = container.getClass().getDeclaredField(node.getRoot().getName());
            field.setAccessible(true);

            definitions = (LinkedHashMap) field.get(container);
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException ignored) {}

        if (definitions == null) {
            return;
        }

        for (Map.Entry<?, ?> definition : definitions.entrySet()) {
            current = this.findByLevel(definition.getValue(), node.getLevel());

            if (!(current instanceof Set)) {
                continue;
            }

            for (Object o : (Set) current) {
                if (((Map.Entry) o).getKey().equals(node.getName()) && node.supports(((Map.Entry) o).getValue())) {
                    result.put(definition.getKey(), node.linearize(((Map.Entry) o).getValue(), null));
                }
            }
        }

        node.setValue(result);
    }

    private Object findByLevel(Object definitions, int level) {
        if (level == 0) {
            return definitions;
        }

        if (definitions instanceof LinkedHashMap) {
            level -= 1;
            return findByLevel(((LinkedHashMap) definitions).entrySet(), level);
        }

        return null;
    }

    private void solveDependencyTree(TreeRunner runner, Container container) {
        LinkedHashMap dependencies = this.mergeMaps(
            runner.linearizeValues(runner.findByClass(DependencyNode.class, null))
        );
        LinkedHashMap instances = this.mergeMaps(
            runner.linearizeValues(runner.findByClass(InstanceNode.class, null))
        );

        Map<String, Class<?>> instanceClasses = new HashMap<String, Class<?>>();
        Map<String, Object> loadedInstances = new HashMap<String, Object>();

        Map.Entry current;

        //boolean resolved = false;

        try {
            for (Object instance : instances.entrySet()) {
                current = (Map.Entry) instance;
                instanceClasses.put(
                    (String) current.getKey(),
                    Class.forName((String) current.getValue())
                );

                if (dependencies.get(current.getKey()) == null) {
                    loadedInstances.put(
                        (String) current.getKey(),
                        instanceClasses.get(current.getKey()).getConstructor().newInstance()
                    );
                }
            }


//            while (!resolved) {
//                resolved = true;
//            }

            ArrayList args = new ArrayList();
            ArrayList dependenciesList;
            String dependencyLocator;

            for (Object dependency : dependencies.entrySet()) {
                current = (Map.Entry) dependency;
                dependenciesList = (ArrayList) current.getValue();
                args.clear();

                for (Object o : dependenciesList) {
                    dependencyLocator = (String) o;

                    args.add(instanceClasses.get(dependencyLocator) == null
                        ? dependenciesList
                        : instanceClasses.get(dependencyLocator)
                    );
                }

                container.set(
                    (String) current.getKey(),
                    matchConstructor(
                        instanceClasses.get(current.getKey()),
                        dependenciesList
                    ).newInstance(args)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private LinkedHashMap mergeMaps(ArrayList map) {
        LinkedHashMap from = new LinkedHashMap();

        for (Object entry : map) {
            if (entry instanceof LinkedHashMap) {
                from.putAll((Map) entry);
            }
        }

        return from;
    }

    private Constructor<?> matchConstructor(Class clazz, ArrayList<?> arguments) throws NoSuchMethodException {
        Class[] classes = new Class[arguments.size()];

        for (int i = 0; i < classes.length; i++) {
            classes[i] = arguments.get(i).getClass();
        }

        return clazz.getDeclaredConstructor(classes);
    }
}
