package ru.leonidm.telegram_utils.commands;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.leonidm.telegram_utils.entities.TriPredicate;

public class Setting<T> {

    public static final Setting<Integer> MIN_ARGS_AMOUNT = new Setting<>((user, args, integer) -> args.length >= integer);
    public static final Setting<Integer> MAX_ARGS_AMOUNT = new Setting<>((user, args, integer) -> args.length <= integer);

    private final TriPredicate<User, String[], T> predicate;
    private final T t;

    private Setting(TriPredicate<User, String[], T> predicate) {
        this.predicate = predicate;
        this.t = null;
    }

    private Setting(TriPredicate<User, String[], T> predicate, @NotNull T t) {
        this.predicate = predicate;
        this.t = t;
    }

    /**
     * @param user User who sent the command
     * @param args Command arguments
     * @throws IllegalStateException If value wasn't set via {@link Setting#setValue(T)}
     * @return Predicate result
     */
    public boolean test(User user, String[] args) throws IllegalStateException {
        if(t == null) {
            throw new IllegalStateException("You must set value of T before use any setting!");
        }
        return predicate.test(user, args, t);
    }

    /**
     * @param newT Value that will be sent to the predicate
     * @return New instance of Setting
     */
    public Setting<T> setValue(T newT) {
        return new Setting<>(predicate, newT);
    }
}
