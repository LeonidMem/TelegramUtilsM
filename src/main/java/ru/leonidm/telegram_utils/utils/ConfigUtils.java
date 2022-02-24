package ru.leonidm.telegram_utils.utils;

import ru.leonidm.telegram_utils.config.YamlConfiguration;

import java.lang.reflect.Field;

public class ConfigUtils {

    public static void load(String resourceName, Class<?> configClass) {
        YamlConfiguration yamlConfiguration = new YamlConfiguration(resourceName);
        loadClass(yamlConfiguration, configClass);
        for(Class<?> nestClazz : configClass.getNestMembers()) {
            loadClass(yamlConfiguration, nestClazz);
        }
    }

    private static void loadClass(YamlConfiguration yamlConfiguration, Class<?> clazz) {
        for(Field field : clazz.getFields()) {
            try {
                if(!field.canAccess(null)) continue;

                Class<?> fieldClazz = field.getType();
                String path = clazz.getName().toLowerCase()
                        .substring(clazz.getName().toLowerCase().lastIndexOf(".") + 1)
                        .replace('$', '.') + '.' + field.getName().toLowerCase();

                path = path.substring(path.indexOf('.') + 1);

                if(fieldClazz == String.class) {
                    String out = yamlConfiguration.getString(path);

                    field.set(null, out);
                }
                else if(fieldClazz == int.class || fieldClazz == Integer.class) {
                    field.set(null, yamlConfiguration.getInt(path));
                }
                else if(fieldClazz == long.class || fieldClazz == Long.class) {
                    field.set(null, yamlConfiguration.getLong(path));
                }
                else if(fieldClazz == double.class || fieldClazz == Double.class) {
                    field.set(null, yamlConfiguration.getDouble(path));
                }
                else if(fieldClazz == boolean.class || fieldClazz == Boolean.class) {
                    field.set(null, yamlConfiguration.getBoolean(path));
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}

