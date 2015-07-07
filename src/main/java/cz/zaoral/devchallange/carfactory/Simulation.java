package cz.zaoral.devchallange.carfactory;

import cz.zaoral.devchallange.carfactory.application.CarFactory;
import cz.zaoral.devchallange.carfactory.application.CarFactorySupply;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static cz.zaoral.devchallange.carfactory.util.Utils.*;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Simulation {
    static final AtomicInteger SECOND_COUNTER = new AtomicInteger();
    static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = newSingleThreadScheduledExecutor();
    static final AtomicInteger ROLLED_OUT = new AtomicInteger(EMPTY);
    static final CarFactorySupply carFactorySupply = new CarFactorySupply();
    static final CarFactory carFactory = new CarFactory(carFactorySupply);

    public static void main(String[] args) throws InterruptedException, IOException {
        for (int i = 0; i < 50; i++) {
            consumeCarByCar();
        }
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(
                () -> System.out.println(SECOND_COUNTER.incrementAndGet() + ": " + ROLLED_OUT.getAndSet(EMPTY)),
                DELAY_ONE_SECOND, EVERY_ONE_SECOND, SECONDS);
    }

    private static void consumeCarByCar() {
        carFactory.rollOutACar((c) -> {
            ROLLED_OUT.incrementAndGet();
            consumeCarByCar();
        });
    }
}
