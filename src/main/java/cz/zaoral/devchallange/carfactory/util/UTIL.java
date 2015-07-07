package cz.zaoral.devchallange.carfactory.util;

import java.util.function.Predicate;

public class Util {
    private Util() {
    }

    public static final int EMPTY = 0;
    public static final long NO_DELAY = 0L;
    public static final long ONE = 1L;
    public static final boolean SOFT_SHUTDOWN = false;

    public static <T> T ensuring(T t, Predicate<T> p) {
        if (!p.test(t)) {
            throw new ValidationException();
        }
        return t;
    }

    public static <T> T ensuringNotNull(T t) {
        if (t == null) {
            throw new ValidationException();
        }
        return t;
    }

    public static class ValidationException extends RuntimeException {
    }
}
