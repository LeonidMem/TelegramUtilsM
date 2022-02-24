package ru.leonidm.telegram_utils;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.leonidm.telegram_utils.commands.AbstractCommand;
import ru.leonidm.telegram_utils.commands.CommandManager;
import ru.leonidm.telegram_utils.utils.Utils;

public abstract class TelegramLongPollingBotM extends TelegramLongPollingBot {

    private final String unknownCommandMessage;
    private final String usageMessageTemplate;

    /**
     * @param unknownCommandMessage Message that will be sent if user
     *                              typed unknown command
     * @param usageMessageTemplate Messages that will be formatted and
     *                             sent to the user if his message
     *                             doesn't fit in the settings
     */
    protected TelegramLongPollingBotM(String unknownCommandMessage, String usageMessageTemplate) {
        this.unknownCommandMessage = unknownCommandMessage;
        this.usageMessageTemplate = usageMessageTemplate;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String test = message.getText();
            if(!test.startsWith("/")) return;

            String[] args = test.strip().replaceAll("\s\s+/g", " ").split(" ");

            User author = message.getFrom();

            AbstractCommand abstractCommand = CommandManager.get(this, args[0].substring(1));
            if(abstractCommand == null) {
                Utils.sendMessage(this, author, unknownCommandMessage);
                return;
            }

            String[] outArgs = new String[args.length - 1];
            System.arraycopy(args, 1, outArgs, 0, outArgs.length);

            if(!abstractCommand.fitsInSettings(author, outArgs)) {
                Utils.sendMessage(this, author, usageMessageTemplate);
                return;
            }

            String outMessage = abstractCommand.execute(author, outArgs);
            if(outMessage != null) {
                Utils.sendMessage(this, author, outMessage);
            }
        }
    }
}
