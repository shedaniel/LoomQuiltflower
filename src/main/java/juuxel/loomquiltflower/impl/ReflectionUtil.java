package juuxel.loomquiltflower.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ReflectionUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getFieldOrRecordComponent(Object o, String name) {
        Class<?> c = o.getClass();

        try {
            try {
                Method accessor = c.getMethod(name);
                return (T) accessor.invoke(o);
            } catch (NoSuchMethodException e) {
                Field field = c.getField(name);
                return (T) field.get(o);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not find property " + name + " in " + c, e);
        }
    }

    public static boolean classExists(String fqn) {
        try {
            Class.forName(fqn);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(String fqn) throws ReflectiveOperationException {
        Class<?> c = Class.forName(fqn);
        return (T) c.getConstructor().newInstance();
    }
}
