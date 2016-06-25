package environment.component.dependency_injection;

import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.TreeRunner;
import environment.component.tree_builder.nodes.*;
import environment.unit.Container;
import environment.unit.Extension;

import java.lang.reflect.Field;
import java.util.*;

public class DependencyBuilder
{
    private LinkedList<Extension> extensions = new LinkedList<Extension>();

    private TreeRunner runner;

    private Node mainRoot;

    private TreeBuilder builder;

    public DependencyBuilder setExtensions(LinkedList<Extension> extensions)
    {
        this.extensions = extensions;

        this.mainRoot = new ArrayNode("main_root");
        Field treeBuilder;

        for (Extension extension : this.extensions) {
            try {
                treeBuilder = extension.getClass().getSuperclass().getDeclaredField("treeBuilder");
                treeBuilder.setAccessible(true);

                this.builder = (TreeBuilder) treeBuilder.get(extension);
                this.mainRoot.addChild(builder.getRoot().setParent(this.mainRoot));
            } catch (Exception ignored) {}
        }

        this.runner = new TreeRunner(
            new TreeBuilder(this.mainRoot)
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

//    private LinkedHashMap buildDependencyMap(ContainerInterface container)
//    {
//        LinkedHashMap elements = new LinkedHashMap();
//        LinkedHashMap dependencyMap = new LinkedHashMap();
//
//        try {
//            Field f = container.getClass().getSuperclass().getDeclaredField("definitions");
//            f.setAccessible(true);
//            elements = (LinkedHashMap) f.get(container);
//        }
//        catch (NoSuchFieldException ignored) {}
//        catch (IllegalAccessException ignored) {}
//
//        for (Object o : elements.entrySet()) {
//            if (((Map.Entry) o).getValue() instanceof LinkedHashMap) {
//                dependencyMap.put(
//                    ((Map.Entry) o).getKey(),
//                    this.find((LinkedHashMap) ((Map.Entry) o).getValue())
//                );
//            }
//        }
//
//        return dependencyMap;
//    }
//
//    private ArrayList find(LinkedHashMap elements) {
//        ArrayList dependencyMap = new ArrayList();
//
//        for (Object o : elements.entrySet()) {
//            if (((Map.Entry) o).getValue() instanceof LinkedHashMap) {
//                dependencyMap = this.find(
//                    (LinkedHashMap) ((Map.Entry) o).getValue()
//                );
//            }
//
//            if (((Map.Entry) o).getValue() instanceof ArrayList) {
//                ArrayList current = (ArrayList) ((Map.Entry) o).getValue();
//                int i = 0;
//
//                while (current != null) {
//                    if (i == current.size()) {
//                        break;
//                    }
//
//                    if (current.get(i) instanceof ArrayList) {
//                        current = (ArrayList) current.get(i);
//                        i = 0;
//                    }
//
//                    if (current.get(i) instanceof String &&
//                        this.hasResolver((String) current.get(i))
//                    ) {
//                        dependencyMap.add(current.get(i));
//                    }
//
//                    i++;
//                }
//            }
//
//            if (((Map.Entry) o).getValue() instanceof String &&
//                this.hasResolver((String) ((Map.Entry) o).getValue())
//            ) {
//                dependencyMap.add(((Map.Entry) o).getValue());
//            }
//        }
//
//        return dependencyMap;
//    }
//
//    private boolean hasResolver(String str) {
//        String prefix = str.substring(0, 1);
//        String postfix = str.substring(str.length() - 1, str.length());
//
//        for (Extension r : this.extensions) {
//            if (r.getPrefix().equals(prefix)) {
//                return true;
//            }
//
//            if (r.getPostfix().isEmpty()) {
//                postfix = "";
//            }
//
//            if (r.getPostfix().equals(postfix) && r.getPrefix().equals(prefix)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private Extension findResolver(String str) {
//        String prefix = str.substring(0, 1);
//        String postfix = str.substring(str.length() - 1, str.length());
//
//        for (Extension r : this.extensions) {
//            if (r.getPrefix().equals(prefix)) {
//                return r;
//            }
//
//            if (r.getPostfix().isEmpty()) {
//                postfix = "";
//            }
//
//            if (r.getPostfix().equals(postfix) && r.getPrefix().equals(prefix)) {
//                return r;
//            }
//        }
//
//        return null;
//    }
}
