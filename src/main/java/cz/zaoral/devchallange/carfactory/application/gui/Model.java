package cz.zaoral.devchallange.carfactory.application.gui;

import cz.zaoral.devchallange.carfactory.util.ExponentialMovingAverage;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lombok.Getter;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static javafx.collections.FXCollections.observableList;

@Getter
public class Model {
    public static final Integer EMA_PERIOD = 5;
    public static final int TIME_WINDOW_SIZE = 780;

    private final ObservableList<XYChart.Data<Number, Number>> throughputSmoothed = observableList(new LinkedList<>());
    private final AtomicInteger secondsCounter = new AtomicInteger();
    private final AtomicLong totalThroughput = new AtomicLong();
    private ExponentialMovingAverage ema;

    public void reset() {
        throughputSmoothed.clear();
        secondsCounter.set(0);
        totalThroughput.set(0);
        ema = new ExponentialMovingAverage(EMA_PERIOD);
    }
}
