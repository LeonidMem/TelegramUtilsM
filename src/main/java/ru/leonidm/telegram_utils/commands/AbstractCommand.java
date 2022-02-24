package ru.leonidm.telegram_utils.commands;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand {

    private final List<Setting<?>> settings;

    protected AbstractCommand() {
        this.settings = Collections.emptyList();
    }

    /**
     * @param settings List of settings, can be created from
     *
     */
    protected AbstractCommand(List<Setting<?>> settings) {
        if(settings instanceof ImmutableList) {
            this.settings = settings;
        }
        else {
            this.settings = ImmutableList.copyOf(settings);
        }
    }

    /**
     * This method calls when user
     * @param user User who sent this command
     * @param args Arguments provided by users
     * @return String that must be sent to the user or null
     * if message was already sent inside this method
     */
    @Nullable
    public abstract String execute(User user, String[] args);

    /**
     * Checks if user and arguments fits in the settings
     * from constructor
     * @param user User who sent the command
     * @param args Command arguments
     * @return Predicate result
     */
    public final boolean fitsInSettings(User user, String[] args) {
        return settings.stream().allMatch(setting -> setting.test(user, args));
    }

    @NotNull
    public abstract String getUsage();
}
