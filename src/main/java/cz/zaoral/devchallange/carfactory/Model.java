package cz.zaoral.devchallange.carfactory;

import cz.zaoral.devchallange.carfactory.util.ExponentialMovingAverage;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lombok.Getter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static javafx.collections.FXCollections.observableArrayList;

@Getter
public class Model {
    public static final Integer EMA_PERIOD = 5;

    private final ObservableList<XYChart.Data<Number, Number>> throughputSmoothed = observableArrayList();
    private final ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();
    private final AtomicInteger secondsCounter = new AtomicInteger();
    private ExponentialMovingAverage ema = new ExponentialMovingAverage(EMA_PERIOD);
}
