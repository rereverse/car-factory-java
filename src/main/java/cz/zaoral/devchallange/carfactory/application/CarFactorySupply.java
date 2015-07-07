package cz.zaoral.devchallange.carfactory.application;

import cz.zaoral.devchallange.carfactory.IncrementSerialNumber;
import cz.zaoral.devchallange.carfactory.application.model.CarColour;
import cz.zaoral.devchallange.carfactory.application.model.Coachwork;
import cz.zaoral.devchallange.carfactory.application.model.Engine;
import cz.zaoral.devchallange.carfactory.application.model.Wheel;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import static cz.zaoral.devchallange.carfactory.util.Utils.ASYNC_MODE_ON;
import static cz.zaoral.devchallange.carfactory.util.Utils.NO_UNCAUGHT_EXCEPTION_HANDLER;
import static java.lang.Runtime.getRuntime;
import static java.util.concurrent.ForkJoinPool.defaultForkJoinWorkerThreadFactory;

public class CarFactorySupply {
    private ExecutorService executorService = new ForkJoinPool(getRuntime().availableProcessors(), defaultForkJoinWorkerThreadFactory, NO_UNCAUGHT_EXCEPTION_HANDLER, ASYNC_MODE_ON);
    private Supplier<Long> incrementSerialNumber = new IncrementSerialNumber();
    private Supplier<Boolean> faultSupplier = () -> ThreadLocalRandom.current().nextDouble() < 0.1;
    private Supplier<Engine> engineSupplier = () -> new Engine(incrementSerialNumber.get(), faultSupplier.get());
    private Supplier<Coachwork> coachworkSupplier = () -> new Coachwork(incrementSerialNumber.get(), faultSupplier.get());
    private Supplier<Wheel> wheelSupplier = () -> new Wheel(incrementSerialNumber.get(), faultSupplier.get());
    private Supplier<CarColour> colourSupplier = () -> CarColour.values()[ThreadLocalRandom.current().nextInt(1, CarColour.values().length)];

    public Long serialNumber() {
        return incrementSerialNumber.get();
    }

    public CarColour pickRandomColour() {
        return colourSupplier.get();
    }

    public Supplier<Wheel> wheelSupplier() {
        return wheelSupplier;
    }

    public Supplier<Coachwork> coachworkSupplier() {
        return coachworkSupplier;
    }

    public Supplier<Engine> engineSupplier() {
        return engineSupplier;
    }

    public Executor worker() {
        return executorService;
    }

    public ExecutorService executorService() {
        return executorService;
    }
}
