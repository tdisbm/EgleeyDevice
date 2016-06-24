package environment.unit.dependency_injection;

import environment.unit.container.ContainerInterface;
import environment.unit.resolver.ResolverInterface;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class DependencyBuilder
{
    private LinkedList<ResolverInterface> resolvers = new LinkedList<ResolverInterface>();

    public DependencyBuilder setResolvers(LinkedList<ResolverInterface> resolvers)
    {
        this.resolvers = resolvers;

        return this;
    }

    public void compile(ContainerInterface container)
    {
        if (!container.isCompiled()) {
            return;
        }

        LinkedHashMap dependencyMap = this.buildDependencyMap(container);
    }

    private LinkedHashMap buildDependencyMap(ContainerInterface container)
    {
        LinkedHashMap elements = new LinkedHashMap();
        LinkedHashMap dependencyMap = new LinkedHashMap();

        try {
            Field f = container.getClass().getSuperclass().getDeclaredField("elements");
            f.setAccessible(true);
            elements = (LinkedHashMap) f.get(container);
        }
        catch (NoSuchFieldException ignored) {}
        catch (IllegalAccessException ignored) {}

        for (Object o : elements.entrySet()) {
            if (((Map.Entry) o).getValue() instanceof LinkedHashMap) {
                dependencyMap.put(
                    ((Map.Entry) o).getKey(),
                    this.find((LinkedHashMap) ((Map.Entry) o).getValue())
                );
            }
        }

        return dependencyMap;
    }

    private ArrayList find(LinkedHashMap elements)
    {
        ArrayList dependencyMap = new ArrayList();

        for (Object o : elements.entrySet()) {
            if (((Map.Entry) o).getValue() instanceof LinkedHashMap) {
                dependencyMap = this.find(
                    (LinkedHashMap) ((Map.Entry) o).getValue()
                );
            }

            if (((Map.Entry) o).getValue() instanceof ArrayList) {
                ArrayList current = (ArrayList) ((Map.Entry) o).getValue();
                int i = 0;

                while (current != null) {
                    if (i == current.size()) {
                        break;
                    }

                    if (current.get(i) instanceof ArrayList) {
                        current = (ArrayList) current.get(i);
                        i = 0;
                    }

                    if (current.get(i) instanceof String &&
                        this.hasResolver((String) current.get(i))
                    ) {
                        dependencyMap.add(current.get(i));
                    }

                    i++;
                }
            }

            if (((Map.Entry) o).getValue() instanceof String &&
                this.hasResolver((String) ((Map.Entry) o).getValue())
            ) {
                dependencyMap.add(((Map.Entry) o).getValue());
            }
        }

        return dependencyMap;
    }

    private boolean hasResolver(String str)
    {
        String prefix = str.substring(0, 1);
        String postfix = str.substring(str.length() - 1, str.length());

        for (ResolverInterface r : this.resolvers) {
            if (r.getPrefix().equals(prefix)) {
                return true;
            }

            if (r.getPostfix().isEmpty()) {
                postfix = "";
            }

            if (r.getPostfix().equals(postfix) && r.getPrefix().equals(prefix)) {
                return true;
            }
        }

        return false;
    }

    private ResolverInterface findResolver(String str)
    {
        String prefix = str.substring(0, 1);
        String postfix = str.substring(str.length() - 1, str.length());

        for (ResolverInterface r : this.resolvers) {
            if (r.getPrefix().equals(prefix)) {
                return r;
            }

            if (r.getPostfix().isEmpty()) {
                postfix = "";
            }

            if (r.getPostfix().equals(postfix) && r.getPrefix().equals(prefix)) {
                return r;
            }
        }

        return null;
    }
}
