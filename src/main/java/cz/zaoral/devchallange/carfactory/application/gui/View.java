package cz.zaoral.devchallange.carfactory.application.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.stream.Stream;

import static cz.zaoral.devchallange.carfactory.application.gui.Model.SMA_PERIOD;
import static cz.zaoral.devchallange.carfactory.util.Utils.DEFAULT_DEFECTION_PROBABILITY;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;

@Getter
public class View {
    private final Model model;
    private final Controller controller;

    private final Button startButton = new Button("Start");
    private final Button stopButton = new Button("Stop");
    private final TextField engineDefectionsTextField = new TextField(String.valueOf(DEFAULT_DEFECTION_PROBABILITY));
    private final TextField coachworkDefectionsTextField = new TextField(String.valueOf(DEFAULT_DEFECTION_PROBABILITY));
    private final TextField wheelDefectionsTextField = new TextField(String.valueOf(DEFAULT_DEFECTION_PROBABILITY));
    private final Button applyDefectionsButton = new Button("Apply");

    public View(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }

    public void show(Stage stage) {
        createAndShow(stage);
    }

    private void createAndShow(Stage stage) {
        final VBox vBox = createVBox();
        vBox.getChildren().addAll(createInputs(), createChart(), createProducedCarsList(), createControls());
        Scene scene = new Scene(vBox, 1000, 750);
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

        applyDefectionsButton.setDisable(TRUE);
        applyDefectionsButton.setOnAction(e -> runLater(
                () -> controller.applyNewDefections(
                        engineDefectionsTextField,
                        coachworkDefectionsTextField,
                        wheelDefectionsTextField)));

        hbox.getChildren().addAll(new Label("DEFECTIVE (from 0.0 to 1.0):"), new Label("ENGINE"),
                engineDefectionsTextField, new Label("COACHWORK"), coachworkDefectionsTextField, new Label("WHEEL"),
                wheelDefectionsTextField, applyDefectionsButton);

        return hbox;
    }

    private LineChart<Number, Number> createChart() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        xAxis.setForceZeroInRange(false);

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setForceZeroInRange(false);
        yAxis.setLabel(format("SMA(%d)", SMA_PERIOD));

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.getData().add(new XYChart.Series<>("Cars per second", model.getThroughputSmoothed()));
        lineChart.setTitle("Car Factory Throughput");
        lineChart.setAnimated(FALSE);
        lineChart.setCreateSymbols(FALSE);
        return lineChart;
    }

    private HBox createControls() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);

        startButton.setOnAction(e -> runLater(() -> {
            toggleControls();
            applyDefectionsButton.fire();
            controller.startSimulation();
        }));

        stopButton.setOnAction(e -> runLater(() -> {
            toggleControls();
            controller.stopSimulation();
        }));

        stopButton.setDisable(TRUE);
        hBox.getChildren().addAll(startButton, stopButton);
        return hBox;
    }

    private ListView<String> createProducedCarsList() {
        final ListView<String> producedCarsList = new ListView<>(model.getProducedCarsList());
        producedCarsList.setPrefHeight(200);
        return producedCarsList;
    }

    private VBox createVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);
        return vbox;
    }

    private void toggleControls() {
        toggleDisable(startButton, stopButton, applyDefectionsButton);
    }

    private void toggleDisable(Node... nodes) {
        Stream.of(nodes).forEach(node -> node.setDisable(!node.isDisable()));
    }
}
