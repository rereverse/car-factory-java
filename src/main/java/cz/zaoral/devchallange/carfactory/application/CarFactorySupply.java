package cz.zaoral.devchallange.carfactory.application;

import cz.zaoral.devchallange.carfactory.IncrementSerialNumber;
import cz.zaoral.devchallange.carfactory.application.model.CarColour;
import cz.zaoral.devchallange.carfactory.application.model.Coachwork;
import cz.zaoral.devchallange.carfactory.application.model.Engine;
import cz.zaoral.devchallange.carfactory.application.model.Wheel;

import java.util.concurrent.*;
import java.util.function.Supplier;

public class CarFactorySupply {
    private ExecutorService executorService = ForkJoinPool.commonPool();
    private Supplier<Long> incrementSerialNumber = new IncrementSerialNumber();
    private Supplier<Boolean> faultSupplier = () -> ThreadLocalRandom.current().nextBoolean();
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
