package cz.zaoral.devchallange.carfactory;

import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

import static cz.zaoral.devchallange.carfactory.util.Utils.DEFAULT_DEFECTION_PROBABILITY;
import static cz.zaoral.devchallange.carfactory.util.Utils.ensuring;

@RequiredArgsConstructor
public class Controller {
    private final Model model;
    private final Simulation simulation = new Simulation();

    public void startSimulation() {
        model.getThroughputSmoothed().clear();
        model.getSecondsCounter().set(0);
        simulation.start();
    }

    public void stopSimulation() {
        simulation.stop();
    }

    public void applyNewDefections(TextField engineDefProb, TextField coachworkDefProb, TextField wheelDefProb) {
        simulation.getCarFactorySupply().updateEngineDefProb(handleDefProbField(engineDefProb));
        simulation.getCarFactorySupply().updateCoachworkDefProb(handleDefProbField(coachworkDefProb));
        simulation.getCarFactorySupply().updateWheelDefProb(handleDefProbField(wheelDefProb));
    }

    public Long getTotalProduced() {
        return simulation.getCarFactoryProductionControl().getTotalProduced();
    }

    public

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
}
