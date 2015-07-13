package cz.zaoral.devchallange.carfactory.application;

import cz.zaoral.devchallange.carfactory.application.model.CarColour;
import cz.zaoral.devchallange.carfactory.application.model.Coachwork;
import cz.zaoral.devchallange.carfactory.application.model.Engine;
import cz.zaoral.devchallange.carfactory.application.model.Wheel;
import cz.zaoral.devchallange.carfactory.util.IncrementSerialNumber;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class CarFactorySupply {
    private ExecutorService executorService;
    private Supplier<Long> incrementingSerialNumber = new IncrementSerialNumber(0L);
    private AtomicReference<Double> engineDefectionProbability;
    private AtomicReference<Double> coachworkDefectionProbability;
    private AtomicReference<Double> wheelDefectionProbability;

    private Supplier<Engine> engineSupplier = () -> new Engine(incrementingSerialNumber.get(), isDefective(engineDefectionProbability.get()));
    private Supplier<Coachwork> coachworkSupplier = () -> new Coachwork(incrementingSerialNumber.get(), isDefective(coachworkDefectionProbability.get()));
    private Supplier<Wheel> wheelSupplier = () -> new Wheel(incrementingSerialNumber.get(), isDefective(wheelDefectionProbability.get()));
    private Supplier<CarColour> colourSupplier = () -> CarColour.values()[ThreadLocalRandom.current().nextInt(1, CarColour.values().length)];

    public CarFactorySupply(ExecutorService executorService, Double engineDefectionProbability, Double coachworkDefectionProbability, Double wheelDefectionProbability) {
        this.executorService = executorService;
        this.engineDefectionProbability = new AtomicReference<>(engineDefectionProbability);
        this.coachworkDefectionProbability = new AtomicReference<>(coachworkDefectionProbability);
        this.wheelDefectionProbability = new AtomicReference<>(wheelDefectionProbability);
    }

    public Long serialNumber() {
        return incrementingSerialNumber.get();
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

    public void updateEngineDefProb(double probability) {
        engineDefectionProbability.set(probability);
    }

    public void updateCoachworkDefProb(double probability) {
        coachworkDefectionProbability.set(probability);
    }

    public void updateWheelDefProb(double probability) {
        wheelDefectionProbability.set(probability);
    }

    private Boolean isDefective(double probability) {
        return ThreadLocalRandom.current().nextDouble() < probability;
    }
}
