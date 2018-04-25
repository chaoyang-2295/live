package com.huashengke.com.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author junbo_xu
 * @since iddd_common
 * @create 2015年5月18日
 */
public class EnumUtil {

    public static Object getEnum(Class<?> enumClass, String value)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (!enumClass.isEnum())
            return null;

        Method values = enumClass.getMethod("valueOf", String.class);

        return values.invoke(null, value);
    }

    public static Object getEnum(Class<?> enumClass, int value)
            throws NoSuchMethodException, SecurityException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (!enumClass.isEnum())
            return null;

        Method valuesMethod = enumClass.getMethod("values");

        Enum<?>[] values = (Enum[]) valuesMethod.invoke(null);

        if (values != null && values.length > 0) {
            for (Enum<?> e : values) {
                if (e.ordinal() == value) {
                    return e;
                }
            }
        }

        return null;
    }

    public static Object safeEnum(Class<?> enumClass, Object value) {
        try {
            if (value instanceof String) {
                return getEnum(enumClass, (String) value);
            } else if (value instanceof Integer) {
                return getEnum(enumClass, (Integer) value);
            }
        } catch (NoSuchMethodException | SecurityException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
