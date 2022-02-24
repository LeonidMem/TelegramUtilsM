package ru.leonidm.telegram_utils.commands;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SettingsBuilder {

    private final List<Setting<?>> settings;

    private SettingsBuilder() {
        this.settings = new ArrayList<>();
    }

    private SettingsBuilder(Setting<?> setting) {
        this.settings = new ArrayList<>();
        this.settings.add(setting);
    }

    /**
     * @return New instance of SettingsBuilder
     */
    public static SettingsBuilder create() {
        return new SettingsBuilder();
    }

    /**
     * @param setting Cloned {@link Setting} that must be included
     * @return New instance of with given setting
     */
    public static SettingsBuilder create(@NotNull Setting<?> setting) {
        return new SettingsBuilder(setting);
    }

    /**
     * @param setting Base {@link Setting}
     * @param newValue Value that will be set in the base setting
     * @return New instance of with given setting
     */
    public static <T> SettingsBuilder create(@NotNull Setting<T> setting, T newValue) {
        return new SettingsBuilder(setting.setValue(newValue));
    }

    /**
     * @param setting Cloned {@link Setting} that must be included
     * @return This
     */
    public SettingsBuilder add(@NotNull Setting<?> setting) {
        this.settings.add(setting);
        return this;
    }

    /**
     * @param setting Base {@link Setting}
     * @param newValue Value that will be set in the base setting
     * @return This
     */
    public <T> SettingsBuilder add(@NotNull Setting<T> setting, T newValue) {
        this.settings.add(setting);
        return this;
    }

    /**
     * @return {@link ImmutableList} with all given settings
     */
    public ImmutableList<Setting<?>> build() {
        return ImmutableList.copyOf(settings);
    }
}
