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

public class SimulationModel {
    static final double THROUGHPUT_DECREASE_RATIO = 1 / 3.0;
    static final double THROUGHPUT_INCREASE_RATIO = 2.0;
    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    static Integer requestedThroughput = 1;
    static MeasuredThroughput throughputMeasuring = new MeasuredThroughput();

    static CarFactorySupply carFactorySupply = new CarFactorySupply();
    static CarFactory carFactory = new CarFactory(carFactorySupply);

    public static void main(String[] args) throws InterruptedException, IOException {
        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                final Integer measuredThroughput = throughputMeasuring.getAndReset();
                requestedThroughput = adjustThrougput(measuredThroughput);
                System.out.println(format("Measured throughput: %d, Requested throughput: %d", measuredThroughput, requestedThroughput));
                carFactory.rollOutCars(requestedThroughput, throughputMeasuring);
            }
        }, NO_DELAY, ONE, MINUTES);
        cancelOnUserInteraction(scheduledFuture);
        carFactorySupply.executorService().awaitTermination(1, MINUTES);
        scheduledExecutorService.shutdownNow();
    }

    private static void cancelOnUserInteraction(ScheduledFuture<?> factoryProduction) throws IOException {
        factoryProduction.cancel(SOFT_SHUTDOWN);
    }


    private static int adjustThrougput(Integer measuredThroughput) {
        return (int) (measuredThroughput < requestedThroughput
                ? requestedThroughput * THROUGHPUT_DECREASE_RATIO
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
