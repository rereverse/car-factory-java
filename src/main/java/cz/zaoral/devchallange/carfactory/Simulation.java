package cz.zaoral.devchallange.carfactory;

import cz.zaoral.devchallange.carfactory.application.CarFactory;
import cz.zaoral.devchallange.carfactory.application.CarFactoryProductionControl;
import cz.zaoral.devchallange.carfactory.application.CarFactorySupply;
import lombok.Getter;

import java.util.concurrent.ExecutorService;

import static cz.zaoral.devchallange.carfactory.util.Utils.DEFAULT_DEFECTION_PROBABILITY;
import static cz.zaoral.devchallange.carfactory.util.Utils.defaultAsyncForkJoinPool;

public class Simulation {
    private ExecutorService executorService;
    @Getter
    private CarFactorySupply carFactorySupply;
    @Getter
    private CarFactory carFactory;
    @Getter
    private CarFactoryProductionControl carFactoryProductionControl;

    public void start() {
        executorService = defaultAsyncForkJoinPool();
        carFactorySupply = new CarFactorySupply(executorService, DEFAULT_DEFECTION_PROBABILITY,
                DEFAULT_DEFECTION_PROBABILITY, DEFAULT_DEFECTION_PROBABILITY);
        carFactory = new CarFactory(carFactorySupply);
        carFactoryProductionControl = new CarFactoryProductionControl(carFactory);
        carFactoryProductionControl.startProduction();
    }

    public void stop() {
        executorService.shutdown();
    }
}
