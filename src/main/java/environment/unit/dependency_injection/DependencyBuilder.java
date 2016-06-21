package environment.unit.dependency_injection;

import environment.unit.container.ContainerInterface;
import environment.unit.resolver.ResolverInterface;
import environment.unit.tree_builder.TreeBuilder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class DependencyBuilder
{
    private LinkedList<ResolverInterface> resolvers = new LinkedList<ResolverInterface>();

    private ArrayList<String> resolverTemplates = new ArrayList<String>();

    public DependencyBuilder setResolvers(LinkedList<ResolverInterface> resolvers)
    {
        this.resolvers = resolvers;
        this.setResolverTemplates();

        return this;
    }

    public LinkedHashMap buildDependencyTree(ContainerInterface container)
    {
        if (!container.isCompiled()) {
            return null;
        }

        LinkedHashMap elements;
        LinkedHashMap dependencyMap = new LinkedHashMap();

        ArrayList<TreeBuilder> treeBuilders = new ArrayList<TreeBuilder>();
        TreeBuilder dependencyNodes = new TreeBuilder();

        Field field;

        for (ResolverInterface r : this.resolvers) {
            try {
                field = r.getClass().getSuperclass().getDeclaredField("treeBuilder");
                field.setAccessible(true);
                treeBuilders.add((TreeBuilder) field.get(r));

                field = container.getClass().getSuperclass().getDeclaredField(
                    ((TreeBuilder) field.get(r)).getRootName()
                );
                field.setAccessible(true);
                elements = (LinkedHashMap) field.get(container);
            } catch (NoSuchFieldException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }



        return dependencyMap;
    }

    private LinkedHashMap solveNode(String node, LinkedHashMap elements)
    {
        return elements;
    }

    private boolean hasResolver(String prefix, String postfix)
    {
        return this.resolverTemplates.contains(prefix + postfix);
    }

    private void setResolverTemplates()
    {
        for (ResolverInterface resolver : this.resolvers) {
            this.resolverTemplates.add(resolver.getPrefix() + resolver.getPostfix());
        }
    }
}
