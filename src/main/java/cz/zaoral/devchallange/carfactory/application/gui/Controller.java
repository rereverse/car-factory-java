package cz.zaoral.devchallange.carfactory.application.gui;

import cz.zaoral.devchallange.carfactory.application.Simulation;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static cz.zaoral.devchallange.carfactory.application.gui.Model.TIME_WINDOW_SIZE;
import static cz.zaoral.devchallange.carfactory.util.Utils.*;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

@RequiredArgsConstructor
public class Controller {
    private final Model model;
    private final Simulation simulation = new Simulation();
    private ScheduledExecutorService scheduledExecutorService;

    public void startSimulation() {
        model.reset();
        startHeartBeat(runCountersUpdate(), runGraphUpdate());
        simulation.start();
    }

    public void stopSimulation() {
        stopHeartBeat();
        simulation.stop();
    }

    public void applyNewDefections(TextField engineDefProb, TextField coachworkDefProb, TextField wheelDefProb) {
        simulation.getCarFactorySupply().updateEngineDefProb(handleDefProbField(engineDefProb));
        simulation.getCarFactorySupply().updateCoachworkDefProb(handleDefProbField(coachworkDefProb));
        simulation.getCarFactorySupply().updateWheelDefProb(handleDefProbField(wheelDefProb));
    }

    private double handleDefProbField(TextField defectionField) {
        try {
            return ensuring(Double.valueOf(defectionField.getText()), defFieldInBounds());
        } catch (Exception ignored) {
        }
        defectionField.setText(String.valueOf(DEFAULT_DEFECTION_PROBABILITY));
        return DEFAULT_DEFECTION_PROBABILITY;
    }

    private Predicate<Double> defFieldInBounds() {
        return p -> p >= 0.0 && p <= 0.99;
    }

    private void startHeartBeat(Runnable... heartBeatActions) {
        scheduledExecutorService = newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(() ->
                Stream.of(heartBeatActions).forEach(Runnable::run), HEART_BEAT_SPEED, HEART_BEAT_SPEED, SECONDS);
    }

    private void stopHeartBeat() {
        scheduledExecutorService.shutdown();
    }

    private Runnable runCountersUpdate() {
        return () -> {
            model.getSecondsCounter().getAndAdd(HEART_BEAT_SPEED.intValue());
            model.getTotalThroughput().set(simulation.getCarFactoryProductionControl().getTotalProduced());
        };
    }

    private Runnable runGraphUpdate() {
        final AtomicLong previousThroughput = new AtomicLong();
        return () -> {
            final Long totalProduced = model.getTotalThroughput().get();
            final Long sample = totalProduced - previousThroughput.getAndSet(totalProduced);
            model.getEma().apply(sample.doubleValue()).ifPresent(this::updateTimeSeriesSourceData);
        };
    }

    private void updateTimeSeriesSourceData(Double newSmoothedValue) {
        if (model.getThroughputSmoothed().size() == TIME_WINDOW_SIZE) {
            model.getThroughputSmoothed().remove(0);
        }
        model.getThroughputSmoothed().add(new XYChart.Data<>(model.getSecondsCounter().intValue(), newSmoothedValue));
    }
}
