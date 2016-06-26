package environment.component.dependency_injection;

import java.util.ArrayList;

public class InstanceLoader {

    private Object newInstance(Class clazz, ArrayList<?> arguments) {
        Class[] classes = new Class[arguments.size()];

        for (int i = 0; i < classes.length; i++) {
            classes[i] = arguments.get(i).getClass();
        }

        try {
            return clazz.getDeclaredConstructor(classes).newInstance(arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
