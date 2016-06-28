package environment.resolver.dependency_injection;

import environment.component.tree_builder.TreeBuilder;
import environment.component.tree_builder.TreeRunner;
import environment.component.tree_builder.nodes.*;
import environment.unit.Extension;
import environment.unit.Container;

import java.lang.reflect.Field;
import java.util.*;

class DependencyTreeBuilder
{
    private TreeRunner runner;

    DependencyTreeBuilder(LinkedList<Extension> extensions) {
        Node mainRoot = new ArrayNode("main_root");
        Field treeBuilder;

        for (Extension extension : extensions) {
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
    }

    final TreeRunner build(Container container) {
        if (this.runner == null) {
            return null;
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
            return null;
        }

        try {
            for (Node node : dependencyNodes) {
                linkReferences(node, container);
                dependencyTree.addChild(node);
            }

            for (Node node : instanceNodes) {
                linkReferences(node, container);
                dependencyTree.addChild(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return new TreeRunner(dependencyTree);
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

    private void linkReferences(Node node, Container container) {
        LinkedHashMap<?, ?> definitions = null;
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        Object current;

        try {
            Field field = container.getClass().getDeclaredField(node.getRoot().getName());
            field.setAccessible(true);

            definitions = (LinkedHashMap) field.get(container);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}

        if (definitions == null) {
            return;
        }

        for (Map.Entry<?, ?> definition : definitions.entrySet()) {
            current = this.findByLevel(definition.getValue(), node.getLevel());

            if (current instanceof Set) {
                for (Object o : (Set) current) {
                    if (!(((Map.Entry) o).getKey().equals(node.getName())) &&
                        !node.supports(((Map.Entry) o).getValue())
                    ) {
                        continue;
                    }

                    result.put(
                        (String) definition.getKey(),
                        node.linearize(((Map.Entry) o).getValue(), null)
                    );
                }
            }
        }

        node.setValue(result);
    }
}
