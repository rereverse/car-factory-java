package cz.zaoral.devchallange.carfactory;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class AtomicLongSerialNumberSupplier implements Supplier<Long> {
    private final AtomicLong atomicLong = new AtomicLong();

    @Override
    public Long get() {
        return atomicLong.getAndIncrement();
    }
}
