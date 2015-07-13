package cz.zaoral.devchallange.carfactory;

import cz.zaoral.devchallange.carfactory.application.gui.Controller;
import cz.zaoral.devchallange.carfactory.application.gui.Model;
import cz.zaoral.devchallange.carfactory.application.gui.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private View view;

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();

        view = new View(model, new Controller(model));

        view.show(stage);
    }

    @Override
    public void stop() throws Exception {
        view.getStopButton().fire();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
