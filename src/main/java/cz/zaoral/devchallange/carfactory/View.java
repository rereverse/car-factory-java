package cz.zaoral.devchallange.carfactory;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static cz.zaoral.devchallange.carfactory.View.ViewState.SIMULATION_STARTED;
import static cz.zaoral.devchallange.carfactory.View.ViewState.SIMULATION_STOPPED;
import static cz.zaoral.devchallange.carfactory.util.Utils.DEFAULT_DEFECTION_PROBABILITY;
import static cz.zaoral.devchallange.carfactory.util.Utils.SAMPLING_SPEED;
import static cz.zaoral.devchallange.carfactory.util.Utils.TIME_WINDOW_SIZE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javafx.application.Platform.runLater;

@Getter
public class View {
    public enum ViewState {
        SIMULATION_STARTED, SIMULATION_STOPPED;
    }

    private final Model model;
    private final Controller controller;

    private final Button startButton = new Button("Start");
    private final Button stopButton = new Button("Stop");
    private final TextField engineDefectionsTextField = new TextField(String.valueOf(DEFAULT_DEFECTION_PROBABILITY));
    private final TextField coachworkDefectionsTextField = new TextField(String.valueOf(DEFAULT_DEFECTION_PROBABILITY));
    private final TextField wheelDefectionsTextField = new TextField(String.valueOf(DEFAULT_DEFECTION_PROBABILITY));
    private final Button applyDefectionsButton = new Button("Apply");
    private ScheduledFuture<?> heartBeat;

    public View(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }

    public void show(Stage stage) {
        createAndShow(stage);
    }

    public void updateView(ViewState viewState) {
        if (viewState == SIMULATION_STARTED) {
            startHeartBeat(updateGraph(), () -> model.getSecondsCounter().getAndAdd(SAMPLING_SPEED.intValue()));
            startButton.setDisable(TRUE);
            stopButton.setDisable(FALSE);
            applyDefectionsButton.setDisable(FALSE);
        } else if (viewState == SIMULATION_STOPPED) {
            heartBeat.cancel(TRUE);
            startButton.setDisable(FALSE);
            stopButton.setDisable(TRUE);
            applyDefectionsButton.setDisable(TRUE);
        }
    }

    private void startHeartBeat(Runnable... actions) {
        heartBeat = model.getScheduledExecutorService().scheduleAtFixedRate(() ->
                Stream.of(actions).forEach(Runnable::run), SAMPLING_SPEED, SAMPLING_SPEED, SECONDS);
    }

    private VBox createVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        return vbox;
    }

    private LineChart<Number, Number> createChart() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        xAxis.setForceZeroInRange(false);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setForceZeroInRange(false);
        yAxis.setLabel("EMA(5)");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(new XYChart.Series<>("Cars per second", model.getThroughputSmoothed()));
        lineChart.setTitle("Car Factory Throughput");
        lineChart.setAnimated(FALSE);
        lineChart.setCreateSymbols(FALSE);
        return lineChart;
    }

    private void createAndShow(Stage stage) {
        final VBox vBox = createVBox();
        vBox.getChildren().addAll(createInputs(), createChart(), createControls());
        Scene scene = new Scene(vBox, 800, 600);
        stage.setTitle("CarFactory");
        stage.setScene(scene);
        stage.show();
    }

    private HBox createInputs() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        engineDefectionsTextField.setPrefWidth(50);
        coachworkDefectionsTextField.setPrefWidth(50);
        wheelDefectionsTextField.setPrefWidth(50);

        applyDefectionsButton.setOnAction(e -> runLater(
                () -> controller.applyNewDefections(
                        engineDefectionsTextField,
                        coachworkDefectionsTextField,
                        wheelDefectionsTextField)));
        applyDefectionsButton.setDisable(TRUE);

        hbox.getChildren().addAll(new Label("DEFECTIVE % :"), new Label("ENGINE"), engineDefectionsTextField,
                new Label("COACHWORK"), coachworkDefectionsTextField, new Label("WHEEL"), wheelDefectionsTextField,
                applyDefectionsButton);

        return hbox;
    }

    private HBox createControls() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        startButton.setOnAction(e -> runLater(controller::startSimulation));
        stopButton.setOnAction(event -> runLater(controller::stopSimulation));
        stopButton.setDisable(TRUE);
        hBox.getChildren().addAll(startButton, stopButton);
        return hBox;
    }

    private Runnable updateGraph() {
        final AtomicLong previousThroughput = new AtomicLong();
        return () -> {
            final Long totalProduced = controller.getTotalProduced();
            final Long sample = totalProduced - previousThroughput.getAndSet(totalProduced);
            final Integer second = secondsCounter.getAndAdd(SAMPLING_SPEED.intValue());
            final Optional<Double> emaValue = ema.apply(sample.doubleValue());

            emaValue.ifPresent(value -> trackThroughput(second, value));
        };
    }

    private void trackThroughput(Integer second, Double newSmoothedValue) {
        if (model.getThroughputSmoothed().size() == TIME_WINDOW_SIZE) {
            model.getThroughputSmoothed().remove(0);
        }
        model.getThroughputSmoothed().add(new XYChart.Data<>(second, newSmoothedValue));
    }

}
