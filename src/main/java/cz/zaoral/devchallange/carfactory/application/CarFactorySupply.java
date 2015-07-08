package cz.zaoral.devchallange.carfactory.application;

import cz.zaoral.devchallange.carfactory.IncrementSerialNumber;
import cz.zaoral.devchallange.carfactory.application.model.CarColour;
import cz.zaoral.devchallange.carfactory.application.model.Coachwork;
import cz.zaoral.devchallange.carfactory.application.model.Engine;
import cz.zaoral.devchallange.carfactory.application.model.Wheel;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class CarFactorySupply {
    private ExecutorService executorService;
    private Supplier<Long> incrementSerialNumber = new IncrementSerialNumber();
    private Supplier<Boolean> faultSupplier = () -> ThreadLocalRandom.current().nextDouble() < 0.1;
    private Supplier<Engine> engineSupplier = () -> new Engine(incrementSerialNumber.get(), faultSupplier.get());
    private Supplier<Coachwork> coachworkSupplier = () -> new Coachwork(incrementSerialNumber.get(), faultSupplier.get());
    private Supplier<Wheel> wheelSupplier = () -> new Wheel(incrementSerialNumber.get(), faultSupplier.get());
    private Supplier<CarColour> colourSupplier = () -> CarColour.values()[ThreadLocalRandom.current().nextInt(1, CarColour.values().length)];

    public CarFactorySupply(ExecutorService executorService) {
        this.executorService = executorService;
    }

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
}
