package environment.resolver.dependency_injection;

import environment.component.tree_builder.TreeRunner;
import environment.component.tree_builder.nodes.DependencyNode;
import environment.component.tree_builder.nodes.InstanceNode;
import environment.unit.Container;
import environment.resolver.container.ContainerResolver;

import java.util.*;

import static environment.component.util.graph.TopologicalSort.topologicalSort;

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

        Map<String, Class<?>> instanceClasses = this.getInstanceClasses(instances);

        List<Integer> loadStackIndexes = topologicalSort(this.createDependenctMatrix(
            instances,
            dependencies,
            container
        ));

        Collections.reverse(loadStackIndexes);
    }

    private Map<String, Class<?>> getInstanceClasses(LinkedHashMap<String, ?> instances) {
        Map<String, Class<?>> classes = new HashMap<>();

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

    private List<Integer>[] createDependenctMatrix(
        LinkedHashMap<String, ?> instances,
        LinkedHashMap<String, ?> dependencies,
        Container container
    ) {
        ArrayList<String> instancesKeySet = new ArrayList<>(instances.keySet());

        int[][] dependencyMatrixMirror = new int[instances.size()][instances.size()];
        List<Integer>[] dependencyMatrix = new List[instances.size()];

        for (int i = 0; i < instances.size(); i++) {
            dependencyMatrix[i] = new ArrayList<>();
        }

        for (Map.Entry<String, ?> dependency : dependencies.entrySet()) {
            for (String dependencyLocator : (ArrayList<? extends String>) dependency.getValue()) {
                if (!(dependencyLocator != null)) {
                    continue;
                }

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

                dependencyMatrixMirror
                    [instancesKeySet.indexOf(dependency.getKey())]
                    [instancesKeySet.indexOf(dependencyLocator)] = 1;
            }
        }

        try {
            this.validateDependencyMatrix(dependencyMatrixMirror);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return dependencyMatrix;
    }

    private void validateDependencyMatrix(int[][] matrix) throws Exception {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][i] == 1) {
                throw new Exception("Fatal error: Definition can't be dependent by self");
            }

            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1 && matrix[j][i] == 1) {
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
