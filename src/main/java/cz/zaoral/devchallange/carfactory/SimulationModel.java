package cz.zaoral.devchallange.carfactory;

import cz.zaoral.devchallange.carfactory.application.CarFactory;
import cz.zaoral.devchallange.carfactory.application.CarFactorySupply;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static cz.zaoral.devchallange.carfactory.util.Util.EMPTY;
import static cz.zaoral.devchallange.carfactory.util.Util.ONE;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SimulationModel {
    static final double THROUGHPUT_DECREASE_RATIO = 1 / 3.0;
    static final double THROUGHPUT_INCREASE_RATIO = 2.0;
    static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();
    static final AtomicInteger ROLLED_OUT = new AtomicInteger(EMPTY);
    static final CarFactorySupply carFactorySupply = new CarFactorySupply();
    static final CarFactory carFactory = new CarFactory(carFactorySupply);

    public static void main(String[] args) throws InterruptedException, IOException {
        rollOutCarsOneByOne();
        SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(() -> System.out.println(ROLLED_OUT.getAndSet(EMPTY)), ONE, ONE, SECONDS);
    }

    private static void rollOutCarsOneByOne() {
        carFactory.rollOutACar((c) -> {
            ROLLED_OUT.incrementAndGet();
            rollOutCarsOneByOne();
        });
    }
}
