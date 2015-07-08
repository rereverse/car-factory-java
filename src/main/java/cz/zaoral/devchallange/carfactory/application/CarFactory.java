package cz.zaoral.devchallange.carfactory.application;

import cz.zaoral.devchallange.carfactory.application.model.*;
import cz.zaoral.devchallange.carfactory.application.model.Car.CarBeingBuilt;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.*;

import static cz.zaoral.devchallange.carfactory.util.Utils.ensuringNotNull;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CarFactory {
    private final CarFactorySupply supply;

    public CarFactory(CarFactorySupply supply) {
        this.supply = ensuringNotNull(supply);
    }

    public void rollOutACar(Consumer<Car> carConsumer) {
        produceCar().thenAcceptAsync(carConsumer);
    }

    CompletableFuture<Car> produceCar() {
        return completedFuture(new CarBeingBuilt(supply.serialNumber()))
                .thenCombineAsync(produceCoachwork(), CarBeingBuilt::addCoachwork, supply.worker())
                .thenCombineAsync(produceEngine(), CarBeingBuilt::addEngine, supply.worker())
                .thenCombineAsync(produceWheel(), CarBeingBuilt::addWheel, supply.worker())
                .thenCombineAsync(produceWheel(), CarBeingBuilt::addWheel, supply.worker())
                .thenCombineAsync(produceWheel(), CarBeingBuilt::addWheel, supply.worker())
                .thenCombineAsync(produceWheel(), CarBeingBuilt::addWheel, supply.worker())
                .thenApplyAsync(CarBeingBuilt::build, supply.worker())
                .thenApplyAsync(paintWithRandomColour(), supply.worker());
    }

    CompletableFuture<Engine> produceEngine() {
        return produceCarPart(supply.engineSupplier());
    }

    CompletableFuture<Coachwork> produceCoachwork() {
        return produceCarPart(supply.coachworkSupplier());
    }

    CompletableFuture<Wheel> produceWheel() {
        return produceCarPart(supply.wheelSupplier());
    }

    private <T extends CarPart> CompletableFuture<T> produceCarPart(Supplier<T> carPartSupplier) {
        return supplyAsync(carPartSupplier::get, supply.worker())
                .thenApplyAsync(qualityControl(), supply.worker())
                .thenComposeAsync(anotherIfThisDefective(carPartSupplier), supply.worker());

    }

    private <T extends CarPart> Function<Optional<T>, CompletionStage<T>> anotherIfThisDefective(
            Supplier<T> carPartSupplier) {
        return carPart -> carPart.isPresent()
                ? completedFuture(carPart.get())
                : produceCarPart(carPartSupplier); // not recursion!
    }

    private <T extends CarPart> Function<T, Optional<T>> qualityControl() {
        return t -> t.defective() ? Optional.<T>empty() : Optional.of(t);
    }

    private UnaryOperator<Car> paintWithRandomColour() {
        return car -> car.paint(supply.pickRandomColour());
    }
}
