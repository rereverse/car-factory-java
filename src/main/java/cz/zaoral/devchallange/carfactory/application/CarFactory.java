package cz.zaoral.devchallange.carfactory.application;

import cz.zaoral.devchallange.carfactory.application.model.*;
import cz.zaoral.devchallange.carfactory.application.model.Car.CarBeingBuilt;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static cz.zaoral.devchallange.carfactory.util.Utils.ensuringNotNull;
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
        return supplyAsync(() -> new CarBeingBuilt(supply.serialNumber()), supply.worker())
                .thenCombine(produceCoachwork(), CarBeingBuilt::addCoachwork)
                .thenCombine(produceEngine(), CarBeingBuilt::addEngine)
                .thenCombine(produceWheel(), CarBeingBuilt::addWheel)
                .thenCombine(produceWheel(), CarBeingBuilt::addWheel)
                .thenCombine(produceWheel(), CarBeingBuilt::addWheel)
                .thenCombine(produceWheel(), CarBeingBuilt::addWheel)
                .thenApply(CarBeingBuilt::build)
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
        return supplyAsync(() -> {
            T carPart = carPartSupplier.get();
            while (carPart.defective()) {
                carPart = carPartSupplier.get();
            }
            return carPart;
        }, supply.worker());
    }

    private UnaryOperator<Car> paintWithRandomColour() {
        return car -> car.paint(supply.pickRandomColour());
    }
}
