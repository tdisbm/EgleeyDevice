package environment;

import environment.unit.resolver.ResolverInterface;

import java.util.LinkedList;

public class Kernel
{
    protected AppRegister register;

    public Kernel()
    {
        this.register = new AppRegister();
    }

    public void ini()
    {
        this.runResolvers();
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
