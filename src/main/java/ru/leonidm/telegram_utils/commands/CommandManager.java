package ru.leonidm.telegram_utils.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.leonidm.telegram_utils.TelegramLongPollingBotM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private static final Map<TelegramLongPollingBotM, Map<String, AbstractCommand>> commands = new HashMap<>();

    /**
     * Registers {@link AbstractCommand}, so it can be recognized by bot
     * @param bot Instance of telegram bot
     * @param label Command label
     * @param abstractCommand Instance of class, extended from AbstractCommand
     */
    public static void register(@NotNull TelegramLongPollingBotM bot,
                                @NotNull String label,
                                @NotNull AbstractCommand abstractCommand) {
        if(label.isEmpty()) {
            throw new IllegalArgumentException("String can't be empty!");
        }

        commands.computeIfAbsent(bot, v -> new HashMap<>()).put(label, abstractCommand);
    }

    /**
     * Returns instance of {@link AbstractCommand} if it's presented
     * @param bot Instance of telegram bot
     * @param label Command label
     * @return Instance of {@link AbstractCommand} if it's presented or null if not
     */
    @Nullable
    public static AbstractCommand get(@NotNull TelegramLongPollingBotM bot,
                                      @NotNull String label) {
        Map<String, AbstractCommand> botCommands = commands.get(bot);
        if(botCommands == null) {
            return null;
        }

        return botCommands.get(label);
    }
}
