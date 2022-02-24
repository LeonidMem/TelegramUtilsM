package ru.leonidm.telegram_utils.logger;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import ru.leonidm.telegram_utils.utils.Utils;
import ru.leonidm.telegram_utils.threads.RepeatingThread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {

    private static final LoggerThread loggerThread;
    private static final SavingThread savingThread;

    static {
        savingThread = new SavingThread(5000);
        savingThread.start();

        loggerThread = new LoggerThread(500);
        loggerThread.start();

        File file = new File("logs/latest.log");
        if(file.exists()) {
            Utils.zipFile(file, new SimpleDateFormat("dd-MM-yy_HH-mm-ss").format(new Date()));
            file.delete();
        }
    }

    /**
     * Disabled all relative to this Logger threads
     */
    public static void disableRelativeThreads() {
        loggerThread.disable();
        savingThread.disable();
    }

    /**
     * @param object Any object
     */
    public static void info(Object object) {
        LoggerThread.queue.add(new Line(object.toString(), Attribute.WHITE_TEXT()));
    }

    /**
     * @param object Any object
     * @param textAttribute Text color
     */
    public static void info(Object object, Attribute textAttribute) {
        LoggerThread.queue.add(new Line(object.toString(), textAttribute));
    }

    /**
     * @param string String that will be formatted
     * @param objects All objects that will be pasted in the string
     */
    public static void info(String string, Object... objects) {
        LoggerThread.queue.add(new Line(string.formatted(objects), Attribute.WHITE_TEXT()));
    }

    /**
     * @param string String that will be formatted
     * @param textAttribute Text color
     * @param objects All objects that will be pasted in the string
     */
    public static void info(String string, Attribute textAttribute, Object... objects) {
        LoggerThread.queue.add(new Line(string.formatted(objects), textAttribute));
    }

    /**
     * @param object Any object
     */
    public static void warn(Object object) {
        LoggerThread.queue.add(new Line(object.toString(), Attribute.YELLOW_TEXT()));
    }

    /**
     * @param string String that will be formatted
     * @param objects All objects that will be pasted in the string
     */
    public static void warn(String string, Object... objects) {
        LoggerThread.queue.add(new Line(string.formatted(objects), Attribute.YELLOW_TEXT()));
    }

    private static class LoggerThread extends RepeatingThread {

        private static final ConcurrentLinkedQueue<Line> queue = new ConcurrentLinkedQueue<>();
        private static final String format = "[%s] [%s] %s";

        private LoggerThread(long delay) {
            super(delay);
        }

        @Override
        public void runAfterDelay() {
            for(Line task : new ArrayList<>(queue)) {
                String line = format.formatted(Utils.getFormattedCurrentTime(), "INFO", task.getLine());
                SavingThread.queue.add(line);
                System.out.println(Ansi.colorize(line, task.getAttribute()));

                queue.remove();
            }
        }
    }

    private static class SavingThread extends RepeatingThread {

        private static final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        private static final String format = "[%s] [%s] %s";

        private SavingThread(long delay) {
            super(delay);
        }

        @Override
        public void runAfterDelay() {
            if(queue.isEmpty()) return;

            try {
                File file = new File("logs/latest.log");
                file.getParentFile().mkdirs();
                if(!file.exists()) {
                    file.createNewFile();
                }

                BufferedWriter writer = new BufferedWriter(new FileWriter("logs/latest.log", true));

                int amount = queue.size();

                for(String line : new ArrayList<>(queue)) {
                    writer.write(line);
                    writer.newLine();

                    queue.remove();
                }

                writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Line {

        private final String text;
        private final Attribute attribute;

        private Line(String text, Attribute attribute) {
            this.text = text;
            this.attribute = attribute;
        }

        public String getLine() {
            return text;
        }

        public Attribute getAttribute() {
            return attribute;
        }
    }
}
