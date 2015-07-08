package cz.zaoral.devchallange.carfactory;

import cz.zaoral.devchallange.carfactory.application.CarFactory;
import cz.zaoral.devchallange.carfactory.application.CarFactorySupply;
import cz.zaoral.devchallange.carfactory.application.model.Car;
import cz.zaoral.devchallange.carfactory.util.ExponentialMovingAverage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static cz.zaoral.devchallange.carfactory.util.Utils.FIVE;
import static cz.zaoral.devchallange.carfactory.util.Utils.defaultAsyncForkJoinPool;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javafx.collections.FXCollections.observableList;

public class Main extends Application {
    private static final int NUMBER_OF_CARS_AT_A_TIME = 10;

    public static void main(String[] args) {
        launch(args);
    }

    private final ExecutorService executorService = defaultAsyncForkJoinPool(6);
    private final ScheduledExecutorService scheduledExecutorService = newSingleThreadScheduledExecutor();
    private final AtomicLong rolledOutCounter = new AtomicLong();
    private final AtomicInteger secondsCounter = new AtomicInteger();
    private final ExponentialMovingAverage ema = new ExponentialMovingAverage(3);

    private final ObservableList<Data<Number, Number>> throughput = observableList(new LinkedList<>());
    private final ObservableList<Data<Number, Number>> throughputSmoothed = observableList(new LinkedList<>());

    @Override
    public void start(Stage stage) throws Exception {
        // https://community.oracle.com/thread/2413087
        startCarFactory();
        startThroughputSampling();
        createAndShow(stage);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.runLater(() -> {
            executorService.shutdown();
            scheduledExecutorService.shutdown();
        });
    }

    private void startThroughputSampling() {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            final Integer second = secondsCounter.addAndGet(FIVE.intValue());
            final Long rolledOutSinceLastCheck = rolledOutCounter.getAndSet(0);
            final Optional<Double> ema = this.ema.apply(rolledOutSinceLastCheck.doubleValue());
            Platform.runLater(updateThroughPuts(second, rolledOutSinceLastCheck, ema));
        }, FIVE, FIVE, SECONDS);
    }

    private Runnable updateThroughPuts(Integer second, Long immediateValue, Optional<Double> smoothedValue) {
        return () -> {
            throughput.add(new Data<>(second, immediateValue));
            smoothedValue.ifPresent(v -> throughputSmoothed.add(new Data<>(second, v)));
        };
    }

    private void startCarFactory() {
        final CarFactorySupply carFactorySupply = new CarFactorySupply(executorService);
        final CarFactory carFactory = new CarFactory(carFactorySupply);
        for (int i = 0; i < NUMBER_OF_CARS_AT_A_TIME; i++) {
            carFactory.rollOutACar(recordCarOutAndRequestAnother(carFactory));
        }
    }

    private Consumer<Car> recordCarOutAndRequestAnother(CarFactory carFactory) {
        return (c) -> {
            rolledOutCounter.incrementAndGet();
            carFactory.rollOutACar(recordCarOutAndRequestAnother(carFactory));
        };
    }

    private void createAndShow(Stage stage) {
        Scene scene = new Scene(createChart(), 800, 600);
        stage.setTitle("Line Chart Sample");
        stage.setScene(scene);
        stage.show();
    }

    private LineChart<Number, Number> createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        yAxis.setLabel("TP / 5 s");
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(new Series<>("TP", throughput));
        final Series<Number, Number> e = new Series<>("EMA(TP, 3)", throughputSmoothed);
        lineChart.getData().add(e);
        lineChart.setTitle("Car Factory throughput");
        return lineChart;
    }
}
