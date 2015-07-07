package cz.zaoral.devchallange.carfactory;

import cz.zaoral.devchallange.carfactory.application.CarFactory;
import cz.zaoral.devchallange.carfactory.application.CarFactorySupply;
import cz.zaoral.devchallange.carfactory.application.model.Car;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static cz.zaoral.devchallange.carfactory.util.Util.*;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SimulationModel {
    static final double THROUGHPUT_DECREASE_RATIO = 1 / 3.0;
    static final double THROUGHPUT_INCREASE_RATIO = 2.0;
    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    static Integer requestedThroughput = 1;
    static MeasuredThroughput throughputMeasuring = new MeasuredThroughput();

    static CarFactorySupply carFactorySupply = new CarFactorySupply();
    static CarFactory carFactory = new CarFactory(carFactorySupply);

    public static void main(String[] args) throws InterruptedException, IOException {
        //schedule one and wait a second!
        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                final Integer measuredThroughput = throughputMeasuring.getAndReset();
                requestedThroughput = adjustThroughput(measuredThroughput);
                System.out.println(format("Measured throughput: %d, Requested throughput: %d", measuredThroughput, requestedThroughput));
                carFactory.rollOutCars(requestedThroughput, throughputMeasuring);
            }
        }, ONE, ONE, SECONDS);
        cancelOnUserInteraction(scheduledFuture);
        carFactorySupply.executorService().awaitTermination(1, MINUTES);
        scheduledExecutorService.shutdownNow();
    }

    private static void cancelOnUserInteraction(ScheduledFuture<?> factoryProduction) throws IOException {
        System.in.read();
        factoryProduction.cancel(SOFT_SHUTDOWN);
    }


    private static int adjustThroughput (Integer measuredThroughput) {
        return (int) (measuredThroughput < requestedThroughput
                ? Math.max(1, requestedThroughput * THROUGHPUT_DECREASE_RATIO)
                : requestedThroughput * THROUGHPUT_INCREASE_RATIO);
    }

    private static final class MeasuredThroughput implements Consumer<Car> {
        private final AtomicInteger carCounter = new AtomicInteger(EMPTY);

        @Override
        public void accept(Car car) {
            carCounter.incrementAndGet();
        }

        public Integer getAndReset() {
            return carCounter.getAndSet(EMPTY);
        }
    }
}
