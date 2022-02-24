package ru.leonidm.telegram_utils.config;

import org.yaml.snakeyaml.Yaml;
import ru.leonidm.telegram_utils.logger.Logger;

import java.io.InputStream;
import java.util.LinkedHashMap;

public class YamlConfiguration extends ConfigurationSection {

    private static final Yaml yaml = new Yaml();

    /**
     * @param resourceName Just a resource name
     */
    public YamlConfiguration(String resourceName) {
        resourceName = resourceName.endsWith(".yml") ? resourceName : resourceName + ".yml";
        InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        if(resourceStream == null) {
            Logger.warn("There is no resource \"%s\"!", resourceName);
            setData(new LinkedHashMap<>());
        }
        else {
            setData(yaml.load(resourceStream));
        }
    }
}
