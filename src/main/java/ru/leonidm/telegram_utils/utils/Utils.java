package ru.leonidm.telegram_utils.utils;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.leonidm.telegram_utils.TelegramLongPollingBotM;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {

    /**
     * Just sends message to the user
     * @param bot Instance of telegram bot
     * @param user Receiver
     * @param text Text to send
     * @param enableMarkdown If true, markdown will be enabled
     */
    public static void sendMessage(@NotNull TelegramLongPollingBot bot, @NotNull User user, @NotNull String text, boolean enableMarkdown) {
        if(text.isEmpty()) {
            throw new IllegalArgumentException("String can't be empty!");
        }

        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(user.getId()), text);
            if(enableMarkdown) sendMessage.enableMarkdown(true);

            bot.execute(sendMessage);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Just sends message to the user.
     * Markdown is enabled
     * @param bot Instance of telegram bot
     * @param user Receiver
     * @param text Text to send
     */
    public static void sendMessage(@NotNull TelegramLongPollingBot bot, @NotNull User user, @NotNull String text) {
        sendMessage(bot, user, text, true);
    }

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * @return Current time formatted as HH:mm:ss
     */
    public static String getFormattedCurrentTime() {
        return simpleDateFormat.format(new Date());
        // TODO: don't use java.util.Date
    }

    /**
     * Archives given file in .zip format
     * @param file File that must be zipped
     * @param zipName Name of the new file without extension
     */
    public static void zipFile(File file, String zipName) {
        try {
            FileOutputStream outputStream = new FileOutputStream("logs/" + zipName + ".zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(zipEntry);

            FileInputStream inputStream = new FileInputStream(file);

            zipOutputStream.write(inputStream.readAllBytes());

            zipOutputStream.close();
            inputStream.close();
            outputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
