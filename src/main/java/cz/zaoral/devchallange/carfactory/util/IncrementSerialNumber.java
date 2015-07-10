package cz.zaoral.devchallange.carfactory.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class IncrementSerialNumber implements Supplier<Long> {
    private final AtomicLong atomicLong = new AtomicLong();

    @Override
    public Long get() {
        return atomicLong.getAndIncrement();
    }
}
