package cz.zaoral.devchallange.carfactory.util;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;

import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;
import static java.util.concurrent.ForkJoinPool.getCommonPoolParallelism;

public class Utils {
    private Utils() {
    }

    public static final int TIME_WINDOW_SIZE = 780;
    public static final Long SAMPLING_SPEED = 1L;
    public static final boolean DONT_INTERRUPT_IF_RUNNING = Boolean.FALSE;
    public static final double DEFAULT_DEFECTION_PROBABILITY = 0.1;

    public static final Thread.UncaughtExceptionHandler NO_UNCAUGHT_EXCEPTION_HANDLER = (t, e) -> {
    };
    public static final boolean ASYNC_MODE_ON = true;

    public static ForkJoinPool defaultAsyncForkJoinPool() {
        return new ForkJoinPool(getCommonPoolParallelism(), defaultForkJoinWorkerThreadFactory,
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
