package cz.zaoral.devchallange.carfactory.application.gui;

import cz.zaoral.devchallange.carfactory.util.SimpleMovingAverage;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lombok.Getter;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static javafx.collections.FXCollections.observableList;

@Getter
public class Model {
    public static final Integer SMA_PERIOD = 8;
    public static final Integer TIME_WINDOW_SIZE = 900;
    public static final Integer MILLION = 1000000;

    private final ObservableList<XYChart.Data<Number, Number>> throughputSmoothed = observableList(new LinkedList<>());
    private final ObservableList<String> producedCarsList = observableList(new LinkedList<>());
    private final AtomicInteger secondsCounter = new AtomicInteger();
    private final AtomicLong totalThroughput = new AtomicLong();
    private SimpleMovingAverage sma;

    public void reset() {
        throughputSmoothed.clear();
        producedCarsList.clear();
        secondsCounter.set(0);
        totalThroughput.set(0);
        sma = new SimpleMovingAverage(SMA_PERIOD);
    }

    public Integer currentSeconds() {
        return secondsCounter.get();
    }
}
