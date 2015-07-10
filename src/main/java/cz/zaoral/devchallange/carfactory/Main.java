package cz.zaoral.devchallange.carfactory;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Model model;
    private Controller controller;
    private View view;

    @Override
    public void start(Stage stage) throws Exception {
        model = new Model();
        controller = new Controller(model);
        view = new View(model, controller);


        view.show(stage);
    }

    @Override
    public void stop() throws Exception {
        view.getStopButton().fire();
        model.getScheduledExecutorService().shutdown();
    }
}
