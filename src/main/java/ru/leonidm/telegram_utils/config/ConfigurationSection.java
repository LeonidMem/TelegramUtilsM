package ru.leonidm.telegram_utils.config;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConfigurationSection {

    protected LinkedHashMap<String, Object> data;
    protected final HashMap<String, ConfigurationSection> sections = new HashMap<>();
    protected ImmutableList<String> keys = null;

    protected ConfigurationSection() {
        this.data = null;
    }

    protected ConfigurationSection(LinkedHashMap<String, Object> data) {
        setData(data);
    }

    protected void setData(LinkedHashMap<String, Object> data) {
        if(this.data != null) {
            throw new IllegalStateException("Data was already set!");
        }

        this.data = new LinkedHashMap<>();

        ImmutableList.Builder<String> builder = ImmutableList.builder();

        for(Map.Entry<String, Object> entry : data.entrySet()) {
            String key = String.valueOf(entry.getKey());

            if(entry.getValue() instanceof LinkedHashMap value) {
                this.sections.put(key, new ConfigurationSection((LinkedHashMap<String, Object>) value));
            }
            else {
                this.data.put(key, entry.getValue());
            }

            builder.add(key);
        }

        this.keys = builder.build();
    }

    @Nullable
    public ConfigurationSection getSection(String path) {
        return sections.get(path);
    }

    public <V> V get(String path, V def, Class<V> vClass) {
        if(this.data == null) {
            throw new IllegalStateException("Data wasn't set yet!");
        }

        String[] split = path.split("\\.", 2);
        if(split.length == 1) {
            Object value = data.get(path);

            if(value == null || !vClass.isAssignableFrom(value.getClass())) {
                return def;
            }
            else {
                return (V) value;
            }
        }

        ConfigurationSection section = sections.get(split[0]);
        if(section != null) {
            return section.get(split[1], def, vClass);
        }
        else {
            return def;
        }
    }

    public <V> List<V> getList(String path, List<V> def, Class<V> vClass) {
        if(this.data == null) {
            throw new IllegalStateException("Data wasn't set yet!");
        }

        String[] split = path.split("\\.", 2);
        if(split.length == 1) {
            Object value = data.get(path);

            if(value == null || !List.class.isAssignableFrom(value.getClass())) {
                return def;
            }

            if(((List<?>) value).stream().allMatch(o -> vClass.isAssignableFrom(o.getClass()))) {
                return (List<V>) value;
            }

            return def;
        }

        ConfigurationSection section = sections.get(split[0]);
        if(section != null) {
            return section.getList(split[1], def, vClass);
        }
        else {
            return def;
        }
    }

    public ImmutableList<String> getKeys() {
        return keys;
    }

    public boolean hasKey(String path) {
        String[] split = path.split("\\.", 2);
        if(split.length == 1) {
            if(keys == null) getKeys();

            return keys.contains(path);
        }

        ConfigurationSection section = sections.get(split[0]);
        if(section != null) {
            return section.hasKey(split[1]);
        }
        else {
            return false;
        }
    }

    public Boolean getBoolean(String path, boolean def) {
        return get(path, def, Boolean.class);
    }

    public Boolean getBoolean(String path) {
        return get(path, false, Boolean.class);
    }

    @Nullable
    public List<Boolean> getBooleanList(String path, List<Boolean> def) {
        return getList(path, def, Boolean.class);
    }

    @NotNull
    public List<Boolean> getBooleanList(String path) {
        return getList(path, Collections.emptyList(), Boolean.class);
    }

    public Integer getInt(String path, int def) {
       return get(path, def, Integer.class);
    }

    public Integer getInt(String path) {
        return get(path, 0, Integer.class);
    }

    @Nullable
    public List<Integer> getIntList(String path, List<Integer> def) {
        return getList(path, def, Integer.class);
    }

    @NotNull
    public List<Integer> getIntList(String path) {
        return getList(path, Collections.emptyList(), Integer.class);
    }

    public Long getLong(String path, long def) {
        return get(path, def, Long.class);
    }

    public Long getLong(String path) {
        return get(path, 0L, Long.class);
    }

    @Nullable
    public List<Long> getLongList(String path, List<Long> def) {
        return getList(path, def, Long.class);
    }

    @NotNull
    public List<Long> getLongList(String path) {
        return getList(path, Collections.emptyList(), Long.class);
    }

    public Float getFloat(String path, float def) {
        return get(path, def, Float.class);
    }

    public Float getFloat(String path) {
        return get(path, 0F, Float.class);
    }

    @Nullable
    public List<Float> getFloatList(String path, List<Float> def) {
        return getList(path, def, Float.class);
    }

    @NotNull
    public List<Float> getFloatList(String path) {
        return getList(path, Collections.emptyList(), Float.class);
    }

    public Double getDouble(String path, double def) {
        return get(path, def, Double.class);
    }

    public Double getDouble(String path) {
        return get(path, 0D, Double.class);
    }

    @Nullable
    public List<Double> getDoubleList(String path, List<Double> def) {
        List<Float> list = getList(path, null, Float.class);
        if(list == null) return def;

        return list.stream().map(o -> (Double) (double) (float) o).toList();
    }

    @NotNull
    public List<Double> getDoubleList(String path) {
        List<Float> list = getList(path, null, Float.class);
        if(list == null) return Collections.emptyList();

        return list.stream().map(o -> (Double) (double) (float) o).toList();
    }

    @Nullable
    public String getString(String path, String def) {
        return get(path, def, String.class);
    }

    @Nullable
    public String getString(String path) {
        return get(path, null, String.class);
    }

    @Nullable
    public List<String> getStringList(String path, List<String> def) {
        return getList(path, def, String.class);
    }

    @NotNull
    public List<String> getStringList(String path) {
        return getList(path, Collections.emptyList(), String.class);
    }
}
