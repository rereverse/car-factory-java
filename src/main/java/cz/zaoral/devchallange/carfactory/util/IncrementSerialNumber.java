package cz.zaoral.devchallange.carfactory.util;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class IncrementSerialNumber implements Supplier<Long> {
    private final AtomicLong atomicLong;

    public IncrementSerialNumber(Long startingValue) {
        this.atomicLong = new AtomicLong(startingValue);
    }

    @Override
    public Long get() {
        return atomicLong.getAndIncrement();
    }
}
