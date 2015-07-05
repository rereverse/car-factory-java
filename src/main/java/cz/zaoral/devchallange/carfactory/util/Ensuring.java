package cz.zaoral.devchallange.carfactory.util;

import java.util.function.Predicate;

public class Ensuring {
    private Ensuring() {
    }

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
