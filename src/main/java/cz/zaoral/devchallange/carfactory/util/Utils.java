package cz.zaoral.devchallange.carfactory.util;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;

import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;

public class Utils {
    private Utils() {
    }

    public static final int ZERO = 0;
    public static final long DELAY_ONE = 1L;
    public static final long EVERY_ONE = 1L;
    public static final long ONE = 1L;
    public static final Long FIVE = 5L;
    public static final boolean DONT_INTERRUPT_IF_RUNNING = Boolean.FALSE;
    public static final Thread.UncaughtExceptionHandler NO_UNCAUGHT_EXCEPTION_HANDLER = (t, e) -> {
    };
    public static final boolean ASYNC_MODE_ON = true;

    public static ForkJoinPool defaultAsyncForkJoinPool(int threads) {
        return new ForkJoinPool(threads, defaultForkJoinWorkerThreadFactory,
                NO_UNCAUGHT_EXCEPTION_HANDLER, ASYNC_MODE_ON);
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
