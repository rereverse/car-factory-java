package cz.zaoral.devchallange.carfactory.application;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Boolean.FALSE;

public class CarFactoryProductionControl {
    private final Queue<RunningCarProduction> runningSeeds = new LinkedList<>();
    private final CarFactory carFactory;
    private final AtomicLong totalProduced;

    public CarFactoryProductionControl(CarFactory carFactory) {
        this.carFactory = carFactory;
        this.totalProduced = new AtomicLong();
    }

    public void startProduction() {
        for (int i = 0; i < 10; i++) {
            plantASeed();
        }
    }

    private void plantASeed() {
        final RunningCarProduction p = new RunningCarProduction();
        seed(p);
        runningSeeds.add(p);
    }

    private RunningCarProduction seed(RunningCarProduction runningCarProduction) {
        carFactory.rollOutACar(car -> {
            totalProduced.incrementAndGet();
            if (!runningCarProduction.isStopped()) {
                seed(runningCarProduction);
            }
        });
        return runningCarProduction;
    }

    public Long getTotalProduced() {
        return totalProduced.get();
    }

    public static final class RunningCarProduction {
        private AtomicBoolean shouldStop = new AtomicBoolean(FALSE);

        public Boolean isStopped() {
            return shouldStop.get();
        }
    }
}
