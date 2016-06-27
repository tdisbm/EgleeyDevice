package environment.resolver.dependency_injection;

import environment.component.tree_builder.TreeRunner;
import environment.component.tree_builder.nodes.DependencyNode;
import environment.component.tree_builder.nodes.InstanceNode;
import environment.unit.Container;
import environment.resolver.container.ContainerResolver;

import java.util.*;

import static environment.component.util.graph.TopologicalSort.topologicalSort;
import static environment.component.util.instance.InstanceLoader.newInstance;

public class DependencyResolver extends ContainerResolver {

    @Override
    public void resolve(Container container) {
        DependencyTreeBuilder builder = new DependencyTreeBuilder(this.getExtensions());
        TreeRunner runner = builder.build(container);

        LinkedHashMap<String, ?> dependencies = this.mergeMaps(
            runner.linearizeValues(runner.findByClass(DependencyNode.class, null))
        );
        LinkedHashMap<String, ?> instances = this.mergeMaps(
            runner.linearizeValues(runner.findByClass(InstanceNode.class, null))
        );

        this.push(container, this.createInstances(instances, dependencies, container));
    }

    private LinkedHashMap<String, Object> createInstances(
        LinkedHashMap<String, ?> instances,
        LinkedHashMap<String, ?> dependencies,
        Container container
    ) {
        LinkedHashMap<String, Class<?>> classes = this.getInstanceClasses(instances);
        LinkedHashMap<String, Object> loaded = new LinkedHashMap<>();

        Object instance;

        List<Integer> loadStack = this.getLoadStackIndexes(instances, dependencies, container);
        ArrayList<String> dependencyArgumentNames;
        ArrayList<Object> arguments = new ArrayList<>();

        String[] instanceKeys = Arrays.copyOf(
            classes.keySet().toArray(),
            classes.keySet().toArray().length,
            String[].class
        );

        for (int i : loadStack) {
            if (dependencies.get(instanceKeys[i]) != null &&
                dependencies.get(instanceKeys[i]) instanceof ArrayList
            ) {
                dependencyArgumentNames = (ArrayList<String>) dependencies.get(instanceKeys[i]);

                for (Object argument : dependencyArgumentNames) {
                    arguments.add(null != loaded.get(argument.toString())
                        ? loaded.get(argument.toString())
                        : argument
                    );
                }
            }

            instance = newInstance(classes.get(instanceKeys[i]), arguments);

            if (null == instance) {
                throw new Error("Can't create instance for definition " + instanceKeys[i]);
            }

            loaded.put(instanceKeys[i], instance);
            arguments.clear();
        }

        return loaded;
    }

    private void push(Container container, LinkedHashMap<String, Object> instances) {
        for (Map.Entry<String, Object> obj : instances.entrySet()) {
            container.set(obj.getKey(), obj.getValue());
        }
    }

    private LinkedHashMap<String, Class<?>> getInstanceClasses(LinkedHashMap<String, ?> instances) {
        LinkedHashMap<String, Class<?>> classes = new LinkedHashMap<>();

        for (Map.Entry<String, ?> instance : instances.entrySet()) {
            try {
                classes.put(
                    instance.getKey(),
                    Class.forName((String) instance.getValue())
                );
            } catch (ClassNotFoundException e) {
                System.out.format("Fatal error: Can't find class %s" + instance.getValue());
                System.exit(1);
            }
        }

        return classes;
    }

    private List<Integer> getLoadStackIndexes(
        LinkedHashMap<String, ?> instances,
        LinkedHashMap<String, ?> dependencies,
        Container container
    ) {
        List<Integer> loadStackIndexes = topologicalSort(this.createDependencyMatrix(
            instances,
            dependencies,
            container
        ));

        Collections.reverse(loadStackIndexes);

        return loadStackIndexes;
    }

    private List<Integer>[] createDependencyMatrix(
        LinkedHashMap<String, ?> instances,
        LinkedHashMap<String, ?> dependencies,
        Container container
    ) {
        ArrayList<String> instancesKeySet = new ArrayList<>(instances.keySet());
        List<Integer>[] dependencyMatrix = new List[instances.size()];

        for (int i = 0; i < instances.size(); i++) {
            dependencyMatrix[i] = new ArrayList<>();
        }

        String dependencyLocator;

        for (Map.Entry<String, ?> dependency : dependencies.entrySet()) {
            for (Object o : (ArrayList) dependency.getValue()) {
                dependencyLocator = o.toString();

                if (!container.has(dependencyLocator) &&
                    container.hasExtension(dependencyLocator)
                ) {
                    System.out.format(
                        "Fatal error: '%s' has dependency to non existent definition '%s'",
                        dependency.getKey(),
                        dependencyLocator
                    );

                    System.exit(1);
                }

                if (instancesKeySet.indexOf(dependencyLocator) == -1) {
                    continue;
                }

                dependencyMatrix[instancesKeySet.indexOf(dependency.getKey())].add(
                    instancesKeySet.indexOf(dependencyLocator)
                );
            }
        }

        try {
            this.validateDependencyMatrix(dependencyMatrix);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return dependencyMatrix;
    }

    private void validateDependencyMatrix(List<Integer>[] matrix) throws Exception {
        int[][] mirror = new int[matrix.length][matrix.length];
        int iterator = -1;

        for (List<Integer> i : matrix) {
            iterator++;
            for (int j : i) {
                mirror[iterator][j] = 1;
            }
        }

        for (int i = 0; i < mirror.length; i++) {
            if (mirror[i][i] == 1) {
                throw new Exception("Fatal error: Definition can't be dependent by self");
            }

            for (int j = 0; j < mirror[i].length; j++) {
                if (mirror[i][j] == 1 && mirror[j][i] == 1) {
                    throw new Exception("Fatal error: Circular dependency detected");
                }
            }
        }
    }

    private LinkedHashMap<String, ?> mergeMaps(ArrayList map) {
        LinkedHashMap<String, ?> merged = new LinkedHashMap<>();

        for (Object entry : map) {
            if (entry instanceof LinkedHashMap) {
                merged.putAll((Map) entry);
            }
        }

        return merged;
    }
}
