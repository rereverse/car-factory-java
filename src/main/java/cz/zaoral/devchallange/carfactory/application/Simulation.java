package cz.zaoral.devchallange.carfactory.application;

import lombok.Getter;

import java.util.concurrent.ExecutorService;

import static cz.zaoral.devchallange.carfactory.util.Utils.DEFAULT_DEFECTION_PROBABILITY;
import static cz.zaoral.devchallange.carfactory.util.Utils.defaultAsyncForkJoinPool;

public class Simulation {
    private ExecutorService executorService;
    @Getter
    private CarFactorySupply carFactorySupply;
    @Getter
    private CarFactoryProductionControl carFactoryProductionControl;

    public void start() {
        executorService = defaultAsyncForkJoinPool();
        carFactorySupply = new CarFactorySupply(executorService, DEFAULT_DEFECTION_PROBABILITY,
                DEFAULT_DEFECTION_PROBABILITY, DEFAULT_DEFECTION_PROBABILITY);
        carFactoryProductionControl = new CarFactoryProductionControl(new CarFactory(carFactorySupply));
        carFactoryProductionControl.startProduction();
    }

    public void stop() {
        executorService.shutdown();
    }
}
