package ru.leonidm.telegram_utils.entities;

/**
 * It's kinda stupid functional interface, because
 * it can be replaced only with boolean type,
 * but it was created for unit tests, so it's possible
 * to write more difficult tests in constructor<br><br>
 * <b>Don't use it outside of the unit tests</b>
 */
@FunctionalInterface
public interface StaticPredicate {

    boolean test();
}
