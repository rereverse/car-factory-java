package cz.zaoral.devchallange.carfactory;

import cz.zaoral.devchallange.carfactory.model.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static cz.zaoral.devchallange.carfactory.model.CarColour.*;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CarFactory {
    public static void main(String[] args) {
        new CarFactory();
    }

    Supplier<Long> serialNumberSupplier = new AtomicLongSerialNumberSupplier();

    Supplier<Boolean> faultSupplier = () -> ThreadLocalRandom.current().nextBoolean();
    Supplier<Engine> engineSupplier = () -> new Engine(serialNumberSupplier.get(), faultSupplier.get());
    Supplier<Coachwork> coachworkSupplier = () -> new Coachwork(serialNumberSupplier.get(), faultSupplier.get());

    Supplier<Wheel> wheelSupplier = () -> new Wheel(serialNumberSupplier.get(), faultSupplier.get());
    Function<Engine, Optional<Engine>> engineCheck = e -> e.getFaulty() ? Optional.<Engine>empty() : Optional.of(e);
    Function<Coachwork, Optional<Coachwork>> coachworkCheck = c -> c.getFaulty() ? Optional.<Coachwork>empty() : Optional.of(c);

    Function<Wheel, Optional<Wheel>> wheelCheck = w -> w.getFaulty() ? Optional.<Wheel>empty() : Optional.of(w);
    UnaryOperator<Car> redPainter = car -> car.paint(RED);
    UnaryOperator<Car> greenPainter = car -> car.paint(GREEN);
    UnaryOperator<Car> bluePainter = car -> car.paint(BLUE);

    List<UnaryOperator<Car>> painters = unmodifiableList(asList(redPainter, greenPainter, bluePainter));

    Consumer<Car> carFactoryRollOut = System.out::println;

    CarFactory() {
        produceCar().thenAcceptAsync(carFactoryRollOut).join();
    }

    CompletableFuture<Car> produceCar() {
        return completedFuture(new Car.Builder(serialNumberSupplier.get()))
                .thenCombineAsync(produceEngine(), Car.Builder::setEngine)
                .thenCombineAsync(produceCoachwork(), Car.Builder::setCoachwork)
                .thenCombineAsync(produceWheels(), Car.Builder::setWheels)
                .thenApplyAsync(Car.Builder::build)
                .thenApplyAsync(randomPainter());
    }

    Function<Car, Car> randomPainter() {
        return painters.get(ThreadLocalRandom.current().nextInt(painters.size()));
    }

    CompletableFuture<Engine> produceEngine() {
        return produceCarPart(engineSupplier, engineCheck);
    }

    CompletableFuture<Coachwork> produceCoachwork() {
        return produceCarPart(coachworkSupplier, coachworkCheck);
    }

    CompletableFuture<Wheels> produceWheels() {
        return completedFuture(new Wheels.Builder())
                .thenCombineAsync(produceWheel(), Wheels.Builder::add)
                .thenCombineAsync(produceWheel(), Wheels.Builder::add)
                .thenCombineAsync(produceWheel(), Wheels.Builder::add)
                .thenCombineAsync(produceWheel(), Wheels.Builder::add)
                .thenApplyAsync(Wheels.Builder::build);
    }

    CompletableFuture<Wheel> produceWheel() {
        return produceCarPart(wheelSupplier, wheelCheck);
    }

    <T extends CarPart> CompletableFuture<T> produceCarPart(
            Supplier<T> carPartSupplier, Function<T, Optional<T>> check) {
        return supplyAsync(carPartSupplier::get)
                .thenApplyAsync(check)
                .thenComposeAsync(optionalCarPart -> optionalCarPart.isPresent()
                        ? completedFuture(optionalCarPart.get())
                        : produceCarPart(carPartSupplier, check));
    }
}
