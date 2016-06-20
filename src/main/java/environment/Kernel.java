package environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import environment.unit.container.ContainerInterface;
import environment.unit.resolver.ResolverInterface;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class Kernel
{
    private AppRegister register = new AppRegister();

    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    final public void up()
    {
        this.loadResources();
        this.runResolvers();
    }

    private Kernel loadResources()
    {
        ContainerInterface container = this.register.getContainer();

        for (File f : this.register.getResources()) {
            try {
                container.merge(
                    this.mapper.readValue(f, container.getClass())
                );
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    private Kernel runResolvers()
    {
        LinkedList<ResolverInterface> resolvers = this.register.getResolvers();

        for (ResolverInterface o : resolvers) {
            try {
                o.resolve();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            this.register.getContainer().compile();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return this;
    }
}
