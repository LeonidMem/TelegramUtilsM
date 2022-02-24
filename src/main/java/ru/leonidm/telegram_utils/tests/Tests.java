package ru.leonidm.telegram_utils.tests;

import ru.leonidm.telegram_utils.config.YamlConfiguration;
import ru.leonidm.telegram_utils.commands.Setting;
import ru.leonidm.telegram_utils.logger.Logger;

public class Tests {

    public static void main(String[] args) {
        runInternal();
        Logger.disableRelativeThreads();
    }

    public static void runInternal() {
        runYaml();
        runSetting();
    }

    public static void runYaml() {
        YamlConfiguration config = new YamlConfiguration("tasks");

        UnitTest.run("YamlConfiguration",
                new UnitTest("getString", "string".equals(config.getString("string"))),
                new UnitTest("getInt", config.getInt("int") == 15),
                new UnitTest("getLong", config.getLong("long") == 1099511627776L),
                new UnitTest("getBoolean-1", config.getBoolean("boolean.1")),
                new UnitTest("getBoolean-2", !config.getBoolean("boolean.2")),
                new UnitTest("getStringList-1", config.getStringList("list.strings").size() == 3),
                new UnitTest("getStringList-2", config.getStringList("list.unknown").isEmpty()),
                new UnitTest("getSection-1", config.getSection("boolean") != null),
                new UnitTest("getSection-2", config.getSection("unknown") == null),
                new UnitTest("hasKey-1", config.hasKey("list.strings")),
                new UnitTest("hasKey-2", !config.hasKey("unknown")),
                new UnitTest("getKeys", config.getKeys().size() == 5));
    }

    public static void runSetting() {
        UnitTest.run("Setting",
                new UnitTest("minArgsAmount-1", Setting.MIN_ARGS_AMOUNT.setValue(1).test(null, new String[3])),
                new UnitTest("minArgsAmount-2", Setting.MIN_ARGS_AMOUNT.setValue(3).test(null, new String[3])),
                new UnitTest("minArgsAmount-3", !Setting.MIN_ARGS_AMOUNT.setValue(5).test(null, new String[3])),
                new UnitTest("maxArgsAmount-1", !Setting.MAX_ARGS_AMOUNT.setValue(1).test(null, new String[3])),
                new UnitTest("maxArgsAmount-2", Setting.MAX_ARGS_AMOUNT.setValue(3).test(null, new String[3])),
                new UnitTest("maxArgsAmount-3", Setting.MAX_ARGS_AMOUNT.setValue(5).test(null, new String[3])));
    }
}
