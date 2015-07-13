package cz.zaoral.devchallange.carfactory.application;

import cz.zaoral.devchallange.carfactory.application.model.Car;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static java.lang.Runtime.getRuntime;

public class CarFactoryProductionControl {
    private static final int CARS_AT_A_TIME = getRuntime().availableProcessors();
    private final CarFactory carFactory;
    private final AtomicLong totalProduced;

    public CarFactoryProductionControl(CarFactory carFactory) {
        this.carFactory = carFactory;
        this.totalProduced = new AtomicLong();
    }

    public void startProduction(Consumer<Car> carConsumer) {
        for (int i = 0; i < CARS_AT_A_TIME; i++) {
            seed(carConsumer);
        }
    }

    private void seed(Consumer<Car> carConsumer) {
        carFactory.rollOutACar(car -> {
            totalProduced.incrementAndGet();
            carConsumer.accept(car);
            seed(carConsumer);
        });
    }

    public Long getTotalProduced() {
        return totalProduced.get();
    }
}
