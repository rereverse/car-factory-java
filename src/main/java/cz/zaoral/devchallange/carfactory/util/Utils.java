package cz.zaoral.devchallange.carfactory.util;

import java.util.function.Predicate;

public class Utils {
    private Utils() {
    }

    public static final int EMPTY = 0;
    public static final long DELAY_ONE_SECOND = 1L;
    public static final long EVERY_ONE_SECOND = 1L;
    public static final Thread.UncaughtExceptionHandler NO_UNCAUGHT_EXCEPTION_HANDLER = (t, e) -> {
    };
    public static final boolean ASYNC_MODE_ON = true;

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
