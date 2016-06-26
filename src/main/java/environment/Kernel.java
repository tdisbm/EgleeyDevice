package environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import environment.unit.Extension;
import environment.unit.Container;
import environment.resolver.container.ContainerResolver;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Kernel
{
    private AppRegister register = new AppRegister();

    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    final public void up()
    {
        this

        .loadResources()
        .loadContainer();
    }

    private Kernel loadResources()
    {
        Container container = this.register.getContainer();

        for (File f : this.register.getResources()) {
            try {
                container.merge(
                    this.mapper.readValue(f, container.getClass())
                );
            } catch (IntrospectionException |
                InvocationTargetException |
                IllegalAccessException |
                IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    private Kernel loadContainer()
    {
        try {
            this.register.getContainer().compile();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return this;
    }

    final public Kernel extend(Object o) {
        if (o instanceof ContainerResolver) {
            this.register.registerResolver((ContainerResolver) o);
            return this;
        }

        if (o instanceof Extension) {
            this.register.registerExtension((Extension) o);
            return this;
        }

        if (o instanceof File) {
            this.register.registerResource((File) o);
        }

        return this;
    }
}
