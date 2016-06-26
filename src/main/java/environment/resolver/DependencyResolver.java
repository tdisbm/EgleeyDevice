package environment.resolver;

import environment.component.dependency_injection.DependencyTreeBuilder;
import environment.component.tree_builder.TreeRunner;
import environment.component.tree_builder.nodes.DependencyNode;
import environment.component.tree_builder.nodes.InstanceNode;
import environment.unit.container.Container;
import environment.unit.container.ContainerResolver;

import java.util.*;

import static environment.unit.graph.TopologicalSort.topologicalSort;

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

        Map<String, Class<?>> instanceClasses = new HashMap<String, Class<?>>();
        ArrayList<String> instancesKeySet = new ArrayList<String>(instances.keySet());

        for (Map.Entry<String, ?> instance : instances.entrySet()) {
            try {
                instanceClasses.put(
                    instance.getKey(),
                    Class.forName((String) instance.getValue())
                );
            } catch (ClassNotFoundException e) {
                System.out.format("Fatal error: Can't find class %s" + instance.getValue());
            }
        }

        List<Integer>[] dependencyMatrix = new List[instances.size()];
        int[][] dependencyMatrixMirror;

        for (int i = 0; i < instances.size(); i++) {
            dependencyMatrix[i] = new ArrayList<Integer>();
        }

        dependencyMatrixMirror = new int[instances.size()][instances.size()];

        for (Map.Entry<String, ?> dependency : dependencies.entrySet()) {
            for (Object dependencyLocator : (ArrayList) dependency.getValue()) {
                if (!(dependencyLocator instanceof String)) {
                    continue;
                }

                if (!container.has((String) dependencyLocator) &&
                        container.hasExtension((String) dependencyLocator)
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

        List<Integer> loadStackIndexes = topologicalSort(dependencyMatrix);
        Collections.reverse(loadStackIndexes);
    }

    private LinkedHashMap<String, ?> mergeMaps(ArrayList map) {
        LinkedHashMap<String, ?> merged = new LinkedHashMap();

        for (Object entry : map) {
            if (entry instanceof LinkedHashMap) {
                merged.putAll((Map) entry);
            }
        }

        return merged;
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
}
