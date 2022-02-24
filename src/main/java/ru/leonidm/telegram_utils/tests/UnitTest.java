package ru.leonidm.telegram_utils.tests;

import com.diogonunes.jcolor.Attribute;
import ru.leonidm.telegram_utils.logger.Logger;
import ru.leonidm.telegram_utils.entities.StaticPredicate;

public class UnitTest {

    private final String name;
    private final boolean result;

    /**
     * @param name This name will be displayed as test name
     * @param result Unit test result
     */
    public UnitTest(String name, boolean result) {
        this.name = name;
        this.result = result;
    }

    /**
     * @param name This name will be displayed as test name
     * @param predicate Unit test predicate
     */
    public UnitTest(String name, StaticPredicate predicate) {
        this.name = name;
        this.result = predicate.test();
    }

    public boolean run() {
        return result;
    }

    /**
     * @param name This name will be displayed as test name
     * @param unitTests All unit tests
     */
    public static void run(String name, UnitTest... unitTests) {
        int index = 1;
        int totalPassed = 0;

        Logger.info("=====", Attribute.BRIGHT_BLUE_TEXT());
        Logger.info("Running \"%s\" tasks:", Attribute.BRIGHT_BLUE_TEXT(), name);

        for(UnitTest unitTest : unitTests) {

            if(unitTest.run()) {
                Logger.info("\t✔ Task #%s \"%s\" passed successfully!", Attribute.GREEN_TEXT(), index, unitTest.name);
                totalPassed += 1;
            }
            else {
                Logger.info("\t❌ Task #%s \"%s\" didn't pass!", Attribute.RED_TEXT(), index, unitTest.name);
            }

            index++;
        }

        Logger.info("Passed tests: %s", Attribute.BRIGHT_BLUE_TEXT(), totalPassed);
        Logger.info("Total tests: %s", Attribute.BRIGHT_BLUE_TEXT(), index - 1);
        Logger.info("=====", Attribute.BRIGHT_BLUE_TEXT());
    }
}
