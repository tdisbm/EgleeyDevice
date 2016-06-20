package environment.worker.resolver;


import environment.unit.resolver.AbstractResolver;

import java.util.LinkedHashMap;
import java.util.Map;

public class ParameterResolver extends AbstractResolver
{
    public Object resolve(Map.Entry entry) throws Exception
    {
        return entry.getValue();
    }

    public void done(LinkedHashMap instances)
    {

    }

    public String getPrefix() {
        return "%";
    }

    public String getPostfix() {
        return "%";
    }

    public String getProperty() {
        return "parameters";
    }
}
