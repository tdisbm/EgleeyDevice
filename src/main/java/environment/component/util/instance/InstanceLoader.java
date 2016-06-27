package environment.component.util.instance;

import java.util.ArrayList;

public class InstanceLoader {

    public static Object newInstance(Class<?> clazz, ArrayList<?> arguments) {
        Class[] classes = new Class[arguments.size()];

        for (int i = 0; i < classes.length; i++) {
            classes[i] = arguments.get(i).getClass();
        }

        try {
            return clazz.getConstructor(classes).newInstance(arguments.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
